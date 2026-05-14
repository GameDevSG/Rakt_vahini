package com.example.raktvahini

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.raktvahini.databinding.ActivityMainBinding

/**
 * MainActivity: The entry point of the Rakta-Vahini app.
 * In this project version, LoginActivity is set as the Launcher in AndroidManifest.
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Navigation
        binding.btnFindBlood.setOnClickListener {
            val intent = android.content.Intent(this, SearchActivity::class.java)
            startActivity(intent)
        }

        binding.btnRegisterDonor.setOnClickListener {
            val intent = android.content.Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}
