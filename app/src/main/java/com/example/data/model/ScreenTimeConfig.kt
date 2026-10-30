package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "screen_time_configs")
data class ScreenTimeConfig(
    @PrimaryKey
    val childId: Long,
    val dailyLimitMinutes: Int = 45,
    val maxSessionMinutes: Int = 20,
    val breakMinutes: Int = 5,
    val scheduleStartHour: Int = 8, // 8 AM
    val scheduleEndHour: Int = 20, // 8 PM
    val isLocked: Boolean = false,
    val parentPin: String = "1234"
)
