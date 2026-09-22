package com.bookstore.model;

import java.time.LocalDateTime;

/**
 * A single completed transaction, kept so the owner can view a sales
 * report (total revenue, best-selling titles, top customers).
 */
public class PurchaseRecord {

    private final String customerUsername;
    private final String bookName;
    private final double amountPaid;
    private final LocalDateTime timestamp;

    public PurchaseRecord(String customerUsername, String bookName, double amountPaid, LocalDateTime timestamp) {
        this.customerUsername = customerUsername;
        this.bookName = bookName;
        this.amountPaid = amountPaid;
        this.timestamp = timestamp;
    }

    public String getCustomerUsername() {
        return customerUsername;
    }

    public String getBookName() {
        return bookName;
    }

    public double getAmountPaid() {
        return amountPaid;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
