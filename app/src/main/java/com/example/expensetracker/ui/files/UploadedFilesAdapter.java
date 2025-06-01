package com.example.expensetracker.ui.files;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.expensetracker.R;
import com.example.expensetracker.databinding.ItemUploadedFileBinding;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class UploadedFilesAdapter extends RecyclerView.Adapter<UploadedFilesAdapter.ViewHolder> {
    private final List<UploadedFile> files;
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault());

    public static class UploadedFile {
        private final String uri;
        private final String accountName;
        private final int transactionCount;
        private final Date uploadDate;

        public UploadedFile(String uri, String accountName, int transactionCount, Date uploadDate) {
            this.uri = uri;
            this.accountName = accountName;
            this.transactionCount = transactionCount;
            this.uploadDate = uploadDate;
        }

        public String getUri() { return uri; }
        public String getAccountName() { return accountName; }
        public int getTransactionCount() { return transactionCount; }
        public Date getUploadDate() { return uploadDate; }
    }

    public UploadedFilesAdapter(List<UploadedFile> files) {
        this.files = new ArrayList<>(files);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemUploadedFileBinding binding = ItemUploadedFileBinding.inflate(
            LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(files.get(position));
    }

    @Override
    public int getItemCount() {
        return files.size();
    }

    public void addFile(UploadedFile file) {
        files.add(0, file);
        notifyItemInserted(0);
    }

    public void updateFiles(List<UploadedFile> newFiles) {
        files.clear();
        files.addAll(newFiles);
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemUploadedFileBinding binding;

        ViewHolder(ItemUploadedFileBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(UploadedFile file) {
            binding.uploadDate.setText(DATE_FORMAT.format(file.getUploadDate()));
            binding.accountName.setText(file.getAccountName());
            binding.transactionCount.setText(
                binding.getRoot().getContext().getString(
                    R.string.transaction_count_format, 
                    file.getTransactionCount()
                )
            );
        }
    }
} 