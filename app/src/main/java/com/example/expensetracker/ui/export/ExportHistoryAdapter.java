package com.example.expensetracker.ui.export;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.expensetracker.data.entity.ExportHistory;
import com.example.expensetracker.databinding.ItemExportHistoryBinding;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ExportHistoryAdapter extends RecyclerView.Adapter<ExportHistoryAdapter.ViewHolder> {
    private final List<ExportHistory> exportHistory;
    private final OnDownloadClickListener listener;
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault());

    public interface OnDownloadClickListener {
        void onDownloadClick(ExportHistory history);
    }

    public ExportHistoryAdapter(List<ExportHistory> exportHistory, OnDownloadClickListener listener) {
        this.exportHistory = new ArrayList<>(exportHistory);
        this.listener = listener;
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
        holder.bind(exportHistory.get(position));
    }

    @Override
    public int getItemCount() {
        return exportHistory.size();
    }

    public void addHistory(ExportHistory history) {
        exportHistory.add(0, history);
        notifyItemInserted(0);
    }

    public void updateHistory(List<ExportHistory> newHistory) {
        exportHistory.clear();
        exportHistory.addAll(newHistory);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemExportHistoryBinding binding;

        ViewHolder(ItemExportHistoryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(ExportHistory history) {
            binding.exportDate.setText(DATE_FORMAT.format(history.getExportDate()));
            binding.fileName.setText(history.getFileName());
            binding.btnDownload.setOnClickListener(v -> listener.onDownloadClick(history));
        }
    }
} 