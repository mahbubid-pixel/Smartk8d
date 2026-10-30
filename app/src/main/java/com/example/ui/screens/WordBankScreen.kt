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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ChildProfile
import com.example.data.model.VocabularyWord
import com.example.ui.theme.CheerfulIndigo
import com.example.ui.theme.CoralOrange
import com.example.ui.theme.MintGreen
import com.example.ui.theme.PrimaryBlue
import com.example.ui.theme.SunshineYellow

@Composable
fun WordBankScreen(
    child: ChildProfile,
    words: List<VocabularyWord>,
    onBack: () -> Unit,
    onSpeakWord: (String) -> Unit,
    onUpdateMastery: (VocabularyWord, Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedCategory by remember { mutableStateOf("All") }
    var searchQuery by remember { mutableStateOf("") }
    val categories = listOf("All", "Science", "Space", "Nature", "Bangla")

    val filteredWords = words.filter { word ->
        (selectedCategory == "All" || word.category.equals(selectedCategory, ignoreCase = true)) &&
                (searchQuery.isBlank() || word.word.contains(searchQuery, ignoreCase = true) ||
                        word.definition.contains(searchQuery, ignoreCase = true) ||
                        word.banglaMeaning.contains(searchQuery, ignoreCase = true))
    }

    val masteredCount = words.count { it.masteryLevel >= 3 }

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
                        modifier = Modifier.testTag("word_bank_back_btn")
                    ) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "📚 My Word Bank",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = PrimaryBlue
                        )
                        Text(
                            text = "${child.name}'s Vocabulary Treasury",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = SunshineYellow.copy(alpha = 0.2f),
                        modifier = Modifier.clip(RoundedCornerShape(12.dp))
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = Color(0xFFD97706),
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(
                                text = "${child.totalStars}",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF92400E)
                            )
                        }
                    }
                }
            }
        },
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Stats Hero Banner
            item {
                Card(
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(containerColor = CheerfulIndigo),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Vocabulary Power! 🚀",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Every word you read & collect builds your superpower.",
                                fontSize = 12.sp,
                                color = Color.White.copy(alpha = 0.85f)
                            )
                        }

                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "${words.size}",
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color.White
                                )
                                Text("Collected", fontSize = 10.sp, color = Color.White.copy(alpha = 0.8f))
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "$masteredCount",
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = SunshineYellow
                                )
                                Text("Mastered", fontSize = 10.sp, color = Color.White.copy(alpha = 0.8f))
                            }
                        }
                    }
                }
            }

            // Search Bar
            item {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Search word, meaning, or definition...") },
                    leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = null) },
                    shape = RoundedCornerShape(16.dp),
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("word_search_field")
                )
            }

            // Category Filter Chips
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    categories.forEach { cat ->
                        FilterChip(
                            selected = selectedCategory == cat,
                            onClick = { selectedCategory = cat },
                            label = { Text(cat) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = PrimaryBlue,
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }
            }

            // Word Cards List
            if (filteredWords.isEmpty()) {
                item {
                    Card(
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 20.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(30.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = "🔍", fontSize = 36.sp)
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = "No vocabulary words found here.",
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.titleMedium
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Open a storybook in the library and tap any key word to collect it!",
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            } else {
                items(filteredWords, key = { it.id }) { word ->
                    WordItemCard(
                        word = word,
                        onSpeak = { onSpeakWord(word.word) },
                        onPromoteMastery = {
                            val next = (word.masteryLevel % 3) + 1
                            onUpdateMastery(word, next)
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun WordItemCard(
    word: VocabularyWord,
    onSpeak: () -> Unit,
    onPromoteMastery: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("word_card_${word.word}")
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = word.word,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = PrimaryBlue
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = CoralOrange.copy(alpha = 0.15f)
                        ) {
                            Text(
                                text = word.category,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = CoralOrange,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                    Text(
                        text = word.syllables,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Surface(
                    shape = CircleShape,
                    color = PrimaryBlue.copy(alpha = 0.12f),
                    modifier = Modifier.size(40.dp)
                ) {
                    IconButton(onClick = onSpeak) {
                        Icon(
                            imageVector = Icons.Default.VolumeUp,
                            contentDescription = "Pronounce",
                            tint = PrimaryBlue,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = word.definition,
                fontSize = 13.sp,
                lineHeight = 18.sp,
                color = MaterialTheme.colorScheme.onSurface
            )

            if (word.banglaMeaning.isNotBlank()) {
                Spacer(modifier = Modifier.height(6.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "🇧🇩 ", fontSize = 12.sp)
                    Text(
                        text = word.banglaMeaning,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF047857)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Mastery indicator
                val (badgeLabel, badgeColor) = when (word.masteryLevel) {
                    3 -> "🌟 Mastered" to Color(0xFFD97706)
                    2 -> "🌿 Practiced" to MintGreen
                    else -> "🌱 Learning" to PrimaryBlue
                }

                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = badgeColor.copy(alpha = 0.15f)
                ) {
                    Text(
                        text = badgeLabel,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = badgeColor,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }

                Button(
                    onClick = onPromoteMastery,
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (word.masteryLevel >= 3) MintGreen.copy(alpha = 0.3f) else PrimaryBlue
                    ),
                    modifier = Modifier.height(34.dp)
                ) {
                    Text(
                        text = if (word.masteryLevel >= 3) "Mastered ⭐" else "Practice +10⭐",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (word.masteryLevel >= 3) Color(0xFF065F46) else Color.White
                    )
                }
            }
        }
    }
}
