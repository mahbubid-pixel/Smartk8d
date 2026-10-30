package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Book
import com.example.data.model.ChildProfile
import com.example.data.model.ScreenTimeConfig
import com.example.ui.components.BookCard
import com.example.ui.components.ChildHeader
import com.example.ui.theme.CheerfulIndigo
import com.example.ui.theme.CoralOrange
import com.example.ui.theme.MintGreen
import com.example.ui.theme.PrimaryBlue
import com.example.ui.theme.SunshineYellow

@Composable
fun ChildHomeScreen(
    child: ChildProfile,
    books: List<Book>,
    screenTimeConfig: ScreenTimeConfig?,
    elapsedSeconds: Int,
    dueRevisionCount: Int = 0,
    collectedWordsCount: Int = 0,
    dailyQuestChestClaimed: Boolean = false,
    onBookClick: (Book) -> Unit,
    onQuizClick: (Book) -> Unit,
    onOpenRewards: () -> Unit,
    onOpenSmartRevision: () -> Unit = {},
    onOpenWordBank: () -> Unit = {},
    onClaimDailyQuest: () -> Unit = {},
    onSwitchProfile: () -> Unit,
    onParentZoneClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedCategory by remember { mutableStateOf("All") }
    val categories = listOf("All", "Story & Adventure", "Science & Space", "Bangla Learning", "Math Adventures")

    val filteredBooks = if (selectedCategory == "All") {
        books
    } else {
        books.filter { it.subject.contains(selectedCategory.split(" ")[0], ignoreCase = true) }
    }

    val featuredBook = books.firstOrNull()

    Column(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Child Header
        ChildHeader(
            child = child,
            elapsedSeconds = elapsedSeconds,
            dailyLimitMinutes = screenTimeConfig?.dailyLimitMinutes ?: 45,
            onAvatarClick = onSwitchProfile,
            onParentZoneClick = onParentZoneClick
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Welcome & Daily Learning Goal
            item {
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
                            Column {
                                Text(
                                    text = "Ready to learn, ${child.name}? ✨",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Daily Goal: ${child.dailyGoalMinutes} mins of reading",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                // Smart Revision Hub Button
                                Surface(
                                    shape = RoundedCornerShape(14.dp),
                                    color = if (dueRevisionCount > 0) CoralOrange.copy(alpha = 0.15f) else PrimaryBlue.copy(alpha = 0.12f),
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(14.dp))
                                        .clickable { onOpenSmartRevision() }
                                        .testTag("revision_hub_chip")
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(text = "🧠", fontSize = 13.sp)
                                        Spacer(modifier = Modifier.width(3.dp))
                                        Text(
                                            text = if (dueRevisionCount > 0) "$dueRevisionCount Due" else "Revision",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 11.sp,
                                            color = if (dueRevisionCount > 0) CoralOrange else PrimaryBlue
                                        )
                                    }
                                }

                                // Word Bank Hub Button
                                Surface(
                                    shape = RoundedCornerShape(14.dp),
                                    color = MintGreen.copy(alpha = 0.2f),
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(14.dp))
                                        .clickable { onOpenWordBank() }
                                        .testTag("word_bank_hub_chip")
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(text = "📚", fontSize = 13.sp)
                                        Spacer(modifier = Modifier.width(3.dp))
                                        Text(
                                            text = "$collectedWordsCount Words",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 11.sp,
                                            color = Color(0xFF065F46)
                                        )
                                    }
                                }

                                // Quick Rewards Button
                                Surface(
                                    shape = RoundedCornerShape(14.dp),
                                    color = SunshineYellow.copy(alpha = 0.15f),
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(14.dp))
                                        .clickable { onOpenRewards() }
                                        .testTag("rewards_hub_chip")
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.EmojiEvents,
                                            contentDescription = "Rewards",
                                            tint = SunshineYellow,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(3.dp))
                                        Text(
                                            text = "Rewards",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 11.sp,
                                            color = Color(0xFFB45309)
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        val progressFraction = minOf(1f, (elapsedSeconds / 60f) / maxOf(1, child.dailyGoalMinutes))
                        LinearProgressIndicator(
                            progress = { progressFraction },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp)),
                            color = MintGreen,
                            trackColor = MaterialTheme.colorScheme.surfaceVariant,
                            strokeCap = StrokeCap.Round
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "${elapsedSeconds / 60} of ${child.dailyGoalMinutes} mins completed today",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // Daily Learning Adventure Quest Card
            item {
                val goal1Done = elapsedSeconds >= 60 || books.isNotEmpty()
                val goal2Done = dueRevisionCount == 0
                val goal3Done = collectedWordsCount > 0
                val completedGoalsCount = (if (goal1Done) 1 else 0) + (if (goal2Done) 1 else 0) + (if (goal3Done) 1 else 0)

                Card(
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                    modifier = Modifier.fillMaxWidth().testTag("daily_quest_card")
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = "🗺️", fontSize = 20.sp)
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(
                                        text = "Daily Adventure Quest",
                                        fontWeight = FontWeight.Bold,
                                        style = MaterialTheme.typography.titleSmall,
                                        color = PrimaryBlue
                                    )
                                    Text(
                                        text = "$completedGoalsCount/3 Quests Conquered",
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }

                            if (completedGoalsCount == 3 && !dailyQuestChestClaimed) {
                                Button(
                                    onClick = onClaimDailyQuest,
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = SunshineYellow),
                                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                                    modifier = Modifier.testTag("claim_chest_btn")
                                ) {
                                    Text(
                                        text = "🎁 Open Chest! +25⭐",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp,
                                        color = Color(0xFF92400E)
                                    )
                                }
                            } else if (dailyQuestChestClaimed) {
                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = MintGreen.copy(alpha = 0.2f)
                                ) {
                                    Text(
                                        text = "🏆 Chest Claimed!",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF065F46),
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // 3 Quest Pills
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            QuestPill(
                                icon = "📖",
                                label = "Read Story",
                                isDone = goal1Done,
                                modifier = Modifier.weight(1f)
                            )
                            QuestPill(
                                icon = "🧠",
                                label = "Smart Revise",
                                isDone = goal2Done,
                                modifier = Modifier.weight(1f)
                            )
                            QuestPill(
                                icon = "📚",
                                label = "Word Bank",
                                isDone = goal3Done,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }

            // Smart Revision Spaced Repetition Banner
            if (dueRevisionCount > 0) {
                item {
                    Card(
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = CoralOrange.copy(alpha = 0.12f)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(20.dp))
                            .clickable { onOpenSmartRevision() }
                            .testTag("smart_revision_home_card")
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.weight(1f)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(46.dp)
                                        .clip(CircleShape)
                                        .background(CoralOrange.copy(alpha = 0.2f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = "🧠", fontSize = 24.sp)
                                }

                                Spacer(modifier = Modifier.width(12.dp))

                                Column {
                                    Text(
                                        text = "Smart Revision ($dueRevisionCount Due)",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        color = CoralOrange
                                    )
                                    Text(
                                        text = "Review missed questions & turn them into stars!",
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }

                            Button(
                                onClick = onOpenSmartRevision,
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = CoralOrange),
                                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp)
                            ) {
                                Text("Practice", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            }
                        }
                    }
                }
            }

            // Featured Hero Story Banner
            if (featuredBook != null) {
                item {
                    Card(
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp)
                            .clip(RoundedCornerShape(24.dp))
                            .clickable { onBookClick(featuredBook) }
                            .testTag("featured_book_banner")
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.linearGradient(
                                        listOf(PrimaryBlue, CheerfulIndigo)
                                    )
                                )
                                .padding(18.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth(0.72f)
                                    .align(Alignment.CenterStart)
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = Color.White.copy(alpha = 0.25f)
                                ) {
                                    Text(
                                        text = "⭐ Continue Reading",
                                        color = Color.White,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.height(6.dp))

                                Text(
                                    text = featuredBook.title,
                                    color = Color.White,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    maxLines = 1
                                )

                                Text(
                                    text = "Tap to listen with AI narration",
                                    color = Color.White.copy(alpha = 0.85f),
                                    fontSize = 12.sp,
                                    modifier = Modifier.padding(top = 2.dp)
                                )
                            }

                            // Big Round Play Button
                            Box(
                                modifier = Modifier
                                    .size(50.dp)
                                    .clip(CircleShape)
                                    .background(Color.White)
                                    .align(Alignment.CenterEnd),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PlayArrow,
                                    contentDescription = "Resume Reading",
                                    tint = PrimaryBlue,
                                    modifier = Modifier.size(30.dp)
                                )
                            }
                        }
                    }
                }
            }

            // Subject Category Filter Chips
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    categories.forEach { cat ->
                        val isSelected = selectedCategory == cat
                        FilterChip(
                            selected = isSelected,
                            onClick = { selectedCategory = cat },
                            label = { Text(cat, fontSize = 12.sp) },
                            shape = RoundedCornerShape(16.dp),
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = PrimaryBlue.copy(alpha = 0.15f),
                                selectedLabelColor = PrimaryBlue
                            )
                        )
                    }
                }
            }

            // Section Title
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "My Digital Library (${filteredBooks.size})",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Books Shelf
            items(filteredBooks) { book ->
                BookCard(
                    book = book,
                    onReadClick = { onBookClick(book) },
                    onQuizClick = { onQuizClick(book) }
                )
            }
        }
    }
}

@Composable
private fun QuestPill(
    icon: String,
    label: String,
    isDone: Boolean,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = if (isDone) MintGreen.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(vertical = 10.dp, horizontal = 6.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = if (isDone) "✅" else icon, fontSize = 18.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = label,
                fontSize = 11.sp,
                fontWeight = if (isDone) FontWeight.Bold else FontWeight.Medium,
                color = if (isDone) Color(0xFF065F46) else MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
