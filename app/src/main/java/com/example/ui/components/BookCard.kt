package com.example.ui.components

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Book
import com.example.ui.theme.MintGreen
import com.example.ui.theme.PrimaryBlue

@Composable
fun BookCard(
    book: Book,
    onReadClick: () -> Unit,
    onQuizClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val startColor = try {
        Color(android.graphics.Color.parseColor(book.coverGradientStartHex))
    } catch (_: Exception) {
        PrimaryBlue
    }

    val endColor = try {
        Color(android.graphics.Color.parseColor(book.coverGradientEndHex))
    } catch (_: Exception) {
        Color(0xFF1D4ED8)
    }

    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        modifier = modifier
            .fillMaxWidth()
            .clickable { onReadClick() }
            .testTag("book_card_${book.id}")
    ) {
        Column {
            // Book Cover Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
                    .background(Brush.linearGradient(listOf(startColor, endColor)))
                    .padding(14.dp)
            ) {
                // Subject Tag
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color.White.copy(alpha = 0.25f),
                    modifier = Modifier.align(Alignment.TopStart)
                ) {
                    Text(
                        text = book.subject,
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }

                // Audio Ready Badge
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color.Black.copy(alpha = 0.25f),
                    modifier = Modifier.align(Alignment.TopEnd)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Headphones,
                            contentDescription = "Audiobook",
                            tint = Color.White,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = if (book.language == "bn") "বাংলা অডিও" else "AI Audio",
                            color = Color.White,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                // Center Icon/Graphic
                Box(
                    modifier = Modifier
                        .size(54.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.2f))
                        .align(Alignment.Center),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoStories,
                        contentDescription = book.title,
                        tint = Color.White,
                        modifier = Modifier.size(30.dp)
                    )
                }

                // Bottom Age range & offline indicator
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomStart),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = book.ageRange,
                        color = Color.White.copy(alpha = 0.9f),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Downloaded",
                            tint = Color(0xFF86EFAC),
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(
                            text = "Offline Ready",
                            color = Color.White,
                            fontSize = 10.sp
                        )
                    }
                }
            }

            // Book Details & Actions
            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    text = book.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = "By ${book.author}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 2.dp)
                )

                Text(
                    text = book.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(top = 6.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Actions: Read & Listen vs Take Quiz
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = onReadClick,
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("read_button_${book.id}")
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "Read & Listen", fontSize = 13.sp)
                    }

                    OutlinedButton(
                        onClick = onQuizClick,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.testTag("quiz_button_${book.id}")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Quiz,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "Quiz", fontSize = 13.sp)
                    }
                }
            }
        }
    }
}
