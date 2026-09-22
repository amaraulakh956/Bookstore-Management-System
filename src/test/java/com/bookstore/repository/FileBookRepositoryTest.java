package com.bookstore.repository;

import com.bookstore.model.Book;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileBookRepositoryTest {

    @Test
    void savedBooksCanBeReloaded(@TempDir File tempDir) {
        File dataFile = new File(tempDir, "books.txt");
        FileBookRepository repo = new FileBookRepository(dataFile);

        repo.saveAll(List.of(new Book("Dune", 19.99, 4), new Book("1984", 12.50, 0)));

        List<Book> loaded = repo.findAll();

        assertEquals(2, loaded.size());
        assertTrue(loaded.contains(new Book("Dune", 19.99, 4)));
        assertEquals(4, loaded.get(0).getQuantity());
    }

    @Test
    void findAllReturnsEmptyListWhenFileDoesNotExist(@TempDir File tempDir) {
        FileBookRepository repo = new FileBookRepository(new File(tempDir, "missing.txt"));
        assertTrue(repo.findAll().isEmpty());
    }
}
