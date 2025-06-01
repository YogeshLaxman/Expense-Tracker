package com.example.expensetracker.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import com.example.expensetracker.data.entity.TransactionTagCrossRef;
import java.util.List;

@Dao
public interface TransactionTagCrossRefDao {
    @Insert
    void insert(TransactionTagCrossRef crossRef);

    @Insert
    void insertAll(List<TransactionTagCrossRef> crossRefs);

    @Delete
    void delete(TransactionTagCrossRef crossRef);

    @Query("SELECT * FROM transaction_tag_cross_ref WHERE transactionId = :transactionId")
    List<TransactionTagCrossRef> getCrossRefsForTransaction(String transactionId);

    @Query("SELECT * FROM transaction_tag_cross_ref WHERE tagId = :tagId")
    List<TransactionTagCrossRef> getCrossRefsForTag(String tagId);

    @Query("DELETE FROM transaction_tag_cross_ref WHERE transactionId = :transactionId")
    void deleteCrossRefsForTransaction(String transactionId);

    @Query("DELETE FROM transaction_tag_cross_ref WHERE tagId = :tagId")
    void deleteCrossRefsForTag(String tagId);
} 