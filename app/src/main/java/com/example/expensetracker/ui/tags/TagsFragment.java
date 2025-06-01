package com.example.expensetracker.ui.tags;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.expensetracker.ExpenseTrackerApplication;
import com.example.expensetracker.R;
import com.example.expensetracker.data.entity.Tag;
import com.example.expensetracker.databinding.FragmentTagsBinding;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import java.util.ArrayList;
import java.util.List;

public class TagsFragment extends Fragment implements TagsAdapter.OnTagClickListener {
    private FragmentTagsBinding binding;
    private TagsAdapter adapter;
    private List<Tag> tags = new ArrayList<>();
    private ExpenseTrackerApplication application;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                           @Nullable Bundle savedInstanceState) {
        binding = FragmentTagsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        application = (ExpenseTrackerApplication) requireActivity().getApplication();
        setupViews();
        observeTags();
    }

    private void setupViews() {
        binding.tagsList.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new TagsAdapter(tags);
        adapter.setOnTagClickListener(this);
        binding.tagsList.setAdapter(adapter);

        binding.fabAddTag.setOnClickListener(v -> showAddTagDialog());
    }

    private void observeTags() {
        application.getRepository().getAllTags().observe(getViewLifecycleOwner(), tags -> {
            this.tags.clear();
            this.tags.addAll(tags);
            adapter.setTags(tags);
        });
    }

    private void showAddTagDialog() {
        AddTagDialog dialog = new AddTagDialog();
        dialog.setOnTagAddedListener(tag -> {
            application.getRepository().insertTag(tag);
        });
        dialog.show(getChildFragmentManager(), "ADD_TAG_DIALOG");
    }

    @Override
    public void onTagClick(Tag tag) {
        // TODO: Implement tag editing
    }

    @Override
    public void onTagDelete(Tag tag) {
        new MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.delete_tag_title)
            .setMessage(getString(R.string.delete_tag_message, tag.getName()))
            .setPositiveButton(R.string.delete, (dialog, which) -> {
                application.getRepository().deleteTag(tag);
            })
            .setNegativeButton(R.string.cancel, null)
            .show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
} 