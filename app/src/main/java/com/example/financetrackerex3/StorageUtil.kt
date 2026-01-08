package com.example.financetrackerex3

import android.content.Context
import android.os.Environment
import java.io.File
import java.io.FileOutputStream
import java.io.FileInputStream

object StorageUtil {
    private const val FILE_NAME = "transactions_backup.json"

    fun exportData(context: Context, jsonData: String): Boolean {
        return try {
            val file = File(context.filesDir, FILE_NAME)
            FileOutputStream(file).use {
                it.write(jsonData.toByteArray())
            }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    fun importData(context: Context): String? {
        return try {
            val file = File(context.filesDir, FILE_NAME)
            FileInputStream(file).bufferedReader().use { it.readText() }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
