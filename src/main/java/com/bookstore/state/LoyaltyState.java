package com.bookstore.state;

import com.bookstore.model.Customer;

/**
 * Strategy/State interface for a customer's loyalty tier. Each tier decides
 * how points are earned on a purchase and how points are redeemed against a
 * cost, and can transition the customer to a different tier as a result.
 */
public interface LoyaltyState {

    String getStatus();

    void buy(Customer customer, double cost);

    double redeem(Customer customer, double cost);
}
