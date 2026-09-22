package com.bookstore.service;

import com.bookstore.model.PurchaseRecord;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SalesReportTest {

    @Test
    void emptyHistoryProducesZeroedReport() {
        SalesReport report = SalesReport.from(List.of());
        assertEquals(0.0, report.getTotalRevenue());
        assertEquals(0, report.getTotalSales());
        assertEquals("N/A", report.getBestSellingBook());
    }

    @Test
    void revenueIsSumOfAllAmountsPaid() {
        List<PurchaseRecord> records = List.of(
                new PurchaseRecord("alice", "Dune", 20.0, LocalDateTime.now()),
                new PurchaseRecord("bob", "1984", 10.0, LocalDateTime.now())
        );
        SalesReport report = SalesReport.from(records);
        assertEquals(30.0, report.getTotalRevenue(), 0.001);
        assertEquals(2, report.getTotalSales());
    }

    @Test
    void bestSellingBookIsTheMostFrequentTitle() {
        List<PurchaseRecord> records = List.of(
                new PurchaseRecord("alice", "Dune", 20.0, LocalDateTime.now()),
                new PurchaseRecord("bob", "Dune", 20.0, LocalDateTime.now()),
                new PurchaseRecord("carol", "1984", 10.0, LocalDateTime.now())
        );
        assertEquals("Dune", SalesReport.from(records).getBestSellingBook());
    }

    @Test
    void topCustomerIsHighestTotalSpender() {
        List<PurchaseRecord> records = List.of(
                new PurchaseRecord("alice", "Dune", 20.0, LocalDateTime.now()),
                new PurchaseRecord("bob", "1984", 100.0, LocalDateTime.now())
        );
        assertEquals("bob", SalesReport.from(records).getTopCustomer());
    }
}
