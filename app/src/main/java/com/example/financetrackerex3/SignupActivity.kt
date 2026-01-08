package com.example.financetrackerex3

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class SignupActivity : AppCompatActivity() {

    private lateinit var emailInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var signupBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        emailInput = findViewById(R.id.etEmail)
        passwordInput = findViewById(R.id.etPassword)
        signupBtn = findViewById(R.id.btnSignup)

        signupBtn.setOnClickListener {
            val email = emailInput.text.toString()
            val password = passwordInput.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                // Save in "users" preference
                val prefs = getSharedPreferences("users", MODE_PRIVATE)
                prefs.edit().putString(email, password).apply()

                // Also save in "user_credentials" for SettingsFragment
                val userPref = getSharedPreferences("user_credentials", MODE_PRIVATE)
                userPref.edit()
                    .putString("email", email)
                    .putString("password", password)
                    .apply()

                Toast.makeText(this, "Account Created!", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
