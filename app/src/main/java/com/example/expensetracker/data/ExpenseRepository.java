package com.example.expensetracker.data;

import android.app.Application;
import androidx.lifecycle.LiveData;
import com.example.expensetracker.data.dao.ExportHistoryDao;
import com.example.expensetracker.data.dao.TagDao;
import com.example.expensetracker.data.dao.TransactionDao;
import com.example.expensetracker.data.entity.ExportHistory;
import com.example.expensetracker.data.entity.Tag;
import com.example.expensetracker.data.entity.Transaction;
import com.example.expensetracker.data.entity.TransactionTagCrossRef;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExpenseRepository {
    private final TransactionDao transactionDao;
    private final TagDao tagDao;
    private final ExportHistoryDao exportHistoryDao;
    private final ExecutorService executorService;

    public ExpenseRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        transactionDao = db.transactionDao();
        tagDao = db.tagDao();
        exportHistoryDao = db.exportHistoryDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    // Transaction operations
    public LiveData<List<Transaction>> getAllTransactions() {
        return transactionDao.getAllTransactions();
    }

    public LiveData<List<Transaction>> getTransactionsBetweenDates(Date startDate, Date endDate) {
        return transactionDao.getTransactionsBetweenDates(startDate, endDate);
    }

    public List<Transaction> getTransactionsByFileUri(String fileUri) {
        return transactionDao.getTransactionsByFileUri(fileUri);
    }

    public void insertTransactions(List<Transaction> transactions) {
        executorService.execute(() -> transactionDao.insertAll(transactions));
    }

    // Tag operations
    public LiveData<List<Tag>> getAllTags() {
        return tagDao.getAllTags();
    }

    public Tag getTagByName(String name) {
        return tagDao.getTagByName(name);
    }

    public void insertTag(Tag tag) {
        executorService.execute(() -> tagDao.insert(tag));
    }

    public void deleteTag(Tag tag) {
        executorService.execute(() -> tagDao.delete(tag));
    }

    public LiveData<List<Tag>> getTagsForTransaction(long transactionId) {
        return tagDao.getTagsForTransaction(transactionId);
    }

    public void addTagToTransaction(TransactionTagCrossRef crossRef) {
        executorService.execute(() -> tagDao.addTagToTransaction(crossRef));
    }

    public void removeTagFromTransaction(TransactionTagCrossRef crossRef) {
        executorService.execute(() -> tagDao.removeTagFromTransaction(crossRef));
    }

    // Export history operations
    public LiveData<List<ExportHistory>> getAllExportHistory() {
        return exportHistoryDao.getAllExportHistory();
    }

    public void insertExportHistory(ExportHistory exportHistory) {
        executorService.execute(() -> exportHistoryDao.insert(exportHistory));
    }

    public void deleteExportHistory(long id) {
        executorService.execute(() -> exportHistoryDao.deleteById(id));
    }
} 