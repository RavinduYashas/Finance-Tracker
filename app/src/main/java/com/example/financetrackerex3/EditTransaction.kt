package com.example.financetrackerex3

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class EditTransaction : AppCompatActivity() {

    private lateinit var titleInput: EditText
    private lateinit var amountInput: EditText
    private lateinit var categoryInput: EditText
    private lateinit var dateInput: EditText
    private lateinit var saveBtn: Button
    private lateinit var transaction: Transaction

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_transaction)

        titleInput = findViewById(R.id.etTitle)
        amountInput = findViewById(R.id.etAmount)
        categoryInput = findViewById(R.id.etCategory)
        dateInput = findViewById(R.id.etDate)
        saveBtn = findViewById(R.id.btnSaveTransaction)
        transaction = intent.getSerializableExtra("transaction") as Transaction

//        transaction = intent.getParcelableExtra("transaction")!!

        titleInput.setText(transaction.title)
        amountInput.setText(transaction.amount.toString())
        categoryInput.setText(transaction.category)
        dateInput.setText(transaction.date)

        saveBtn.setOnClickListener {
            val title = titleInput.text.toString().trim()
            val amountText = amountInput.text.toString().trim()
            val category = categoryInput.text.toString().trim()
            val date = dateInput.text.toString().trim()

            // Title validation
            if (title.isEmpty()) {
                titleInput.error = "Title is required"
                return@setOnClickListener
            } else if (title.length < 3) {
                titleInput.error = "Title must be at least 3 characters"
                return@setOnClickListener
            } else if (!title.matches(Regex("^[a-zA-Z ]+$"))) {
                titleInput.error = "Title must contain only letters and spaces"
                return@setOnClickListener
            }

            // Amount validation
            val amount = amountText.toDoubleOrNull()
            if (amount == null) {
                amountInput.error = "Enter a valid number for amount"
                return@setOnClickListener
            }

            // Category validation
            if (category.isEmpty()) {
                categoryInput.error = "Category is required"
                return@setOnClickListener
            }

            // Date validation
            if (date.isEmpty()) {
                dateInput.error = "Date is required"
                return@setOnClickListener
            }

            val updatedTransaction = transaction.copy(
                title = title,
                amount = amount,
                category = category,
                date = date
            )

            val transactions = TransactionStorage.loadTransactions(this)
            val index = transactions.indexOfFirst { it.id == transaction.id }

            if (index != -1) {
                transactions[index] = updatedTransaction
                TransactionStorage.saveTransactions(this, transactions)
                Toast.makeText(this, "Transaction updated", Toast.LENGTH_SHORT).show()
                finish()
            }

        }

    }
}
