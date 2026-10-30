package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "child_profiles")
data class ChildProfile(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val avatarKey: String = "owl", // "owl", "fox", "bear", "dino", "star", "bunny"
    val age: Int = 7,
    val gradeLevel: String = "Grade 2",
    val dailyGoalMinutes: Int = 20,
    val totalStars: Int = 120,
    val currentStreakDays: Int = 4,
    val unlockedBadgesJson: String = "[\"first_book\", \"quiz_ace\"]",
    val unlockedAvatarsJson: String = "[\"owl\", \"fox\", \"bear\"]",
    val pin: String? = null // optional child pin
)
