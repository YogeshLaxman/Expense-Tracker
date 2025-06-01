package com.example.expensetracker.ui.review;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.expensetracker.ExpenseTrackerApplication;
import com.example.expensetracker.R;
import com.example.expensetracker.data.entity.Tag;
import com.example.expensetracker.data.entity.Transaction;
import com.example.expensetracker.data.entity.TransactionTagCrossRef;
import com.example.expensetracker.databinding.ItemTransactionReviewBinding;
import com.example.expensetracker.ui.dialog.TagSelectionDialog;
import com.google.android.material.chip.Chip;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TransactionsPagerAdapter extends RecyclerView.Adapter<TransactionsPagerAdapter.TransactionViewHolder> {
    private final ReviewFragment fragment;
    private final List<Transaction> transactions = new ArrayList<>();
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());

    public TransactionsPagerAdapter(ReviewFragment fragment) {
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemTransactionReviewBinding binding = ItemTransactionReviewBinding.inflate(
            LayoutInflater.from(parent.getContext()), parent, false);
        return new TransactionViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionViewHolder holder, int position) {
        Transaction transaction = transactions.get(position);
        holder.bind(transaction);
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions.clear();
        this.transactions.addAll(transactions);
        notifyDataSetChanged();
    }

    class TransactionViewHolder extends RecyclerView.ViewHolder {
        private final ItemTransactionReviewBinding binding;
        private final ExpenseTrackerApplication application;

        TransactionViewHolder(ItemTransactionReviewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            this.application = (ExpenseTrackerApplication) fragment.requireActivity().getApplication();
        }

        void bind(Transaction transaction) {
            binding.transactionDate.setText(DATE_FORMAT.format(transaction.getDate()));
            binding.transactionDescription.setText(transaction.getDescription());
            binding.transactionAmount.setText(String.format(Locale.getDefault(), "$%.2f", transaction.getAmount()));

            // Observe tags for this transaction
            application.getRepository().getTagsForTransaction(transaction.getId())
                .observe(fragment.getViewLifecycleOwner(), tags -> {
                    binding.tagsContainer.removeAllViews();
                    for (Tag tag : tags) {
                        Chip chip = new Chip(binding.getRoot().getContext());
                        chip.setText(tag.getName());
                        chip.setChipBackgroundColorResource(tag.getColor());
                        binding.tagsContainer.addView(chip);
                    }
                });

            // Set up tag addition
            binding.btnAddTags.setOnClickListener(v -> {
                application.getRepository().getAllTags().observe(fragment.getViewLifecycleOwner(), allTags -> {
                    application.getRepository().getTagsForTransaction(transaction.getId()).observe(fragment.getViewLifecycleOwner(), tags -> {
                        TagSelectionDialog dialog = TagSelectionDialog.newInstance(
                            new ArrayList<>(allTags),
                            new ArrayList<>(tags)
                        );
                        dialog.setOnTagsSelectedListener(selected -> {
                            // Remove old tags
                            for (Tag tag : tags) {
                                application.getRepository().removeTagFromTransaction(
                                    new TransactionTagCrossRef(transaction.getId(), tag.getId()));
                            }
                            // Add new tags
                            for (Tag tag : selected) {
                                application.getRepository().addTagToTransaction(
                                    new TransactionTagCrossRef(transaction.getId(), tag.getId()));
                            }
                        });
                        dialog.show(fragment.getChildFragmentManager(), "TAG_SELECTION_DIALOG");
                    });
                });
            });

            // Set up approval
            binding.btnApprove.setOnClickListener(v -> {
                // TODO: Implement approval logic
                Toast.makeText(fragment.requireContext(), "Transaction approved", Toast.LENGTH_SHORT).show();
            });
        }
    }
} 