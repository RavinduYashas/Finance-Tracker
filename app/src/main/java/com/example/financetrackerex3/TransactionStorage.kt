package com.example.financetrackerex3

//import Transaction

////package com.example.financetrackerex3
////
////import android.content.Context
////import com.google.gson.Gson
////import com.google.gson.reflect.TypeToken
////
////object TransactionStorage {
////    private const val PREFS_NAME = "FinancePrefs"
////    private const val KEY_TRANSACTIONS = "transactions"
////
////    fun saveTransaction(context: Context, transaction: Transaction) {
////        val list = getTransactions(context).toMutableList()
////        list.add(transaction)
////
////        val json = Gson().toJson(list)
////        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
////            .edit().putString(KEY_TRANSACTIONS, json).apply()
////    }
////
////    fun getTransactions(context: Context): List<Transaction> {
////        val json = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
////            .getString(KEY_TRANSACTIONS, null)
////        return if (json != null) {
////            val type = object : TypeToken<List<Transaction>>() {}.type
////            Gson().fromJson(json, type)
////        } else {
////            emptyList()
////        }
////    }
////}
//
////
////package com.example.financetrackerex3
////
////import Transaction
////import android.content.Context
////import com.google.gson.Gson
////import com.google.gson.reflect.TypeToken
////
////object TransactionStorage {
////    private const val PREF_NAME = "transaction_prefs"
////    private const val KEY_TRANSACTIONS = "transactions"
////
////    fun saveTransactions(context: Context, transactions: List<Transaction>) {
////        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
////        val editor = sharedPreferences.edit()
////        val json = Gson().toJson(transactions)
////        editor.putString(KEY_TRANSACTIONS, json)
////        editor.apply()
////    }
////
////    fun loadTransactions(context: Context): MutableList<Transaction> {
////        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
////        val json = sharedPreferences.getString(KEY_TRANSACTIONS, null)
////        return if (json != null) {
////            val type = object : TypeToken<MutableList<Transaction>>() {}.type
////            Gson().fromJson(json, type)
////        } else {
////            mutableListOf()
////        }
////    }
////
////    fun getTotalSpent(context: Context): Double {
////        val transactions = loadTransactions(context)
////        return transactions.sumOf { it.amount }
////    }
////}

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object TransactionStorage {
    private const val PREF_NAME = "transaction_prefs"
    private const val KEY_TRANSACTIONS = "transactions"

    fun saveTransactions(context: Context, transactions: List<Transaction>) {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val json = Gson().toJson(transactions)
        editor.putString(KEY_TRANSACTIONS, json)
        editor.apply()
    }

    fun loadTransactions(context: Context): MutableList<Transaction> {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val json = sharedPreferences.getString(KEY_TRANSACTIONS, null)
        return if (json != null) {
            val type = object : TypeToken<MutableList<Transaction>>() {}.type
            Gson().fromJson(json, type)
        } else {
            mutableListOf()
        }
    }

    fun getTotalSpent(context: Context): Double {
        val transactions = loadTransactions(context)
        return transactions.sumOf { it.amount }
    }
}
