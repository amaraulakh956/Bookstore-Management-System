package com.bookstore.repository;

import com.bookstore.model.Customer;
import com.bookstore.state.GoldState;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SqliteCustomerRepository implements CustomerRepository {

    private static final int GOLD_THRESHOLD = 1000;

    private final String jdbcUrl;

    public SqliteCustomerRepository(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
    }

    public SqliteCustomerRepository() {
        this("jdbc:sqlite:bookstore.db");
    }

    @Override
    public List<Customer> findAll() {
        List<Customer> customers = new ArrayList<>();
        try (Connection conn = SqliteSupport.connect(jdbcUrl);
             PreparedStatement stmt = conn.prepareStatement("SELECT username, password, points FROM customers");
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Customer c = new Customer(rs.getString("username"), rs.getString("password"));
                int points = rs.getInt("points");
                c.setPoints(points);
                if (points >= GOLD_THRESHOLD) {
                    c.setState(new GoldState());
                }
                customers.add(c);
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to load customers from SQLite", e);
        }
        return customers;
    }

    @Override
    public void saveAll(List<Customer> customers) {
        try (Connection conn = SqliteSupport.connect(jdbcUrl)) {
            conn.setAutoCommit(false);
            try (PreparedStatement clear = conn.prepareStatement("DELETE FROM customers")) {
                clear.executeUpdate();
            }
            try (PreparedStatement insert = conn.prepareStatement(
                    "INSERT INTO customers (username, password, points) VALUES (?, ?, ?)")) {
                for (Customer c : customers) {
                    insert.setString(1, c.getUsername());
                    insert.setString(2, c.getPassword());
                    insert.setInt(3, c.getPoints());
                    insert.addBatch();
                }
                insert.executeBatch();
            }
            conn.commit();
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to save customers to SQLite", e);
        }
    }
}
