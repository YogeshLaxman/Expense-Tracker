package com.example.expensetracker.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;

@Entity(tableName = "tags")
public class Tag {
    @PrimaryKey
    @ColumnInfo(name = "tag_name")
    private String tagName;

    @ColumnInfo(name = "created_at")
    private long createdAt;

    public Tag(String tagName) {
        this.tagName = tagName;
        this.createdAt = System.currentTimeMillis();
    }

    public String getTagName() { return tagName; }
    public void setTagName(String tagName) { this.tagName = tagName; }

    public long getCreatedAt() { return createdAt; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }
} 