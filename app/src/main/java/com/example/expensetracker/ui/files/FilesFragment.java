package com.example.expensetracker.ui.files;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.expensetracker.ExpenseTrackerApplication;
import com.example.expensetracker.R;
import com.example.expensetracker.data.entity.Transaction;
import com.example.expensetracker.data.entity.ExportHistory;
import com.example.expensetracker.databinding.FragmentFilesBinding;
import com.example.expensetracker.util.CsvParser;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FilesFragment extends Fragment {
    private FragmentFilesBinding binding;
    private ActivityResultLauncher<Intent> csvFilePickerLauncher;
    private static final int STORAGE_PERMISSION_CODE = 100;
    private UploadedFilesAdapter adapter;
    private List<UploadedFilesAdapter.UploadedFile> files = new ArrayList<>();
    private ExpenseTrackerApplication application;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupFilePickerLauncher();
        application = (ExpenseTrackerApplication) requireActivity().getApplication();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                           @Nullable Bundle savedInstanceState) {
        binding = FragmentFilesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupViews();
        observeFiles();
    }

    private void setupViews() {
        binding.filesList.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new UploadedFilesAdapter(files);
        binding.filesList.setAdapter(adapter);

        binding.fabUploadFile.setOnClickListener(v -> showFilePicker());
    }

    private void observeFiles() {
        application.getRepository().getAllExportHistory().observe(getViewLifecycleOwner(), history -> {
            files.clear();
            for (ExportHistory item : history) {
                files.add(new UploadedFilesAdapter.UploadedFile(
                    item.getFileUri(),
                    item.getFileName(),
                    item.getTransactionCount(),
                    item.getExportDate()
                ));
            }
            adapter.updateFiles(files);
        });
    }

    private void showFilePicker() {
        checkPermissionAndOpenFilePicker();
    }

    private void setupFilePickerLauncher() {
        csvFilePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    Uri uri = result.getData().getData();
                    if (uri != null) {
                        processCsvFile(uri);
                    }
                }
            }
        );
    }

    private void checkPermissionAndOpenFilePicker() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(requireContext(),
                    Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{
                    Manifest.permission.READ_MEDIA_IMAGES,
                    Manifest.permission.READ_MEDIA_VIDEO,
                    Manifest.permission.READ_MEDIA_AUDIO
                }, STORAGE_PERMISSION_CODE);
            } else {
                openCsvFilePicker();
            }
        } else {
            if (ContextCompat.checkSelfPermission(requireContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        STORAGE_PERMISSION_CODE);
            } else {
                openCsvFilePicker();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                         @NonNull int[] grantResults) {
        if (requestCode == STORAGE_PERMISSION_CODE) {
            boolean allGranted = true;
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    allGranted = false;
                    break;
                }
            }
            if (allGranted) {
                openCsvFilePicker();
            } else {
                Toast.makeText(requireContext(), "Storage permission is required to read CSV files",
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    private void openCsvFilePicker() {
        try {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("*/*");
            String[] mimeTypes = {
                "text/csv",
                "text/comma-separated-values",
                "application/csv",
                "application/excel",
                "application/vnd.ms-excel",
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
            };
            intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
            
            csvFilePickerLauncher.launch(intent);
        } catch (Exception e) {
            Toast.makeText(requireContext(), "Error opening file picker: " + e.getMessage(),
                    Toast.LENGTH_LONG).show();
        }
    }

    private void processCsvFile(Uri uri) {
        try (InputStream inputStream = requireContext().getContentResolver().openInputStream(uri)) {
            if (inputStream != null) {
                List<Transaction> transactions = CsvParser.parseTransactions(requireContext(), inputStream, uri.toString());
            if (transactions.isEmpty()) {
                Toast.makeText(requireContext(), "No valid transactions found in the file",
                        Toast.LENGTH_SHORT).show();
                return;
            }

                // Show loading indicator
                requireActivity().runOnUiThread(() -> 
                    Toast.makeText(requireContext(), 
                        "Processing transactions...", 
                        Toast.LENGTH_SHORT).show()
                );

                // Insert transactions in a background thread
                new Thread(() -> {
                    try {
                        // First check if file was already processed
                        List<Transaction> existingTransactions = application.getRepository()
                            .getTransactionsByFileUri(uri.toString());
                        
                        if (!existingTransactions.isEmpty()) {
                            requireActivity().runOnUiThread(() ->
                                Toast.makeText(requireContext(), 
                                    "This file has already been processed", 
                                    Toast.LENGTH_SHORT).show()
                            );
                            return;
                        }

                        // Insert transactions
            application.getRepository().insertTransactions(transactions);
            
            // Create export history entry
            ExportHistory exportHistory = new ExportHistory(
                new Date(),
                uri.getLastPathSegment(),
                uri.toString(),
                "COMPLETED",
                transactions.size()
            );
            application.getRepository().insertExportHistory(exportHistory);
            
                        // Show success message on the main thread
                        requireActivity().runOnUiThread(() -> 
                            Toast.makeText(requireContext(), 
                                "File processed successfully. Total transactions: " + transactions.size(),
                                Toast.LENGTH_SHORT).show()
                        );
                    } catch (Exception e) {
                        e.printStackTrace();
                        requireActivity().runOnUiThread(() ->
                            Toast.makeText(requireContext(), 
                                "Error saving transactions: " + e.getMessage(),
                                Toast.LENGTH_SHORT).show()
                        );
                    }
                }).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(requireContext(), "Error reading file: " + e.getMessage(), 
                Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (binding != null) {
            binding.filesList.setAdapter(null);
            binding = null;
        }
    }
} 