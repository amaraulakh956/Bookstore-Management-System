package com.bookstore.ui;

import com.bookstore.model.Customer;

import javax.swing.*;
import java.awt.*;

public class CustomerCostScreen extends JPanel {

    private final JLabel costLabel = new JLabel();
    private final JLabel pointsStatusLabel = new JLabel();

    public CustomerCostScreen(BookstoreApp app) {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.insets = new Insets(15, 20, 15, 20);
        gbc.anchor = GridBagConstraints.CENTER;

        gbc.gridy = 0;
        costLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        add(costLabel, gbc);

        gbc.gridy = 1;
        pointsStatusLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        add(pointsStatusLabel, gbc);

        gbc.gridy = 2;
        JButton logoutB = new JButton("Logout");
        add(logoutB, gbc);
        logoutB.addActionListener(e -> app.switchScreen("LOGIN"));
    }

    public void update(double cost, Customer c) {
        costLabel.setText(String.format("Total Cost: $%.2f", cost));
        pointsStatusLabel.setText("Points: " + c.getPoints() + ", Status: " + c.getStatus());
    }
}
