package com.example.financetrackerex3

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat


class OnboardingActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private val PREF_NAME = "onboarding_prefs"
    private val IS_FIRST_TIME = "IsFirstTime"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

        // Check if already logged in
        val userPrefs = getSharedPreferences("user_credentials", Context.MODE_PRIVATE)
        val email = userPrefs.getString("email", null)
        val password = userPrefs.getString("password", null)

        if (email != null && password != null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            return
        }

        // Check if first time
        if (!sharedPreferences.getBoolean(IS_FIRST_TIME, true)) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        setContentView(R.layout.activity_onboarding)

        val startButton = findViewById<Button>(R.id.btnStart)
        startButton.setOnClickListener {
            sharedPreferences.edit().putBoolean(IS_FIRST_TIME, false).apply()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

}
