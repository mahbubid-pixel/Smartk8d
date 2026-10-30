package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BookmarkAdd
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.VocabularyWord
import com.example.ui.theme.CheerfulIndigo
import com.example.ui.theme.CoralOrange
import com.example.ui.theme.MintGreen
import com.example.ui.theme.PrimaryBlue
import com.example.ui.theme.SunshineYellow

@Composable
fun WordDictionaryDialog(
    word: VocabularyWord,
    isAlreadyCollected: Boolean,
    onSpeakWord: (String) -> Unit,
    onCollectWord: (VocabularyWord) -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
                .testTag("word_dictionary_dialog")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                // Header with Category Chip and Close
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = CheerfulIndigo.copy(alpha = 0.15f),
                        modifier = Modifier.clip(RoundedCornerShape(12.dp))
                    ) {
                        Text(
                            text = "📖 ${word.category} Explorer",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = CheerfulIndigo,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .size(32.dp)
                            .testTag("word_dialog_close_btn")
                    ) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = Color.Gray)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Word Title & Speak Button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = word.word,
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = PrimaryBlue
                        )
                        Text(
                            text = word.syllables,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Surface(
                        shape = CircleShape,
                        color = PrimaryBlue,
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                    ) {
                        IconButton(
                            onClick = { onSpeakWord(word.word) },
                            modifier = Modifier.testTag("word_pronounce_btn")
                        ) {
                            Icon(
                                imageVector = Icons.Default.VolumeUp,
                                contentDescription = "Listen to pronunciation",
                                tint = Color.White
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Child-Friendly Definition Box
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = SunshineYellow.copy(alpha = 0.18f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = "💡 What it means:",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFB45309)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = word.definition,
                            fontSize = 14.sp,
                            lineHeight = 20.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                // Bilingual Meaning (Bangla)
                if (word.banglaMeaning.isNotBlank()) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = MintGreen.copy(alpha = 0.15f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "🇧🇩", fontSize = 16.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = "বাংলা অর্থ (Bangla Meaning):",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF065F46)
                                )
                                Text(
                                    text = word.banglaMeaning,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF047857)
                                )
                            }
                        }
                    }
                }

                // Example Sentence
                if (word.exampleSentence.isNotBlank()) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = "💬 In a sentence:",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "\"${word.exampleSentence}\"",
                                fontSize = 13.sp,
                                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Bottom Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Done")
                    }

                    Button(
                        onClick = {
                            if (!isAlreadyCollected) {
                                onCollectWord(word)
                            }
                        },
                        enabled = !isAlreadyCollected,
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isAlreadyCollected) MintGreen else PrimaryBlue
                        ),
                        modifier = Modifier
                            .weight(1.5f)
                            .testTag("collect_word_btn")
                    ) {
                        Icon(
                            imageVector = if (isAlreadyCollected) Icons.Default.Check else Icons.Default.BookmarkAdd,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (isAlreadyCollected) "Collected! ⭐" else "Save Word +5⭐",
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
