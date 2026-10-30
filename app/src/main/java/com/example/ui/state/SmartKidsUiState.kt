package com.example.ui.state

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

enum class AppScreen {
    PROFILE_SELECT,
    CHILD_HOME,
    BOOK_READER,
    QUIZ_ASSESSMENT,
    SMART_REVISION,
    WORD_BANK,
    REWARDS_GALLERY,
    PARENT_DASHBOARD,
    PARENT_BOOK_UPLOAD
}

data class SmartKidsUiState(
    val currentScreen: AppScreen = AppScreen.PROFILE_SELECT,
    val activeChild: ChildProfile? = null,
    val childrenList: List<ChildProfile> = emptyList(),
    val books: List<Book> = emptyList(),
    val rewards: List<ParentReward> = emptyList(),
    val allQuizAttempts: List<QuizAttempt> = emptyList(),
    val allReadingSessions: List<ReadingSession> = emptyList(),
    val childRevisions: List<RevisionItem> = emptyList(),
    val dueRevisions: List<RevisionItem> = emptyList(),
    val masteredRevisions: List<RevisionItem> = emptyList(),
    val collectedWords: List<VocabularyWord> = emptyList(),
    val childVoiceRecordings: List<VoiceRecording> = emptyList(),
    val allVoiceRecordings: List<VoiceRecording> = emptyList(),
    val activeBook: Book? = null,
    val activePages: List<BookPage> = emptyList(),
    val activePageIndex: Int = 0,
    val activeQuestions: List<QuizQuestion> = emptyList(),
    val currentScreenTimeConfig: ScreenTimeConfig? = null,
    val sessionElapsedSeconds: Int = 0,
    val isScreenTimeLocked: Boolean = false,
    val isAutoPageTurnEnabled: Boolean = true,
    val isPronunciationDialogOpen: Boolean = false,
    val selectedPronounceWord: String = "",
    val parentPinVerified: Boolean = false,
    val dailyQuestChestClaimed: Boolean = false,
    val readPagesCountToday: Int = 1,
    val notificationMessage: String? = null
)
