package com.example.expensetracker.data;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import com.example.expensetracker.data.converter.DateConverter;
import com.example.expensetracker.data.dao.ExportHistoryDao;
import com.example.expensetracker.data.dao.TagDao;
import com.example.expensetracker.data.dao.TransactionDao;
import com.example.expensetracker.data.entity.ExportHistory;
import com.example.expensetracker.data.entity.Tag;
import com.example.expensetracker.data.entity.Transaction;
import com.example.expensetracker.data.entity.TransactionTagCrossRef;

@Database(entities = {
        Transaction.class,
        Tag.class,
        TransactionTagCrossRef.class,
        ExportHistory.class
}, version = 4, exportSchema = false)
@TypeConverters({DateConverter.class})
public abstract class AppDatabase extends RoomDatabase {
    private static final String DATABASE_NAME = "expense_tracker.db";
    private static volatile AppDatabase instance;

    public abstract TransactionDao transactionDao();
    public abstract TagDao tagDao();
    public abstract ExportHistoryDao exportHistoryDao();

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                    context.getApplicationContext(),
                    AppDatabase.class,
                    DATABASE_NAME)
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries() // Temporarily allow main thread queries for debugging
                    .build();
        }
        return instance;
    }
} 