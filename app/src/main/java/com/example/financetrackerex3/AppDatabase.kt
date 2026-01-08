package com.example.financetrackerex3

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.financetrackerex3.Transaction

@Database(entities = [Transaction::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun transactionDao(): TransactionDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "finance_tracker_db"
                ).build().also {
                    INSTANCE = it
                }
            }
        }
    }
}


//package com.example.financetrackerex3;
//
////
////public class AppDatabase {
////}
//import Transaction
//import android.content.Context
//import androidx.room.Database
//import androidx.room.Room
//import androidx.room.RoomDatabase
//
//
//@Database(entities = [Transaction::class], version = 1)
//abstract class AppDatabase : RoomDatabase() {
//    abstract fun transactionDao(): TransactionDao
//
//    companion object {
//        @Volatile private var INSTANCE: AppDatabase? = null
//
//        fun getDatabase(context: Context): AppDatabase {
//            return INSTANCE ?: synchronized(this) {
//                Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "finance_tracker_db").build().also {
//                    INSTANCE = it
//                }
//            }
//        }
//    }
//}
