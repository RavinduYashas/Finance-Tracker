package com.example.financetrackerex3

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File

class SettingsFragment : Fragment() {

    private val gson = Gson()
    private val KEY_TRANSACTIONS = "transactions"
    private val BACKUP_FILENAME = "transaction_backup.json"
    private lateinit var sharedPreferences: android.content.SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_settings, container, false)

        val context = requireContext()
        sharedPreferences = context.getSharedPreferences("FinancePrefs", Context.MODE_PRIVATE)
        val userPref = context.getSharedPreferences("user_credentials", Context.MODE_PRIVATE)

        val etBudget = view.findViewById<EditText>(R.id.etBudget)
        val btnSaveBudget = view.findViewById<Button>(R.id.btnSaveBudget)
        val btnExport = view.findViewById<Button>(R.id.btnExportData)
        val btnImport = view.findViewById<Button>(R.id.btnImportData)
        val btnLogout = view.findViewById<Button>(R.id.btnLogout)

        val tvEmail = view.findViewById<TextView>(R.id.tvProfileEmail)
        val tvPassword = view.findViewById<TextView>(R.id.tvProfilePassword)

        val spinnerCurrency = view.findViewById<Spinner>(R.id.spinnerCurrency)
        val btnSaveCurrency = view.findViewById<Button>(R.id.btnSaveCurrency)

        val email = userPref.getString("email", "Not Found")
        val password = userPref.getString("password", "Not Found")
        tvEmail.text = "Email: $email"
        tvPassword.text = "Password: $password"

        val savedBudget = SharedPrefManager.getBudget(context)
        if (savedBudget > 0f) {
            etBudget.setText(savedBudget.toString())
        }

        btnSaveBudget.setOnClickListener {
            val budgetStr = etBudget.text.toString()
            if (budgetStr.isNotEmpty()) {
                val budget = budgetStr.toFloatOrNull()
                if (budget != null) {
                    SharedPrefManager.saveBudget(context, budget)
                    Toast.makeText(context, "Budget saved: Rs. $budget", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Invalid budget value", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(context, "Please enter a budget", Toast.LENGTH_SHORT).show()
            }
        }

        val currencyList = listOf("LKR", "USD", "EUR", "INR", "JPY")
        val adapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, currencyList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCurrency.adapter = adapter

        val currentCurrency = SharedPrefManager.getCurrency(context)
        val selectedIndex = currencyList.indexOf(currentCurrency)
        if (selectedIndex != -1) {
            spinnerCurrency.setSelection(selectedIndex)
        }

        btnSaveCurrency.setOnClickListener {
            val selectedCurrency = spinnerCurrency.selectedItem.toString()
            SharedPrefManager.saveCurrency(context, selectedCurrency)

            val currencySymbols = mapOf(
                "LKR" to "Rs.",
                "USD" to "$",
                "EUR" to "€",
                "INR" to "₹",
                "JPY" to "¥"
            )
            val selectedSymbol = currencySymbols[selectedCurrency] ?: selectedCurrency
            SharedPrefManager.setCurrencySymbol(context, selectedSymbol)

            Toast.makeText(context, "Currency set to $selectedCurrency ($selectedSymbol)", Toast.LENGTH_SHORT).show()
        }

        btnLogout.setOnClickListener {
            userPref.edit().clear().apply()
            context.getSharedPreferences("onboarding_prefs", Context.MODE_PRIVATE)
                .edit().putBoolean("IsFirstTime", true).apply()

            val intent = Intent(context, OnboardingActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

//        // Export
//        btnExport.setOnClickListener {
//            val transactions = TransactionStorage.loadTransactions(context)
//            val jsonData = gson.toJson(transactions)
//            if (StorageUtil.exportData(context, jsonData)) {
//                saveToInternalStorage(jsonData)
//                Toast.makeText(context, "Backup saved successfully", Toast.LENGTH_SHORT).show()
//            } else {
//                Toast.makeText(context, "Failed to save backup", Toast.LENGTH_SHORT).show()
//            }
//        }
        btnExport.setOnClickListener {
            val transactions = TransactionStorage.loadTransactions(context)
            val jsonData = gson.toJson(transactions)
            val success = saveToDownloads(context, "transaction_backup.json", jsonData)
            if (success) {
                Toast.makeText(context, "Backup saved to Downloads folder", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Failed to save backup", Toast.LENGTH_SHORT).show()
            }
        }


        // Import
        btnImport.setOnClickListener {
            val jsonData = StorageUtil.importData(context)
            if (jsonData != null) {
                try {
                    val type = object : TypeToken<List<Transaction>>() {}.type
                    val transactions = gson.fromJson<List<Transaction>>(jsonData, type)
                    TransactionStorage.saveTransactions(context, transactions)
                    sharedPreferences.edit().putString(KEY_TRANSACTIONS, jsonData).apply()
                    saveToInternalStorage(jsonData)
                    Toast.makeText(context, "Data restored successfully", Toast.LENGTH_SHORT).show()
                } catch (e: Exception) {
                    Toast.makeText(context, "Failed to parse and restore data", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(context, "Failed to restore data", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }

    private fun saveToInternalStorage(transactionsJson: String): Boolean {
        return try {
            requireContext().openFileOutput(BACKUP_FILENAME, Context.MODE_PRIVATE).use { outputStream ->
                outputStream.write(transactionsJson.toByteArray(Charsets.UTF_8))
                outputStream.flush()
            }
            true
        } catch (e: Exception) {
            android.util.Log.e("TransactionRepository", "Internal storage backup failed: ${e.message}", e)
            false
        }
    }

    private fun saveToDownloads(context: Context, fileName: String, data: String): Boolean {
        return try {
            val resolver = context.contentResolver
            val contentValues = android.content.ContentValues().apply {
                put(android.provider.MediaStore.Downloads.DISPLAY_NAME, fileName)
                put(android.provider.MediaStore.Downloads.MIME_TYPE, "application/json")
                put(android.provider.MediaStore.Downloads.IS_PENDING, 1)
            }

            val collection = android.provider.MediaStore.Downloads.getContentUri(android.provider.MediaStore.VOLUME_EXTERNAL_PRIMARY)
            val fileUri = resolver.insert(collection, contentValues)

            fileUri?.let { uri ->
                resolver.openOutputStream(uri)?.use { outputStream ->
                    outputStream.write(data.toByteArray(Charsets.UTF_8))
                }
                contentValues.clear()
                contentValues.put(android.provider.MediaStore.Downloads.IS_PENDING, 0)
                resolver.update(uri, contentValues, null, null)
                true
            } ?: false
        } catch (e: Exception) {
            android.util.Log.e("Export", "Error saving to Downloads: ${e.message}", e)
            false
        }
    }


    companion object {
        @JvmStatic
        fun newInstance() = SettingsFragment()
    }
}














































































//
//
//// Export
//btnExport.setOnClickListener {
//    val jsonData = sharedPref.getString("transactions", "[]")
//    if (jsonData != null && StorageUtil.exportData(requireContext(), jsonData)) {
//        Toast.makeText(requireContext(), "Backup saved successfully", Toast.LENGTH_SHORT).show()
//    } else {
//        Toast.makeText(requireContext(), "Failed to save backup", Toast.LENGTH_SHORT).show()
//    }
//}
//
//// Import
//btnImport.setOnClickListener {
//    val jsonData = StorageUtil.importData(requireContext())
//    if (jsonData != null) {
//        sharedPref.edit().putString("transactions", jsonData).apply()
//        Toast.makeText(requireContext(), "Data restored successfully", Toast.LENGTH_SHORT).show()
//    } else {
//        Toast.makeText(requireContext(), "Failed to restore data", Toast.LENGTH_SHORT).show()
//    }
//}
//
//// Export using TransactionStorage
//btnExport.setOnClickListener {
//    val transactions = TransactionStorage.loadTransactions(requireContext())
//    val jsonData = Gson().toJson(transactions)
//    if (StorageUtil.exportData(requireContext(), jsonData)) {
//        Toast.makeText(requireContext(), "Backup saved successfully", Toast.LENGTH_SHORT).show()
//    } else {
//        Toast.makeText(requireContext(), "Failed to save backup", Toast.LENGTH_SHORT).show()
//    }
//}
//
//// Import using TransactionStorage
//btnImport.setOnClickListener {
//    val jsonData = StorageUtil.importData(requireContext())
//    if (jsonData != null) {
//        val type = object : TypeToken<List<Transaction>>() {}.type
//        val transactions = Gson().fromJson<List<Transaction>>(jsonData, type)
//        TransactionStorage.saveTransactions(requireContext(), transactions)
//        Toast.makeText(requireContext(), "Data restored successfully", Toast.LENGTH_SHORT).show()
//    } else {
//        Toast.makeText(requireContext(), "Failed to restore data", Toast.LENGTH_SHORT).show()
//    }
//}




//
//package com.example.financetrackerex3
//
//import android.content.Context
//import android.content.Intent
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.*
//import androidx.fragment.app.Fragment
//import com.google.gson.Gson
//import com.google.gson.reflect.TypeToken
//
//class SettingsFragment : Fragment() {
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        val view = inflater.inflate(R.layout.fragment_settings, container, false)
//
//        val sharedPref = requireContext().getSharedPreferences("FinancePrefs", Context.MODE_PRIVATE)
//        val userPref = requireContext().getSharedPreferences("user_credentials", Context.MODE_PRIVATE)
//
//        val etBudget = view.findViewById<EditText>(R.id.etBudget)
//        val btnSaveBudget = view.findViewById<Button>(R.id.btnSaveBudget)
//        val btnExport = view.findViewById<Button>(R.id.btnExportData)
//        val btnImport = view.findViewById<Button>(R.id.btnImportData)
//        val btnLogout = view.findViewById<Button>(R.id.btnLogout)
//
//        val tvEmail = view.findViewById<TextView>(R.id.tvProfileEmail)
//        val tvPassword = view.findViewById<TextView>(R.id.tvProfilePassword)
//
//        val spinnerCurrency = view.findViewById<Spinner>(R.id.spinnerCurrency)
//        val btnSaveCurrency = view.findViewById<Button>(R.id.btnSaveCurrency)
//
//        val email = userPref.getString("email", "Not Found")
//        val password = userPref.getString("password", "Not Found")
//        tvEmail.text = "Email: $email"
//        tvPassword.text = "Password: $password"
//
//        val savedBudget = SharedPrefManager.getBudget(requireContext())
//        if (savedBudget > 0f) {
//            etBudget.setText(savedBudget.toString())
//        }
//
//        btnSaveBudget.setOnClickListener {
//            val budgetStr = etBudget.text.toString()
//            if (budgetStr.isNotEmpty()) {
//                val budget = budgetStr.toFloatOrNull()
//                if (budget != null) {
//                    SharedPrefManager.saveBudget(requireContext(), budget)
//                    Toast.makeText(requireContext(), "Budget saved: Rs. $budget", Toast.LENGTH_SHORT).show()
//                } else {
//                    Toast.makeText(requireContext(), "Invalid budget value", Toast.LENGTH_SHORT).show()
//                }
//            } else {
//                Toast.makeText(requireContext(), "Please enter a budget", Toast.LENGTH_SHORT).show()
//            }
//        }
//
//        val currencyList = listOf("LKR", "USD", "EUR", "INR", "JPY")
//        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, currencyList)
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//        spinnerCurrency.adapter = adapter
//
//        val currentCurrency = SharedPrefManager.getCurrency(requireContext())
//        val selectedIndex = currencyList.indexOf(currentCurrency)
//        if (selectedIndex != -1) spinnerCurrency.setSelection(selectedIndex)
//
//        btnSaveCurrency.setOnClickListener {
//            val selectedCurrency = spinnerCurrency.selectedItem.toString()
//            SharedPrefManager.saveCurrency(requireContext(), selectedCurrency)
//            Toast.makeText(requireContext(), "Currency set to $selectedCurrency", Toast.LENGTH_SHORT).show()
//        }
//
//        btnExport.setOnClickListener {
//            val jsonData = sharedPref.getString("transactions", "[]")
//            if (jsonData != null && StorageUtil.exportData(requireContext(), jsonData)) {
//                Toast.makeText(requireContext(), "Backup saved successfully", Toast.LENGTH_SHORT).show()
//            } else {
//                Toast.makeText(requireContext(), "Failed to save backup", Toast.LENGTH_SHORT).show()
//            }
//        }
//
//        btnImport.setOnClickListener {
//            val jsonData = StorageUtil.importData(requireContext())
//            if (jsonData != null) {
//                sharedPref.edit().putString("transactions", jsonData).apply()
//                Toast.makeText(requireContext(), "Data restored successfully", Toast.LENGTH_SHORT).show()
//            } else {
//                Toast.makeText(requireContext(), "Failed to restore data", Toast.LENGTH_SHORT).show()
//            }
//        }
//
//        btnLogout.setOnClickListener {
//            userPref.edit().clear().apply()
//
//            val onboardingPref = requireContext().getSharedPreferences("onboarding_prefs", Context.MODE_PRIVATE)
//            onboardingPref.edit().putBoolean("IsFirstTime", true).apply()
//
//            val intent = Intent(requireContext(), OnboardingActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//            startActivity(intent)
//        }
//
//        return view
//    }
//
//    companion object {
//        @JvmStatic
//        fun newInstance() = SettingsFragment()
//    }
//}

