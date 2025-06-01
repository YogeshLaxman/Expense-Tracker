package com.example.expensetracker.ui.export;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.expensetracker.ExpenseTrackerApplication;
import com.example.expensetracker.data.entity.ExportHistory;
import com.example.expensetracker.data.entity.Tag;
import com.example.expensetracker.data.entity.Transaction;
import com.example.expensetracker.databinding.FragmentExportBinding;
import com.example.expensetracker.util.CsvExporter;
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
    private ExpenseTrackerApplication application;
    private ActivityResultLauncher<Intent> saveFileLauncher;
    private ActivityResultLauncher<Intent> openFileLauncher;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupFileLaunchers();
    }

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
        application = (ExpenseTrackerApplication) requireActivity().getApplication();
        setupViews();
        observeExportHistory();
    }

    private void setupViews() {
        binding.exportHistoryList.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new ExportHistoryAdapter(exportHistory, this::downloadFile);
        binding.exportHistoryList.setAdapter(adapter);

        binding.monthYearInput.setOnClickListener(v -> showMonthYearPicker());
        binding.btnExport.setOnClickListener(v -> exportToCsv());
    }

    private void setupFileLaunchers() {
        saveFileLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    Uri uri = result.getData().getData();
                    if (uri != null) {
                        performExport(uri);
                    }
                }
            }
        );

        openFileLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    Uri uri = result.getData().getData();
                    if (uri != null) {
                        requireContext().getContentResolver().takePersistableUriPermission(
                            uri,
                            Intent.FLAG_GRANT_READ_URI_PERMISSION
                        );
                    }
                }
            }
        );
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

        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("text/csv");
        intent.putExtra(Intent.EXTRA_TITLE, CsvExporter.generateFileName(selectedDate));
        saveFileLauncher.launch(intent);
    }

    private void performExport(Uri uri) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(selectedDate);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        Date startDate = calendar.getTime();
        calendar.add(Calendar.MONTH, 1);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        Date endDate = calendar.getTime();

        application.getRepository().getTransactionsBetweenDates(startDate, endDate)
                .observe(getViewLifecycleOwner(), transactions -> {
                    if (transactions.isEmpty()) {
                        Toast.makeText(requireContext(), "No transactions found for the selected month",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }

                    List<Tag> tags = new ArrayList<>();
                    for (Transaction transaction : transactions) {
                        application.getRepository().getTagsForTransaction(transaction.getId())
                            .observe(getViewLifecycleOwner(), tagList -> {
                                tags.addAll(tagList);
                                // Any further logic that depends on tags being added should go here
                            });
                    }

                    CsvExporter.exportTransactions(requireContext(), uri, transactions, tags,
                            binding.includeTagsCheckbox.isChecked());

                    String fileName = CsvExporter.generateFileName(selectedDate);
                    ExportHistory history = new ExportHistory(
                            new Date(),
                            fileName,
                            uri.toString(),
                            "COMPLETED",
                            transactions.size()
                    );
                    application.getRepository().insertExportHistory(history);

                    Toast.makeText(requireContext(), "Export completed successfully", Toast.LENGTH_SHORT).show();
                });
    }

    private void downloadFile(ExportHistory history) {
        try {
            Uri uri = Uri.parse(history.getFileUri());
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(uri, "text/csv");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            
            if (intent.resolveActivity(requireActivity().getPackageManager()) != null) {
                startActivity(intent);
            } else {
                // If no app can handle the file, try to open it with a file picker
                Intent openIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                openIntent.addCategory(Intent.CATEGORY_OPENABLE);
                openIntent.setType("text/csv");
                openFileLauncher.launch(openIntent);
            }
        } catch (Exception e) {
            Toast.makeText(requireContext(), "Error opening file: " + e.getMessage(),
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void observeExportHistory() {
        application.getRepository().getAllExportHistory().observe(getViewLifecycleOwner(), history -> {
            exportHistory.clear();
            exportHistory.addAll(history);
            adapter.notifyDataSetChanged();
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    // Methods for testing
    @VisibleForTesting
    public ExportHistoryAdapter getAdapter() {
        return adapter;
    }

    @VisibleForTesting
    public void setApplication(ExpenseTrackerApplication application) {
        this.application = application;
    }
} 