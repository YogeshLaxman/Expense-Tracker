package com.example.expensetracker.data.entity;

import org.junit.Test;
import java.util.Date;
import static org.junit.Assert.*;

public class ExportHistoryTest {
    @Test
    public void testExportHistoryCreation() {
        Date now = new Date();
        String fileName = "test_export.csv";
        String fileUri = "content://test/uri";
        String status = "Exported";

        ExportHistory history = new ExportHistory(now, fileName, fileUri, status);

        assertEquals(now, history.getExportDate());
        assertEquals(fileName, history.getFileName());
        assertEquals(fileUri, history.getFileUri());
        assertEquals(status, history.getStatus());
    }

    @Test
    public void testExportHistorySetters() {
        ExportHistory history = new ExportHistory(new Date(), "test.csv", "uri", "status");

        Date newDate = new Date();
        String newFileName = "new_test.csv";
        String newFileUri = "new_uri";
        String newStatus = "Downloaded";

        history.setExportDate(newDate);
        history.setFileName(newFileName);
        history.setFileUri(newFileUri);
        history.setStatus(newStatus);

        assertEquals(newDate, history.getExportDate());
        assertEquals(newFileName, history.getFileName());
        assertEquals(newFileUri, history.getFileUri());
        assertEquals(newStatus, history.getStatus());
    }
} 