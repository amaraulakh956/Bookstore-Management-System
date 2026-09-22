package com.bookstore.ui;

import javax.swing.*;
import java.awt.*;

public class OwnerStartScreen extends JPanel {

    public OwnerStartScreen(BookstoreApp app) {
        setLayout(new FlowLayout());
        JButton booksBtn = new JButton("Books");
        JButton customersBtn = new JButton("Customers");
        JButton reportBtn = new JButton("Sales Report");
        JButton logoutBtn = new JButton("Logout");

        add(booksBtn);
        add(customersBtn);
        add(reportBtn);
        add(logoutBtn);

        booksBtn.addActionListener(e -> {
            app.getOwnerBooks().refresh();
            app.switchScreen("OWNER_BOOKS");
        });
        customersBtn.addActionListener(e -> {
            app.getOwnerCustomers().refresh();
            app.switchScreen("OWNER_CUSTOMERS");
        });
        reportBtn.addActionListener(e -> {
            app.getOwnerReport().refresh();
            app.switchScreen("OWNER_REPORT");
        });
        logoutBtn.addActionListener(e -> app.switchScreen("LOGIN"));
    }
}
