package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "quiz_attempts")
data class QuizAttempt(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val childId: Long,
    val bookId: Long,
    val bookTitle: String,
    val chapterTitle: String,
    val scorePercent: Int,
    val totalQuestions: Int,
    val correctCount: Int,
    val timeSpentSeconds: Int,
    val timestamp: Long = System.currentTimeMillis(),
    val isRevisionNeeded: Boolean = false
)
