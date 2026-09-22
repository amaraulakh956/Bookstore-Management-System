package com.bookstore.model;

import com.bookstore.state.LoyaltyState;
import com.bookstore.state.SilverState;

public class Customer extends User {

    private int points;
    private LoyaltyState state;

    public Customer(String username, String password) {
        super(username, password);
        this.points = 0;
        this.state = new SilverState();
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public LoyaltyState getState() {
        return state;
    }

    public void setState(LoyaltyState state) {
        this.state = state;
    }

    public void buy(double totalCost) {
        state.buy(this, totalCost);
    }

    public double redeem(double totalCost) {
        return state.redeem(this, totalCost);
    }

    public String getStatus() {
        return state.getStatus();
    }
}
