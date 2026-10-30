package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "book_pages")
data class BookPage(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val bookId: Long,
    val pageNumber: Int,
    val chapterTitle: String,
    val textContent: String,
    val illustrationKey: String = "forest", // key for illustration scene
    val keyVocabularyJson: String = "[]", // e.g. ["compass", "beacon", "curious"]
    val parentVoiceNotePath: String? = null
)
