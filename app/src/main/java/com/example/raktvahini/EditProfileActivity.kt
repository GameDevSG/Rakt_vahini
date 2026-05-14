package com.example.raktvahini

import android.content.Context
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.raktvahini.data.database.AppDatabase
import com.example.raktvahini.data.entity.User
import com.example.raktvahini.databinding.ActivityEditProfileBinding
import kotlinx.coroutines.launch

/**
 * Activity to edit the user's profile information.
 */
class EditProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditProfileBinding
    private val database by lazy { AppDatabase.getDatabase(this) }
    private var currentUser: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupBloodGroupDropdown()
        loadUserData()

        binding.btnSave.setOnClickListener {
            saveChanges()
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

    private fun loadUserData() {
        val sharedPref = getSharedPreferences("RaktVahiniPrefs", Context.MODE_PRIVATE)
        val userId = sharedPref.getInt("LOGGED_IN_USER_ID", -1)

        lifecycleScope.launch {
            currentUser = database.userDao().getUserById(userId)
            currentUser?.let { user ->
                binding.etName.setText(user.name)
                binding.actvBloodGroup.setText(user.bloodGroup, false)
                binding.etLocation.setText(user.location)
                binding.etPhone.setText(user.phoneNumber)
                binding.switchReady.isChecked = user.isReadyToDonate
            }
        }
    }

    private fun saveChanges() {
        val name = binding.etName.text.toString().trim()
        val bloodGroup = binding.actvBloodGroup.text.toString().trim()
        val location = binding.etLocation.text.toString().trim()
        val phone = binding.etPhone.text.toString().trim()
        val isReady = binding.switchReady.isChecked

        if (name.isEmpty() || bloodGroup.isEmpty() || location.isEmpty() || phone.isEmpty()) {
            Toast.makeText(this, "Please fill in all details ❤️", Toast.LENGTH_SHORT).show()
            return
        }

        currentUser?.let { user ->
            val updatedUser = user.copy(
                name = name,
                bloodGroup = bloodGroup,
                location = location,
                phoneNumber = phone,
                isReadyToDonate = isReady,
                isDonorRegistered = true // Ensure user is marked as a donor if they provide these details
            )

            lifecycleScope.launch {
                database.userDao().updateUser(updatedUser)
                Toast.makeText(this@EditProfileActivity, "Profile Updated Successfully ❤️", Toast.LENGTH_SHORT).show()
                finish() // Returns to the Profile screen
            }
        }
    }
}
