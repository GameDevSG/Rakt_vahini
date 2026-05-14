package com.example.raktvahini

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.raktvahini.data.database.AppDatabase
import com.example.raktvahini.databinding.ActivityHistoryBinding
import com.example.raktvahini.ui.adapter.HistoryAdapter
import kotlinx.coroutines.launch

class HistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHistoryBinding
    private val database by lazy { AppDatabase.getDatabase(this) }
    private val historyAdapter by lazy { HistoryAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupRecyclerView()
        fetchHistory()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun setupRecyclerView() {
        binding.rvHistory.apply {
            adapter = historyAdapter
            layoutManager = LinearLayoutManager(this@HistoryActivity)
        }
    }

    private fun fetchHistory() {
        val sharedPref = getSharedPreferences("RaktVahiniPrefs", Context.MODE_PRIVATE)
        val userId = sharedPref.getInt("LOGGED_IN_USER_ID", -1)

        if (userId != -1) {
            lifecycleScope.launch {
                val records = database.donationRecordDao().getDonationsForUser(userId)
                if (records.isEmpty()) {
                    binding.emptyState.visibility = View.VISIBLE
                    binding.rvHistory.visibility = View.GONE
                } else {
                    binding.emptyState.visibility = View.GONE
                    binding.rvHistory.visibility = View.VISIBLE
                    historyAdapter.submitList(records)
                }
            }
        }
    }
}
