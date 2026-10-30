package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Book
import com.example.data.model.BookPage
import com.example.data.model.VocabularyWord
import com.example.ui.components.AudioPlayerBar
import com.example.ui.components.VoiceStudioDialog
import com.example.ui.components.WordDictionaryDialog
import com.example.ui.theme.CoralOrange
import com.example.ui.theme.HighlightYellow
import com.example.ui.theme.MintGreen
import com.example.ui.theme.PrimaryBlue
import com.example.ui.theme.SunshineYellow

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun BookReaderScreen(
    book: Book,
    pages: List<BookPage>,
    currentPageIndex: Int,
    isPlayingAudio: Boolean,
    currentSentenceIndex: Int,
    speechRate: Float,
    isAutoPageTurn: Boolean,
    collectedWords: List<VocabularyWord> = emptyList(),
    onBack: () -> Unit,
    onNextPage: () -> Unit,
    onPreviousPage: () -> Unit,
    onPlayPauseAudio: () -> Unit,
    onPreviousSentence: () -> Unit,
    onNextSentence: () -> Unit,
    onRepeatSentence: () -> Unit,
    onSpeedChange: (Float) -> Unit,
    onToggleAutoPageTurn: () -> Unit,
    onOpenPronounceCoach: (String) -> Unit,
    onOpenQuiz: () -> Unit,
    onCollectWord: (VocabularyWord) -> Unit = {},
    onSaveVoiceRecording: (bookId: Long, bookTitle: String, pageNumber: Int, snippet: String, audioPath: String, durationSecs: Int) -> Unit = { _, _, _, _, _, _ -> },
    modifier: Modifier = Modifier
) {
    val currentPage = pages.getOrNull(currentPageIndex)
    val textContent = currentPage?.textContent ?: ""

    var selectedWordForDialog by remember { mutableStateOf<VocabularyWord?>(null) }
    var showVoiceStudio by remember { mutableStateOf(false) }

    // Split page text into sentences for synchronized highlight
    val sentences = textContent.split(Regex("(?<=[.!?।])\\s+"))
        .map { it.trim() }
        .filter { it.isNotEmpty() }
        .ifEmpty { listOf(textContent) }

    // Parse vocabulary from json or defaults
    val vocabList = rememberVocabulary(currentPage?.keyVocabularyJson ?: "[]")

    Scaffold(
        topBar = {
            Surface(
                color = MaterialTheme.colorScheme.surface,
                shadowElevation = 2.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.testTag("reader_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = book.title,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1
                        )
                        Text(
                            text = "Page ${currentPageIndex + 1} of ${pages.size}",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        // Record Voice Karaoke Button
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = CoralOrange.copy(alpha = 0.15f),
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .clickable { showVoiceStudio = true }
                                .testTag("reader_record_voice_btn")
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Mic,
                                    contentDescription = "Record",
                                    tint = CoralOrange,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(3.dp))
                                Text(
                                    text = "Record",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = CoralOrange
                                )
                            }
                        }

                        // Jump to Quiz Button
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = PrimaryBlue.copy(alpha = 0.15f),
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .clickable { onOpenQuiz() }
                                .testTag("reader_quiz_launch_btn")
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Quiz,
                                    contentDescription = null,
                                    tint = PrimaryBlue,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(3.dp))
                                Text(
                                    text = "Quiz",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = PrimaryBlue
                                )
                            }
                        }
                    }
                }
            }
        },
        bottomBar = {
            AudioPlayerBar(
                isPlaying = isPlayingAudio,
                currentSentenceIndex = currentSentenceIndex,
                totalSentences = sentences.size,
                speechRate = speechRate,
                isAutoPageTurn = isAutoPageTurn,
                languageCode = book.language,
                onPlayPause = onPlayPauseAudio,
                onPreviousSentence = onPreviousSentence,
                onNextSentence = onNextSentence,
                onRepeatSentence = onRepeatSentence,
                onSpeedChange = onSpeedChange,
                onToggleAutoPageTurn = onToggleAutoPageTurn,
                onOpenCoach = {
                    val firstWord = vocabList.firstOrNull() ?: "reading"
                    onOpenPronounceCoach(firstWord)
                }
            )
        },
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Chapter Title Header
            if (currentPage != null) {
                Text(
                    text = currentPage.chapterTitle,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryBlue
                )
                Spacer(modifier = Modifier.height(10.dp))
            }

            // Thematic Scene Illustration
            PageIllustrationCard(
                illustrationKey = currentPage?.illustrationKey ?: "forest",
                bookSubject = book.subject
            )

            Spacer(modifier = Modifier.height(18.dp))

            // Synchronized Reading Text
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start,
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        sentences.forEachIndexed { index, sentence ->
                            val isCurrent = isPlayingAudio && index == currentSentenceIndex
                            val highlightBg = if (isCurrent) HighlightYellow else Color.Transparent

                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = highlightBg,
                                modifier = Modifier
                                    .padding(vertical = 2.dp)
                                    .clip(RoundedCornerShape(8.dp))
                            ) {
                                Text(
                                    text = "$sentence ",
                                    fontSize = 17.sp,
                                    lineHeight = 26.sp,
                                    fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isCurrent) Color(0xFF1E293B) else MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Key Vocabulary for Word Bank & Dictionary
            if (vocabList.isNotEmpty()) {
                Text(
                    text = "📖 Key Words (Tap for Dictionary & Word Bank 🌟):",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(8.dp))

                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    vocabList.forEach { word ->
                        val isCollected = collectedWords.any { it.word.equals(word, ignoreCase = true) }
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (isCollected) MintGreen.copy(alpha = 0.2f) else SunshineYellow.copy(alpha = 0.25f),
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .clickable {
                                    val wordObj = buildWordModel(word, book.subject)
                                    selectedWordForDialog = wordObj
                                }
                                .testTag("vocab_chip_$word")
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = if (isCollected) "⭐ $word" else "✨ $word",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = if (isCollected) Color(0xFF065F46) else Color(0xFF92400E)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))
            }

            // Page Navigation Controls (Prev Page / Next Page)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedButton(
                    onClick = onPreviousPage,
                    enabled = currentPageIndex > 0,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.testTag("reader_prev_page_btn")
                ) {
                    Icon(imageVector = Icons.Default.ChevronLeft, contentDescription = null)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Previous")
                }

                Text(
                    text = "${currentPageIndex + 1} / ${pages.size}",
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Button(
                    onClick = {
                        if (currentPageIndex < pages.size - 1) {
                            onNextPage()
                        } else {
                            onOpenQuiz()
                        }
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                    modifier = Modifier.testTag("reader_next_page_btn")
                ) {
                    Text(if (currentPageIndex < pages.size - 1) "Next Page" else "Take Quiz 🏆")
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(imageVector = Icons.Default.ChevronRight, contentDescription = null)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    // Word Dictionary Dialog Popup
    selectedWordForDialog?.let { wordItem ->
        val isCollected = collectedWords.any { it.word.equals(wordItem.word, ignoreCase = true) }
        WordDictionaryDialog(
            word = wordItem,
            isAlreadyCollected = isCollected,
            onSpeakWord = { onOpenPronounceCoach(wordItem.word) },
            onCollectWord = {
                onCollectWord(wordItem)
                selectedWordForDialog = null
            },
            onDismiss = { selectedWordForDialog = null }
        )
    }

    // Voice Studio Karaoke Dialog Popup
    if (showVoiceStudio) {
        VoiceStudioDialog(
            bookTitle = book.title,
            pageNumber = currentPageIndex + 1,
            textSnippet = sentences.firstOrNull() ?: textContent,
            onSpeakWithTts = { onOpenPronounceCoach(it) },
            onSaveRecording = { audioPath, durationSecs ->
                onSaveVoiceRecording(
                    book.id,
                    book.title,
                    currentPageIndex + 1,
                    sentences.firstOrNull() ?: textContent,
                    audioPath,
                    durationSecs
                )
            },
            onDismiss = { showVoiceStudio = false }
        )
    }
}

@Composable
private fun PageIllustrationCard(
    illustrationKey: String,
    bookSubject: String,
    modifier: Modifier = Modifier
) {
    val (gradientColors, sceneEmoji) = when (illustrationKey) {
        "owl_branch" -> listOf(Color(0xFF1E293B), Color(0xFF0F172A)) to "🦉 ✨ 🌲"
        "owl_flight" -> listOf(Color(0xFF312E81), Color(0xFF1E1B4B)) to "🌙 🦉 🌟"
        "sun_space" -> listOf(Color(0xFF7C2D12), Color(0xFF431407)) to "☀️ 🪐 🌌"
        "saturn_rings" -> listOf(Color(0xFF1E1B4B), Color(0xFF312E81)) to "🪐 💫 ✨"
        "bangla_river" -> listOf(Color(0xFF065F46), Color(0xFF064E3B)) to "🚣 🌾 🇧🇩"
        "forest" -> listOf(Color(0xFF047857), Color(0xFF064E3B)) to "🌲 🦊 🍃"
        else -> listOf(Color(0xFF1E3A8A), Color(0xFF172554)) to "📖 🌟 🚀"
    }

    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        modifier = modifier
            .fillMaxWidth()
            .height(130.dp)
            .clip(RoundedCornerShape(20.dp))
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.linearGradient(gradientColors))
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = sceneEmoji,
                fontSize = 42.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

private fun rememberVocabulary(json: String): List<String> {
    return try {
        json.removeSurrounding("[", "]")
            .split(",")
            .map { it.trim().removeSurrounding("\"") }
            .filter { it.isNotEmpty() }
    } catch (_: Exception) {
        emptyList()
    }
}

private fun buildWordModel(word: String, subject: String): VocabularyWord {
    val clean = word.trim()
    return when (clean.lowercase()) {
        "photosynthesis" -> VocabularyWord(
            childId = 0,
            word = "Photosynthesis",
            syllables = "pho · to · syn · the · sis",
            definition = "How green plants use sunshine and water to create sweet sugar and fresh oxygen.",
            banglaMeaning = "সালোকসংশ্লেষ",
            category = "Science",
            exampleSentence = "Leaves use photosynthesis to feed the tall oak tree."
        )
        "chlorophyll" -> VocabularyWord(
            childId = 0,
            word = "Chlorophyll",
            syllables = "chlo · ro · phyll",
            definition = "The natural green pigment inside plant leaves that catches bright sunlight.",
            banglaMeaning = "ক্লোরোফিল (সবুজ রঞ্জক)",
            category = "Science",
            exampleSentence = "Chlorophyll turns the spring forest bright green."
        )
        "constellation" -> VocabularyWord(
            childId = 0,
            word = "Constellation",
            syllables = "con · stel · la · tion",
            definition = "A group of sparkling stars that form a recognizable picture in the night sky.",
            banglaMeaning = "নক্ষত্রমণ্ডল",
            category = "Space",
            exampleSentence = "Look up to find the Orion constellation tonight."
        )
        "oxygen" -> VocabularyWord(
            childId = 0,
            word = "Oxygen",
            syllables = "ox · y · gen",
            definition = "The life-giving invisible air that animals and people breathe.",
            banglaMeaning = "অক্সিজেন (প্রাণবায়ু)",
            category = "Nature",
            exampleSentence = "Trees give us fresh oxygen to breathe."
        )
        "saturn" -> VocabularyWord(
            childId = 0,
            word = "Saturn",
            syllables = "Sat · urn",
            definition = "The sixth planet in our solar system, famous for its sparkling rings.",
            banglaMeaning = "শনি গ্রহ",
            category = "Space",
            exampleSentence = "Saturn is surrounded by gleaming rings of ice."
        )
        else -> VocabularyWord(
            childId = 0,
            word = clean.replaceFirstChar { it.uppercase() },
            syllables = clean.map { "$it" }.joinToString(" · "),
            definition = "An important keyword from our '$subject' story.",
            banglaMeaning = "",
            category = if (subject.contains("Space", true)) "Space" else if (subject.contains("Bangla", true)) "Bangla" else "Story",
            exampleSentence = "The hero discovered $clean on their brave journey."
        )
    }
}
