package com.bookstore.state;

import com.bookstore.model.Customer;

/**
 * Top loyalty tier (>= 1000 points). Behaves the same as Silver in terms of
 * earn/redeem math, but a Gold customer who redeems down below the
 * threshold drops back to Silver.
 */
public class GoldState implements LoyaltyState {

    @Override
    public String getStatus() {
        return "Gold";
    }

    @Override
    public void buy(Customer customer, double cost) {
        customer.setPoints(customer.getPoints() + PointsCalculator.pointsEarned(cost));
        // Points only go up on a plain purchase, so Gold never downgrades here.
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
