package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Book
import com.example.data.model.QuizQuestion
import com.example.ui.theme.MintGreen
import com.example.ui.theme.PrimaryBlue
import com.example.ui.theme.SunshineYellow

@Composable
fun QuizAssessmentScreen(
    book: Book,
    questions: List<QuizQuestion>,
    onCompleteQuiz: (total: Int, correct: Int, timeSecs: Int) -> Unit,
    onQuestionIncorrect: (QuizQuestion) -> Unit = {},
    onOpenSmartRevision: () -> Unit = {},
    onBack: () -> Unit,
    onBackToBook: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (questions.isEmpty()) {
        EmptyQuestionsState(book = book, onBack = onBack)
        return
    }

    var currentIndex by remember { mutableIntStateOf(0) }
    var selectedOption by remember { mutableStateOf<String?>(null) }
    var isAnswerSubmitted by remember { mutableStateOf(false) }
    var correctCount by remember { mutableIntStateOf(0) }
    var quizFinished by remember { mutableStateOf(false) }

    val missedQuestions = remember { mutableStateListOf<QuizQuestion>() }
    var quizStartTime by remember { mutableStateOf(System.currentTimeMillis()) }

    val currentQuestion = questions.getOrNull(currentIndex)
    val parsedOptions = remember(currentQuestion) {
        parseOptions(currentQuestion?.optionsJson ?: "[]")
    }

    if (quizFinished) {
        QuizCompletionScorecard(
            bookTitle = book.title,
            totalQuestions = questions.size,
            correctCount = correctCount,
            missedCount = missedQuestions.size,
            onSmartRevision = {
                // Restart with only missed questions or restart all
                currentIndex = 0
                selectedOption = null
                isAnswerSubmitted = false
                correctCount = 0
                quizFinished = false
                quizStartTime = System.currentTimeMillis()
            },
            onReturnHome = onBack,
            onReturnToBook = onBackToBook,
            onOpenSmartRevision = onOpenSmartRevision
        )
        return
    }

    Surface(
        color = MaterialTheme.colorScheme.background,
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Header Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = onBack) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Question ${currentIndex + 1} of ${questions.size}",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = book.title,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // Score preview
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = SunshineYellow.copy(alpha = 0.15f)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(imageVector = Icons.Default.Star, contentDescription = null, tint = SunshineYellow, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(text = "$correctCount", fontWeight = FontWeight.Bold, color = Color(0xFFB45309))
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Progress bar
            LinearProgressIndicator(
                progress = { (currentIndex + 1).toFloat() / questions.size },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = PrimaryBlue,
                trackColor = MaterialTheme.colorScheme.surfaceVariant,
                strokeCap = StrokeCap.Round
            )

            Spacer(modifier = Modifier.height(20.dp))

            if (currentQuestion != null) {
                // Question Card
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = PrimaryBlue.copy(alpha = 0.1f)
                        ) {
                            Text(
                                text = "${currentQuestion.difficulty} • ${currentQuestion.chapterTitle}",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = PrimaryBlue,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = currentQuestion.questionText,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Options List
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    parsedOptions.forEach { option ->
                        val isSelected = selectedOption == option
                        val isCorrect = option == currentQuestion.correctAnswer

                        val (cardBg, borderColor, textColor) = when {
                            !isAnswerSubmitted && isSelected ->
                                Triple(PrimaryBlue.copy(alpha = 0.12f), PrimaryBlue, PrimaryBlue)
                            isAnswerSubmitted && isCorrect ->
                                Triple(MintGreen.copy(alpha = 0.15f), MintGreen, Color(0xFF047857))
                            isAnswerSubmitted && isSelected && !isCorrect ->
                                Triple(Color(0xFFFEE2E2), Color(0xFFEF4444), Color(0xFFB91C1C))
                            else ->
                                Triple(MaterialTheme.colorScheme.surface, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f), MaterialTheme.colorScheme.onSurface)
                        }

                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = cardBg),
                            border = androidx.compose.foundation.BorderStroke(1.5.dp, borderColor),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .clickable(enabled = !isAnswerSubmitted) {
                                    selectedOption = option
                                }
                                .testTag("quiz_option_$option")
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = option,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = textColor,
                                    modifier = Modifier.weight(1f)
                                )

                                if (isAnswerSubmitted) {
                                    if (isCorrect) {
                                        Icon(imageVector = Icons.Default.Check, contentDescription = "Correct", tint = MintGreen)
                                    } else if (isSelected) {
                                        Icon(imageVector = Icons.Default.Close, contentDescription = "Incorrect", tint = Color(0xFFEF4444))
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Answer explanation if submitted
                AnimatedVisibility(visible = isAnswerSubmitted) {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (selectedOption == currentQuestion.correctAnswer) MintGreen.copy(alpha = 0.1f) else Color(0xFFFEF3C7)
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(
                                text = if (selectedOption == currentQuestion.correctAnswer) "🎉 Brilliant Job!" else "💡 Learning Tip:",
                                fontWeight = FontWeight.Bold,
                                color = if (selectedOption == currentQuestion.correctAnswer) Color(0xFF065F46) else Color(0xFF92400E)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = currentQuestion.explanation,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Bottom Action: Check Answer or Next Question
                if (!isAnswerSubmitted) {
                    Button(
                        onClick = {
                            if (selectedOption != null) {
                                isAnswerSubmitted = true
                                if (selectedOption == currentQuestion.correctAnswer) {
                                    correctCount++
                                } else {
                                    missedQuestions.add(currentQuestion)
                                    onQuestionIncorrect(currentQuestion)
                                }
                            }
                        },
                        enabled = selectedOption != null,
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .testTag("check_answer_btn")
                    ) {
                        Text("Check Answer", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }
                } else {
                    Button(
                        onClick = {
                            if (currentIndex < questions.size - 1) {
                                currentIndex++
                                selectedOption = null
                                isAnswerSubmitted = false
                            } else {
                                val totalSeconds = ((System.currentTimeMillis() - quizStartTime) / 1000).toInt()
                                onCompleteQuiz(questions.size, correctCount, totalSeconds)
                                quizFinished = true
                            }
                        },
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .testTag("next_quiz_question_btn")
                    ) {
                        Text(
                            text = if (currentIndex < questions.size - 1) "Next Question ➡️" else "Finish & View Score 🏆",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun QuizCompletionScorecard(
    bookTitle: String,
    totalQuestions: Int,
    correctCount: Int,
    missedCount: Int,
    onSmartRevision: () -> Unit,
    onReturnHome: () -> Unit,
    onReturnToBook: () -> Unit,
    onOpenSmartRevision: () -> Unit = {}
) {
    val scorePercent = if (totalQuestions > 0) (correctCount * 100) / totalQuestions else 100
    val starsAwarded = if (scorePercent >= 80) 25 else 10

    Surface(
        color = MaterialTheme.colorScheme.background,
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(24.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(96.dp)
                    .clip(CircleShape)
                    .background(SunshineYellow.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (scorePercent >= 80) "🏆" else "🌟",
                    fontSize = 50.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = if (scorePercent >= 80) "Outstanding Superstar!" else "Great Practice Session!",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Center
            )

            Text(
                text = bookTitle,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Score Summary Card
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "$scorePercent%", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = PrimaryBlue)
                        Text(text = "Score", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "$correctCount / $totalQuestions", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = MintGreen)
                        Text(text = "Correct", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "+$starsAwarded ⭐", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = SunshineYellow)
                        Text(text = "Stars Earned", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Smart Revision Button (if any mistakes)
            if (missedCount > 0) {
                Button(
                    onClick = onSmartRevision,
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = SunshineYellow),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("smart_revision_btn")
                ) {
                    Icon(imageVector = Icons.Default.Replay, contentDescription = null, tint = Color(0xFF78350F))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Retry Missed Now", fontWeight = FontWeight.Bold, color = Color(0xFF78350F))
                }

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedButton(
                    onClick = onOpenSmartRevision,
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("open_revision_hub_btn")
                ) {
                    Text("🧠 Open Smart Revision Hub ($missedCount Added)", fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(10.dp))
            }

            Button(
                onClick = onReturnToBook,
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("return_to_book_btn")
            ) {
                Text("Return to Book", fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedButton(
                onClick = onReturnHome,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("return_home_btn")
            ) {
                Text("Return to Home")
            }
        }
    }
}

@Composable
fun EmptyQuestionsState(book: Book, onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("📖", fontSize = 48.sp)
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "No questions added yet for ${book.title}",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Parents can generate AI questions from the Parent Dashboard Upload Studio!",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = onBack, shape = RoundedCornerShape(12.dp)) {
            Text("Back")
        }
    }
}

private fun parseOptions(json: String): List<String> {
    return try {
        json.removeSurrounding("[", "]")
            .split(",")
            .map { it.trim().removeSurrounding("\"") }
            .filter { it.isNotEmpty() }
    } catch (_: Exception) {
        listOf("True", "False")
    }
}
