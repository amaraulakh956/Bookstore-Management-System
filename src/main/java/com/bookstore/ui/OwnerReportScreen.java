package com.bookstore.ui;

import com.bookstore.service.SalesReport;

import javax.swing.*;
import java.awt.*;

/** New screen: a rolled-up sales report the owner can check at a glance. */
public class OwnerReportScreen extends JPanel {

    private final BookstoreApp app;
    private final JLabel revenueLabel = new JLabel();
    private final JLabel salesCountLabel = new JLabel();
    private final JLabel bestBookLabel = new JLabel();
    private final JLabel topCustomerLabel = new JLabel();

    public OwnerReportScreen(BookstoreApp app) {
        this.app = app;
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 14, 10, 14);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 0;

        Font font = new Font("Arial", Font.PLAIN, 16);
        for (JLabel label : new JLabel[]{revenueLabel, salesCountLabel, bestBookLabel, topCustomerLabel}) {
            label.setFont(font);
        }

        gbc.gridy = 0;
        add(new JLabel("Sales Report"){{ setFont(new Font("Arial", Font.BOLD, 20)); }}, gbc);
        gbc.gridy = 1;
        add(revenueLabel, gbc);
        gbc.gridy = 2;
        add(salesCountLabel, gbc);
        gbc.gridy = 3;
        add(bestBookLabel, gbc);
        gbc.gridy = 4;
        add(topCustomerLabel, gbc);

        gbc.gridy = 5;
        JButton backB = new JButton("Back");
        add(backB, gbc);
        backB.addActionListener(e -> app.switchScreen("OWNER_START"));
    }

    public void refresh() {
        SalesReport report = app.getStore().getSalesReport();
        revenueLabel.setText(String.format("Total revenue: $%.2f", report.getTotalRevenue()));
        salesCountLabel.setText("Books sold: " + report.getTotalSales());
        bestBookLabel.setText("Best-selling title: " + report.getBestSellingBook());
        topCustomerLabel.setText("Top customer (by spend): " + report.getTopCustomer());
    }
}
