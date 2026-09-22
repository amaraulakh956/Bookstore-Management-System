package com.bookstore.service;

import com.bookstore.model.PurchaseRecord;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Rolls a list of {@link PurchaseRecord}s up into the numbers an owner
 * actually wants to see: total revenue, the best-selling title, and the
 * customer who has spent the most.
 */
public class SalesReport {

    private final double totalRevenue;
    private final int totalSales;
    private final String bestSellingBook;
    private final String topCustomer;

    private SalesReport(double totalRevenue, int totalSales, String bestSellingBook, String topCustomer) {
        this.totalRevenue = totalRevenue;
        this.totalSales = totalSales;
        this.bestSellingBook = bestSellingBook;
        this.topCustomer = topCustomer;
    }

    public static SalesReport from(List<PurchaseRecord> records) {
        if (records.isEmpty()) {
            return new SalesReport(0.0, 0, "N/A", "N/A");
        }

        double revenue = records.stream().mapToDouble(PurchaseRecord::getAmountPaid).sum();

        Map<String, Long> salesByBook = records.stream()
                .collect(Collectors.groupingBy(PurchaseRecord::getBookName, Collectors.counting()));
        String bestBook = topKey(salesByBook);

        Map<String, Double> spendByCustomer = records.stream()
                .collect(Collectors.groupingBy(PurchaseRecord::getCustomerUsername,
                        Collectors.summingDouble(PurchaseRecord::getAmountPaid)));
        String topSpender = spendByCustomerKey(spendByCustomer);

        return new SalesReport(revenue, records.size(), bestBook, topSpender);
    }

    private static String topKey(Map<String, Long> counts) {
        Optional<Map.Entry<String, Long>> max = counts.entrySet().stream()
                .max(Map.Entry.comparingByValue());
        return max.map(Map.Entry::getKey).orElse("N/A");
    }

    private static String spendByCustomerKey(Map<String, Double> spend) {
        Optional<Map.Entry<String, Double>> max = spend.entrySet().stream()
                .max(Map.Entry.comparingByValue());
        return max.map(Map.Entry::getKey).orElse("N/A");
    }

    public double getTotalRevenue() {
        return totalRevenue;
    }

    public int getTotalSales() {
        return totalSales;
    }

    public String getBestSellingBook() {
        return bestSellingBook;
    }

    public String getTopCustomer() {
        return topCustomer;
    }
}
