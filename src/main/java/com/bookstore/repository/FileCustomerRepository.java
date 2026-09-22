package com.bookstore.repository;

import com.bookstore.model.Customer;
import com.bookstore.state.GoldState;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FileCustomerRepository implements CustomerRepository {

    private static final int GOLD_THRESHOLD = 1000;

    private final File file;

    public FileCustomerRepository(File file) {
        this.file = file;
    }

    public FileCustomerRepository() {
        this(new File("customers.txt"));
    }

    @Override
    public List<Customer> findAll() {
        List<Customer> customers = new ArrayList<>();
        if (!file.exists()) {
            return customers;
        }
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] data = line.split(",");
                if (data.length == 3) {
                    Customer c = new Customer(data[0], data[1]);
                    int points = Integer.parseInt(data[2]);
                    c.setPoints(points);
                    if (points >= GOLD_THRESHOLD) {
                        c.setState(new GoldState());
                    }
                    customers.add(c);
                }
            }
        } catch (IOException e) {
            System.err.println("Could not read " + file.getName() + ": " + e.getMessage());
        }
        return customers;
    }

    @Override
    public void saveAll(List<Customer> customers) {
        try (FileWriter writer = new FileWriter(file)) {
            for (Customer c : customers) {
                writer.write(c.getUsername() + "," + c.getPassword() + "," + c.getPoints() + "\n");
            }
        } catch (IOException e) {
            System.err.println("Could not save " + file.getName() + ": " + e.getMessage());
        }
    }
}
