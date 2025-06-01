package com.example.expensetracker.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.*;
import com.example.expensetracker.data.entity.Tag;
import com.example.expensetracker.data.entity.TransactionTagCrossRef;
import java.util.List;

@Dao
public interface TagDao {
    @Insert
    long insert(Tag tag);

    @Insert
    List<Long> insertAll(List<Tag> tags);

    @Update
    void update(Tag tag);

    @Delete
    void delete(Tag tag);

    @Query("SELECT * FROM tags ORDER BY name ASC")
    LiveData<List<Tag>> getAllTags();

    @Query("SELECT * FROM tags WHERE id = :id")
    LiveData<Tag> getTagById(long id);

    @Query("SELECT * FROM tags WHERE name = :name LIMIT 1")
    Tag getTagByName(String name);

    @Query("SELECT COUNT(*) FROM tags")
    int getTagCount();

    @RewriteQueriesToDropUnusedColumns
    @Query("SELECT tags.* FROM tags INNER JOIN transaction_tag_cross_ref ON tags.id = transaction_tag_cross_ref.tagId WHERE transaction_tag_cross_ref.transactionId = :transactionId")
    LiveData<List<Tag>> getTagsForTransaction(long transactionId);

    @Insert
    void addTagToTransaction(TransactionTagCrossRef crossRef);

    @Delete
    void removeTagFromTransaction(TransactionTagCrossRef crossRef);
} 