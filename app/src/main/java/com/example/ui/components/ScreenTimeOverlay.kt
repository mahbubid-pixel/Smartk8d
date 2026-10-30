package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.HourglassBottom
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import com.example.ui.theme.CoralOrange
import com.example.ui.theme.PrimaryBlue

@Composable
fun ScreenTimeOverlay(
    isLocked: Boolean,
    onExtendMinutes: (Int) -> Unit,
    onSwitchProfile: () -> Unit,
    onVerifyPin: (String) -> Boolean
) {
    if (!isLocked) return

    var showPinDialog by remember { mutableStateOf(false) }

    Surface(
        color = MaterialTheme.colorScheme.background.copy(alpha = 0.97f),
        modifier = Modifier
            .fillMaxSize()
            .testTag("screen_time_locked_overlay")
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(96.dp)
                    .clip(CircleShape)
                    .background(CoralOrange.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "🌟",
                    fontSize = 48.sp
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Time for a Healthy Break!",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "You did an awesome job learning today! Rest your eyes, drink some water, and stretch your wings.",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Switch profile button
            Button(
                onClick = onSwitchProfile,
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.testTag("switch_profile_from_lock_btn")
            ) {
                Text("Switch Profile")
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Parent unlock / extension
            OutlinedButton(
                onClick = { showPinDialog = true },
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.testTag("parent_extend_time_btn")
            ) {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.size(6.dp))
                Text("Parent: Add 15 Minutes")
            }
        }
    }

    if (showPinDialog) {
        PinDialog(
            isOpen = showPinDialog,
            title = "Parent Unlock",
            subtitle = "Enter PIN to grant 15 minutes extra learning time",
            onDismiss = { showPinDialog = false },
            onSuccess = {
                showPinDialog = false
                onExtendMinutes(15)
            },
            onVerifyPin = onVerifyPin
        )
    }
}
