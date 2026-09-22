package com.bookstore.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BookTest {

    @Test
    void decrementStockReducesQuantityByOne() {
        Book book = new Book("Clean Code", 45.0, 3);
        assertTrue(book.decrementStock());
        assertEquals(2, book.getQuantity());
    }

    @Test
    void decrementStockFailsWhenOutOfStock() {
        Book book = new Book("Clean Code", 45.0, 0);
        assertFalse(book.decrementStock());
        assertEquals(0, book.getQuantity());
    }

    @Test
    void isInStockReflectsQuantity() {
        assertTrue(new Book("A", 10, 1).isInStock());
        assertFalse(new Book("A", 10, 0).isInStock());
    }

    @Test
    void addStockIncreasesQuantity() {
        Book book = new Book("A", 10, 2);
        book.addStock(5);
        assertEquals(7, book.getQuantity());
    }

    @Test
    void booksWithSameNameAreEqualIgnoringCase() {
        assertEquals(new Book("Dune", 20), new Book("dune", 99));
    }
}
