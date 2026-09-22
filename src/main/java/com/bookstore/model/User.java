package com.bookstore.model;

/**
 * Common base for anyone who can log into the system.
 */
public abstract class User {

    protected String username;
    protected String password;

    protected User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
