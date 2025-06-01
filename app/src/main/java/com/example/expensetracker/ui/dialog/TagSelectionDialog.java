package com.example.expensetracker.ui.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import com.example.expensetracker.R;
import com.example.expensetracker.data.entity.Tag;
import com.example.expensetracker.databinding.DialogTagSelectionBinding;
import com.google.android.material.chip.Chip;
import java.util.ArrayList;
import java.util.List;

public class TagSelectionDialog extends DialogFragment {
    private DialogTagSelectionBinding binding;
    private List<Tag> allTags;
    private List<Tag> selectedTags;
    private OnTagsSelectedListener listener;

    public interface OnTagsSelectedListener {
        void onTagsSelected(List<Tag> selectedTags);
    }

    public static TagSelectionDialog newInstance(List<Tag> allTags, List<Tag> selectedTags) {
        TagSelectionDialog dialog = new TagSelectionDialog();
        dialog.allTags = new ArrayList<>(allTags);
        dialog.selectedTags = new ArrayList<>(selectedTags);
        return dialog;
    }

    public void setOnTagsSelectedListener(OnTagsSelectedListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        binding = DialogTagSelectionBinding.inflate(LayoutInflater.from(getContext()));

        // Create chips for all tags
        for (Tag tag : allTags) {
            Chip chip = new Chip(requireContext());
            chip.setText(tag.getName());
            chip.setCheckable(true);
            chip.setChecked(selectedTags.contains(tag));
            chip.setChipBackgroundColorResource(tag.getColor());
            chip.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    selectedTags.add(tag);
                } else {
                    selectedTags.remove(tag);
                }
            });
            binding.tagsContainer.addView(chip);
        }

        return new AlertDialog.Builder(requireContext())
            .setTitle(R.string.select_tags)
            .setView(binding.getRoot())
            .setPositiveButton(R.string.ok, (dialog, which) -> {
            if (listener != null) {
                    listener.onTagsSelected(new ArrayList<>(selectedTags));
            }
            })
            .setNegativeButton(R.string.cancel, null)
            .create();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
} 