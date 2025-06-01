package com.example.expensetracker.ui.tags;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import com.example.expensetracker.R;
import com.example.expensetracker.data.entity.Tag;
import com.example.expensetracker.databinding.DialogAddTagBinding;
import com.google.android.material.color.MaterialColors;
import java.util.Random;

public class AddTagDialog extends DialogFragment {
    private DialogAddTagBinding binding;
    private OnTagAddedListener listener;

    public interface OnTagAddedListener {
        void onTagAdded(Tag tag);
    }

    public void setOnTagAddedListener(OnTagAddedListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        binding = DialogAddTagBinding.inflate(LayoutInflater.from(getContext()));

        return new AlertDialog.Builder(requireContext())
            .setTitle(R.string.add_tag_title)
            .setView(binding.getRoot())
            .setPositiveButton(R.string.add, null) // Set to null initially to prevent auto-dismiss
            .setNegativeButton(R.string.cancel, (dialog, which) -> dismiss())
            .create();
    }

    @Override
    public void onStart() {
        super.onStart();
        AlertDialog dialog = (AlertDialog) getDialog();
        if (dialog != null) {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
                String tagName = binding.tagNameInput.getText().toString().trim();
                if (tagName.isEmpty()) {
                    binding.tagNameInput.setError(getString(R.string.error_tag_name_required));
                    return;
                }

                if (listener != null) {
                    Tag newTag = new Tag(tagName, getRandomColor());
                    listener.onTagAdded(newTag);
                    dismiss();
                }
            });
        }
    }

    private int getRandomColor() {
        Random random = new Random();
        return MaterialColors.getColor(requireContext(), 
            new int[]{
                com.google.android.material.R.attr.colorPrimary,
                com.google.android.material.R.attr.colorSecondary,
                com.google.android.material.R.attr.colorTertiary,
                com.google.android.material.R.attr.colorError
            }[random.nextInt(4)], 
            getString(R.string.app_name));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
} 