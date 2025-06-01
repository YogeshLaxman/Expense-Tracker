package com.example.expensetracker.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TagDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Tag tag);

    @Update
    void update(Tag tag);

    @Delete
    void delete(Tag tag);

    @Query("SELECT * FROM tags ORDER BY tag_name ASC")
    LiveData<List<Tag>> getAllTags();

    @Query("SELECT * FROM tags WHERE tag_name = :tagName")
    LiveData<Tag> getTag(String tagName);

    @Query("SELECT * FROM tags WHERE tag_name LIKE '%' || :searchQuery || '%'")
    LiveData<List<Tag>> searchTags(String searchQuery);
} 