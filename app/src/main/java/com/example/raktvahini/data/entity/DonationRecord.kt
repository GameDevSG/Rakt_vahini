package com.example.raktvahini.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entity representing a single donation event in the user's history.
 */
@Entity(tableName = "donations")
data class DonationRecord(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val userId: Int,
    val date: String,
    val bloodGroup: String,
    val location: String
)
