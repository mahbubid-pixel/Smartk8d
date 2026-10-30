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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import com.example.data.model.ChildProfile
import com.example.data.model.ParentReward
import com.example.ui.components.avatarEmoji
import com.example.ui.theme.CheerfulIndigo
import com.example.ui.theme.MintGreen
import com.example.ui.theme.PrimaryBlue
import com.example.ui.theme.SunshineYellow

data class BadgeItem(
    val id: String,
    val title: String,
    val description: String,
    val iconEmoji: String,
    val isUnlocked: Boolean
)

data class CharacterBuddy(
    val id: String,
    val name: String,
    val avatarKey: String,
    val starsRequired: Int,
    val isUnlocked: Boolean
)

@Composable
fun RewardsGalleryScreen(
    child: ChildProfile,
    rewards: List<ParentReward>,
    onRedeemReward: (ParentReward) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Badges 🏅", "Buddies 🐾", "Parent Rewards 🎁")

    val badges = listOf(
        BadgeItem("first_book", "First Book Read", "Completed your very first storybook", "📖", isUnlocked = true),
        BadgeItem("streak_hero", "7-Day Streak", "Read every day for one whole week", "🔥", isUnlocked = child.currentStreakDays >= 4),
        BadgeItem("quiz_ace", "Quiz Champion", "Scored 100% on a chapter assessment", "🎯", isUnlocked = true),
        BadgeItem("space_whiz", "Cosmic Explorer", "Completed Mystery of the Solar System", "🚀", isUnlocked = true),
        BadgeItem("bangla_star", "বাংলা মাস্টার", "Learned Bengali poems and stories", "🕊️", isUnlocked = true),
        BadgeItem("math_wizard", "Geometry Genius", "Solved all shapes and counting puzzles", "📐", isUnlocked = child.totalStars > 100)
    )

    val buddies = listOf(
        CharacterBuddy("fox", "Felix Fox", "fox", 0, isUnlocked = true),
        CharacterBuddy("owl", "Ollie Owl", "owl", 50, isUnlocked = true),
        CharacterBuddy("bear", "Barnaby Bear", "bear", 100, isUnlocked = child.totalStars >= 100),
        CharacterBuddy("dino", "Rex Dino", "dino", 150, isUnlocked = child.totalStars >= 150),
        CharacterBuddy("robot", "Robo Pal", "robot", 200, isUnlocked = child.totalStars >= 200),
        CharacterBuddy("star", "Stella Star", "star", 250, isUnlocked = child.totalStars >= 250)
    )

    Surface(
        color = MaterialTheme.colorScheme.background,
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Top Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
                Text(
                    text = "${child.name}'s Rewards & Achievements",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            // Hero Star Vault Card
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.linearGradient(listOf(SunshineYellow, Color(0xFFD97706)))
                        )
                        .padding(20.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = "Current Star Balance",
                                color = Color.White.copy(alpha = 0.9f),
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "${child.totalStars}",
                                    fontSize = 36.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color.White
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(text = "⭐", fontSize = 28.sp)
                            }
                            Text(
                                text = "${child.currentStreakDays}-Day Streak • Keep it up!",
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.25f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "🏆", fontSize = 34.sp)
                        }
                    }
                }
            }

            // Tabs
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
                                fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal,
                                fontSize = 13.sp
                            )
                        }
                    )
                }
            }

            // Tab Content
            when (selectedTab) {
                0 -> {
                    // Badges Grid
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        contentPadding = PaddingValues(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(badges) { badge ->
                            Card(
                                shape = RoundedCornerShape(18.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (badge.isUnlocked) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                                ),
                                elevation = CardDefaults.cardElevation(defaultElevation = if (badge.isUnlocked) 2.dp else 0.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(14.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(54.dp)
                                            .clip(CircleShape)
                                            .background(if (badge.isUnlocked) SunshineYellow.copy(alpha = 0.15f) else Color.Gray.copy(alpha = 0.1f)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(text = badge.iconEmoji, fontSize = 28.sp)
                                    }

                                    Spacer(modifier = Modifier.height(8.dp))

                                    Text(
                                        text = badge.title,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        textAlign = TextAlign.Center
                                    )

                                    Text(
                                        text = badge.description,
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.padding(top = 2.dp)
                                    )

                                    Spacer(modifier = Modifier.height(8.dp))

                                    Surface(
                                        shape = RoundedCornerShape(10.dp),
                                        color = if (badge.isUnlocked) MintGreen.copy(alpha = 0.15f) else Color.Gray.copy(alpha = 0.15f)
                                    ) {
                                        Text(
                                            text = if (badge.isUnlocked) "UNLOCKED ✨" else "IN PROGRESS",
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (badge.isUnlocked) MintGreen else Color.Gray,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                1 -> {
                    // Buddies Grid
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        contentPadding = PaddingValues(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(buddies) { buddy ->
                            Card(
                                shape = RoundedCornerShape(18.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(14.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(56.dp)
                                            .clip(CircleShape)
                                            .background(PrimaryBlue.copy(alpha = 0.1f)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(text = avatarEmoji(buddy.avatarKey), fontSize = 32.sp)
                                    }

                                    Spacer(modifier = Modifier.height(6.dp))

                                    Text(
                                        text = buddy.name,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp
                                    )

                                    Text(
                                        text = if (buddy.isUnlocked) "Unlocked Avatar!" else "Requires ${buddy.starsRequired} ⭐",
                                        fontSize = 11.sp,
                                        color = if (buddy.isUnlocked) MintGreen else MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }

                2 -> {
                    // Real Family Rewards Shop
                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        item {
                            Text(
                                text = "Family Rewards configured by Parents. Redeem your hard-earned stars!",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        items(rewards) { reward ->
                            val canAfford = child.totalStars >= reward.starsCost
                            Card(
                                shape = RoundedCornerShape(18.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                                modifier = Modifier.fillMaxWidth()
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
                                                .background(SunshineYellow.copy(alpha = 0.15f)),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = when (reward.iconKey) {
                                                    "icecream" -> "🍦"
                                                    "game" -> "🎮"
                                                    "movie" -> "🎬"
                                                    "park" -> "🦁"
                                                    else -> "🎁"
                                                },
                                                fontSize = 24.sp
                                            )
                                        }

                                        Spacer(modifier = Modifier.width(12.dp))

                                        Column {
                                            Text(
                                                text = reward.title,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 14.sp
                                            )
                                            Text(
                                                text = reward.description,
                                                fontSize = 11.sp,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                            Text(
                                                text = "${reward.starsCost} Stars ⭐",
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFFB45309),
                                                modifier = Modifier.padding(top = 2.dp)
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.width(8.dp))

                                    Button(
                                        onClick = { onRedeemReward(reward) },
                                        enabled = canAfford,
                                        shape = RoundedCornerShape(12.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                                        modifier = Modifier.testTag("redeem_reward_${reward.id}")
                                    ) {
                                        Text(text = if (canAfford) "Redeem" else "Need ${reward.starsCost - child.totalStars} ⭐", fontSize = 12.sp)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
