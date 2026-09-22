package com.bookstore.ui;

import com.bookstore.service.BookstoreService;
import com.bookstore.service.PersistenceType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class BookstoreApp extends JFrame {

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel container = new JPanel(cardLayout);
    private final BookstoreService store;

    private LoginScreen loginScreen;
    private OwnerStartScreen ownerStart;
    private OwnerBooksScreen ownerBooks;
    private OwnerCustomersScreen ownerCustomers;
    private OwnerReportScreen ownerReport;
    private CustomerStartScreen customerStart;
    private CustomerCostScreen customerCost;

    public BookstoreApp(PersistenceType persistenceType) {
        this.store = BookstoreService.using(persistenceType);

        setTitle("Bookstore App (" + persistenceType + " storage)");
        setSize(850, 620);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        store.loadData();

        loginScreen = new LoginScreen(this);
        ownerStart = new OwnerStartScreen(this);
        ownerBooks = new OwnerBooksScreen(this);
        ownerCustomers = new OwnerCustomersScreen(this);
        ownerReport = new OwnerReportScreen(this);
        customerStart = new CustomerStartScreen(this);
        customerCost = new CustomerCostScreen(this);

        ownerBooks.refresh();
        ownerCustomers.refresh();

        container.add(loginScreen, "LOGIN");
        container.add(ownerStart, "OWNER_START");
        container.add(ownerBooks, "OWNER_BOOKS");
        container.add(ownerCustomers, "OWNER_CUSTOMERS");
        container.add(ownerReport, "OWNER_REPORT");
        container.add(customerStart, "CUSTOMER_START");
        container.add(customerCost, "CUSTOMER_COST");

        add(container);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                store.saveData();
                System.exit(0);
            }
        });
    }

    public void switchScreen(String name) {
        cardLayout.show(container, name);
    }

    public BookstoreService getStore() {
        return store;
    }

    public CustomerStartScreen getCustomerStart() {
        return customerStart;
    }

    public CustomerCostScreen getCustomerCost() {
        return customerCost;
    }

    public OwnerBooksScreen getOwnerBooks() {
        return ownerBooks;
    }

    public OwnerCustomersScreen getOwnerCustomers() {
        return ownerCustomers;
    }

    public OwnerReportScreen getOwnerReport() {
        return ownerReport;
    }

    /**
     * Entry point. Pass {@code --db=sqlite} to use the SQLite backend
     * (bookstore.db); defaults to the original flat-file format.
     */
    public static void main(String[] args) {
        PersistenceType type = PersistenceType.FILE;
        for (String arg : args) {
            if (arg.equalsIgnoreCase("--db=sqlite")) {
                type = PersistenceType.SQLITE;
            }
        }
        PersistenceType finalType = type;
        SwingUtilities.invokeLater(() -> new BookstoreApp(finalType).setVisible(true));
    }
}
