package com.example.expensetracker;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.expensetracker.databinding.FragmentExportBinding;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ExportFragment extends Fragment {
    private FragmentExportBinding binding;
    private ExportHistoryAdapter adapter;
    private List<ExportHistory> exportHistory = new ArrayList<>();
    private Date selectedDate;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                           @Nullable Bundle savedInstanceState) {
        binding = FragmentExportBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupViews();
        loadExportHistory();
    }

    private void setupViews() {
        binding.exportHistoryList.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new ExportHistoryAdapter(exportHistory, this::downloadFile);
        binding.exportHistoryList.setAdapter(adapter);

        binding.monthYearInput.setOnClickListener(v -> showMonthYearPicker());
        binding.btnExport.setOnClickListener(v -> exportToCsv());
    }

    private void showMonthYearPicker() {
        MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select Month and Year")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build();

        datePicker.addOnPositiveButtonClickListener(selection -> {
            selectedDate = new Date(selection);
            updateDateDisplay();
        });

        datePicker.show(getChildFragmentManager(), "MONTH_YEAR_PICKER");
    }

    private void updateDateDisplay() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM yyyy", Locale.getDefault());
        ((TextInputEditText) binding.monthYearInput).setText(dateFormat.format(selectedDate));
    }

    private void exportToCsv() {
        if (selectedDate == null) {
            Toast.makeText(requireContext(), "Please select a month and year", Toast.LENGTH_SHORT).show();
            return;
        }

        // TODO: Implement CSV export
        // 1. Get transactions for the selected month
        // 2. Create CSV file with transaction data
        // 3. Add tags if checkbox is checked
        // 4. Save file and add to export history

        Toast.makeText(requireContext(), "Export functionality coming soon", Toast.LENGTH_SHORT).show();
    }

    private void downloadFile(ExportHistory history) {
        // TODO: Implement file download
        Toast.makeText(requireContext(), "Download functionality coming soon", Toast.LENGTH_SHORT).show();
    }

    private void loadExportHistory() {
        // TODO: Load export history from database
        // For now, add some dummy data
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -1);
        exportHistory.add(new ExportHistory(
                calendar.getTime(),
                "expenses_2024_02.csv",
                "Downloaded"
        ));
        calendar.add(Calendar.MONTH, -1);
        exportHistory.add(new ExportHistory(
                calendar.getTime(),
                "expenses_2024_01.csv",
                "Downloaded"
        ));
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public static class ExportHistory {
        private final Date exportDate;
        private final String fileName;
        private final String status;

        public ExportHistory(Date exportDate, String fileName, String status) {
            this.exportDate = exportDate;
            this.fileName = fileName;
            this.status = status;
        }

        public Date getExportDate() {
            return exportDate;
        }

        public String getFileName() {
            return fileName;
        }

        public String getStatus() {
            return status;
        }
    }
} 