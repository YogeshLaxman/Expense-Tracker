package com.example.expensetracker.util;

import android.content.Context;
import android.net.Uri;
import com.example.expensetracker.data.entity.Transaction;
import com.example.expensetracker.data.entity.Tag;
import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CsvExporter {
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    private static final SimpleDateFormat FILE_NAME_FORMAT = new SimpleDateFormat("yyyy-MM", Locale.getDefault());

    public static void exportTransactions(Context context, Uri uri, List<Transaction> transactions, 
                                        List<Tag> tags, boolean includeTags) {
        try (OutputStream outputStream = context.getContentResolver().openOutputStream(uri);
             BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream))) {
            
            // Write header
            StringBuilder header = new StringBuilder("Date,Description,Amount,Account");
            if (includeTags) {
                header.append(",Tags");
            }
            writer.write(header.toString());
            writer.newLine();

            // Write transactions
            for (Transaction transaction : transactions) {
                StringBuilder line = new StringBuilder()
                    .append(DATE_FORMAT.format(transaction.getDate())).append(",")
                    .append(escapeCsv(transaction.getDescription())).append(",")
                    .append(transaction.getAmount()).append(",")
                    .append(escapeCsv(transaction.getAccountName()));

                if (includeTags) {
                    line.append(",").append(escapeCsv(getTagsString(tags)));
                }

                writer.write(line.toString());
                writer.newLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String escapeCsv(String value) {
        if (value == null) return "";
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }

    private static String getTagsString(List<Tag> tags) {
        if (tags == null || tags.isEmpty()) return "";
        StringBuilder tagString = new StringBuilder();
        for (int i = 0; i < tags.size(); i++) {
            if (i > 0) tagString.append(";");
            tagString.append(tags.get(i).getName());
        }
        return tagString.toString();
    }

    public static String generateFileName(Date date) {
        return "expenses_" + FILE_NAME_FORMAT.format(date) + ".csv";
    }
} 