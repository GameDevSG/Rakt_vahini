package com.example.raktvahini

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.raktvahini.data.database.AppDatabase
import com.example.raktvahini.databinding.ActivitySearchResultsBinding
import com.example.raktvahini.ui.adapter.DonorAdapter
import com.example.raktvahini.utils.DonorEligibilityFilter
import kotlinx.coroutines.launch

class SearchResultsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchResultsBinding
    private val database by lazy { AppDatabase.getDatabase(this) }
    private lateinit var donorAdapter: DonorAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchResultsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bloodGroup = intent.getStringExtra("BLOOD_GROUP") ?: ""
        
        setupToolbar()
        setupRecyclerView()
        
        if (bloodGroup.isNotEmpty()) {
            binding.tvResultSummary.text = "Showing eligible donors for $bloodGroup"
            fetchEligibleDonors(bloodGroup)
        }
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun setupRecyclerView() {
        donorAdapter = DonorAdapter { user ->
            val intent = Intent(Intent.ACTION_DIAL).apply {
                data = Uri.parse("tel:${user.phoneNumber}")
            }
            startActivity(intent)
        }
        
        binding.rvDonors.apply {
            adapter = donorAdapter
            layoutManager = LinearLayoutManager(this@SearchResultsActivity)
        }
    }

    private fun fetchEligibleDonors(bloodGroup: String) {
        lifecycleScope.launch {
            // 1. Fetch matching users from unified table
            val usersInGroup = database.userDao().searchDonorsByBloodGroup(bloodGroup)
            
            // 2. Apply 90-day filter
            val eligibleUsers = DonorEligibilityFilter.filterEligibleUsers(usersInGroup)
            
            // 3. Update UI
            if (eligibleUsers.isEmpty()) {
                binding.emptyState.visibility = View.VISIBLE
                binding.rvDonors.visibility = View.GONE
            } else {
                binding.emptyState.visibility = View.GONE
                binding.rvDonors.visibility = View.VISIBLE
                donorAdapter.submitList(eligibleUsers)
            }
        }
    }
}
