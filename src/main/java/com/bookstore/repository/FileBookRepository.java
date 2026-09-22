package com.bookstore.repository;

import com.bookstore.model.Book;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Comma-separated flat-file storage: {@code name,price,quantity} per line.
 * This is the original persistence format the project shipped with,
 * extended with a quantity column.
 */
public class FileBookRepository implements BookRepository {

    private final File file;

    public FileBookRepository(File file) {
        this.file = file;
    }

    public FileBookRepository() {
        this(new File("books.txt"));
    }

    @Override
    public List<Book> findAll() {
        List<Book> books = new ArrayList<>();
        if (!file.exists()) {
            return books;
        }
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] data = line.split(",");
                if (data.length >= 2) {
                    String name = data[0];
                    double price = Double.parseDouble(data[1]);
                    int quantity = data.length >= 3 ? Integer.parseInt(data[2]) : 1;
                    books.add(new Book(name, price, quantity));
                }
            }
        } catch (IOException e) {
            System.err.println("Could not read " + file.getName() + ": " + e.getMessage());
        }
        return books;
    }

    @Override
    public void saveAll(List<Book> books) {
        try (FileWriter writer = new FileWriter(file)) {
            for (Book b : books) {
                writer.write(b.getName() + "," + b.getPrice() + "," + b.getQuantity() + "\n");
            }
        } catch (IOException e) {
            System.err.println("Could not save " + file.getName() + ": " + e.getMessage());
        }
    }
}
