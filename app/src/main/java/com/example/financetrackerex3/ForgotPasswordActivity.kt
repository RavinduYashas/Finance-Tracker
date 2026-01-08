package com.example.financetrackerex3

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var emailInput: EditText
    private lateinit var retrieveBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        emailInput = findViewById(R.id.etEmail)
        retrieveBtn = findViewById(R.id.btnRetrieve)

        retrieveBtn.setOnClickListener {
            val email = emailInput.text.toString()
            val prefs = getSharedPreferences("users", MODE_PRIVATE)
            val password = prefs.getString(email, null)

            if (password != null) {
                Toast.makeText(this, "Your password is: $password", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Email not found!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
