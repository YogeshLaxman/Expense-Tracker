package com.example.expensetracker;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expensetracker.databinding.ItemExportHistoryBinding;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class ExportHistoryAdapter extends RecyclerView.Adapter<ExportHistoryAdapter.ViewHolder> {
    private final List<ExportFragment.ExportHistory> history;
    private final OnDownloadClickListener downloadListener;

    public interface OnDownloadClickListener {
        void onDownload(ExportFragment.ExportHistory history);
    }

    public ExportHistoryAdapter(List<ExportFragment.ExportHistory> history,
                              OnDownloadClickListener downloadListener) {
        this.history = history;
        this.downloadListener = downloadListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemExportHistoryBinding binding = ItemExportHistoryBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(history.get(position));
    }

    @Override
    public int getItemCount() {
        return history.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemExportHistoryBinding binding;
        private final SimpleDateFormat dateFormat;

        public ViewHolder(ItemExportHistoryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            this.dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
        }

        public void bind(ExportFragment.ExportHistory history) {
            binding.exportDate.setText(dateFormat.format(history.getExportDate()));
            binding.fileName.setText(history.getFileName());
            binding.btnDownload.setOnClickListener(v -> downloadListener.onDownload(history));
        }
    }
} 