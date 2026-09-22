package com.bookstore.repository;

import com.bookstore.model.PurchaseRecord;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/** Appends one CSV line per purchase to purchases.txt. */
public class FilePurchaseRepository implements PurchaseRepository {

    private final File file;

    public FilePurchaseRepository(File file) {
        this.file = file;
    }

    public FilePurchaseRepository() {
        this(new File("purchases.txt"));
    }

    @Override
    public List<PurchaseRecord> findAll() {
        List<PurchaseRecord> records = new ArrayList<>();
        if (!file.exists()) {
            return records;
        }
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] data = line.split(",", 4);
                if (data.length == 4) {
                    records.add(new PurchaseRecord(
                            data[0],
                            data[1],
                            Double.parseDouble(data[2]),
                            LocalDateTime.parse(data[3])));
                }
            }
        } catch (IOException e) {
            System.err.println("Could not read " + file.getName() + ": " + e.getMessage());
        }
        return records;
    }

    @Override
    public void save(PurchaseRecord record) {
        try (FileWriter writer = new FileWriter(file, true)) {
            writer.write(record.getCustomerUsername() + "," + record.getBookName() + ","
                    + record.getAmountPaid() + "," + record.getTimestamp() + "\n");
        } catch (IOException e) {
            System.err.println("Could not save purchase: " + e.getMessage());
        }
    }
}
