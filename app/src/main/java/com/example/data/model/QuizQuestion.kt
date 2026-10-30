package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class QuestionType {
    MCQ,
    TRUE_FALSE,
    FILL_BLANK,
    VOCAB_MATCH
}

@Entity(tableName = "quiz_questions")
data class QuizQuestion(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val bookId: Long,
    val chapterTitle: String = "All",
    val questionText: String,
    val questionType: QuestionType = QuestionType.MCQ,
    val optionsJson: String = "[]", // e.g. ["Fox", "Owl", "Bear", "Rabbit"]
    val correctAnswer: String,
    val explanation: String,
    val difficulty: String = "Easy" // "Easy", "Medium", "Hard"
)
