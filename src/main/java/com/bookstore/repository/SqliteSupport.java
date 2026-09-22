package com.bookstore.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Shared JDBC connection + schema bootstrap for the SQLite-backed
 * repositories. Kept as one place so the three tables are always created
 * together against the same database file.
 */
final class SqliteSupport {

    private SqliteSupport() {
    }

    static Connection connect(String jdbcUrl) {
        try {
            Connection connection = DriverManager.getConnection(jdbcUrl);
            createSchema(connection);
            return connection;
        } catch (SQLException e) {
            throw new IllegalStateException("Could not open SQLite database: " + jdbcUrl, e);
        }
    }

    private static void createSchema(Connection connection) throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS books (
                    name TEXT PRIMARY KEY,
                    price REAL NOT NULL,
                    quantity INTEGER NOT NULL DEFAULT 0
                )
                """);
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS customers (
                    username TEXT PRIMARY KEY,
                    password TEXT NOT NULL,
                    points INTEGER NOT NULL DEFAULT 0
                )
                """);
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS purchases (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    customer_username TEXT NOT NULL,
                    book_name TEXT NOT NULL,
                    amount_paid REAL NOT NULL,
                    purchased_at TEXT NOT NULL
                )
                """);
        }
    }
}
