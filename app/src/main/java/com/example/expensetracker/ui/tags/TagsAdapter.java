package com.example.expensetracker.ui.tags;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.expensetracker.data.entity.Tag;
import com.example.expensetracker.databinding.ItemTagBinding;
import java.util.ArrayList;
import java.util.List;

public class TagsAdapter extends RecyclerView.Adapter<TagsAdapter.ViewHolder> {
    private final List<Tag> tags;
    private OnTagClickListener listener;

    public interface OnTagClickListener {
        void onTagClick(Tag tag);
        void onTagDelete(Tag tag);
    }

    public TagsAdapter(List<Tag> tags) {
        this.tags = new ArrayList<>(tags);
    }

    public void setOnTagClickListener(OnTagClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemTagBinding binding = ItemTagBinding.inflate(
            LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(tags.get(position));
    }

    @Override
    public int getItemCount() {
        return tags.size();
    }

    public void setTags(List<Tag> newTags) {
        tags.clear();
        tags.addAll(newTags);
        notifyDataSetChanged();
    }

    public void addTag(Tag tag) {
        tags.add(tag);
        notifyItemInserted(tags.size() - 1);
    }

    public void removeTag(Tag tag) {
        int position = tags.indexOf(tag);
        if (position != -1) {
            tags.remove(position);
            notifyItemRemoved(position);
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemTagBinding binding;

        ViewHolder(ItemTagBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(Tag tag) {
            binding.tagName.setText(tag.getName());
            binding.tagColor.setBackgroundColor(tag.getColor());
            
            binding.getRoot().setOnClickListener(v -> {
                if (listener != null) {
                    listener.onTagClick(tag);
                }
            });

            binding.deleteButton.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onTagDelete(tag);
                }
            });
        }
    }
} 