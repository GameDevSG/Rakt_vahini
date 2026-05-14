package com.example.raktvahini

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import com.example.raktvahini.databinding.ActivitySplashBinding

/**
 * SplashActivity: The first screen the user sees.
 * Features a cute animation and transitions to the Login screen.
 */
class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Step 1: Start Animations
        startAnimations()

        // Step 2: Navigate to Login after 2.5 seconds
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, LoginActivity::class.java))
            finish() // Close splash screen so user can't go back to it
        }, 2500)
    }

    /**
     * Creates a smooth fade-in and slide-up animation for the text.
     */
    private fun startAnimations() {
        // App Name Animation
        binding.tvAppName.animate()
            .alpha(1f)
            .translationY(-20f)
            .setDuration(1000)
            .setInterpolator(AccelerateDecelerateInterpolator())
            .start()

        // Tagline Animation (starts slightly later)
        binding.tvTagline.animate()
            .alpha(1f)
            .translationY(-10f)
            .setDuration(1000)
            .setStartDelay(300)
            .setInterpolator(AccelerateDecelerateInterpolator())
            .start()
            
        // Logo Card Animation (gentle bounce)
        binding.logoCard.scaleX = 0.5f
        binding.logoCard.scaleY = 0.5f
        binding.logoCard.animate()
            .scaleX(1f)
            .scaleY(1f)
            .setDuration(800)
            .setInterpolator(AccelerateDecelerateInterpolator())
            .start()
    }
}
