package com.example.expensetracker.data.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;
import com.example.expensetracker.data.converter.DateConverter;
import java.util.Date;

@Entity(tableName = "transactions")
@TypeConverters(DateConverter.class)
public class Transaction {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private Date date;
    private String description;
    private double amount;
    private String accountName;
    private String fileUri;

    public Transaction(Date date, String description, double amount, String accountName, String fileUri) {
        this.date = date;
        this.description = description;
        this.amount = amount;
        this.accountName = accountName;
        this.fileUri = fileUri;
    }

    // Getters and Setters
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
    public String getAccountName() { return accountName; }
    public void setAccountName(String accountName) { this.accountName = accountName; }
    public String getFileUri() { return fileUri; }
    public void setFileUri(String fileUri) { this.fileUri = fileUri; }
} 