package com.example.expensetracker;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.expensetracker.data.entity.Transaction;
import com.example.expensetracker.databinding.ItemTransactionBinding;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TransactionsAdapter extends RecyclerView.Adapter<TransactionsAdapter.ViewHolder> {
    private final List<Transaction> transactions;
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
    private static final NumberFormat CURRENCY_FORMAT = NumberFormat.getCurrencyInstance(Locale.getDefault());

    public TransactionsAdapter(List<Transaction> transactions) {
        this.transactions = new ArrayList<>(transactions);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemTransactionBinding binding = ItemTransactionBinding.inflate(
            LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(transactions.get(position));
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    public void setTransactions(List<Transaction> newTransactions) {
        transactions.clear();
        transactions.addAll(newTransactions);
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemTransactionBinding binding;

        ViewHolder(ItemTransactionBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(Transaction transaction) {
            binding.transactionDate.setText(DATE_FORMAT.format(transaction.getDate()));
            binding.transactionDescription.setText(transaction.getDescription());
            binding.transactionAmount.setText(CURRENCY_FORMAT.format(transaction.getAmount()));
            binding.transactionAccount.setText(transaction.getAccountName());
            
            // Set the amount color based on transaction type
            int colorRes = transaction.getAmount() >= 0 ? 
                android.R.color.holo_green_dark : android.R.color.holo_red_dark;
            binding.transactionAmount.setTextColor(
                binding.getRoot().getContext().getColor(colorRes));
        }
    }
} 