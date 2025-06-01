package com.example.expensetracker.data.dao;

import android.content.Context;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import com.example.expensetracker.data.AppDatabase;
import com.example.expensetracker.data.entity.ExportHistory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import java.util.Date;
import java.util.List;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class ExportHistoryDaoTest {
    private AppDatabase db;
    private ExportHistoryDao dao;

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase.class).build();
        dao = db.exportHistoryDao();
    }

    @After
    public void closeDb() {
        db.close();
    }

    @Test
    public void insertAndGetExportHistory() throws Exception {
        ExportHistory history = new ExportHistory(
            new Date(),
            "test_export.csv",
            "content://test/uri",
            "Exported"
        );

        dao.insert(history);
        List<ExportHistory> allHistory = dao.getAllExportHistory().getValue();
        assertNotNull(allHistory);
        assertEquals(1, allHistory.size());
        assertEquals(history.getFileName(), allHistory.get(0).getFileName());
    }

    @Test
    public void deleteExportHistory() throws Exception {
        ExportHistory history = new ExportHistory(
            new Date(),
            "test_export.csv",
            "content://test/uri",
            "Exported"
        );

        dao.insert(history);
        List<ExportHistory> allHistory = dao.getAllExportHistory().getValue();
        assertNotNull(allHistory);
        assertEquals(1, allHistory.size());

        dao.deleteById(allHistory.get(0).getId());
        allHistory = dao.getAllExportHistory().getValue();
        assertNotNull(allHistory);
        assertEquals(0, allHistory.size());
    }
} 