package com.example.expensetracker.data;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;
import com.example.expensetracker.data.entity.Tag;
import com.example.expensetracker.data.entity.Transaction;
import com.example.expensetracker.data.entity.TransactionTagCrossRef;
import java.util.List;

public class TransactionWithTags {
    @Embedded
    public Transaction transaction;

    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = @Junction(
            value = TransactionTagCrossRef.class,
            parentColumn = "transactionId",
            entityColumn = "tagId"
        )
    )
    public List<Tag> tags;

    public Transaction getTransaction() { return transaction; }
    public void setTransaction(Transaction transaction) { this.transaction = transaction; }

    public List<Tag> getTags() { return tags; }
    public void setTags(List<Tag> tags) { this.tags = tags; }
} 