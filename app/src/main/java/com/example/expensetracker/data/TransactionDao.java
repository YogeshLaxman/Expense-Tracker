package com.example.expensetracker.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TransactionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Transaction transaction);

    @Update
    void update(Transaction transaction);

    @Delete
    void delete(Transaction transaction);

    @Query("SELECT * FROM transactions WHERE transaction_id = :transactionId")
    LiveData<Transaction> getTransaction(String transactionId);

    @Query("SELECT * FROM transactions ORDER BY transaction_date DESC")
    LiveData<List<Transaction>> getAllTransactions();

    @Query("SELECT * FROM transactions WHERE transaction_date BETWEEN :startDate AND :endDate ORDER BY transaction_date DESC")
    LiveData<List<Transaction>> getTransactionsByDateRange(String startDate, String endDate);

    @Query("SELECT * FROM transactions WHERE is_final = 0 ORDER BY transaction_date DESC")
    LiveData<List<Transaction>> getUnreviewedTransactions();

    @Query("SELECT * FROM transactions WHERE description LIKE '%' || :searchQuery || '%'")
    LiveData<List<Transaction>> searchTransactions(String searchQuery);

    @Transaction
    @Query("SELECT * FROM transactions WHERE transaction_id = :transactionId")
    LiveData<TransactionWithTags> getTransactionWithTags(String transactionId);
} 