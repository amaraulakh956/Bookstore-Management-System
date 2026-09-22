package com.bookstore.state;

/**
 * Shared point-earning / point-redemption math used by every loyalty tier.
 * Extracted here because Gold and Silver previously duplicated this logic
 * verbatim; centralizing it means a rule change (e.g. the earn rate) only
 * has to happen in one place.
 */
final class PointsCalculator {

    static final int POINTS_PER_DOLLAR_EARNED = 10;
    static final int POINTS_PER_DOLLAR_REDEEMED = 100;
    static final int GOLD_THRESHOLD = 1000;

    private PointsCalculator() {
    }

    static int pointsEarned(double amountSpent) {
        return (int) (amountSpent * POINTS_PER_DOLLAR_EARNED);
    }

    /** Result of redeeming points against a cost: remaining points balance and final cost owed. */
    record RedemptionResult(int remainingPoints, double finalCost) {
    }

    static RedemptionResult redeem(int currentPoints, double cost) {
        int redeemableDollarValue = currentPoints / POINTS_PER_DOLLAR_REDEEMED;

        double finalCost;
        int pointsAfterRedeeming;

        if (redeemableDollarValue >= cost) {
            // Points fully cover the cost; only spend exactly what's needed.
            finalCost = 0.0;
            int pointsUsed = (int) (cost * POINTS_PER_DOLLAR_REDEEMED);
            pointsAfterRedeeming = currentPoints - pointsUsed;
        } else {
            // Spend every redeemable 100-point block; the remainder (< 100) carries over.
            finalCost = cost - redeemableDollarValue;
            pointsAfterRedeeming = currentPoints % POINTS_PER_DOLLAR_REDEEMED;
        }

        int newPoints = pointsAfterRedeeming + pointsEarned(finalCost);
        return new RedemptionResult(newPoints, finalCost);
    }
}
