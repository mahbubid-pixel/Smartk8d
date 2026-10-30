package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "parent_rewards")
data class ParentReward(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val description: String,
    val starsCost: Int,
    val iconKey: String = "icecream", // "icecream", "game", "movie", "park", "book", "pizza"
    val isRedeemed: Boolean = false,
    val redeemedByChildId: Long? = null
)
