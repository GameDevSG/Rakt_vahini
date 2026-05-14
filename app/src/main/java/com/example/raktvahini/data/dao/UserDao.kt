package com.example.raktvahini.data.dao

import androidx.room.*
import com.example.raktvahini.data.entity.User

@Dao
interface UserDao {

    @Insert
    suspend fun registerUser(user: User)

    @Query("SELECT * FROM users WHERE email = :email AND password = :password LIMIT 1")
    suspend fun login(email: String, password: String): User?

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): User?

    @Query("SELECT * FROM users WHERE id = :userId LIMIT 1")
    suspend fun getUserById(userId: Int): User?

    @Query("UPDATE users SET lastDonationDate = :newDate WHERE id = :userId")
    suspend fun updateLastDonation(userId: Int, newDate: String)

    @Update
    suspend fun updateUser(user: User)

    // Donor Search: Only find users who have completed donor registration
    @Query("SELECT * FROM users WHERE bloodGroup = :bloodGroup AND isReadyToDonate = 1 AND isDonorRegistered = 1")
    suspend fun searchDonorsByBloodGroup(bloodGroup: String): List<User>

    @Query("SELECT COUNT(*) FROM users WHERE isDonorRegistered = 1")
    suspend fun getTotalDonorsCount(): Int

    @Query("SELECT COUNT(DISTINCT bloodGroup) FROM users WHERE isDonorRegistered = 1")
    suspend fun getUniqueBloodGroupsCount(): Int

    @Query("SELECT * FROM users")
    suspend fun getAllUsers(): List<User>
}
