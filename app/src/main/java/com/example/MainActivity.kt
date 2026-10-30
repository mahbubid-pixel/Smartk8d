package com.example

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.PronunciationCoachDialog
import com.example.ui.components.ScreenTimeOverlay
import com.example.ui.screens.BookReaderScreen
import com.example.ui.screens.ChildHomeScreen
import com.example.ui.screens.ParentBookUploadScreen
import com.example.ui.screens.ParentDashboardScreen
import com.example.ui.screens.ProfileSelectScreen
import com.example.ui.screens.QuizAssessmentScreen
import com.example.ui.screens.RewardsGalleryScreen
import com.example.ui.screens.SmartRevisionScreen
import com.example.ui.screens.WordBankScreen
import com.example.ui.state.AppScreen
import com.example.ui.state.SmartKidsViewModel
import com.example.ui.theme.SmartKidsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SmartKidsTheme {
                SmartKidsApp()
            }
        }
    }
}

@Composable
fun SmartKidsApp(
    viewModel: SmartKidsViewModel = viewModel()
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val isTtsPlaying by viewModel.ttsManager.isPlaying.collectAsStateWithLifecycle()
    val currentSentenceIndex by viewModel.ttsManager.currentSentenceIndex.collectAsStateWithLifecycle()
    val speechRate by viewModel.ttsManager.speechRate.collectAsStateWithLifecycle()

    // Show toast for notification messages
    LaunchedEffect(uiState.notificationMessage) {
        uiState.notificationMessage?.let { msg ->
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
        }
    }

    // Handle back button for sub-screens
    BackHandler(enabled = uiState.currentScreen != AppScreen.PROFILE_SELECT) {
        when (uiState.currentScreen) {
            AppScreen.CHILD_HOME -> viewModel.navigateTo(AppScreen.PROFILE_SELECT)
            AppScreen.BOOK_READER -> viewModel.navigateTo(AppScreen.CHILD_HOME)
            AppScreen.QUIZ_ASSESSMENT -> {
                if (uiState.activeBook != null) {
                    viewModel.navigateTo(AppScreen.BOOK_READER)
                } else {
                    viewModel.navigateTo(AppScreen.CHILD_HOME)
                }
            }
            AppScreen.SMART_REVISION -> viewModel.navigateTo(AppScreen.CHILD_HOME)
            AppScreen.WORD_BANK -> viewModel.navigateTo(AppScreen.CHILD_HOME)
            AppScreen.REWARDS_GALLERY -> viewModel.navigateTo(AppScreen.CHILD_HOME)
            AppScreen.PARENT_DASHBOARD -> viewModel.navigateTo(AppScreen.PROFILE_SELECT)
            AppScreen.PARENT_BOOK_UPLOAD -> viewModel.navigateTo(AppScreen.PARENT_DASHBOARD)
            AppScreen.PROFILE_SELECT -> { /* system default back */ }
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            AnimatedContent(
                targetState = uiState.currentScreen,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "ScreenTransition"
            ) { screen ->
                when (screen) {
                    AppScreen.PROFILE_SELECT -> {
                        ProfileSelectScreen(
                            children = uiState.childrenList,
                            onSelectChild = { child -> viewModel.selectChild(child) },
                            onAddChild = { name, age, grade, avatar ->
                                viewModel.createChildProfile(name, age, grade, avatar)
                            },
                            onOpenParentDashboard = { viewModel.navigateTo(AppScreen.PARENT_DASHBOARD) },
                            onVerifyPin = { pin -> viewModel.verifyParentPin(pin) }
                        )
                    }

                    AppScreen.CHILD_HOME -> {
                        uiState.activeChild?.let { child ->
                            ChildHomeScreen(
                                child = child,
                                books = uiState.books,
                                screenTimeConfig = uiState.currentScreenTimeConfig,
                                elapsedSeconds = uiState.sessionElapsedSeconds,
                                dueRevisionCount = uiState.dueRevisions.size,
                                collectedWordsCount = uiState.collectedWords.size,
                                dailyQuestChestClaimed = uiState.dailyQuestChestClaimed,
                                onBookClick = { book -> viewModel.openBook(book) },
                                onQuizClick = { book ->
                                    viewModel.openBook(book)
                                    viewModel.navigateTo(AppScreen.QUIZ_ASSESSMENT)
                                },
                                onOpenRewards = { viewModel.navigateTo(AppScreen.REWARDS_GALLERY) },
                                onOpenSmartRevision = { viewModel.navigateTo(AppScreen.SMART_REVISION) },
                                onOpenWordBank = { viewModel.navigateTo(AppScreen.WORD_BANK) },
                                onClaimDailyQuest = { viewModel.claimDailyQuestChest() },
                                onSwitchProfile = { viewModel.navigateTo(AppScreen.PROFILE_SELECT) },
                                onParentZoneClick = { viewModel.navigateTo(AppScreen.PARENT_DASHBOARD) }
                            )
                        } ?: run {
                            viewModel.navigateTo(AppScreen.PROFILE_SELECT)
                        }
                    }

                    AppScreen.BOOK_READER -> {
                        uiState.activeBook?.let { book ->
                            BookReaderScreen(
                                book = book,
                                pages = uiState.activePages,
                                currentPageIndex = uiState.activePageIndex,
                                isPlayingAudio = isTtsPlaying,
                                currentSentenceIndex = currentSentenceIndex,
                                speechRate = speechRate,
                                isAutoPageTurn = uiState.isAutoPageTurnEnabled,
                                collectedWords = uiState.collectedWords,
                                onBack = { viewModel.navigateTo(AppScreen.CHILD_HOME) },
                                onNextPage = { viewModel.nextPage() },
                                onPreviousPage = { viewModel.previousPage() },
                                onPlayPauseAudio = {
                                    if (isTtsPlaying) {
                                        viewModel.ttsManager.pause()
                                    } else {
                                        viewModel.ttsManager.play()
                                    }
                                },
                                onPreviousSentence = { viewModel.ttsManager.previousSentence() },
                                onNextSentence = { viewModel.ttsManager.nextSentence() },
                                onRepeatSentence = { viewModel.ttsManager.repeatCurrentSentence() },
                                onSpeedChange = { rate -> viewModel.ttsManager.setSpeed(rate) },
                                onToggleAutoPageTurn = { viewModel.toggleAutoPageTurn() },
                                onOpenPronounceCoach = { word -> viewModel.openPronunciationCoach(word) },
                                onOpenQuiz = { viewModel.navigateTo(AppScreen.QUIZ_ASSESSMENT) },
                                onCollectWord = { word -> viewModel.collectVocabularyWord(word) },
                                onSaveVoiceRecording = { bId, bTitle, pNum, snippet, path, dur ->
                                    viewModel.saveVoiceRecording(bId, bTitle, pNum, snippet, path, dur)
                                }
                            )
                        }
                    }

                    AppScreen.QUIZ_ASSESSMENT -> {
                        uiState.activeBook?.let { book ->
                            QuizAssessmentScreen(
                                book = book,
                                questions = uiState.activeQuestions,
                                onCompleteQuiz = { total, correct, timeSecs ->
                                    viewModel.recordQuizCompletion(
                                        bookId = book.id,
                                        bookTitle = book.title,
                                        chapterTitle = uiState.activePages.getOrNull(uiState.activePageIndex)?.chapterTitle ?: "All",
                                        totalQuestions = total,
                                        correctAnswers = correct,
                                        timeSeconds = timeSecs
                                    )
                                },
                                onQuestionIncorrect = { question ->
                                    viewModel.recordIncorrectQuestion(question, book.title)
                                },
                                onOpenSmartRevision = { viewModel.navigateTo(AppScreen.SMART_REVISION) },
                                onBack = { viewModel.navigateTo(AppScreen.CHILD_HOME) },
                                onBackToBook = { viewModel.navigateTo(AppScreen.BOOK_READER) }
                            )
                        }
                    }

                    AppScreen.SMART_REVISION -> {
                        uiState.activeChild?.let { child ->
                            SmartRevisionScreen(
                                child = child,
                                allRevisions = uiState.childRevisions,
                                dueRevisions = uiState.dueRevisions,
                                masteredRevisions = uiState.masteredRevisions,
                                onAnswerRevision = { item, isCorrect ->
                                    viewModel.answerRevisionQuestion(item, isCorrect)
                                },
                                onClearMastered = { viewModel.clearMasteredRevisions() },
                                onBack = { viewModel.navigateTo(AppScreen.CHILD_HOME) }
                            )
                        }
                    }

                    AppScreen.WORD_BANK -> {
                        uiState.activeChild?.let { child ->
                            WordBankScreen(
                                child = child,
                                words = uiState.collectedWords,
                                onBack = { viewModel.navigateTo(AppScreen.CHILD_HOME) },
                                onSpeakWord = { word -> viewModel.ttsManager.speak(word) },
                                onUpdateMastery = { word, level -> viewModel.updateWordMastery(word, level) }
                            )
                        }
                    }

                    AppScreen.REWARDS_GALLERY -> {
                        uiState.activeChild?.let { child ->
                            RewardsGalleryScreen(
                                child = child,
                                rewards = uiState.rewards,
                                onRedeemReward = { reward -> viewModel.redeemReward(reward) },
                                onBack = { viewModel.navigateTo(AppScreen.CHILD_HOME) }
                            )
                        }
                    }

                    AppScreen.PARENT_DASHBOARD -> {
                        ParentDashboardScreen(
                            children = uiState.childrenList,
                            books = uiState.books,
                            quizAttempts = uiState.allQuizAttempts,
                            readingSessions = uiState.allReadingSessions,
                            revisions = uiState.childRevisions,
                            voiceRecordings = uiState.allVoiceRecordings.ifEmpty { uiState.childVoiceRecordings },
                            vocabularyWords = uiState.collectedWords,
                            screenTimeConfig = uiState.currentScreenTimeConfig,
                            onUpdateScreenTime = { cfg -> viewModel.updateScreenTime(cfg) },
                            onAddNewReward = { title, desc, stars, icon ->
                                viewModel.addNewParentReward(title, desc, stars, icon)
                            },
                            onNavigateUploadBook = { viewModel.navigateTo(AppScreen.PARENT_BOOK_UPLOAD) },
                            onNavigateSmartRevision = { viewModel.navigateTo(AppScreen.SMART_REVISION) },
                            onPlayVoiceSnippet = { snippet -> viewModel.ttsManager.speak(snippet) },
                            onBack = {
                                if (uiState.activeChild != null) {
                                    viewModel.navigateTo(AppScreen.CHILD_HOME)
                                } else {
                                    viewModel.navigateTo(AppScreen.PROFILE_SELECT)
                                }
                            }
                        )
                    }

                    AppScreen.PARENT_BOOK_UPLOAD -> {
                        ParentBookUploadScreen(
                            children = uiState.childrenList,
                            onCreateBook = { title, author, subject, desc, text, childId ->
                                viewModel.createCustomBook(title, author, subject, desc, text, childId)
                            },
                            onBack = { viewModel.navigateTo(AppScreen.PARENT_DASHBOARD) }
                        )
                    }
                }
            }

            // Screen Time Lock Overlay (gentle eye break reminder)
            ScreenTimeOverlay(
                isLocked = uiState.isScreenTimeLocked,
                onExtendMinutes = { mins -> viewModel.extendScreenTime(mins) },
                onSwitchProfile = { viewModel.navigateTo(AppScreen.PROFILE_SELECT) },
                onVerifyPin = { pin -> viewModel.verifyParentPin(pin) }
            )

            // Pronunciation Coach Modal
            if (uiState.isPronunciationDialogOpen) {
                PronunciationCoachDialog(
                    isOpen = uiState.isPronunciationDialogOpen,
                    word = uiState.selectedPronounceWord,
                    onListenWord = { word -> viewModel.ttsManager.speakWord(word) },
                    onDismiss = { viewModel.closePronunciationCoach() }
                )
            }
        }
    }
}
