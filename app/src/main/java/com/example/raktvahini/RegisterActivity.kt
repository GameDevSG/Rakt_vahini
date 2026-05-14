package com.example.raktvahini

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.raktvahini.data.database.AppDatabase
import com.example.raktvahini.databinding.ActivityRegisterBinding
import kotlinx.coroutines.launch
import java.util.*

/**
 * Activity for completing Donor Registration.
 * This updates an existing user account with health/donor details.
 */
class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val database by lazy { AppDatabase.getDatabase(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupBloodGroupDropdown()
        setupDatePicker()

        binding.btnRegister.setOnClickListener {
            completeDonorRegistration()
        }
    }

    private fun completeDonorRegistration() {
        val sharedPref = getSharedPreferences("RaktVahiniPrefs", Context.MODE_PRIVATE)
        val userId = sharedPref.getInt("LOGGED_IN_USER_ID", -1)

        if (userId == -1) {
            Toast.makeText(this, "Please log in first", Toast.LENGTH_SHORT).show()
            return
        }

        val bloodGroup = binding.actvBloodGroup.text.toString().trim()
        val location = binding.etLocation.text.toString().trim()
        val phone = binding.etPhone.text.toString().trim()
        val lastDonation = binding.etLastDonation.text.toString().trim()
        val isReady = binding.switchReady.isChecked

        if (bloodGroup.isEmpty() || location.isEmpty() || phone.isEmpty()) {
            Toast.makeText(this, "Please fill in all health details", Toast.LENGTH_SHORT).show()
            return
        }

        lifecycleScope.launch {
            try {
                // Fetch the existing user
                val user = database.userDao().getUserById(userId)
                user?.let {
                    // Update user fields and mark as registered donor
                    val updatedUser = it.copy(
                        bloodGroup = bloodGroup,
                        location = location,
                        phoneNumber = phone,
                        lastDonationDate = lastDonation,
                        isReadyToDonate = isReady,
                        isDonorRegistered = true // KEY CHANGE: User is now a donor
                    )
                    database.userDao().updateUser(updatedUser)
                    
                    Toast.makeText(this@RegisterActivity, "Successfully Registered as Donor! ❤️", Toast.LENGTH_LONG).show()
                    finish() // Close screen and return to Home
                }
            } catch (e: Exception) {
                Toast.makeText(this@RegisterActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun setupBloodGroupDropdown() {
        val bloodGroups = arrayOf("A+", "A-", "B+", "B-", "O+", "O-", "AB+", "AB-")
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, bloodGroups)
        binding.actvBloodGroup.setAdapter(adapter)
    }

    private fun setupDatePicker() {
        val calendar = Calendar.getInstance()
        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            val selectedDate = "${dayOfMonth}/${month + 1}/${year}"
            binding.etLastDonation.setText(selectedDate)
        }

        binding.etLastDonation.setOnClickListener {
            DatePickerDialog(
                this,
                dateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }
}
