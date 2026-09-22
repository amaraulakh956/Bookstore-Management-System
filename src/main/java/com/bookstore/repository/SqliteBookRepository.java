package com.bookstore.repository;

import com.bookstore.model.Book;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SqliteBookRepository implements BookRepository {

    private final String jdbcUrl;

    public SqliteBookRepository(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
    }

    public SqliteBookRepository() {
        this("jdbc:sqlite:bookstore.db");
    }

    @Override
    public List<Book> findAll() {
        List<Book> books = new ArrayList<>();
        try (Connection conn = SqliteSupport.connect(jdbcUrl);
             PreparedStatement stmt = conn.prepareStatement("SELECT name, price, quantity FROM books");
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                books.add(new Book(rs.getString("name"), rs.getDouble("price"), rs.getInt("quantity")));
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to load books from SQLite", e);
        }
        return books;
    }

    @Override
    public void saveAll(List<Book> books) {
        try (Connection conn = SqliteSupport.connect(jdbcUrl)) {
            conn.setAutoCommit(false);
            try (PreparedStatement clear = conn.prepareStatement("DELETE FROM books")) {
                clear.executeUpdate();
            }
            try (PreparedStatement insert = conn.prepareStatement(
                    "INSERT INTO books (name, price, quantity) VALUES (?, ?, ?)")) {
                for (Book b : books) {
                    insert.setString(1, b.getName());
                    insert.setDouble(2, b.getPrice());
                    insert.setInt(3, b.getQuantity());
                    insert.addBatch();
                }
                insert.executeBatch();
            }
            conn.commit();
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to save books to SQLite", e);
        }
    }
}
