package com.example.raktvahini

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.raktvahini.databinding.ActivitySearchBinding

/**
 * Step 1: The Search Screen
 * This activity allows the user to select what they are looking for.
 */
class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupDropdowns()

        // When user clicks "Search Donors"
        binding.btnSearch.setOnClickListener {
            val selectedBloodGroup = binding.actvBloodGroup.text.toString()

            if (selectedBloodGroup.isEmpty()) {
                Toast.makeText(this, "Please select a blood group", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Step 2: Move to the Results screen and pass the selected blood group
            val intent = Intent(this, SearchResultsActivity::class.java).apply {
                putExtra("BLOOD_GROUP", selectedBloodGroup)
            }
            startActivity(intent)
        }
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun setupDropdowns() {
        val bloodGroups = arrayOf("A+", "A-", "B+", "B-", "O+", "O-", "AB+", "AB-")
        val bloodAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, bloodGroups)
        binding.actvBloodGroup.setAdapter(bloodAdapter)

        val radiusOptions = arrayOf("10 km", "20 km", "50 km")
        val radiusAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, radiusOptions)
        binding.actvRadius.setAdapter(radiusAdapter)
    }
}
