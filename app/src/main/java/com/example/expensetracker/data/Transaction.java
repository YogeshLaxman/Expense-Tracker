package com.example.expensetracker.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;

@Entity(tableName = "transactions")
public class Transaction {
    @PrimaryKey
    @ColumnInfo(name = "transaction_id")
    private String transactionId;

    @ColumnInfo(name = "account")
    private String account;

    @ColumnInfo(name = "transaction_date")
    private String transactionDate;

    @ColumnInfo(name = "post_date")
    private String postDate;

    @ColumnInfo(name = "description")
    private String description;

    @ColumnInfo(name = "category")
    private String category;

    @ColumnInfo(name = "type")
    private String type;

    @ColumnInfo(name = "amount")
    private double amount;

    @ColumnInfo(name = "memo")
    private String memo;

    @ColumnInfo(name = "is_final")
    private boolean isFinal;

    // Constructor
    public Transaction(String transactionId, String account, String transactionDate, 
                      String postDate, String description, String category, 
                      String type, double amount, String memo, boolean isFinal) {
        this.transactionId = transactionId;
        this.account = account;
        this.transactionDate = transactionDate;
        this.postDate = postDate;
        this.description = description;
        this.category = category;
        this.type = type;
        this.amount = amount;
        this.memo = memo;
        this.isFinal = isFinal;
    }

    // Getters and Setters
    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }

    public String getAccount() { return account; }
    public void setAccount(String account) { this.account = account; }

    public String getTransactionDate() { return transactionDate; }
    public void setTransactionDate(String transactionDate) { this.transactionDate = transactionDate; }

    public String getPostDate() { return postDate; }
    public void setPostDate(String postDate) { this.postDate = postDate; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public String getMemo() { return memo; }
    public void setMemo(String memo) { this.memo = memo; }

    public boolean isFinal() { return isFinal; }
    public void setFinal(boolean isFinal) { this.isFinal = isFinal; }
} 