package com.bookstore.ui;

import com.bookstore.model.Book;
import com.bookstore.model.Customer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerStartScreen extends JPanel {

    private final BookstoreApp app;
    private Customer currentCustomer;
    private final JLabel header = new JLabel();
    private final JTextField searchField = new JTextField(12);
    private final JTable table;
    private final DefaultTableModel model;

    public CustomerStartScreen(BookstoreApp app) {
        this.app = app;
        setLayout(new BorderLayout());

        model = new BookTableModel();
        table = new JTable(model);

        JPanel top = new JPanel(new BorderLayout());
        top.add(header, BorderLayout.NORTH);
        JPanel searchBar = new JPanel();
        searchBar.add(new JLabel("Search:"));
        searchBar.add(searchField);
        top.add(searchBar, BorderLayout.SOUTH);

        add(top, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JButton buyB = new JButton("Buy");
        JButton redeemB = new JButton("Redeem points and Buy");
        JButton logoutB = new JButton("Logout");
        JPanel p = new JPanel();
        p.add(buyB);
        p.add(redeemB);
        p.add(logoutB);
        add(p, BorderLayout.SOUTH);

        searchField.getDocument().addDocumentListener(new SimpleDocumentListener(this::refreshTable));
        buyB.addActionListener(e -> process(false));
        redeemB.addActionListener(e -> process(true));
        logoutB.addActionListener(e -> app.switchScreen("LOGIN"));
    }

    private List<Book> currentlyShown() {
        String query = searchField.getText().trim();
        return query.isEmpty() ? app.getStore().getBooks() : app.getStore().searchBooks(query);
    }

    public void refreshTable() {
        model.setRowCount(0);
        for (Book b : currentlyShown()) {
            model.addRow(new Object[]{b.getName(), b.getPrice(), b.getQuantity(), false});
        }
        if (currentCustomer != null) {
            updateHeader();
        }
    }

    public void setCustomer(Customer c) {
        this.currentCustomer = c;
        updateHeader();
    }

    private void updateHeader() {
        header.setText("Welcome " + currentCustomer.getUsername()
                + ". You have " + currentCustomer.getPoints()
                + " points. Your status is " + currentCustomer.getStatus());
    }

    private void process(boolean redeem) {
        List<Book> shown = currentlyShown();
        List<Book> selected = new ArrayList<>();
        for (int i = 0; i < model.getRowCount(); i++) {
            Object isSelected = model.getValueAt(i, 3);
            if (isSelected instanceof Boolean && (Boolean) isSelected) {
                selected.add(shown.get(i));
            }
        }

        if (selected.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select at least one book.");
            return;
        }

        double finalCost = app.getStore().checkout(currentCustomer, selected, redeem);

        app.getCustomerCost().update(finalCost, currentCustomer);
        app.switchScreen("CUSTOMER_COST");
        refreshTable();
    }
}
