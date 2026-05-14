package com.example.raktvahini

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.raktvahini.data.database.AppDatabase
import com.example.raktvahini.data.entity.User
import com.example.raktvahini.data.entity.DonationRecord
import com.example.raktvahini.databinding.ActivityProfileBinding
import com.example.raktvahini.utils.DonorEligibilityFilter
import com.example.raktvahini.utils.NotificationHelper
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private val database by lazy { AppDatabase.getDatabase(this) }
    private var currentUserId: Int = -1
    private var currentUser: User? = null

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            NotificationHelper.sendThankYouNotification(this)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()

        val sharedPref = getSharedPreferences("RaktVahiniPrefs", Context.MODE_PRIVATE)
        currentUserId = sharedPref.getInt("LOGGED_IN_USER_ID", -1)

        if (currentUserId != -1) {
            fetchUserProfile()
        }

        binding.btnLogDonation.setOnClickListener {
            handleLogDonationClick()
        }

        binding.btnEditProfile.setOnClickListener {
            startActivity(Intent(this, EditProfileActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        if (currentUserId != -1) {
            fetchUserProfile()
        }
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun fetchUserProfile() {
        lifecycleScope.launch {
            currentUser = database.userDao().getUserById(currentUserId)
            currentUser?.let { updateUI(it) }
        }
    }

    private fun updateUI(user: User) {
        binding.tvProfileName.text = user.name
        
        if (user.isDonorRegistered) {
            binding.tvBloodGroup.visibility = View.VISIBLE
            binding.tvBloodGroup.text = user.bloodGroup
            binding.statusCard.visibility = View.VISIBLE
            
            // Check eligibility using CENTRAL logic
            val isEligible = DonorEligibilityFilter.isEligible(
                user.lastDonationDate, 
                user.isReadyToDonate,
                user.isDonorRegistered
            )

            if (isEligible) {
                binding.statusCard.setCardBackgroundColor(ContextCompat.getColor(this, R.color.success_green))
                binding.tvEligibilityStatus.text = "You are eligible to donate! ❤️"
                binding.tvEligibilityStatus.setTextColor(ContextCompat.getColor(this, R.color.text_success))
            } else {
                binding.statusCard.setCardBackgroundColor(ContextCompat.getColor(this, R.color.soft_red))
                binding.tvEligibilityStatus.text = "Ineligible. Next donation in 90 days."
                binding.tvEligibilityStatus.setTextColor(ContextCompat.getColor(this, R.color.on_secondary_container))
            }
        } else {
            // Hide donor specific info if not registered
            binding.tvBloodGroup.visibility = View.GONE
            binding.statusCard.visibility = View.GONE
        }
    }

    private fun handleLogDonationClick() {
        currentUser?.let { user ->
            if (!user.isDonorRegistered) {
                showRegisterFirstDialog()
                return
            }
            logNewDonation(user)
        }
    }

    private fun logNewDonation(user: User) {
        val today = LocalDate.now().format(DateTimeFormatter.ofPattern("d/M/yyyy"))
        
        lifecycleScope.launch {
            // PRE-CHECK ELIGIBILITY
            val isEligible = DonorEligibilityFilter.isEligible(
                user.lastDonationDate, 
                user.isReadyToDonate,
                user.isDonorRegistered
            )

            if (!isEligible) {
                showIneligibilityDialog()
                return@launch
            }

            // Proceed with logging
            val record = DonationRecord(
                userId = currentUserId,
                date = today,
                bloodGroup = user.bloodGroup,
                location = user.location
            )
            database.donationRecordDao().insertDonation(record)
            database.userDao().updateLastDonation(currentUserId, today)
            
            checkAndSendNotification()
            fetchUserProfile()
            
            Toast.makeText(this@ProfileActivity, "Donation Logged Successfully! ❤️", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showRegisterFirstDialog() {
        com.google.android.material.dialog.MaterialAlertDialogBuilder(this)
            .setTitle("Donor Registration Required")
            .setMessage("Please register as a donor first to log your donations and help the community.")
            .setPositiveButton("Register Now") { _, _ ->
                startActivity(Intent(this, RegisterActivity::class.java))
            }
            .setNegativeButton("Maybe Later", null)
            .show()
    }

    private fun showIneligibilityDialog() {
        com.google.android.material.dialog.MaterialAlertDialogBuilder(this)
            .setTitle("Not Eligible Yet ❤️")
            .setMessage("You are not eligible to donate yet. Please wait until 90 days are completed since your last donation.")
            .setPositiveButton("I Understand", null)
            .show()
    }

    private fun checkAndSendNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
                NotificationHelper.sendThankYouNotification(this)
            } else {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        } else {
            NotificationHelper.sendThankYouNotification(this)
        }
    }
}
