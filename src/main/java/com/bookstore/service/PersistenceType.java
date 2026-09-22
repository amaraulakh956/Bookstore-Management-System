package com.bookstore.service;

/** Which storage backend {@link BookstoreService} should use. */
public enum PersistenceType {
    /** Original comma-separated text files: books.txt, customers.txt, purchases.txt. */
    FILE,
    /** SQLite database file (bookstore.db) accessed over JDBC. */
    SQLITE
}
