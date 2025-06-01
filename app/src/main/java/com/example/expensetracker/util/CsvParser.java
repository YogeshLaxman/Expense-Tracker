package com.example.expensetracker.util;

import android.content.Context;
import com.example.expensetracker.ExpenseTrackerApplication;
import com.example.expensetracker.data.entity.Transaction;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CsvParser {
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());

    public static List<Transaction> parseTransactions(Context context, InputStream inputStream, String fileUri) {
        List<Transaction> transactions = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            // Skip header row
            reader.readLine();

            String line;
            while ((line = reader.readLine()) != null) {
                String[] values = parseCSVLine(line);
                if (values.length < 6) continue;

                try {
                    Date date = DATE_FORMAT.parse(values[0]);
                    String description = values[2];
                    double amount = parseAmount(values[5]);

                    Transaction transaction = new Transaction(date, description, amount, values[1], fileUri);
                    transactions.add(transaction);
                } catch (Exception e) {
                    // Skip invalid rows
                    continue;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error parsing CSV file: " + e.getMessage());
        }

        return transactions;
    }

    private static String[] parseCSVLine(String line) {
        List<String> values = new ArrayList<>();
        StringBuilder currentValue = new StringBuilder();
        boolean inQuotes = false;

        for (char c : line.toCharArray()) {
            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
                values.add(currentValue.toString().trim());
                currentValue = new StringBuilder();
            } else {
                currentValue.append(c);
            }
        }
        values.add(currentValue.toString().trim());

        return values.toArray(new String[0]);
    }

    private static double parseAmount(String amountStr) {
        // Remove currency symbol and commas
        amountStr = amountStr.replaceAll("[$,]", "");
        return Double.parseDouble(amountStr);
    }
} 