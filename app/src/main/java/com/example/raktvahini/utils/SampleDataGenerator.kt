package com.example.raktvahini.utils

import android.content.Context
import com.example.raktvahini.data.database.AppDatabase
import com.example.raktvahini.data.entity.DonationRecord
import com.example.raktvahini.data.entity.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object SampleDataGenerator {

    suspend fun generateIfNeeded(context: Context) {
        val database = AppDatabase.getDatabase(context)
        
        withContext(Dispatchers.IO) {
            val existingUsers = database.userDao().getAllUsers()
            
            // Only add sample data if the table is empty
            if (existingUsers.isEmpty()) {
                val sampleUsers = listOf(
                    User(name = "Rohit Patil", email = "rohit@test.com", password = "123", bloodGroup = "O+", location = "Pune", phoneNumber = "9876543210", lastDonationDate = "15/1/2024", isReadyToDonate = true),
                    User(name = "Priya Mehta", email = "priya@test.com", password = "123", bloodGroup = "O+", location = "Mumbai", phoneNumber = "9822113344", lastDonationDate = "12/5/2024", isReadyToDonate = true),
                    User(name = "Aman Verma", email = "aman@test.com", password = "123", bloodGroup = "A+", location = "Pune", phoneNumber = "9900887766", lastDonationDate = "25/5/2026", isReadyToDonate = true),
                    User(name = "Neha Sharma", email = "neha@test.com", password = "123", bloodGroup = "O-", location = "Baner", phoneNumber = "9122334455", lastDonationDate = "10/11/2023", isReadyToDonate = true),
                    User(name = "Vikram Singh", email = "vikram@test.com", password = "123", bloodGroup = "B+", location = "Nagpur", phoneNumber = "9555666777", lastDonationDate = "20/2/2024", isReadyToDonate = false)
                )

                sampleUsers.forEach { user ->
                    database.userDao().registerUser(user)
                }

                // Add a default logged-in user for testing
                val mainUser = User(
                    name = "Shubham Gandhar",
                    email = "test@donor.com",
                    password = "password",
                    bloodGroup = "O+",
                    location = "Pune, Maharashtra",
                    lastDonationDate = "12/5/2024",
                    phoneNumber = "9000011111",
                    isReadyToDonate = true
                )
                database.userDao().registerUser(mainUser)
                
                // Add history for main user
                val history = listOf(
                    DonationRecord(userId = 6, date = "12/05/2024", bloodGroup = "O+", location = "Pune"),
                    DonationRecord(userId = 6, date = "05/02/2024", bloodGroup = "O+", location = "Mumbai")
                )
                history.forEach { database.donationRecordDao().insertDonation(it) }
            }
        }
    }
}
