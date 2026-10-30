package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.FastForward
import androidx.compose.material.icons.filled.FastRewind
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
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
import com.example.ui.theme.MintGreen
import com.example.ui.theme.PrimaryBlue

@Composable
fun AudioPlayerBar(
    isPlaying: Boolean,
    currentSentenceIndex: Int,
    totalSentences: Int,
    speechRate: Float,
    isAutoPageTurn: Boolean,
    languageCode: String,
    onPlayPause: () -> Unit,
    onPreviousSentence: () -> Unit,
    onNextSentence: () -> Unit,
    onRepeatSentence: () -> Unit,
    onSpeedChange: (Float) -> Unit,
    onToggleAutoPageTurn: () -> Unit,
    onOpenCoach: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showSpeedMenu by remember { mutableStateOf(false) }

    Surface(
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 8.dp,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp)
        ) {
            // Top mini indicator row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Sentence position & Language
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.VolumeUp,
                        contentDescription = "Audio status",
                        tint = if (isPlaying) PrimaryBlue else MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (totalSentences > 0) "Sentence ${currentSentenceIndex + 1} of $totalSentences" else "Audio Ready",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = PrimaryBlue.copy(alpha = 0.1f)
                    ) {
                        Text(
                            text = if (languageCode == "bn") "বাংলা কন্ঠ" else "English Voice",
                            fontSize = 10.sp,
                            color = PrimaryBlue,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }

                // Auto-Turn & Pronounce Coach Badges
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    // Pronounce Coach
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MintGreen.copy(alpha = 0.15f),
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { onOpenCoach() }
                            .testTag("pronounce_coach_btn")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.RecordVoiceOver,
                                contentDescription = "Coach",
                                tint = MintGreen,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(
                                text = "Coach",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = MintGreen
                            )
                        }
                    }

                    // Auto-Turn Chip
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (isAutoPageTurn) PrimaryBlue.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { onToggleAutoPageTurn() }
                    ) {
                        Text(
                            text = if (isAutoPageTurn) "Auto-Turn ON" else "Auto-Turn OFF",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (isAutoPageTurn) PrimaryBlue else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Main playback controls row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Speed Button
                Box {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { showSpeedMenu = true }
                            .testTag("speed_selector_btn")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Speed,
                                contentDescription = "Speed",
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(
                                text = "${speechRate}x",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    DropdownMenu(
                        expanded = showSpeedMenu,
                        onDismissRequest = { showSpeedMenu = false }
                    ) {
                        listOf(0.5f, 0.75f, 1.0f, 1.25f, 1.5f).forEach { rate ->
                            DropdownMenuItem(
                                text = { Text("${rate}x") },
                                onClick = {
                                    onSpeedChange(rate)
                                    showSpeedMenu = false
                                }
                            )
                        }
                    }
                }

                // Previous Sentence
                IconButton(
                    onClick = onPreviousSentence,
                    modifier = Modifier
                        .size(44.dp)
                        .testTag("prev_sentence_btn")
                ) {
                    Icon(
                        imageVector = Icons.Default.FastRewind,
                        contentDescription = "Previous sentence",
                        modifier = Modifier.size(24.dp)
                    )
                }

                // Large Main Play / Pause Button
                FilledIconButton(
                    onClick = onPlayPause,
                    shape = CircleShape,
                    colors = IconButtonDefaults.filledIconButtonColors(containerColor = PrimaryBlue),
                    modifier = Modifier
                        .size(56.dp)
                        .testTag("play_pause_btn")
                ) {
                    Icon(
                        imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = if (isPlaying) "Pause" else "Play",
                        tint = Color.White,
                        modifier = Modifier.size(32.dp)
                    )
                }

                // Next Sentence
                IconButton(
                    onClick = onNextSentence,
                    modifier = Modifier
                        .size(44.dp)
                        .testTag("next_sentence_btn")
                ) {
                    Icon(
                        imageVector = Icons.Default.FastForward,
                        contentDescription = "Next sentence",
                        modifier = Modifier.size(24.dp)
                    )
                }

                // Repeat Sentence
                IconButton(
                    onClick = onRepeatSentence,
                    modifier = Modifier
                        .size(44.dp)
                        .testTag("repeat_sentence_btn")
                ) {
                    Icon(
                        imageVector = Icons.Default.Replay,
                        contentDescription = "Repeat sentence",
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
        }
    }
}
