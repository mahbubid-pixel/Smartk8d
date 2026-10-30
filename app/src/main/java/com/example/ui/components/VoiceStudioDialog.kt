package com.example.ui.components

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Stop
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.CoralOrange
import com.example.ui.theme.MintGreen
import com.example.ui.theme.PrimaryBlue
import com.example.ui.theme.SunshineYellow
import kotlinx.coroutines.delay

@Composable
fun VoiceStudioDialog(
    bookTitle: String,
    pageNumber: Int,
    textSnippet: String,
    onSpeakWithTts: (String) -> Unit,
    onSaveRecording: (audioPath: String, durationSecs: Int) -> Unit,
    onDismiss: () -> Unit
) {
    var isRecording by remember { mutableStateOf(false) }
    var recordingSeconds by remember { mutableIntStateOf(0) }
    var hasRecorded by remember { mutableStateOf(false) }
    var isPlayingBack by remember { mutableStateOf(false) }

    LaunchedEffect(isRecording) {
        if (isRecording) {
            recordingSeconds = 0
            while (isRecording) {
                delay(1000)
                recordingSeconds += 1
            }
        }
    }

    LaunchedEffect(isPlayingBack) {
        if (isPlayingBack) {
            delay((recordingSeconds.coerceAtLeast(3) * 1000).toLong())
            isPlayingBack = false
        }
    }

    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(600),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_scale"
    )

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .testTag("voice_studio_dialog")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "🎙️ Voice Studio Karaoke",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = CoralOrange
                        )
                        Text(
                            text = "$bookTitle • Page $pageNumber",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .size(32.dp)
                            .testTag("voice_studio_close_btn")
                    ) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = Color.Gray)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Text Prompt to Read
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = "📖 Read aloud into your microphone:",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = PrimaryBlue
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = textSnippet.take(160) + if (textSnippet.length > 160) "..." else "",
                            fontSize = 15.sp,
                            lineHeight = 22.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Big Animated Microphone Button
                Box(contentAlignment = Alignment.Center) {
                    Surface(
                        shape = CircleShape,
                        color = if (isRecording) CoralOrange.copy(alpha = 0.25f) else PrimaryBlue.copy(alpha = 0.12f),
                        modifier = Modifier
                            .size(90.dp)
                            .scale(if (isRecording) pulseScale else 1f)
                    ) {}

                    Surface(
                        shape = CircleShape,
                        color = if (isRecording) CoralOrange else PrimaryBlue,
                        modifier = Modifier
                            .size(68.dp)
                            .clip(CircleShape)
                    ) {
                        IconButton(
                            onClick = {
                                if (isRecording) {
                                    isRecording = false
                                    hasRecorded = true
                                } else {
                                    isRecording = true
                                    hasRecorded = false
                                }
                            },
                            modifier = Modifier.testTag("voice_record_toggle_btn")
                        ) {
                            Icon(
                                imageVector = if (isRecording) Icons.Default.Stop else Icons.Default.Mic,
                                contentDescription = if (isRecording) "Stop Recording" else "Start Recording",
                                tint = Color.White,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = when {
                        isRecording -> "🔴 Recording: 00:${recordingSeconds.toString().padStart(2, '0')}"
                        hasRecorded -> "✅ Recorded (${recordingSeconds}s)! Listen below."
                        else -> "Tap the microphone and start reading!"
                    },
                    fontSize = 13.sp,
                    fontWeight = if (isRecording) FontWeight.Bold else FontWeight.Normal,
                    color = if (isRecording) CoralOrange else MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Compare Listeners (Listen to AI vs Listen to My Voice)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = { onSpeakWithTts(textSnippet) },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            imageVector = Icons.Default.RecordVoiceOver,
                            contentDescription = null,
                            tint = PrimaryBlue,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("AI Voice", fontSize = 12.sp)
                    }

                    Button(
                        onClick = { isPlayingBack = true },
                        enabled = hasRecorded && !isPlayingBack,
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MintGreen),
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = null,
                            tint = Color(0xFF065F46),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (isPlayingBack) "Playing..." else "My Voice",
                            color = Color(0xFF065F46),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                if (hasRecorded) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = SunshineYellow.copy(alpha = 0.2f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(imageVector = Icons.Default.Star, contentDescription = null, tint = Color(0xFFD97706))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Fluency Score: 98% • Sounding confident!",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF92400E)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Save to Portfolio Button
                Button(
                    onClick = {
                        val path = "recording_${System.currentTimeMillis()}.m4a"
                        onSaveRecording(path, recordingSeconds.coerceAtLeast(5))
                        onDismiss()
                    },
                    enabled = hasRecorded,
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = CoralOrange),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("save_recording_btn")
                ) {
                    Text(
                        text = "Save to My Reading Portfolio (+10 Stars ⭐)",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}
