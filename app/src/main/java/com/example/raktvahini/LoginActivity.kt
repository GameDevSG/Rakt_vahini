package com.example.raktvahini

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.raktvahini.data.database.AppDatabase
import com.example.raktvahini.databinding.ActivityLoginBinding
import com.example.raktvahini.utils.SampleDataGenerator
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val database by lazy { AppDatabase.getDatabase(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Generate sample data for demo purposes
        lifecycleScope.launch {
            SampleDataGenerator.generateIfNeeded(this@LoginActivity)
        }

        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                val user = database.userDao().login(email, password)
                if (user != null) {
                    // Step 1: Save logged-in User ID to SharedPreferences
                    val sharedPref = getSharedPreferences("RaktVahiniPrefs", android.content.Context.MODE_PRIVATE)
                    with(sharedPref.edit()) {
                        putInt("LOGGED_IN_USER_ID", user.id)
                        apply()
                    }

                    Toast.makeText(this@LoginActivity, "Login Successful ❤️", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
                    finish() // Close login screen
                } else {
                    Toast.makeText(this@LoginActivity, "Invalid Credentials", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.tvGoToRegister.setOnClickListener {
            startActivity(Intent(this, UserRegisterActivity::class.java))
        }
    }
}
