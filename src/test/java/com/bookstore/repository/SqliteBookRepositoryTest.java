package com.bookstore.repository;

import com.bookstore.model.Book;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SqliteBookRepositoryTest {

    @Test
    void savedBooksCanBeReloadedFromSqlite(@TempDir File tempDir) {
        String jdbcUrl = "jdbc:sqlite:" + new File(tempDir, "test.db").getAbsolutePath();
        SqliteBookRepository repo = new SqliteBookRepository(jdbcUrl);

        repo.saveAll(List.of(new Book("Dune", 19.99, 4), new Book("1984", 12.50, 2)));
        List<Book> loaded = repo.findAll();

        assertEquals(2, loaded.size());
        assertTrue(loaded.stream().anyMatch(b -> b.getName().equals("Dune") && b.getQuantity() == 4));
    }

    @Test
    void saveAllReplacesPreviousContents(@TempDir File tempDir) {
        String jdbcUrl = "jdbc:sqlite:" + new File(tempDir, "test.db").getAbsolutePath();
        SqliteBookRepository repo = new SqliteBookRepository(jdbcUrl);

        repo.saveAll(List.of(new Book("Old Book", 5.0, 1)));
        repo.saveAll(List.of(new Book("New Book", 8.0, 1)));

        List<Book> loaded = repo.findAll();
        assertEquals(1, loaded.size());
        assertEquals("New Book", loaded.get(0).getName());
    }
}
