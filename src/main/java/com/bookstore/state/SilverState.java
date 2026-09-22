package com.bookstore.state;

import com.bookstore.model.Customer;

/**
 * Starting loyalty tier. Upgrades to Gold once the customer crosses the
 * points threshold.
 */
public class SilverState implements LoyaltyState {

    @Override
    public String getStatus() {
        return "Silver";
    }

    @Override
    public void buy(Customer customer, double cost) {
        customer.setPoints(customer.getPoints() + PointsCalculator.pointsEarned(cost));

        if (customer.getPoints() >= PointsCalculator.GOLD_THRESHOLD) {
            customer.setState(new GoldState());
        }
    }

    @Override
    public double redeem(Customer customer, double cost) {
        PointsCalculator.RedemptionResult result = PointsCalculator.redeem(customer.getPoints(), cost);
        customer.setPoints(result.remainingPoints());

        customer.setState(result.remainingPoints() >= PointsCalculator.GOLD_THRESHOLD
                ? new GoldState()
                : new SilverState());

        return result.finalCost();
    }
}
