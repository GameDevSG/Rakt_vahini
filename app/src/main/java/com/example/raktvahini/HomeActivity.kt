package com.example.raktvahini

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.raktvahini.data.database.AppDatabase
import com.example.raktvahini.databinding.ActivityHomeBinding
import com.example.raktvahini.utils.DonorEligibilityFilter
import kotlinx.coroutines.launch

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private val database by lazy { AppDatabase.getDatabase(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupButtons()
        setupBottomNavigation()
    }

    override fun onResume() {
        super.onResume()
        // Refresh dashboard every time user returns to Home
        loadDashboardData()
    }

    private fun loadDashboardData() {
        val sharedPref = getSharedPreferences("RaktVahiniPrefs", Context.MODE_PRIVATE)
        val currentUserId = sharedPref.getInt("LOGGED_IN_USER_ID", -1)

        lifecycleScope.launch {
            // 1. Personalized Greeting
            if (currentUserId != -1) {
                val user = database.userDao().getUserById(currentUserId)
                binding.tvWelcomeName.text = "Hello, ${user?.name}!"
            }

            // 2. Fetch Dashboard Stats from Unified Table
            val totalUsers = database.userDao().getTotalDonorsCount()
            val totalDonations = database.donationRecordDao().getTotalDonationsCount()
            val uniqueGroups = database.userDao().getUniqueBloodGroupsCount()

            // 3. Calculate Eligible Users
            val allUsers = database.userDao().getAllUsers()
            val eligibleCount = DonorEligibilityFilter.filterEligibleUsers(allUsers).size

            // 4. Update UI
            binding.tvTotalDonors.text = totalUsers.toString()
            binding.tvTotalDonations.text = totalDonations.toString()
            binding.tvGroupsCount.text = uniqueGroups.toString()
            binding.tvEligibleDonors.text = eligibleCount.toString()
        }
    }

    private fun setupButtons() {
        binding.btnFindBlood.setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
        }

        binding.btnRegisterAsDonor.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupBottomNavigation() {
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> true
                R.id.nav_history -> {
                    startActivity(Intent(this, HistoryActivity::class.java))
                    true
                }
                R.id.nav_profile -> {
                    startActivity(Intent(this, ProfileActivity::class.java))
                    true
                }
                else -> false
            }
        }
        // Set Home as selected
        binding.bottomNavigation.selectedItemId = R.id.nav_home
    }
}
