package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "books")
data class Book(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val author: String,
    val subject: String, // "Storybook", "Science & Nature", "Math Adventures", "Bangla Learning", "English Phonics", "Adventure"
    val ageRange: String = "6-9 yrs",
    val description: String,
    val coverGradientStartHex: String = "#3B82F6",
    val coverGradientEndHex: String = "#1D4ED8",
    val iconName: String = "auto_stories",
    val isPreloaded: Boolean = true,
    val isDownloaded: Boolean = true,
    val assignedChildIdsJson: String = "[]", // empty means assigned to all children
    val totalPages: Int = 4,
    val language: String = "en", // "en" or "bn"
    val createdAt: Long = System.currentTimeMillis()
)
