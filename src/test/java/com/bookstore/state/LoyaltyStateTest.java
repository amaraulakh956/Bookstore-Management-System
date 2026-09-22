package com.bookstore.state;

import com.bookstore.model.Customer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LoyaltyStateTest {

    @Test
    void newCustomerStartsSilverWithZeroPoints() {
        Customer c = new Customer("alice", "pw");
        assertEquals("Silver", c.getStatus());
        assertEquals(0, c.getPoints());
    }

    @Test
    void buyingEarnsTenPointsPerDollar() {
        Customer c = new Customer("alice", "pw");
        c.buy(20.0);
        assertEquals(200, c.getPoints());
        assertEquals("Silver", c.getStatus());
    }

    @Test
    void crossingOneThousandPointsUpgradesToGold() {
        Customer c = new Customer("alice", "pw");
        c.buy(100.0); // 1000 points
        assertEquals("Gold", c.getStatus());
    }

    @Test
    void redeemFullyCoveringCostChargesZeroAndDeductsExactPoints() {
        Customer c = new Customer("alice", "pw");
        c.setPoints(500); // worth $5.00

        double finalCost = c.redeem(3.0);

        assertEquals(0.0, finalCost, 0.001);
        // 500 - (3.00 * 100) = 200 points left, plus 0 earned on a $0 remaining cost
        assertEquals(200, c.getPoints());
    }

    @Test
    void redeemPartiallyCoveringCostChargesRemainderAndKeepsLeftoverPoints() {
        Customer c = new Customer("alice", "pw");
        c.setPoints(150); // worth $1.00, 50 points left over

        double finalCost = c.redeem(5.0);

        assertEquals(4.0, finalCost, 0.001);
        // 50 leftover + 10 pts/dollar earned on the $4 remaining cost = 90
        assertEquals(90, c.getPoints());
    }

    @Test
    void goldCustomerDropsToSilverIfRedeemingBelowThreshold() {
        Customer c = new Customer("alice", "pw");
        c.setPoints(1000);
        c.setState(new GoldState());

        c.redeem(9.0); // uses 900 points, leaves 100, no purchase to re-earn much

        assertEquals("Silver", c.getStatus());
    }
}
