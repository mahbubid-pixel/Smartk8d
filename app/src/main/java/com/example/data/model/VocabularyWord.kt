package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "vocabulary_words")
data class VocabularyWord(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val childId: Long,
    val word: String,
    val syllables: String,
    val definition: String,
    val banglaMeaning: String,
    val category: String, // "Science", "Space", "Nature", "Bangla", "General"
    val exampleSentence: String,
    val isCollected: Boolean = true,
    val masteryLevel: Int = 1, // 1: Learning, 2: Practiced, 3: Mastered
    val dateCollected: Long = System.currentTimeMillis()
)
