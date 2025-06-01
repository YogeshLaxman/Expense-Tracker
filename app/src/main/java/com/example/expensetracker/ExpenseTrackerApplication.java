package com.example.expensetracker;

import android.app.Application;
import com.example.expensetracker.data.ExpenseRepository;

public class ExpenseTrackerApplication extends Application {
    private ExpenseRepository repository;

    @Override
    public void onCreate() {
        super.onCreate();
        repository = new ExpenseRepository(this);
    }

    public ExpenseRepository getRepository() {
        return repository;
    }
} 