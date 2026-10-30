package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.DocumentScanner
import androidx.compose.material.icons.filled.LibraryBooks
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
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
import com.example.ui.theme.MintGreen
import com.example.ui.theme.PrimaryBlue

@Composable
fun ParentBookUploadScreen(
    children: List<ChildProfile>,
    onCreateBook: (title: String, author: String, subject: String, desc: String, text: String, assignedChildId: Long?) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var title by remember { mutableStateOf("") }
    var author by remember { mutableStateOf("") }
    var subject by remember { mutableStateOf("Story & Adventure") }
    var description by remember { mutableStateOf("") }
    var bookText by remember { mutableStateOf("") }
    var selectedChildId by remember { mutableStateOf<Long?>(null) }

    var showSubjectDropdown by remember { mutableStateOf(false) }
    var showChildDropdown by remember { mutableStateOf(false) }

    val subjectOptions = listOf(
        "Story & Adventure",
        "Science & Space",
        "Bangla Learning",
        "Math Adventures",
        "English Phonics",
        "History & World"
    )

    Surface(
        color = MaterialTheme.colorScheme.background,
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // Header Bar
            Surface(
                color = MaterialTheme.colorScheme.surface,
                shadowElevation = 2.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                    Text(
                        text = "Upload & Create Book",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Info Banner
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = PrimaryBlue.copy(alpha = 0.1f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = null, tint = PrimaryBlue)
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Smart Page Processor will automatically detect chapters, split pages, enable AI TTS audio reading, and build quizzes!",
                            fontSize = 12.sp,
                            color = PrimaryBlue,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                // Sample Templates / Quick Import
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            title = "The Journey of the Seedling"
                            author = "Parent Studio"
                            subject = "Science & Space"
                            description = "A delightful biology tale of a small acorn becoming an ancient majestic oak tree."
                            bookText = "Deep beneath the soft brown soil, a little acorn named Pip rested quietly in the dark.\n\nWhen the warm spring rain arrived, Pip felt a magical surge of energy. A tender green sprout burst upward toward the bright sunshine!\n\nYear after year, Pip grew sturdy branches that sheltered singing bluebirds and cozy squirrels."
                        },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(imageVector = Icons.Default.DocumentScanner, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Sample PDF", fontSize = 12.sp)
                    }

                    OutlinedButton(
                        onClick = {
                            title = "রঙিন প্রজাপতির ডানা"
                            author = "বাবা-মা এর গল্প"
                            subject = "Bangla Learning"
                            description = "একটি সুন্দর বাংলা শিক্ষামূলক গল্প যা শিশুদের প্রকৃতির রূপ শেখায়।"
                            bookText = "এক বাগানে ছিল একটি ছোট্ট রঙিন প্রজাপতি। তার নাম ছিল টিয়া।\n\nটিয়া প্রতিদিন ফুলের মধু খেত আর রঙিন ডানা মেলে মিষ্টি মিষ্টি নাচ দেখাত।\n\nবাগান জুড়ে ছড়িয়ে থাকত আনন্দের সুর। শিশুরা সবাই টিয়ার সাথে বন্ধুত্ব করতে চাইল।"
                        },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(imageVector = Icons.Default.MenuBook, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Sample Bangla", fontSize = 12.sp)
                    }
                }

                // Title Input
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Book Title *") },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("upload_book_title_input")
                )

                // Author Input
                OutlinedTextField(
                    value = author,
                    onValueChange = { author = it },
                    label = { Text("Author / Creator") },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                // Subject Selector
                Box {
                    OutlinedTextField(
                        value = subject,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Subject / Category") },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showSubjectDropdown = true }
                    )

                    DropdownMenu(
                        expanded = showSubjectDropdown,
                        onDismissRequest = { showSubjectDropdown = false }
                    ) {
                        subjectOptions.forEach { opt ->
                            DropdownMenuItem(
                                text = { Text(opt) },
                                onClick = {
                                    subject = opt
                                    showSubjectDropdown = false
                                }
                            )
                        }
                    }
                }

                // Description Input
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Book Description / Learning Objective") },
                    maxLines = 2,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                // Child Assignment
                Box {
                    val assignedLabel = if (selectedChildId == null) "All Children" else children.find { it.id == selectedChildId }?.name ?: "All"
                    OutlinedTextField(
                        value = assignedLabel,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Assign to Child") },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showChildDropdown = true }
                    )

                    DropdownMenu(
                        expanded = showChildDropdown,
                        onDismissRequest = { showChildDropdown = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("All Children") },
                            onClick = {
                                selectedChildId = null
                                showChildDropdown = false
                            }
                        )
                        children.forEach { ch ->
                            DropdownMenuItem(
                                text = { Text(ch.name) },
                                onClick = {
                                    selectedChildId = ch.id
                                    showChildDropdown = false
                                }
                            )
                        }
                    }
                }

                // Book Content / OCR Input
                OutlinedTextField(
                    value = bookText,
                    onValueChange = { bookText = it },
                    label = { Text("Book Content / Scanned Text (Separate pages with blank lines)") },
                    minLines = 6,
                    maxLines = 12,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("upload_book_content_input")
                )

                // Estimated Page & Quiz preview
                val estimatedPages = maxOf(1, bookText.split(Regex("\n{2,}")).filter { it.isNotBlank() }.size)
                Text(
                    text = "📄 Estimated: $estimatedPages readable pages with AI synchronized audio & 2 auto-generated comprehension quizzes",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Submit Button
                Button(
                    onClick = {
                        if (title.isNotBlank()) {
                            onCreateBook(title, author, subject, description, bookText, selectedChildId)
                            onBack()
                        }
                    },
                    enabled = title.isNotBlank() && bookText.isNotBlank(),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("publish_book_btn")
                ) {
                    Icon(imageVector = Icons.Default.Save, contentDescription = null)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Publish to Digital Library ✨", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}
