package com.example.raktvahini.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.raktvahini.data.entity.DonationRecord

@Dao
interface DonationRecordDao {

    @Insert
    suspend fun insertDonation(donation: DonationRecord)

    @Query("SELECT * FROM donations WHERE userId = :userId ORDER BY id DESC")
    suspend fun getDonationsForUser(userId: Int): List<DonationRecord>

    @Query("SELECT COUNT(*) FROM donations")
    suspend fun getTotalDonationsCount(): Int
}
