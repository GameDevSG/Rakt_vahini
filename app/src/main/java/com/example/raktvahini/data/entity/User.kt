package com.example.raktvahini.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entity representing a Registered User of the app.
 * This table is the single source of truth for both basic user accounts 
 * and donor-specific health profiles.
 */
@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val email: String,
    val password: String,
    
    // Donor-specific fields (default values for non-registered donors)
    var isDonorRegistered: Boolean = false,
    var bloodGroup: String = "O+",
    var location: String = "Not Set",
    var phoneNumber: String = "",
    var lastDonationDate: String = "",
    var isReadyToDonate: Boolean = true
)
