package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "voice_recordings")
data class VoiceRecording(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val childId: Long,
    val bookId: Long,
    val bookTitle: String,
    val pageNumber: Int,
    val pageSnippet: String,
    val audioFilePath: String,
    val durationSeconds: Int,
    val fluencyScore: Int = 95, // 0-100 rating
    val timestamp: Long = System.currentTimeMillis()
)
