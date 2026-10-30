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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ChildProfile
import com.example.ui.theme.CoralOrange
import com.example.ui.theme.MintGreen
import com.example.ui.theme.PrimaryBlue
import com.example.ui.theme.SunshineYellow

@Composable
fun ChildHeader(
    child: ChildProfile,
    elapsedSeconds: Int,
    dailyLimitMinutes: Int,
    onAvatarClick: () -> Unit,
    onParentZoneClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val remainingMinutes = maxOf(0, dailyLimitMinutes - (elapsedSeconds / 60))

    Surface(
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 2.dp,
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Child Avatar & Name
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(24.dp))
                    .clickable { onAvatarClick() }
                    .padding(4.dp)
                    .testTag("child_avatar_header")
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(PrimaryBlue.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = avatarEmoji(child.avatarKey),
                        fontSize = 24.sp
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Column {
                    Text(
                        text = child.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = child.gradeLevel,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Stats: Stars, Streak & Screen Time
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Stars Chip
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = SunshineYellow.copy(alpha = 0.15f)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Stars",
                            tint = SunshineYellow,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = "${child.totalStars}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = Color(0xFFB45309)
                        )
                    }
                }

                // Streak Chip
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = CoralOrange.copy(alpha = 0.15f)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocalFireDepartment,
                            contentDescription = "Streak",
                            tint = CoralOrange,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(
                            text = "${child.currentStreakDays}d",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = CoralOrange
                        )
                    }
                }

                // Screen Time Remaining Chip
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = if (remainingMinutes > 10) MintGreen.copy(alpha = 0.15f) else CoralOrange.copy(alpha = 0.15f)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Timer,
                            contentDescription = "Screen Time",
                            tint = if (remainingMinutes > 10) MintGreen else CoralOrange,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = "${remainingMinutes}m",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (remainingMinutes > 10) Color(0xFF047857) else CoralOrange
                        )
                    }
                }

                // Parent Portal Button
                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier
                        .clip(CircleShape)
                        .clickable { onParentZoneClick() }
                        .testTag("parent_portal_button")
                ) {
                    Box(
                        modifier = Modifier.padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = "Parent Settings",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }
}

fun avatarEmoji(key: String): String = when (key.lowercase()) {
    "owl" -> "🦉"
    "fox" -> "🦊"
    "bear" -> "🐻"
    "dino" -> "🦖"
    "star" -> "⭐"
    "bunny" -> "🐰"
    "lion" -> "🦁"
    "panda" -> "🐼"
    "robot" -> "🤖"
    else -> "🧒"
}
