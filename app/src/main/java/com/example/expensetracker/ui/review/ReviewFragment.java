package com.example.expensetracker.ui.review;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;
import com.example.expensetracker.ExpenseTrackerApplication;
import com.example.expensetracker.data.entity.Tag;
import com.example.expensetracker.data.entity.Transaction;
import com.example.expensetracker.databinding.FragmentReviewBinding;
import com.example.expensetracker.ui.dialog.TagSelectionDialog;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointBackward;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ReviewFragment extends Fragment {
    private FragmentReviewBinding binding;
    private TransactionsPagerAdapter pagerAdapter;
    private List<Transaction> transactions = new ArrayList<>();
    private List<Tag> selectedTags = new ArrayList<>();
    private Date startDate;
    private Date endDate;
    private ExpenseTrackerApplication application;
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                           @Nullable Bundle savedInstanceState) {
        binding = FragmentReviewBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        application = (ExpenseTrackerApplication) requireActivity().getApplication();
        setupViews();
        observeTransactions();
    }

    private void setupViews() {
        pagerAdapter = new TransactionsPagerAdapter(this);
        binding.transactionsPager.setAdapter(pagerAdapter);
        binding.transactionsPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                updatePendingCount();
            }
        });

        binding.dateRangeInput.setOnClickListener(v -> showDateRangePicker());
        binding.fabAddTag.setOnClickListener(v -> showAddTagDialog());
    }

    private void observeTransactions() {
        application.getRepository().getAllTransactions().observe(getViewLifecycleOwner(), transactions -> {
            this.transactions.clear();
            this.transactions.addAll(transactions);
            filterTransactions();
        });
    }

    private void showDateRangePicker() {
        MaterialDatePicker<androidx.core.util.Pair<Long, Long>> datePicker = MaterialDatePicker.Builder.dateRangePicker()
            .setTitleText("Select Date Range")
            .setCalendarConstraints(new CalendarConstraints.Builder()
                .setValidator(DateValidatorPointBackward.now())
                .build())
            .build();

        datePicker.addOnPositiveButtonClickListener(selection -> {
            startDate = new Date(selection.first);
            endDate = new Date(selection.second);
            updateDateRangeDisplay();
            filterTransactions();
        });

        datePicker.show(getChildFragmentManager(), "DATE_RANGE_PICKER");
    }

    private void updateDateRangeDisplay() {
        if (startDate != null && endDate != null) {
            String dateRange = DATE_FORMAT.format(startDate) + " - " + DATE_FORMAT.format(endDate);
            binding.dateRangeInput.setText(dateRange);
        }
    }

    private void showAddTagDialog() {
        application.getRepository().getAllTags().observe(getViewLifecycleOwner(), tags -> {
            TagSelectionDialog dialog = TagSelectionDialog.newInstance(
                new ArrayList<>(tags),
                new ArrayList<>(selectedTags)
            );
            
            dialog.setOnTagsSelectedListener(selected -> {
                selectedTags.clear();
                selectedTags.addAll(selected);
                filterTransactions();
            });
            
            dialog.show(getChildFragmentManager(), "TAG_SELECTION_DIALOG");
        });
    }

    private void filterTransactions() {
        List<Transaction> filteredTransactions = new ArrayList<>(transactions);
        
        // Filter by date range
        if (startDate != null && endDate != null) {
            filteredTransactions.removeIf(transaction -> {
                Date transactionDate = transaction.getDate();
                return transactionDate.before(startDate) || transactionDate.after(endDate);
            });
        }
        
        // Filter by tags
        if (!selectedTags.isEmpty()) {
            // Create a copy of the list to avoid concurrent modification
            List<Transaction> finalFilteredTransactions = new ArrayList<>(filteredTransactions);
            List<Transaction> toRemove = new ArrayList<>();
            
            // Use a counter to track when all tag checks are complete
            final int[] remainingChecks = {finalFilteredTransactions.size()};
            
            for (Transaction transaction : finalFilteredTransactions) {
                application.getRepository().getTagsForTransaction(transaction.getId())
                    .observe(getViewLifecycleOwner(), tags -> {
                        if (!tags.containsAll(selectedTags)) {
                            toRemove.add(transaction);
                        }
                        
                        remainingChecks[0]--;
                        if (remainingChecks[0] == 0) {
                            // All tag checks are complete
                            finalFilteredTransactions.removeAll(toRemove);
                            pagerAdapter.setTransactions(finalFilteredTransactions);
                            updatePendingCount();
                        }
            });
        }
            return;
        }

        pagerAdapter.setTransactions(filteredTransactions);
        updatePendingCount();
    }

    private void updatePendingCount() {
        int totalTransactions = pagerAdapter.getItemCount();
        int currentPosition = binding.transactionsPager.getCurrentItem() + 1;
        binding.pendingCount.setText(String.format(Locale.getDefault(), 
            "Reviewing transaction %d of %d", currentPosition, totalTransactions));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
} 