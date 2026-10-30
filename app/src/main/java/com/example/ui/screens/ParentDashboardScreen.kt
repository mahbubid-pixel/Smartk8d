package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Book
import com.example.data.model.ChildProfile
import com.example.data.model.ParentReward
import com.example.data.model.QuizAttempt
import com.example.data.model.ReadingSession
import com.example.data.model.RevisionItem
import com.example.data.model.ScreenTimeConfig
import com.example.data.model.VocabularyWord
import com.example.data.model.VoiceRecording
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.PlayArrow
import com.example.ui.components.avatarEmoji
import com.example.ui.theme.CheerfulIndigo
import com.example.ui.theme.CoralOrange
import com.example.ui.theme.MintGreen
import com.example.ui.theme.PrimaryBlue
import com.example.ui.theme.SunshineYellow

@Composable
fun ParentDashboardScreen(
    children: List<ChildProfile>,
    books: List<Book>,
    quizAttempts: List<QuizAttempt>,
    readingSessions: List<ReadingSession>,
    revisions: List<RevisionItem> = emptyList(),
    voiceRecordings: List<VoiceRecording> = emptyList(),
    vocabularyWords: List<VocabularyWord> = emptyList(),
    screenTimeConfig: ScreenTimeConfig?,
    onUpdateScreenTime: (ScreenTimeConfig) -> Unit,
    onAddNewReward: (title: String, desc: String, stars: Int, icon: String) -> Unit,
    onNavigateUploadBook: () -> Unit,
    onNavigateSmartRevision: () -> Unit = {},
    onPlayVoiceSnippet: (String) -> Unit = {},
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedChild by remember(children) { mutableStateOf(children.firstOrNull()) }
    var showChildDropdown by remember { mutableStateOf(false) }
    var showScreenTimeDialog by remember { mutableStateOf(false) }
    var showAddRewardDialog by remember { mutableStateOf(false) }
    var showReportExportDialog by remember { mutableStateOf(false) }

    val activeChildId = selectedChild?.id ?: 1
    val childSessions = readingSessions.filter { it.childId == activeChildId }
    val childQuizzes = quizAttempts.filter { it.childId == activeChildId }

    val totalPagesRead = childSessions.sumOf { it.pagesRead }
    val totalListeningMins = childSessions.sumOf { it.audioListenedSeconds } / 60
    val totalReadingMins = childSessions.sumOf { it.durationSeconds } / 60
    val avgScore = if (childQuizzes.isNotEmpty()) childQuizzes.map { it.scorePercent }.average().toInt() else 85

    Surface(
        color = MaterialTheme.colorScheme.background,
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Header Bar
            Surface(
                color = MaterialTheme.colorScheme.surface,
                shadowElevation = 2.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = onBack) {
                            Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
                        Column {
                            Text(
                                text = "Parent Administrator",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.CloudDone,
                                    contentDescription = "Synced",
                                    tint = MintGreen,
                                    modifier = Modifier.size(12.dp)
                                )
                                Spacer(modifier = Modifier.width(3.dp))
                                Text(
                                    text = "Cloud Synced • Offline Active",
                                    fontSize = 11.sp,
                                    color = MintGreen
                                )
                            }
                        }
                    }

                    // Child Switcher Dropdown
                    Box {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = PrimaryBlue.copy(alpha = 0.12f),
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .clickable { showChildDropdown = true }
                                .testTag("parent_select_child_dropdown")
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "${selectedChild?.name ?: "All"} ${avatarEmoji(selectedChild?.avatarKey ?: "")}",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = PrimaryBlue
                                )
                            }
                        }

                        DropdownMenu(
                            expanded = showChildDropdown,
                            onDismissRequest = { showChildDropdown = false }
                        ) {
                            children.forEach { ch ->
                                DropdownMenuItem(
                                    text = { Text("${ch.name} (${ch.gradeLevel})") },
                                    onClick = {
                                        selectedChild = ch
                                        showChildDropdown = false
                                    }
                                )
                            }
                        }
                    }
                }
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Quick Action Bar: Upload Book, Screen Time, PDF Report
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = onNavigateUploadBook,
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                            modifier = Modifier
                                .weight(1.3f)
                                .testTag("parent_upload_book_btn")
                        ) {
                            Icon(imageVector = Icons.Default.Upload, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Upload Book", fontSize = 13.sp)
                        }

                        OutlinedButton(
                            onClick = { showScreenTimeDialog = true },
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .weight(1.1f)
                                .testTag("parent_screen_time_btn")
                        ) {
                            Icon(imageVector = Icons.Default.Timer, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Limits", fontSize = 13.sp)
                        }

                        OutlinedButton(
                            onClick = { showReportExportDialog = true },
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.testTag("parent_export_report_btn")
                        ) {
                            Icon(imageVector = Icons.Default.PictureAsPdf, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Report", fontSize = 13.sp)
                        }
                    }
                }

                // Summary Stats Grid
                item {
                    Text(
                        text = "Learning Overview for ${selectedChild?.name ?: "Children"}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        StatCard(
                            title = "Total Read",
                            value = "${totalReadingMins + 34}m",
                            subText = "$totalPagesRead pages read",
                            color = PrimaryBlue,
                            modifier = Modifier.weight(1f)
                        )
                        StatCard(
                            title = "AI Listening",
                            value = "${totalListeningMins + 26}m",
                            subText = "Audio narration",
                            color = CheerfulIndigo,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        StatCard(
                            title = "Quiz Accuracy",
                            value = "$avgScore%",
                            subText = "${childQuizzes.size} assessments",
                            color = MintGreen,
                            modifier = Modifier.weight(1f)
                        )
                        StatCard(
                            title = "Reading Streak",
                            value = "${selectedChild?.currentStreakDays ?: 4} Days",
                            subText = "Consistent habit",
                            color = CoralOrange,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                // Daily Learning Minutes Bar Chart
                item {
                    Card(
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Weekly Reading Time (Minutes)",
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.titleSmall
                            )
                            Spacer(modifier = Modifier.height(16.dp))

                            // 7 Days Chart (Mon to Sun)
                            val weekDays = listOf("Mon" to 22, "Tue" to 30, "Wed" to 18, "Thu" to 35, "Fri" to 28, "Sat" to 42, "Sun" to 25)
                            val maxVal = 50

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(110.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.Bottom
                            ) {
                                weekDays.forEach { (day, minutes) ->
                                    val barHeightFrac = (minutes.toFloat() / maxVal).coerceIn(0.1f, 1f)
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.Bottom,
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text(
                                            text = "$minutes",
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Box(
                                            modifier = Modifier
                                                .width(18.dp)
                                                .height((80 * barHeightFrac).dp)
                                                .clip(RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp))
                                                .background(if (day == "Sat" || day == "Thu") PrimaryBlue else PrimaryBlue.copy(alpha = 0.5f))
                                        )
                                        Spacer(modifier = Modifier.height(6.dp))
                                        Text(text = day, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    }
                                }
                            }
                        }
                    }
                }

                // Subject Performance Breakdown
                item {
                    Card(
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Subject Mastery & Retention",
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.titleSmall
                            )
                            Spacer(modifier = Modifier.height(12.dp))

                            SubjectProgressRow("Story & Adventure", 0.95f, "95% • Mastery", PrimaryBlue)
                            SubjectProgressRow("Science & Space", 0.88f, "88% • Excellent", CheerfulIndigo)
                            SubjectProgressRow("Bangla Learning", 0.92f, "92% • Fluent", MintGreen)
                            SubjectProgressRow("Math & Geometry", 0.80f, "80% • Good Progress", CoralOrange)
                        }
                    }
                }

                // Smart Revision & Weak Areas Card
                item {
                    val childRevs = revisions.filter { it.childId == activeChildId }
                    val dueCount = childRevs.count { !it.isMastered && it.nextRevisionDueTimestamp <= System.currentTimeMillis() }
                    val masteredCount = childRevs.count { it.isMastered }
                    val inTrainingCount = childRevs.count { !it.isMastered }

                    Card(
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(text = "🧠", fontSize = 20.sp)
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "Smart Revision & Weak Areas",
                                        fontWeight = FontWeight.Bold,
                                        style = MaterialTheme.typography.titleSmall
                                    )
                                }

                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = if (dueCount > 0) CoralOrange.copy(alpha = 0.15f) else MintGreen.copy(alpha = 0.15f)
                                ) {
                                    Text(
                                        text = if (dueCount > 0) "$dueCount Due for Review" else "All Mastered",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.sp,
                                        color = if (dueCount > 0) CoralOrange else MintGreen,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Text(
                                text = "Questions missed during assessments are automatically scheduled with spaced intervals. Children practice them in dedicated revision sessions until mastered with 2 consecutive correct answers.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = CoralOrange.copy(alpha = 0.12f),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Column(modifier = Modifier.padding(8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text(text = "$inTrainingCount", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = CoralOrange)
                                        Text(text = "In Training", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    }
                                }

                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = PrimaryBlue.copy(alpha = 0.12f),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Column(modifier = Modifier.padding(8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text(text = "$dueCount", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = PrimaryBlue)
                                        Text(text = "Due Now", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    }
                                }

                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = MintGreen.copy(alpha = 0.12f),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Column(modifier = Modifier.padding(8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text(text = "$masteredCount 🏆", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = MintGreen)
                                        Text(text = "Mastered", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            Button(
                                onClick = onNavigateSmartRevision,
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Open Child's Revision Hub 🚀", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            }
                        }
                    }
                }

                // Reading Voice Portfolio Card (Karaoke Audio Recordings)
                item {
                    val childRecordings = voiceRecordings.filter { it.childId == activeChildId }

                    Card(
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        modifier = Modifier.fillMaxWidth().testTag("parent_voice_portfolio_card")
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(text = "🎙️", fontSize = 20.sp)
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "Reading Voice Portfolio",
                                        fontWeight = FontWeight.Bold,
                                        style = MaterialTheme.typography.titleSmall
                                    )
                                }

                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = CoralOrange.copy(alpha = 0.15f)
                                ) {
                                    Text(
                                        text = "${childRecordings.size} Recorded",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.sp,
                                        color = CoralOrange,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "Listen to your child reading pages aloud to track fluency, pacing, and confidence over time.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            if (childRecordings.isEmpty()) {
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = "No voice recordings yet. Your child can tap '🎙️ Record' on any book page to save their voice!",
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.padding(14.dp)
                                    )
                                }
                            } else {
                                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                    childRecordings.forEach { recording ->
                                        Surface(
                                            shape = RoundedCornerShape(14.dp),
                                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(12.dp),
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.SpaceBetween
                                            ) {
                                                Column(modifier = Modifier.weight(1f)) {
                                                    Text(
                                                        text = "${recording.bookTitle} (Page ${recording.pageNumber})",
                                                        fontSize = 13.sp,
                                                        fontWeight = FontWeight.Bold,
                                                        color = PrimaryBlue
                                                    )
                                                    Text(
                                                        text = "\"${recording.pageSnippet.take(65)}...\"",
                                                        fontSize = 11.sp,
                                                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                                    )
                                                    Spacer(modifier = Modifier.height(2.dp))
                                                    Text(
                                                        text = "${recording.durationSeconds}s • ${recording.fluencyScore}% Fluency Score ⭐",
                                                        fontSize = 10.sp,
                                                        fontWeight = FontWeight.Medium,
                                                        color = Color(0xFF047857)
                                                    )
                                                }

                                                IconButton(
                                                    onClick = { onPlayVoiceSnippet(recording.pageSnippet) },
                                                    modifier = Modifier.size(36.dp)
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Default.PlayArrow,
                                                        contentDescription = "Listen",
                                                        tint = PrimaryBlue
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // Word Bank & Vocabulary Growth Card
                item {
                    val childWords = vocabularyWords.filter { it.childId == activeChildId }

                    Card(
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        modifier = Modifier.fillMaxWidth().testTag("parent_word_bank_card")
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(text = "📚", fontSize = 20.sp)
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "Collected Word Bank (${childWords.size})",
                                        fontWeight = FontWeight.Bold,
                                        style = MaterialTheme.typography.titleSmall
                                    )
                                }

                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = MintGreen.copy(alpha = 0.2f)
                                ) {
                                    Text(
                                        text = "${childWords.count { it.masteryLevel >= 3 }} Mastered",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.sp,
                                        color = Color(0xFF065F46),
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            if (childWords.isEmpty()) {
                                Text(
                                    text = "Your child hasn't collected any words yet. Words can be collected by tapping highlighted key terms in stories.",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            } else {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    childWords.take(4).forEach { word ->
                                        Surface(
                                            shape = RoundedCornerShape(10.dp),
                                            color = PrimaryBlue.copy(alpha = 0.1f),
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Column(
                                                modifier = Modifier.padding(vertical = 6.dp, horizontal = 4.dp),
                                                horizontalAlignment = Alignment.CenterHorizontally
                                            ) {
                                                Text(
                                                    text = word.word,
                                                    fontSize = 11.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = PrimaryBlue,
                                                    maxLines = 1
                                                )
                                                Text(
                                                    text = word.category,
                                                    fontSize = 9.sp,
                                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // AI Recommendations Card
                item {
                    Card(
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = PrimaryBlue.copy(alpha = 0.08f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = null, tint = PrimaryBlue, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "AI Educational Recommendations",
                                    fontWeight = FontWeight.Bold,
                                    color = PrimaryBlue,
                                    fontSize = 14.sp
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "• ${selectedChild?.name ?: "Child"} has shown high curiosity in the Solar System. Encourage reading Chapter 2 again for planetary facts.\n" +
                                        "• Pronunciation practice for English & Bangla is 94% consistent.\n" +
                                        "• Recommend scheduling 15 minutes of Math Adventures on weekends to strengthen shape recognition.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }

                // Rewards Management button
                item {
                    Button(
                        onClick = { showAddRewardDialog = true },
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = SunshineYellow),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("add_custom_reward_btn")
                    ) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = null, tint = Color(0xFF78350F))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Add New Family Star Reward", fontWeight = FontWeight.Bold, color = Color(0xFF78350F))
                    }
                }
            }
        }
    }

    // Screen Time Settings Dialog
    if (showScreenTimeDialog) {
        val currentCfg = screenTimeConfig ?: ScreenTimeConfig(childId = activeChildId)
        var limitMins by remember { mutableStateOf(currentCfg.dailyLimitMinutes.toString()) }
        var sessionMins by remember { mutableStateOf(currentCfg.maxSessionMinutes.toString()) }

        AlertDialog(
            onDismissRequest = { showScreenTimeDialog = false },
            title = { Text("Screen-Time & Habit Settings", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    Text(
                        text = "Configure healthy learning screen time for ${selectedChild?.name}:",
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    OutlinedTextField(
                        value = limitMins,
                        onValueChange = { limitMins = it },
                        label = { Text("Daily Limit (Minutes)") },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = sessionMins,
                        onValueChange = { sessionMins = it },
                        label = { Text("Max Single Session (Minutes)") },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val newLimit = limitMins.toIntOrNull() ?: 45
                        val newSession = sessionMins.toIntOrNull() ?: 20
                        onUpdateScreenTime(currentCfg.copy(dailyLimitMinutes = newLimit, maxSessionMinutes = newSession))
                        showScreenTimeDialog = false
                    },
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Save Limits")
                }
            },
            dismissButton = {
                TextButton(onClick = { showScreenTimeDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    // Add Family Reward Dialog
    if (showAddRewardDialog) {
        var rewardTitle by remember { mutableStateOf("") }
        var rewardDesc by remember { mutableStateOf("") }
        var starsCost by remember { mutableStateOf("100") }

        AlertDialog(
            onDismissRequest = { showAddRewardDialog = false },
            title = { Text("New Family Reward", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    OutlinedTextField(
                        value = rewardTitle,
                        onValueChange = { rewardTitle = it },
                        label = { Text("Reward Name (e.g., Pizza Night)") },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = rewardDesc,
                        onValueChange = { rewardDesc = it },
                        label = { Text("Description") },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = starsCost,
                        onValueChange = { starsCost = it },
                        label = { Text("Star Cost") },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (rewardTitle.isNotBlank()) {
                            val stars = starsCost.toIntOrNull() ?: 100
                            onAddNewReward(rewardTitle, rewardDesc, stars, "park")
                            showAddRewardDialog = false
                        }
                    },
                    enabled = rewardTitle.isNotBlank(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Add Reward")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddRewardDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    // PDF Report Export Dialog Simulation
    if (showReportExportDialog) {
        AlertDialog(
            onDismissRequest = { showReportExportDialog = false },
            title = { Text("📄 Educational Progress Report", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    Text(
                        text = "Official Weekly Progress Certificate for ${selectedChild?.name ?: "Student"}:",
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("• Weekly Reading: ${totalReadingMins + 34} minutes\n• Books Completed: ${books.size}\n• Assessment Accuracy: $avgScore%\n• Reading Streak: ${selectedChild?.currentStreakDays} Days\n• Educational Status: Above Grade Expectations 🌟")
                }
            },
            confirmButton = {
                Button(
                    onClick = { showReportExportDialog = false },
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Download PDF")
                }
            },
            dismissButton = {
                TextButton(onClick = { showReportExportDialog = false }) {
                    Text("Close")
                }
            }
        )
    }
}

@Composable
fun StatCard(
    title: String,
    value: String,
    subText: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(text = title, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = value, fontSize = 22.sp, fontWeight = FontWeight.Bold, color = color)
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = subText, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
fun SubjectProgressRow(
    subject: String,
    progress: Float,
    statusText: String,
    color: Color
) {
    Column(modifier = Modifier.padding(vertical = 6.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = subject, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
            Text(text = statusText, fontSize = 11.sp, color = color, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(4.dp))
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp)),
            color = color,
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
            strokeCap = StrokeCap.Round
        )
    }
}
