package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "revision_items")
data class RevisionItem(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val childId: Long,
    val questionId: Long,
    val bookId: Long,
    val bookTitle: String,
    val chapterTitle: String,
    val questionText: String,
    val questionType: QuestionType = QuestionType.MCQ,
    val optionsJson: String = "[]",
    val correctAnswer: String,
    val explanation: String,
    val difficulty: String = "Easy",
    val incorrectCount: Int = 1,
    val masteryStreak: Int = 0, // Consecutive correct answers during revision (2 = Mastered!)
    val isMastered: Boolean = false,
    val lastAttemptTimestamp: Long = System.currentTimeMillis(),
    val nextRevisionDueTimestamp: Long = System.currentTimeMillis(), // When revision becomes due
    val stage: Int = 1 // Spaced repetition interval level: 1 = Due Now, 2 = 1 Day, 3 = 3 Days, 4 = Mastered
)
