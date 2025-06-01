package com.example.expensetracker.data.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.util.Date;

@Entity(tableName = "export_history")
public class ExportHistory {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private Date exportDate;
    private String fileName;
    private String fileUri;
    private String status;
    private int transactionCount;

    public ExportHistory(Date exportDate, String fileName, String fileUri, String status, int transactionCount) {
        this.exportDate = exportDate;
        this.fileName = fileName;
        this.fileUri = fileUri;
        this.status = status;
        this.transactionCount = transactionCount;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getExportDate() {
        return exportDate;
    }

    public void setExportDate(Date exportDate) {
        this.exportDate = exportDate;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileUri() {
        return fileUri;
    }

    public void setFileUri(String fileUri) {
        this.fileUri = fileUri;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getTransactionCount() {
        return transactionCount;
    }

    public void setTransactionCount(int transactionCount) {
        this.transactionCount = transactionCount;
    }
} 