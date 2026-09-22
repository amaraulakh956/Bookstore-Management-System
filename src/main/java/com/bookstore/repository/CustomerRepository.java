package com.bookstore.repository;

import com.bookstore.model.Customer;

import java.util.List;

public interface CustomerRepository {

    List<Customer> findAll();

    void saveAll(List<Customer> customers);
}
