package com.bookstore.model;

/**
 * The single administrative account. Actual book/customer management is
 * performed by {@code com.bookstore.service.BookstoreService}; Owner is
 * just the role returned on a successful admin login.
 */
public class Owner extends User {

    public Owner() {
        super("admin", "admin");
    }
}
