package com.bookstore.model;

import java.util.Objects;

/**
 * A book carried by the store. Books now track a stock quantity so that
 * a purchase decrements inventory instead of deleting the title outright.
 */
public class Book {

    private String name;
    private double price;
    private int quantity;

    public Book(String name, double price, int quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    /** Convenience constructor defaulting new stock to 1 unit. */
    public Book(String name, double price) {
        this(name, price, 1);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /** Reduces stock by one unit. Returns false if no stock remained. */
    public boolean decrementStock() {
        if (quantity <= 0) {
            return false;
        }
        quantity--;
        return true;
    }

    public void addStock(int amount) {
        this.quantity += amount;
    }

    public boolean isInStock() {
        return quantity > 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Book)) return false;
        Book book = (Book) o;
        return name.equalsIgnoreCase(book.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name.toLowerCase());
    }

    @Override
    public String toString() {
        return name + " ($" + price + ", qty " + quantity + ")";
    }
}
