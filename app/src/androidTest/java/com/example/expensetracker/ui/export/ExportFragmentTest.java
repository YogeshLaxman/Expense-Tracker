package com.example.expensetracker.ui.export;

import android.content.Context;
import androidx.fragment.app.testing.FragmentScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import com.example.expensetracker.ExpenseTrackerApplication;
import com.example.expensetracker.data.entity.ExportHistory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Date;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(AndroidJUnit4.class)
public class ExportFragmentTest {
    @Mock
    private ExpenseTrackerApplication mockApplication;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFragmentCreation() {
        FragmentScenario<ExportFragment> scenario = FragmentScenario.launchInContainer(ExportFragment.class);
        scenario.onFragment(fragment -> {
            assertNotNull(fragment);
            assertNotNull(fragment.getView());
        });
    }

    @Test
    public void testExportHistoryAdapter() {
        FragmentScenario<ExportFragment> scenario = FragmentScenario.launchInContainer(ExportFragment.class);
        scenario.onFragment(fragment -> {
            ExportHistoryAdapter adapter = fragment.getAdapter();
            assertNotNull(adapter);
            assertEquals(0, adapter.getItemCount());

            // Add test data
            ExportHistory history = new ExportHistory(
                new Date(),
                "test_export.csv",
                "content://test/uri",
                "Exported"
            );
            adapter.addHistory(history);
            assertEquals(1, adapter.getItemCount());
        });
    }
} 