package com.example.raktvahini.utils

import com.example.raktvahini.data.entity.User
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

/**
 * THE CENTRAL SOURCE OF TRUTH for blood donation eligibility.
 */
object DonorEligibilityFilter {

    private val dateFormatter = DateTimeFormatter.ofPattern("d/M/yyyy")

    /**
     * CORE LOGIC: Checks if a donor is eligible.
     * Rule: User must be a registered donor AND ready toggle ON AND last donation > 90 days.
     */
    fun isEligible(lastDonationDate: String, isReadyToDonate: Boolean, isDonorRegistered: Boolean = true): Boolean {
        // 1. Must be a registered donor
        if (!isDonorRegistered) return false
        
        // 2. Must be manually ready
        if (!isReadyToDonate) return false
        
        // 3. If never donated, immediately eligible
        if (lastDonationDate.isEmpty()) return true

        return try {
            val today = LocalDate.now()
            val lastDate = LocalDate.parse(lastDonationDate, dateFormatter)

            // 4. Calculate days passed
            val daysPassed = ChronoUnit.DAYS.between(lastDate, today)

            // 5. Must be more than 90 days ago
            daysPassed > 90
        } catch (e: Exception) {
            false
        }
    }

    /**
     * Filters a list of Users to return only those eligible to donate.
     */
    fun filterEligibleUsers(users: List<User>): List<User> {
        return users.filter { isEligible(it.lastDonationDate, it.isReadyToDonate, it.isDonorRegistered) }
    }
}
