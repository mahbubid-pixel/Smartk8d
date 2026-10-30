package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reading_sessions")
data class ReadingSession(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val childId: Long,
    val bookId: Long,
    val bookTitle: String,
    val durationSeconds: Int,
    val pagesRead: Int,
    val audioListenedSeconds: Int,
    val timestamp: Long = System.currentTimeMillis()
)
