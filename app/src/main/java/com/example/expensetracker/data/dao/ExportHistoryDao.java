package com.example.expensetracker.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import com.example.expensetracker.data.entity.ExportHistory;
import java.util.List;

@Dao
public interface ExportHistoryDao {
    @Insert
    void insert(ExportHistory exportHistory);

    @Query("SELECT * FROM export_history ORDER BY exportDate DESC")
    LiveData<List<ExportHistory>> getAllExportHistory();

    @Query("DELETE FROM export_history WHERE id = :id")
    void deleteById(long id);
} 