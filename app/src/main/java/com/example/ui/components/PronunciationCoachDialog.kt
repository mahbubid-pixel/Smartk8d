package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.MintGreen
import com.example.ui.theme.PrimaryBlue
import com.example.ui.theme.SunshineYellow

@Composable
fun PronunciationCoachDialog(
    isOpen: Boolean,
    word: String,
    onListenWord: (String) -> Unit,
    onDismiss: () -> Unit
) {
    if (!isOpen) return

    var isPracticed by remember { mutableStateOf(false) }

    // Friendly syllable splitting heuristic
    val syllables = word.chunked(3).joinToString(" • ")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "🗣️ Pronunciation Coach",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Listen carefully, then practice saying it aloud!",
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Word Hero Card
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = PrimaryBlue.copy(alpha = 0.1f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = word.replaceFirstChar { it.uppercase() },
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = PrimaryBlue
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "Syllables: $syllables",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = FontWeight.SemiBold
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        FilledIconButton(
                            onClick = { onListenWord(word) },
                            shape = CircleShape,
                            colors = IconButtonDefaults.filledIconButtonColors(containerColor = PrimaryBlue),
                            modifier = Modifier
                                .size(54.dp)
                                .testTag("coach_listen_btn")
                        ) {
                            Icon(
                                imageVector = Icons.Default.VolumeUp,
                                contentDescription = "Listen to pronunciation",
                                tint = Color.White,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Tap to Listen",
                            fontSize = 12.sp,
                            color = PrimaryBlue,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Practice Button
                Button(
                    onClick = {
                        isPracticed = true
                    },
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MintGreen),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("coach_practice_btn")
                ) {
                    Icon(
                        imageVector = Icons.Default.Mic,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isPracticed) "Practiced! Try Again" else "I Said It Aloud!",
                        fontWeight = FontWeight.Bold
                    )
                }

                AnimatedVisibility(visible = isPracticed) {
                    Column(
                        modifier = Modifier.padding(top = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(horizontalArrangement = Arrangement.Center) {
                            repeat(5) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = "Star",
                                    tint = SunshineYellow,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Fantastic pronunciation, superstar! 🌟",
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF047857),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Continue Reading")
            }
        }
    )
}
