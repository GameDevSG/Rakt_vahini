package com.example.raktvahini.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.raktvahini.data.dao.UserDao
import com.example.raktvahini.data.dao.DonationRecordDao
import com.example.raktvahini.data.entity.User
import com.example.raktvahini.data.entity.DonationRecord

/**
 * AppDatabase: Now unified to use only the 'users' table for all donor operations.
 */
@Database(entities = [User::class, DonationRecord::class], version = 7, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun donationRecordDao(): DonationRecordDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "rakta_vahini_db"
                )
                .fallbackToDestructiveMigration()
                .build()
                
                INSTANCE = instance
                instance
            }
        }
    }
}
