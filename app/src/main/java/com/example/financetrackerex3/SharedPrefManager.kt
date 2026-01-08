

package com.example.financetrackerex3

import android.content.Context

object SharedPrefManager {
    private const val PREFS_NAME = "FinancePrefs"
    private const val BUDGET_KEY = "monthly_budget"
    private const val CURRENCY_KEY = "currency"
    private const val CURRENCY_SYMBOL_KEY = "currency_symbol"

    // Save budget value
    fun saveBudget(context: Context, budget: Float) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putFloat(BUDGET_KEY, budget).apply()
    }

    // Retrieve budget value
    fun getBudget(context: Context): Float {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getFloat(BUDGET_KEY, 0f)
    }

    // Save currency abbreviation (e.g., USD, LKR)
    fun saveCurrency(context: Context, currency: String) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(CURRENCY_KEY, currency).apply()
    }

    // Get currency abbreviation
    fun getCurrency(context: Context): String {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getString(CURRENCY_KEY, "LKR") ?: "LKR"
    }

    // Save currency symbol (e.g., $, Rs., ¥)
    fun setCurrencySymbol(context: Context, symbol: String) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(CURRENCY_SYMBOL_KEY, symbol).apply()
    }

    // Get currency symbol (default Rs.)
    fun getCurrencySymbol(context: Context): String {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getString(CURRENCY_SYMBOL_KEY, "Rs.") ?: "Rs."
    }
}
