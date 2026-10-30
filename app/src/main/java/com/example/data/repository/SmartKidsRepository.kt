package com.example.data.repository

import com.example.data.local.PreloadedData
import com.example.data.local.SmartKidsDao
import com.example.data.model.Book
import com.example.data.model.BookPage
import com.example.data.model.ChildProfile
import com.example.data.model.ParentReward
import com.example.data.model.QuizAttempt
import com.example.data.model.QuizQuestion
import com.example.data.model.ReadingSession
import com.example.data.model.RevisionItem
import com.example.data.model.ScreenTimeConfig
import com.example.data.model.VocabularyWord
import com.example.data.model.VoiceRecording
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

class SmartKidsRepository(private val dao: SmartKidsDao) {

    val allChildren: Flow<List<ChildProfile>> = dao.getAllChildren()
    val allBooks: Flow<List<Book>> = dao.getAllBooks()
    val allRewards: Flow<List<ParentReward>> = dao.getAllRewards()
    val allQuizAttempts: Flow<List<QuizAttempt>> = dao.getAllQuizAttempts()
    val allReadingSessions: Flow<List<ReadingSession>> = dao.getAllReadingSessions()

    fun getChild(childId: Long): Flow<ChildProfile?> = dao.getChildById(childId)
    fun getBook(bookId: Long): Flow<Book?> = dao.getBookById(bookId)
    fun getPagesForBook(bookId: Long): Flow<List<BookPage>> = dao.getPagesForBook(bookId)
    fun getQuestionsForBook(bookId: Long): Flow<List<QuizQuestion>> = dao.getQuestionsForBook(bookId)
    fun getQuizAttemptsForChild(childId: Long): Flow<List<QuizAttempt>> = dao.getQuizAttemptsForChild(childId)
    fun getReadingSessionsForChild(childId: Long): Flow<List<ReadingSession>> = dao.getReadingSessionsForChild(childId)
    fun getScreenTimeConfig(childId: Long): Flow<ScreenTimeConfig?> = dao.getScreenTimeConfig(childId)

    suspend fun ensureInitialData() = withContext(Dispatchers.IO) {
        val children = dao.getAllChildren().first()
        if (children.isEmpty()) {
            for (child in PreloadedData.initialChildren()) {
                dao.insertChild(child)
            }
            for (config in PreloadedData.initialScreenTimeConfigs()) {
                dao.insertScreenTimeConfig(config)
            }
            for (reward in PreloadedData.initialRewards()) {
                dao.insertReward(reward)
            }
            for (book in PreloadedData.initialBooks()) {
                dao.insertBook(book)
            }
            dao.insertPages(PreloadedData.initialPages())
            dao.insertQuestions(PreloadedData.initialQuestions())
            dao.insertRevisionItems(PreloadedData.initialRevisionItems())
        }
    }

    suspend fun insertChild(child: ChildProfile): Long = withContext(Dispatchers.IO) {
        val newId = dao.insertChild(child)
        dao.insertScreenTimeConfig(ScreenTimeConfig(childId = newId))
        newId
    }

    suspend fun updateChild(child: ChildProfile) = withContext(Dispatchers.IO) {
        dao.updateChild(child)
    }

    suspend fun addStars(childId: Long, starsToAdd: Int) = withContext(Dispatchers.IO) {
        dao.addStarsToChild(childId, starsToAdd)
    }

    suspend fun incrementStreak(childId: Long) = withContext(Dispatchers.IO) {
        dao.incrementStreak(childId)
    }

    suspend fun deleteChild(childId: Long) = withContext(Dispatchers.IO) {
        dao.deleteChild(childId)
    }

    suspend fun insertReadingSession(session: ReadingSession) = withContext(Dispatchers.IO) {
        dao.insertReadingSession(session)
    }

    suspend fun insertQuizAttempt(attempt: QuizAttempt) = withContext(Dispatchers.IO) {
        dao.insertQuizAttempt(attempt)
        if (attempt.scorePercent >= 80) {
            dao.addStarsToChild(attempt.childId, 25)
        } else {
            dao.addStarsToChild(attempt.childId, 10)
        }
    }

    suspend fun updateScreenTimeConfig(config: ScreenTimeConfig) = withContext(Dispatchers.IO) {
        dao.updateScreenTimeConfig(config)
    }

    suspend fun insertReward(reward: ParentReward) = withContext(Dispatchers.IO) {
        dao.insertReward(reward)
    }

    suspend fun redeemReward(rewardId: Long, childId: Long, cost: Int) = withContext(Dispatchers.IO) {
        val child = dao.getChildById(childId).first()
        if (child != null && child.totalStars >= cost) {
            dao.addStarsToChild(childId, -cost)
            dao.updateReward(
                ParentReward(
                    id = rewardId,
                    title = "", // will be fetched or updated
                    description = "",
                    starsCost = cost,
                    isRedeemed = true,
                    redeemedByChildId = childId
                )
            )
        }
    }

    suspend fun createBookWithContent(
        book: Book,
        pages: List<BookPage>,
        questions: List<QuizQuestion>
    ): Long = withContext(Dispatchers.IO) {
        val bookId = dao.insertBook(book)
        val pagesWithId = pages.map { it.copy(bookId = bookId) }
        dao.insertPages(pagesWithId)
        val questionsWithId = questions.map { it.copy(bookId = bookId) }
        dao.insertQuestions(questionsWithId)
        bookId
    }

    suspend fun deleteBook(bookId: Long) = withContext(Dispatchers.IO) {
        dao.deletePagesForBook(bookId)
        dao.deleteBook(bookId)
    }

    // Smart Revision System
    fun getAllRevisionsForChild(childId: Long): Flow<List<RevisionItem>> = dao.getAllRevisionsForChild(childId)

    fun getDueRevisionsForChild(childId: Long, currentTime: Long = System.currentTimeMillis()): Flow<List<RevisionItem>> =
        dao.getDueRevisionsForChild(childId, currentTime)

    fun getMasteredRevisionsForChild(childId: Long): Flow<List<RevisionItem>> = dao.getMasteredRevisionsForChild(childId)

    suspend fun recordIncorrectQuestion(
        childId: Long,
        question: QuizQuestion,
        bookTitle: String
    ) = withContext(Dispatchers.IO) {
        val existing = dao.getRevisionByQuestionAndChild(childId, question.id)
        if (existing != null) {
            val updated = existing.copy(
                incorrectCount = existing.incorrectCount + 1,
                masteryStreak = 0,
                isMastered = false,
                lastAttemptTimestamp = System.currentTimeMillis(),
                nextRevisionDueTimestamp = System.currentTimeMillis(),
                stage = 1
            )
            dao.updateRevisionItem(updated)
        } else {
            val newItem = RevisionItem(
                childId = childId,
                questionId = question.id,
                bookId = question.bookId,
                bookTitle = bookTitle,
                chapterTitle = question.chapterTitle,
                questionText = question.questionText,
                questionType = question.questionType,
                optionsJson = question.optionsJson,
                correctAnswer = question.correctAnswer,
                explanation = question.explanation,
                difficulty = question.difficulty,
                incorrectCount = 1,
                masteryStreak = 0,
                isMastered = false,
                lastAttemptTimestamp = System.currentTimeMillis(),
                nextRevisionDueTimestamp = System.currentTimeMillis(),
                stage = 1
            )
            dao.insertRevisionItem(newItem)
        }
    }

    suspend fun recordRevisionAnswer(
        item: RevisionItem,
        isCorrect: Boolean
    ): Boolean = withContext(Dispatchers.IO) {
        val now = System.currentTimeMillis()
        if (isCorrect) {
            val newStreak = item.masteryStreak + 1
            val isNowMastered = newStreak >= 2
            val nextDue = if (isNowMastered) {
                Long.MAX_VALUE // Completely mastered!
            } else {
                now + (4 * 3600 * 1000) // Next interval: 4 hours
            }

            val updated = item.copy(
                masteryStreak = newStreak,
                isMastered = isNowMastered,
                stage = if (isNowMastered) 4 else 2,
                lastAttemptTimestamp = now,
                nextRevisionDueTimestamp = nextDue
            )
            dao.updateRevisionItem(updated)

            if (isNowMastered) {
                dao.addStarsToChild(item.childId, 15) // Bonus mastery stars
            } else {
                dao.addStarsToChild(item.childId, 5)
            }
            isNowMastered
        } else {
            val updated = item.copy(
                incorrectCount = item.incorrectCount + 1,
                masteryStreak = 0,
                isMastered = false,
                stage = 1,
                lastAttemptTimestamp = now,
                nextRevisionDueTimestamp = now // Still due immediately
            )
            dao.updateRevisionItem(updated)
            false
        }
    }

    suspend fun clearMasteredRevisions(childId: Long) = withContext(Dispatchers.IO) {
        dao.clearMasteredRevisionsForChild(childId)
    }

    // Vocabulary & Word Bank
    fun getVocabularyWordsForChild(childId: Long): Flow<List<VocabularyWord>> = dao.getVocabularyWordsForChild(childId)

    suspend fun collectVocabularyWord(word: VocabularyWord): Long = withContext(Dispatchers.IO) {
        val existing = dao.getWordByText(word.childId, word.word)
        if (existing == null) {
            val id = dao.insertVocabularyWord(word)
            dao.addStarsToChild(word.childId, 5) // +5 stars reward for discovering a new word!
            id
        } else {
            existing.id
        }
    }

    suspend fun updateVocabularyWord(word: VocabularyWord) = withContext(Dispatchers.IO) {
        dao.updateVocabularyWord(word)
    }

    // Voice Recordings & Reading Portfolio
    fun getVoiceRecordingsForChild(childId: Long): Flow<List<VoiceRecording>> = dao.getVoiceRecordingsForChild(childId)
    val allVoiceRecordings: Flow<List<VoiceRecording>> = dao.getAllVoiceRecordings()

    suspend fun saveVoiceRecording(recording: VoiceRecording): Long = withContext(Dispatchers.IO) {
        val id = dao.insertVoiceRecording(recording)
        dao.addStarsToChild(recording.childId, 10) // +10 stars reward for reading aloud!
        id
    }
}
