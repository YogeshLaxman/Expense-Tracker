package com.example.expensetracker.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.*;
import com.example.expensetracker.data.entity.Transaction;
import java.util.Date;
import java.util.List;

@Dao
public interface TransactionDao {
    @Insert
    long insert(Transaction transaction);

    @Insert
    List<Long> insertAll(List<Transaction> transactions);

    @Update
    void update(Transaction transaction);

    @Delete
    void delete(Transaction transaction);

    @Query("SELECT * FROM transactions ORDER BY date DESC")
    LiveData<List<Transaction>> getAllTransactions();

    @Query("SELECT * FROM transactions WHERE date BETWEEN :startDate AND :endDate ORDER BY date DESC")
    LiveData<List<Transaction>> getTransactionsBetweenDates(Date startDate, Date endDate);

    @Query("SELECT * FROM transactions WHERE id = :id")
    LiveData<Transaction> getTransactionById(long id);

    @Query("SELECT * FROM transactions WHERE fileUri = :fileUri")
    List<Transaction> getTransactionsByFileUri(String fileUri);

    @Query("SELECT COUNT(*) FROM transactions")
    int getTransactionCount();
} 