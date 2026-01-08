package com.example.financetrackerex3;

import androidx.lifecycle.LiveData
import androidx.room.*

import java.util.List;


@Dao
interface TransactionDao {
    @Insert suspend fun insert(transaction: Transaction)
    @Update suspend fun update(transaction: Transaction)
    @Delete suspend fun delete(transaction: Transaction)
    @Query("SELECT * FROM transactions ORDER BY date DESC")
    fun getAll(): LiveData<List<Transaction>>
}
