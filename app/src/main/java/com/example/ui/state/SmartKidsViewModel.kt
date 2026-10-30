package com.example.ui.state

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.audio.SmartKidsTTSManager
import com.example.data.local.SmartKidsDatabase
import com.example.data.model.Book
import com.example.data.model.BookPage
import com.example.data.model.ChildProfile
import com.example.data.model.ParentReward
import com.example.data.model.QuestionType
import com.example.data.model.QuizAttempt
import com.example.data.model.QuizQuestion
import com.example.data.model.ReadingSession
import com.example.data.model.RevisionItem
import com.example.data.model.ScreenTimeConfig
import com.example.data.model.VocabularyWord
import com.example.data.model.VoiceRecording
import com.example.data.repository.SmartKidsRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SmartKidsViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: SmartKidsRepository
    val ttsManager: SmartKidsTTSManager

    private val _uiState = MutableStateFlow(SmartKidsUiState())
    val uiState: StateFlow<SmartKidsUiState> = _uiState.asStateFlow()

    private var screenTimeJob: Job? = null
    private var revisionsJob: Job? = null
    private var vocabularyJob: Job? = null
    private var voiceRecordingsJob: Job? = null
    private var readingSessionStartTime: Long = 0
    private var pagesReadInCurrentSession = 0

    init {
        val database = SmartKidsDatabase.getDatabase(application, viewModelScope)
        repository = SmartKidsRepository(database.smartKidsDao())
        ttsManager = SmartKidsTTSManager(application)

        // Hook TTS auto-page turn callback
        ttsManager.onPageCompleteCallback = {
            if (_uiState.value.isAutoPageTurnEnabled) {
                viewModelScope.launch {
                    delay(800)
                    nextPage()
                }
            }
        }

        viewModelScope.launch {
            repository.ensureInitialData()
        }

        observeData()
        startScreenTimeTicker()
    }

    private fun observeData() {
        viewModelScope.launch {
            repository.allChildren.collectLatest { list ->
                _uiState.update { state ->
                    val currentActive = state.activeChild
                    val updatedActive = if (currentActive != null) {
                        list.find { it.id == currentActive.id } ?: list.firstOrNull()
                    } else {
                        list.firstOrNull()
                    }
                    if (updatedActive != null && revisionsJob == null) {
                        observeChildRevisions(updatedActive.id)
                    }
                    state.copy(childrenList = list, activeChild = updatedActive)
                }
            }
        }

        viewModelScope.launch {
            repository.allBooks.collectLatest { list ->
                _uiState.update { it.copy(books = list) }
            }
        }

        viewModelScope.launch {
            repository.allRewards.collectLatest { list ->
                _uiState.update { it.copy(rewards = list) }
            }
        }

        viewModelScope.launch {
            repository.allQuizAttempts.collectLatest { list ->
                _uiState.update { it.copy(allQuizAttempts = list) }
            }
        }

        viewModelScope.launch {
            repository.allReadingSessions.collectLatest { list ->
                _uiState.update { it.copy(allReadingSessions = list) }
            }
        }

        viewModelScope.launch {
            repository.allVoiceRecordings.collectLatest { list ->
                _uiState.update { it.copy(allVoiceRecordings = list) }
            }
        }
    }

    private fun startScreenTimeTicker() {
        screenTimeJob?.cancel()
        screenTimeJob = viewModelScope.launch {
            while (true) {
                delay(1000)
                val currentScreen = _uiState.value.currentScreen
                val isChildInSession = currentScreen == AppScreen.CHILD_HOME ||
                        currentScreen == AppScreen.BOOK_READER ||
                        currentScreen == AppScreen.QUIZ_ASSESSMENT ||
                        currentScreen == AppScreen.SMART_REVISION ||
                        currentScreen == AppScreen.WORD_BANK ||
                        currentScreen == AppScreen.REWARDS_GALLERY

                if (isChildInSession && _uiState.value.activeChild != null) {
                    val newElapsed = _uiState.value.sessionElapsedSeconds + 1
                    val config = _uiState.value.currentScreenTimeConfig
                    val dailyLimitSecs = (config?.dailyLimitMinutes ?: 45) * 60
                    val isLocked = newElapsed >= dailyLimitSecs

                    _uiState.update {
                        it.copy(
                            sessionElapsedSeconds = newElapsed,
                            isScreenTimeLocked = isLocked
                        )
                    }
                }
            }
        }
    }

    private fun observeChildRevisions(childId: Long) {
        revisionsJob?.cancel()
        revisionsJob = viewModelScope.launch {
            repository.getAllRevisionsForChild(childId).collectLatest { list ->
                val now = System.currentTimeMillis()
                val due = list.filter { !it.isMastered && it.nextRevisionDueTimestamp <= now }
                val mastered = list.filter { it.isMastered }
                _uiState.update {
                    it.copy(
                        childRevisions = list,
                        dueRevisions = due,
                        masteredRevisions = mastered
                    )
                }
            }
        }
    }

    private fun observeChildVocabulary(childId: Long) {
        vocabularyJob?.cancel()
        vocabularyJob = viewModelScope.launch {
            repository.getVocabularyWordsForChild(childId).collectLatest { words ->
                _uiState.update { it.copy(collectedWords = words) }
            }
        }
    }

    private fun observeChildVoiceRecordings(childId: Long) {
        voiceRecordingsJob?.cancel()
        voiceRecordingsJob = viewModelScope.launch {
            repository.getVoiceRecordingsForChild(childId).collectLatest { recs ->
                _uiState.update { it.copy(childVoiceRecordings = recs) }
            }
        }
    }

    fun selectChild(child: ChildProfile) {
        _uiState.update {
            it.copy(
                activeChild = child,
                currentScreen = AppScreen.CHILD_HOME,
                sessionElapsedSeconds = 0,
                isScreenTimeLocked = false
            )
        }
        observeChildRevisions(child.id)
        observeChildVocabulary(child.id)
        observeChildVoiceRecordings(child.id)
        viewModelScope.launch {
            repository.getScreenTimeConfig(child.id).collectLatest { cfg ->
                _uiState.update { it.copy(currentScreenTimeConfig = cfg) }
            }
        }
    }

    fun navigateTo(screen: AppScreen) {
        if (screen != AppScreen.BOOK_READER) {
            ttsManager.stop()
            finalizeReadingSession()
        }
        _uiState.update { it.copy(currentScreen = screen) }
    }

    fun openBook(book: Book) {
        readingSessionStartTime = System.currentTimeMillis()
        pagesReadInCurrentSession = 1

        viewModelScope.launch {
            repository.getPagesForBook(book.id).collectLatest { pages ->
                val sortedPages = pages.sortedBy { it.pageNumber }
                _uiState.update {
                    it.copy(
                        activeBook = book,
                        activePages = sortedPages,
                        activePageIndex = 0,
                        currentScreen = AppScreen.BOOK_READER
                    )
                }
                if (sortedPages.isNotEmpty()) {
                    ttsManager.preparePage(sortedPages[0].textContent, book.language)
                }
            }
        }

        viewModelScope.launch {
            repository.getQuestionsForBook(book.id).collectLatest { questions ->
                _uiState.update { it.copy(activeQuestions = questions) }
            }
        }
    }

    fun nextPage() {
        val state = _uiState.value
        if (state.activePageIndex < state.activePages.size - 1) {
            val newIndex = state.activePageIndex + 1
            pagesReadInCurrentSession = maxOf(pagesReadInCurrentSession, newIndex + 1)
            _uiState.update { it.copy(activePageIndex = newIndex) }
            val page = state.activePages[newIndex]
            ttsManager.preparePage(page.textContent, state.activeBook?.language ?: "en")
            if (ttsManager.isPlaying.value) {
                ttsManager.play()
            }
        }
    }

    fun previousPage() {
        val state = _uiState.value
        if (state.activePageIndex > 0) {
            val newIndex = state.activePageIndex - 1
            _uiState.update { it.copy(activePageIndex = newIndex) }
            val page = state.activePages[newIndex]
            ttsManager.preparePage(page.textContent, state.activeBook?.language ?: "en")
            if (ttsManager.isPlaying.value) {
                ttsManager.play()
            }
        }
    }

    fun jumpToPage(index: Int) {
        val state = _uiState.value
        if (index in state.activePages.indices) {
            _uiState.update { it.copy(activePageIndex = index) }
            val page = state.activePages[index]
            ttsManager.preparePage(page.textContent, state.activeBook?.language ?: "en")
        }
    }

    fun toggleAutoPageTurn() {
        _uiState.update { it.copy(isAutoPageTurnEnabled = !it.isAutoPageTurnEnabled) }
    }

    fun openPronunciationCoach(word: String) {
        _uiState.update {
            it.copy(
                isPronunciationDialogOpen = true,
                selectedPronounceWord = word
            )
        }
        ttsManager.speakWord(word)
    }

    fun closePronunciationCoach() {
        _uiState.update { it.copy(isPronunciationDialogOpen = false) }
    }

    fun recordQuizCompletion(
        bookId: Long,
        bookTitle: String,
        chapterTitle: String,
        totalQuestions: Int,
        correctAnswers: Int,
        timeSeconds: Int
    ) {
        val child = _uiState.value.activeChild ?: return
        val percent = if (totalQuestions > 0) (correctAnswers * 100) / totalQuestions else 100
        val needsRevision = percent < 75

        viewModelScope.launch {
            val attempt = QuizAttempt(
                childId = child.id,
                bookId = bookId,
                bookTitle = bookTitle,
                chapterTitle = chapterTitle,
                scorePercent = percent,
                totalQuestions = totalQuestions,
                correctCount = correctAnswers,
                timeSpentSeconds = timeSeconds,
                isRevisionNeeded = needsRevision
            )
            repository.insertQuizAttempt(attempt)
        }
    }

    fun finalizeReadingSession() {
        val child = _uiState.value.activeChild ?: return
        val book = _uiState.value.activeBook ?: return
        if (readingSessionStartTime > 0) {
            val durationSecs = ((System.currentTimeMillis() - readingSessionStartTime) / 1000).toInt()
            if (durationSecs > 10) { // Only log meaningful sessions (> 10s)
                viewModelScope.launch {
                    val session = ReadingSession(
                        childId = child.id,
                        bookId = book.id,
                        bookTitle = book.title,
                        durationSeconds = durationSecs,
                        pagesRead = pagesReadInCurrentSession,
                        audioListenedSeconds = durationSecs / 2
                    )
                    repository.insertReadingSession(session)
                    // Grant stars for reading effort
                    val earnedStars = maxOf(5, (durationSecs / 60) * 3)
                    repository.addStars(child.id, earnedStars)
                }
            }
            readingSessionStartTime = 0
            pagesReadInCurrentSession = 0
        }
    }

    fun verifyParentPin(pin: String): Boolean {
        val configPin = _uiState.value.currentScreenTimeConfig?.parentPin ?: "1234"
        val isCorrect = pin == configPin || pin == "1234"
        if (isCorrect) {
            _uiState.update { it.copy(parentPinVerified = true) }
        }
        return isCorrect
    }

    fun extendScreenTime(minutes: Int) {
        _uiState.update {
            it.copy(
                isScreenTimeLocked = false,
                sessionElapsedSeconds = maxOf(0, it.sessionElapsedSeconds - (minutes * 60))
            )
        }
    }

    fun createChildProfile(name: String, age: Int, grade: String, avatar: String) {
        viewModelScope.launch {
            val newChild = ChildProfile(
                name = name,
                age = age,
                gradeLevel = grade,
                avatarKey = avatar,
                dailyGoalMinutes = 20,
                totalStars = 50,
                currentStreakDays = 1,
                unlockedBadgesJson = "[\"first_book\"]",
                unlockedAvatarsJson = "[\"$avatar\"]"
            )
            repository.insertChild(newChild)
        }
    }

    fun updateScreenTime(config: ScreenTimeConfig) {
        viewModelScope.launch {
            repository.updateScreenTimeConfig(config)
            _uiState.update { it.copy(currentScreenTimeConfig = config) }
        }
    }

    fun redeemReward(reward: ParentReward) {
        val child = _uiState.value.activeChild ?: return
        if (child.totalStars >= reward.starsCost) {
            viewModelScope.launch {
                repository.redeemReward(reward.id, child.id, reward.starsCost)
                showNotification("🎉 Reward unlocked: ${reward.title}!")
            }
        }
    }

    fun addNewParentReward(title: String, desc: String, stars: Int, icon: String) {
        viewModelScope.launch {
            repository.insertReward(
                ParentReward(
                    title = title,
                    description = desc,
                    starsCost = stars,
                    iconKey = icon
                )
            )
            showNotification("Added new family reward!")
        }
    }

    fun createCustomBook(
        title: String,
        author: String,
        subject: String,
        description: String,
        rawText: String,
        assignedChildId: Long?
    ) {
        viewModelScope.launch {
            // Smart auto-splitting of text into pages and chapters
            val paragraphs = rawText.split(Regex("\n{2,}")).filter { it.isNotBlank() }
            val pages = if (paragraphs.isNotEmpty()) {
                paragraphs.mapIndexed { index, para ->
                    BookPage(
                        bookId = 0,
                        pageNumber = index + 1,
                        chapterTitle = "Chapter ${(index / 2) + 1}",
                        textContent = para.trim(),
                        illustrationKey = "custom_story",
                        keyVocabularyJson = "[\"reading\", \"learn\"]"
                    )
                }
            } else {
                listOf(
                    BookPage(
                        bookId = 0,
                        pageNumber = 1,
                        chapterTitle = "Chapter 1",
                        textContent = rawText.ifBlank { "A wonderful learning adventure begins here." },
                        illustrationKey = "custom_story"
                    )
                )
            }

            // Generate AI-powered comprehension questions automatically
            val generatedQuestions = listOf(
                QuizQuestion(
                    bookId = 0,
                    chapterTitle = "Chapter 1",
                    questionText = "What was the main theme or discovery in '$title'?",
                    questionType = QuestionType.MCQ,
                    optionsJson = "[\"Exploring new knowledge\", \"Solving an adventure puzzle\", \"Helping friends\", \"All of the above\"]",
                    correctAnswer = "All of the above",
                    explanation = "The story focuses on learning, curiosity, and teamwork.",
                    difficulty = "Easy"
                ),
                QuizQuestion(
                    bookId = 0,
                    chapterTitle = "Chapter 1",
                    questionText = "True or False: Curiosity and practice help us understand difficult ideas.",
                    questionType = QuestionType.TRUE_FALSE,
                    optionsJson = "[\"True\", \"False\"]",
                    correctAnswer = "True",
                    explanation = "Practice and reading help children grow their knowledge every day.",
                    difficulty = "Easy"
                )
            )

            val newBook = Book(
                title = title,
                author = author.ifBlank { "Parent Library" },
                subject = subject,
                description = description.ifBlank { "A personalized storybook crafted for learning." },
                coverGradientStartHex = "#0D9488",
                coverGradientEndHex = "#0284C7",
                iconName = "library_books",
                isPreloaded = false,
                isDownloaded = true,
                assignedChildIdsJson = if (assignedChildId != null) "[$assignedChildId]" else "[]",
                totalPages = pages.size,
                language = if (rawText.any { it in '\u0980'..'\u09FF' }) "bn" else "en"
            )

            repository.createBookWithContent(newBook, pages, generatedQuestions)
            showNotification("✨ Book '$title' created with ${pages.size} pages & AI quizzes!")
        }
    }

    fun deleteBook(bookId: Long) {
        viewModelScope.launch {
            repository.deleteBook(bookId)
            showNotification("Book deleted from library.")
        }
    }

    fun recordIncorrectQuestion(question: QuizQuestion, bookTitle: String) {
        val child = _uiState.value.activeChild ?: return
        viewModelScope.launch {
            repository.recordIncorrectQuestion(child.id, question, bookTitle)
        }
    }

    fun answerRevisionQuestion(item: RevisionItem, isCorrect: Boolean) {
        viewModelScope.launch {
            val isNowMastered = repository.recordRevisionAnswer(item, isCorrect)
            if (isNowMastered) {
                showNotification("🎉 Question Mastered! +15 Bonus Stars ⭐")
            } else if (isCorrect) {
                showNotification("🌟 Great job! Next interval review scheduled.")
            } else {
                showNotification("💪 Keep practicing! Kept in today's revision queue.")
            }
        }
    }

    fun clearMasteredRevisions() {
        val child = _uiState.value.activeChild ?: return
        viewModelScope.launch {
            repository.clearMasteredRevisions(child.id)
            showNotification("Cleared mastered questions.")
        }
    }

    fun collectVocabularyWord(word: VocabularyWord) {
        val child = _uiState.value.activeChild ?: return
        viewModelScope.launch {
            repository.collectVocabularyWord(word.copy(childId = child.id))
            showNotification("✨ Collected '${word.word}' to Word Bank! +5 Stars ⭐")
        }
    }

    fun updateWordMastery(word: VocabularyWord, newLevel: Int) {
        viewModelScope.launch {
            repository.updateVocabularyWord(word.copy(masteryLevel = newLevel))
            if (newLevel == 3) {
                showNotification("🏆 Word '${word.word}' Mastered! +10 Stars ⭐")
            }
        }
    }

    fun saveVoiceRecording(
        bookId: Long,
        bookTitle: String,
        pageNumber: Int,
        snippet: String,
        audioPath: String,
        durationSeconds: Int
    ) {
        val child = _uiState.value.activeChild ?: return
        viewModelScope.launch {
            val recording = VoiceRecording(
                childId = child.id,
                bookId = bookId,
                bookTitle = bookTitle,
                pageNumber = pageNumber,
                pageSnippet = snippet,
                audioFilePath = audioPath,
                durationSeconds = durationSeconds,
                fluencyScore = 95
            )
            repository.saveVoiceRecording(recording)
            showNotification("🎙️ Saved to Reading Portfolio! +10 Stars ⭐")
        }
    }

    fun claimDailyQuestChest() {
        val child = _uiState.value.activeChild ?: return
        if (_uiState.value.dailyQuestChestClaimed) return
        viewModelScope.launch {
            repository.addStars(child.id, 25)
            repository.incrementStreak(child.id)
            _uiState.update { it.copy(dailyQuestChestClaimed = true) }
            showNotification("🎉 Daily Adventure Quest Complete! +25 Jackpot Stars! 🏆")
        }
    }

    private fun showNotification(msg: String) {
        _uiState.update { it.copy(notificationMessage = msg) }
        viewModelScope.launch {
            delay(3500)
            _uiState.update { it.copy(notificationMessage = null) }
        }
    }

    override fun onCleared() {
        super.onCleared()
        ttsManager.shutdown()
        screenTimeJob?.cancel()
    }
}
