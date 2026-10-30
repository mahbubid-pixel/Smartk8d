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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ChildProfile
import com.example.ui.components.PinDialog
import com.example.ui.components.avatarEmoji
import com.example.ui.theme.CheerfulIndigo
import com.example.ui.theme.CoralOrange
import com.example.ui.theme.PrimaryBlue
import com.example.ui.theme.SunshineYellow

@Composable
fun ProfileSelectScreen(
    children: List<ChildProfile>,
    onSelectChild: (ChildProfile) -> Unit,
    onAddChild: (name: String, age: Int, grade: String, avatar: String) -> Unit,
    onOpenParentDashboard: () -> Unit,
    onVerifyPin: (String) -> Boolean,
    modifier: Modifier = Modifier
) {
    var showAddDialog by remember { mutableStateOf(false) }
    var showPinDialog by remember { mutableStateOf(false) }

    Surface(
        color = MaterialTheme.colorScheme.background,
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // App Brand Banner
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.linearGradient(
                            listOf(PrimaryBlue, CheerfulIndigo)
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "🚀", fontSize = 38.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "SmartKids Learning",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Text(
                text = "Who is learning today?",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 4.dp)
            )

            Spacer(modifier = Modifier.height(28.dp))

            // Child Profile Cards Grid
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(4.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(children) { child ->
                    ChildProfileCard(
                        child = child,
                        onClick = { onSelectChild(child) }
                    )
                }

                // Add Child Card
                item {
                    Card(
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(160.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .clickable { showAddDialog = true }
                            .testTag("add_child_profile_card")
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(54.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "Add Child",
                                    tint = PrimaryBlue,
                                    modifier = Modifier.size(28.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Add Child",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = PrimaryBlue
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Parent Portal Access Button
            OutlinedButton(
                onClick = { showPinDialog = true },
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("parent_dashboard_entry_btn")
            ) {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Parent Dashboard & Controls",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
        }
    }

    if (showPinDialog) {
        PinDialog(
            isOpen = showPinDialog,
            title = "Parent Dashboard",
            subtitle = "Enter PIN to access parent analytics and library manager (Default: 1234)",
            onDismiss = { showPinDialog = false },
            onSuccess = {
                showPinDialog = false
                onOpenParentDashboard()
            },
            onVerifyPin = onVerifyPin
        )
    }

    if (showAddDialog) {
        AddChildDialog(
            onDismiss = { showAddDialog = false },
            onConfirm = { name, age, grade, avatar ->
                onAddChild(name, age, grade, avatar)
                showAddDialog = false
            }
        )
    }
}

@Composable
fun ChildProfileCard(
    child: ChildProfile,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        modifier = modifier
            .fillMaxWidth()
            .height(160.dp)
            .clip(RoundedCornerShape(20.dp))
            .clickable { onClick() }
            .testTag("child_card_${child.id}")
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(PrimaryBlue.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = avatarEmoji(child.avatarKey),
                    fontSize = 32.sp
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = child.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Text(
                text = child.gradeLevel,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = SunshineYellow,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(3.dp))
                Text(
                    text = "${child.totalStars} Stars",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFB45309)
                )
            }
        }
    }
}

@Composable
fun AddChildDialog(
    onDismiss: () -> Unit,
    onConfirm: (name: String, age: Int, grade: String, avatar: String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var ageText by remember { mutableStateOf("7") }
    var grade by remember { mutableStateOf("Grade 2") }
    var selectedAvatar by remember { mutableStateOf("owl") }

    val avatars = listOf("owl", "fox", "bear", "dino", "star", "bunny", "lion", "panda")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Child Profile", fontWeight = FontWeight.Bold) },
        text = {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Child's Name") },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("new_child_name_input")
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = ageText,
                        onValueChange = { ageText = it },
                        label = { Text("Age") },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1f)
                    )

                    OutlinedTextField(
                        value = grade,
                        onValueChange = { grade = it },
                        label = { Text("Grade/Class") },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1.5f)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "Select Avatar:",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    avatars.take(5).forEach { avatar ->
                        val isSelected = selectedAvatar == avatar
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(if (isSelected) PrimaryBlue.copy(alpha = 0.25f) else Color.Transparent)
                                .clickable { selectedAvatar = avatar },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = avatarEmoji(avatar), fontSize = 24.sp)
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (name.isNotBlank()) {
                        val age = ageText.toIntOrNull() ?: 7
                        onConfirm(name, age, grade, selectedAvatar)
                    }
                },
                enabled = name.isNotBlank(),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
            ) {
                Text("Create Profile")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
