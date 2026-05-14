package com.example.raktvahini

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.raktvahini.data.database.AppDatabase
import com.example.raktvahini.data.entity.User
import com.example.raktvahini.databinding.ActivityUserRegisterBinding
import kotlinx.coroutines.launch

class UserRegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserRegisterBinding
    private val database by lazy { AppDatabase.getDatabase(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnRegister.setOnClickListener {
            val name = binding.etName.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill in all details", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                val existingUser = database.userDao().getUserByEmail(email)
                if (existingUser != null) {
                    Toast.makeText(this@UserRegisterActivity, "User already exists", Toast.LENGTH_SHORT).show()
                } else {
                    val newUser = User(name = name, email = email, password = password)
                    database.userDao().registerUser(newUser)
                    Toast.makeText(this@UserRegisterActivity, "Registration Successful", Toast.LENGTH_SHORT).show()
                    finish() // Return to login
                }
            }
        }

        binding.tvGoToLogin.setOnClickListener {
            finish()
        }
    }
}
