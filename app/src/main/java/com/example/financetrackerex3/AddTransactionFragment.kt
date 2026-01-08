

package com.example.financetrackerex3

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import java.util.*
//
//class AddTransactionFragment : Fragment() {
//
//    private lateinit var titleInput: EditText
//    private lateinit var amountInput: EditText
//    private lateinit var categoryInput: Spinner
//    private lateinit var dateInput: EditText
//    private lateinit var addBtn: Button
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        val view = inflater.inflate(R.layout.fragment_add_transaction, container, false)
//
//        titleInput = view.findViewById(R.id.etTitle)
//        amountInput = view.findViewById(R.id.etAmount)
//        categoryInput = view.findViewById(R.id.etCategory)
//        dateInput = view.findViewById(R.id.etDate)
//        addBtn = view.findViewById(R.id.btnAddTransaction)
//
//        // Show calendar when clicking on date input
//        dateInput.setOnClickListener {
//            showDatePicker()
//        }
//
//        addBtn.setOnClickListener {
//            val title = titleInput.text.toString().trim()
//            val amountText = amountInput.text.toString().trim()
//            val category = categoryInput.selectedItem.toString()
//            val date = dateInput.text.toString().trim()
//
//            // Validation
//            if (title.isEmpty()) {
//                titleInput.error = "Enter a title"
//                return@setOnClickListener
//            }
//
//            val amount = amountText.toIntOrNull()
//            if (amount == null) {
//                amountInput.error = "Enter a valid integer amount"
//                return@setOnClickListener
//            }
//
//            if (categoryInput.selectedItemPosition == 0) {
//                Toast.makeText(requireContext(), "Please select a category", Toast.LENGTH_SHORT).show()
//                return@setOnClickListener
//            }
//
//            if (date.isEmpty()) {
//                dateInput.error = "Select a date"
//                return@setOnClickListener
//            }
//
//            // Add transaction
//            val transactions = TransactionStorage.loadTransactions(requireContext())
//            val transaction = Transaction(transactions.size, title, amount.toDouble(), category, date)
//            transactions.add(transaction)
//            TransactionStorage.saveTransactions(requireContext(), transactions)
//
//            Toast.makeText(requireContext(), "Transaction Added", Toast.LENGTH_SHORT).show()
//            clearFields()
//        }
//
//        return view
//    }
//
//    private fun showDatePicker() {
//        val calendar = Calendar.getInstance()
//        val year = calendar.get(Calendar.YEAR)
//        val month = calendar.get(Calendar.MONTH)
//        val day = calendar.get(Calendar.DAY_OF_MONTH)
//
//        val datePickerDialog = DatePickerDialog(
//            requireContext(),
//            { _, selectedYear, selectedMonth, selectedDay ->
//                val formattedDate = "${selectedYear}-${String.format("%02d", selectedMonth + 1)}-${String.format("%02d", selectedDay)}"
//                dateInput.setText(formattedDate)
//            },
//            year, month, day
//        )
//        datePickerDialog.show()
//    }
//
//    private fun clearFields() {
//        titleInput.text.clear()
//        amountInput.text.clear()
//        categoryInput.setSelection(0) // Reset Spinner to first item
//        dateInput.text.clear()
//    }
//}
class AddTransactionFragment : Fragment() {

    private lateinit var titleInput: EditText
    private lateinit var amountInput: EditText
    private lateinit var categoryInput: Spinner
    private lateinit var dateInput: EditText
    private lateinit var addBtn: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_transaction, container, false)

        titleInput = view.findViewById(R.id.etTitle)
        amountInput = view.findViewById(R.id.etAmount)
        categoryInput = view.findViewById(R.id.etCategory)
        dateInput = view.findViewById(R.id.etDate)
        addBtn = view.findViewById(R.id.btnAddTransaction)

        dateInput.setOnClickListener {
            showDatePicker()
        }

        addBtn.setOnClickListener {
            val title = titleInput.text.toString().trim()
            val amountText = amountInput.text.toString().trim()
            val category = categoryInput.selectedItem.toString()
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
            if (amountText.isEmpty()) {
                amountInput.error = "Amount is required"
                return@setOnClickListener
            } else if (amount == null || amount <= 0) {
                amountInput.error = "Enter a valid positive number"
                return@setOnClickListener
            }

            // Category validation
            if (categoryInput.selectedItemPosition == 0) {
                Toast.makeText(requireContext(), "Please select a category", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Date validation
            if (date.isEmpty()) {
                dateInput.error = "Date is required"
                return@setOnClickListener
            } else {
                val selectedDate = date.split("-")
                val selectedCal = Calendar.getInstance()
                selectedCal.set(
                    selectedDate[0].toInt(),
                    selectedDate[1].toInt() - 1,
                    selectedDate[2].toInt()
                )
                val today = Calendar.getInstance()
                if (selectedCal.after(today)) {
                    dateInput.error = "Date cannot be in the future"
                    return@setOnClickListener
                }
            }

            // Save transaction
            val transactions = TransactionStorage.loadTransactions(requireContext())
            val transaction = Transaction(transactions.size, title, amount, category, date)
            transactions.add(transaction)
            TransactionStorage.saveTransactions(requireContext(), transactions)

            Toast.makeText(requireContext(), "Transaction Added", Toast.LENGTH_SHORT).show()
            clearFields()
            parentFragmentManager.beginTransaction().detach(this).attach(this).commit()

        }

        return view
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->
                val formattedDate = "${selectedYear}-${String.format("%02d", selectedMonth + 1)}-${String.format("%02d", selectedDay)}"
                dateInput.setText(formattedDate)
            },
            year, month, day
        )
        datePickerDialog.show()
    }

    private fun clearFields() {
        titleInput.text.clear()
        amountInput.text.clear()
        categoryInput.setSelection(0)
        dateInput.text.clear()
    }
}
