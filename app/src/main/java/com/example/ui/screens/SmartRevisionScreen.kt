package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Timer
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
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ChildProfile
import com.example.data.model.RevisionItem
import com.example.ui.components.avatarEmoji
import com.example.ui.theme.CoralOrange
import com.example.ui.theme.MintGreen
import com.example.ui.theme.PrimaryBlue
import com.example.ui.theme.SunshineYellow

@Composable
fun SmartRevisionScreen(
    child: ChildProfile,
    allRevisions: List<RevisionItem>,
    dueRevisions: List<RevisionItem>,
    masteredRevisions: List<RevisionItem>,
    onAnswerRevision: (RevisionItem, Boolean) -> Unit,
    onClearMastered: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isPracticingSession by remember { mutableStateOf(false) }
    var singlePracticingItem by remember { mutableStateOf<RevisionItem?>(null) }
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Due Now ⏰ (${dueRevisions.size})", "In Progress (${allRevisions.filter { !it.isMastered }.size})", "Mastered 🏆 (${masteredRevisions.size})")

    // If active session is in progress, show revision session runner
    if (isPracticingSession && dueRevisions.isNotEmpty()) {
        ActiveRevisionSessionRunner(
            revisionQueue = dueRevisions,
            onAnswerQuestion = { item, isCorrect ->
                onAnswerRevision(item, isCorrect)
            },
            onFinishSession = {
                isPracticingSession = false
            }
        )
        return
    }

    if (singlePracticingItem != null) {
        ActiveRevisionSessionRunner(
            revisionQueue = listOf(singlePracticingItem!!),
            onAnswerQuestion = { item, isCorrect ->
                onAnswerRevision(item, isCorrect)
            },
            onFinishSession = {
                singlePracticingItem = null
            }
        )
        return
    }

    val totalTracked = allRevisions.size
    val totalMastered = masteredRevisions.size
    val masteryFraction = if (totalTracked > 0) totalMastered.toFloat() / totalTracked else 0f

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
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = onBack, modifier = Modifier.testTag("revision_back_btn")) {
                            Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "Smart Revision System",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(text = "🧠", fontSize = 18.sp)
                            }
                            Text(
                                text = "${child.name}'s Spaced Memory Palace",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    // Avatar badge
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(PrimaryBlue.copy(alpha = 0.12f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = avatarEmoji(child.avatarKey), fontSize = 20.sp)
                    }
                }
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Hero Progress & Spaced Repetition Metric Card
                item {
                    Card(
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(18.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = "Knowledge Mastery Rate",
                                        fontWeight = FontWeight.Bold,
                                        style = MaterialTheme.typography.titleSmall
                                    )
                                    Text(
                                        text = "$totalMastered of $totalTracked weak-area questions mastered!",
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }

                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = if (masteryFraction >= 0.7f) MintGreen.copy(alpha = 0.15f) else SunshineYellow.copy(alpha = 0.15f)
                                ) {
                                    Text(
                                        text = "${(masteryFraction * 100).toInt()}%",
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 18.sp,
                                        color = if (masteryFraction >= 0.7f) MintGreen else Color(0xFFB45309),
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            LinearProgressIndicator(
                                progress = { masteryFraction },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(8.dp)
                                    .clip(RoundedCornerShape(4.dp)),
                                color = MintGreen,
                                trackColor = MaterialTheme.colorScheme.surfaceVariant,
                                strokeCap = StrokeCap.Round
                            )

                            Spacer(modifier = Modifier.height(14.dp))

                            // 3 Mini Stat Chips
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                MiniRevisionMetric("Due Now", "${dueRevisions.size}", CoralOrange, Modifier.weight(1f))
                                MiniRevisionMetric("In Training", "${allRevisions.filter { !it.isMastered }.size}", PrimaryBlue, Modifier.weight(1f))
                                MiniRevisionMetric("Mastered", "$totalMastered 🏆", MintGreen, Modifier.weight(1f))
                            }
                        }
                    }
                }

                // Dedicated Start Revision Session CTA
                item {
                    if (dueRevisions.isNotEmpty()) {
                        Button(
                            onClick = { isPracticingSession = true },
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(54.dp)
                                .testTag("start_revision_session_btn")
                        ) {
                            Icon(imageVector = Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(22.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Start Smart Revision Session (${dueRevisions.size} Due)",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    } else {
                        Card(
                            shape = RoundedCornerShape(18.dp),
                            colors = CardDefaults.cardColors(containerColor = MintGreen.copy(alpha = 0.1f)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = MintGreen,
                                    modifier = Modifier.size(28.dp)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = "All Caught Up, Superstar!",
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF065F46),
                                        fontSize = 14.sp
                                    )
                                    Text(
                                        text = "No questions are currently due for review. Great retention!",
                                        fontSize = 12.sp,
                                        color = Color(0xFF047857)
                                    )
                                }
                            }
                        }
                    }
                }

                // Filter Tabs
                item {
                    TabRow(
                        selectedTabIndex = selectedTab,
                        containerColor = MaterialTheme.colorScheme.surface,
                        contentColor = PrimaryBlue
                    ) {
                        tabs.forEachIndexed { index, title ->
                            Tab(
                                selected = selectedTab == index,
                                onClick = { selectedTab = index },
                                text = {
                                    Text(
                                        text = title,
                                        fontSize = 12.sp,
                                        fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal
                                    )
                                }
                            )
                        }
                    }
                }

                // Tab Content: Question Cards
                val displayedList = when (selectedTab) {
                    0 -> dueRevisions
                    1 -> allRevisions.filter { !it.isMastered }
                    else -> masteredRevisions
                }

                if (displayedList.isEmpty()) {
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = "✨", fontSize = 36.sp)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = when (selectedTab) {
                                    0 -> "No questions due right now!"
                                    1 -> "No questions currently in training."
                                    else -> "Complete 2 correct revision reviews to master questions!"
                                },
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                } else {
                    items(displayedList) { item ->
                        RevisionQuestionCard(
                            item = item,
                            onPractice = { singlePracticingItem = item }
                        )
                    }

                    if (selectedTab == 2 && masteredRevisions.isNotEmpty()) {
                        item {
                            OutlinedButton(
                                onClick = onClearMastered,
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 8.dp)
                            ) {
                                Icon(imageVector = Icons.Default.DeleteSweep, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Clear Mastered List", fontSize = 12.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MiniRevisionMetric(
    label: String,
    value: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = color.copy(alpha = 0.12f),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = value, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = color)
            Text(text = label, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
fun RevisionQuestionCard(
    item: RevisionItem,
    onPractice: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Source & Stage row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = PrimaryBlue.copy(alpha = 0.1f)
                ) {
                    Text(
                        text = "${item.bookTitle} • ${item.chapterTitle}",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryBlue,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                        maxLines = 1
                    )
                }

                if (item.isMastered) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MintGreen.copy(alpha = 0.15f)
                    ) {
                        Text(
                            text = "MASTERED 🏆",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = MintGreen,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                        )
                    }
                } else {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = CoralOrange.copy(alpha = 0.15f)
                    ) {
                        Text(
                            text = "Missed ${item.incorrectCount}x",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = CoralOrange,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = item.questionText,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Mastery Streak Progress (need 2 consecutive correct to master)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "Mastery Progress: ", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    repeat(2) { index ->
                        val isAchieved = item.masteryStreak > index || item.isMastered
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = if (isAchieved) SunshineYellow else Color.LightGray,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                if (!item.isMastered) {
                    Button(
                        onClick = onPractice,
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text("Practice", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun ActiveRevisionSessionRunner(
    revisionQueue: List<RevisionItem>,
    onAnswerQuestion: (RevisionItem, Boolean) -> Unit,
    onFinishSession: () -> Unit
) {
    var currentIndex by remember { mutableIntStateOf(0) }
    var selectedOption by remember { mutableStateOf<String?>(null) }
    var isSubmitted by remember { mutableStateOf(false) }
    var correctInSession by remember { mutableIntStateOf(0) }
    var isSessionComplete by remember { mutableStateOf(false) }

    val currentItem = revisionQueue.getOrNull(currentIndex)
    val options = remember(currentItem) {
        parseOptions(currentItem?.optionsJson ?: "[]")
    }

    if (isSessionComplete || currentItem == null) {
        RevisionSessionScorecard(
            totalAnswered = revisionQueue.size,
            correctCount = correctInSession,
            onClose = onFinishSession
        )
        return
    }

    Surface(
        color = MaterialTheme.colorScheme.background,
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Top Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onFinishSession) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Exit Session")
                }
                Text(
                    text = "Smart Revision • Question ${currentIndex + 1} of ${revisionQueue.size}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = SunshineYellow.copy(alpha = 0.15f)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(imageVector = Icons.Default.Star, contentDescription = null, tint = SunshineYellow, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "$correctInSession", fontWeight = FontWeight.Bold, color = Color(0xFFB45309))
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            LinearProgressIndicator(
                progress = { (currentIndex + 1).toFloat() / revisionQueue.size },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = PrimaryBlue,
                trackColor = MaterialTheme.colorScheme.surfaceVariant,
                strokeCap = StrokeCap.Round
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Question Card
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = PrimaryBlue.copy(alpha = 0.1f)
                        ) {
                            Text(
                                text = "From: ${currentItem.bookTitle}",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = PrimaryBlue,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = CoralOrange.copy(alpha = 0.12f)
                        ) {
                            Text(
                                text = "Previously Missed",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = CoralOrange,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = currentItem.questionText,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Options
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                options.forEach { option ->
                    val isSelected = selectedOption == option
                    val isCorrect = option == currentItem.correctAnswer

                    val (cardBg, borderColor, textColor) = when {
                        !isSubmitted && isSelected ->
                            Triple(PrimaryBlue.copy(alpha = 0.12f), PrimaryBlue, PrimaryBlue)
                        isSubmitted && isCorrect ->
                            Triple(MintGreen.copy(alpha = 0.15f), MintGreen, Color(0xFF047857))
                        isSubmitted && isSelected && !isCorrect ->
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
                            .clickable(enabled = !isSubmitted) {
                                selectedOption = option
                            }
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

                            if (isSubmitted) {
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

            // Explanation Card
            AnimatedVisibility(visible = isSubmitted) {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (selectedOption == currentItem.correctAnswer) MintGreen.copy(alpha = 0.1f) else Color(0xFFFEF3C7)
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = if (selectedOption == currentItem.correctAnswer) Icons.Default.EmojiEvents else Icons.Default.Lightbulb,
                                contentDescription = null,
                                tint = if (selectedOption == currentItem.correctAnswer) Color(0xFF047857) else Color(0xFFB45309),
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (selectedOption == currentItem.correctAnswer) "🎉 Revision Mastered Step! (+Stars)" else "💡 Remember for next time:",
                                fontWeight = FontWeight.Bold,
                                color = if (selectedOption == currentItem.correctAnswer) Color(0xFF047857) else Color(0xFFB45309)
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = currentItem.explanation,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Action Button
            if (!isSubmitted) {
                Button(
                    onClick = {
                        if (selectedOption != null) {
                            isSubmitted = true
                            val isCorrect = selectedOption == currentItem.correctAnswer
                            if (isCorrect) correctInSession++
                            onAnswerQuestion(currentItem, isCorrect)
                        }
                    },
                    enabled = selectedOption != null,
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                ) {
                    Text("Check Revision Answer", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            } else {
                Button(
                    onClick = {
                        if (currentIndex < revisionQueue.size - 1) {
                            currentIndex++
                            selectedOption = null
                            isSubmitted = false
                        } else {
                            isSessionComplete = true
                        }
                    },
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                ) {
                    Text(
                        text = if (currentIndex < revisionQueue.size - 1) "Next Revision Question ➡️" else "Finish Session 🏆",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}

@Composable
fun RevisionSessionScorecard(
    totalAnswered: Int,
    correctCount: Int,
    onClose: () -> Unit
) {
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
                    .background(PrimaryBlue.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "🧠", fontSize = 48.sp)
            }

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = "Smart Revision Complete!",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Spaced repetition helps cement knowledge permanently into long-term memory.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "$correctCount / $totalAnswered", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = MintGreen)
                        Text(text = "Correct", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "+${correctCount * 10} ⭐", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = SunshineYellow)
                        Text(text = "Stars Earned", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = onClose,
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Text("Return to Learning Hub", fontWeight = FontWeight.Bold)
            }
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
