package com.bookstore.repository;

import com.bookstore.model.Book;

import java.util.List;

/**
 * Persistence boundary for {@link Book}s. Two implementations ship with the
 * project: {@link FileBookRepository} (the original books.txt format) and
 * {@link SqliteBookRepository}. Swapping one for the other only requires
 * changing which repository {@code BookstoreService} is constructed with.
 */
public interface BookRepository {

    List<Book> findAll();

    void saveAll(List<Book> books);
}
