package com.bookstore.repository;

import com.bookstore.model.PurchaseRecord;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SqlitePurchaseRepository implements PurchaseRepository {

    private final String jdbcUrl;

    public SqlitePurchaseRepository(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
    }

    public SqlitePurchaseRepository() {
        this("jdbc:sqlite:bookstore.db");
    }

    @Override
    public List<PurchaseRecord> findAll() {
        List<PurchaseRecord> records = new ArrayList<>();
        try (Connection conn = SqliteSupport.connect(jdbcUrl);
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT customer_username, book_name, amount_paid, purchased_at FROM purchases");
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                records.add(new PurchaseRecord(
                        rs.getString("customer_username"),
                        rs.getString("book_name"),
                        rs.getDouble("amount_paid"),
                        LocalDateTime.parse(rs.getString("purchased_at"))));
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to load purchases from SQLite", e);
        }
        return records;
    }

    @Override
    public void save(PurchaseRecord record) {
        try (Connection conn = SqliteSupport.connect(jdbcUrl);
             PreparedStatement insert = conn.prepareStatement(
                     "INSERT INTO purchases (customer_username, book_name, amount_paid, purchased_at) VALUES (?, ?, ?, ?)")) {
            insert.setString(1, record.getCustomerUsername());
            insert.setString(2, record.getBookName());
            insert.setDouble(3, record.getAmountPaid());
            insert.setString(4, record.getTimestamp().toString());
            insert.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to save purchase to SQLite", e);
        }
    }
}
