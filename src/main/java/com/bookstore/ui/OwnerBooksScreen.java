package com.bookstore.ui;

import com.bookstore.model.Book;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class OwnerBooksScreen extends JPanel {

    private final BookstoreApp app;
    private final JTable table;
    private final DefaultTableModel model;
    private final JTextField nameField = new JTextField(10);
    private final JTextField priceField = new JTextField(6);
    private final JTextField quantityField = new JTextField(4);
    private final JTextField searchField = new JTextField(12);

    public OwnerBooksScreen(BookstoreApp app) {
        this.app = app;
        setLayout(new BorderLayout());

        model = new DefaultTableModel(new String[]{"Book Name", "Book Price", "Quantity"}, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel searchBar = new JPanel();
        searchBar.add(new JLabel("Search:"));
        searchBar.add(searchField);
        JButton clearSearchB = new JButton("Clear");
        searchBar.add(clearSearchB);
        add(searchBar, BorderLayout.NORTH);

        JPanel middle = new JPanel();
        middle.add(new JLabel("Name:"));
        middle.add(nameField);
        middle.add(new JLabel("Price:"));
        middle.add(priceField);
        middle.add(new JLabel("Qty:"));
        middle.add(quantityField);
        JButton addB = new JButton("Add");
        middle.add(addB);
        JButton restockB = new JButton("Restock Selected (+1)");
        middle.add(restockB);

        JButton delB = new JButton("Delete");
        JButton backB = new JButton("Back");
        JPanel bottom = new JPanel();
        bottom.add(delB);
        bottom.add(backB);

        JPanel south = new JPanel(new BorderLayout());
        south.add(middle, BorderLayout.NORTH);
        south.add(bottom, BorderLayout.SOUTH);
        add(south, BorderLayout.SOUTH);

        searchField.getDocument().addDocumentListener(new SimpleDocumentListener(this::refresh));
        clearSearchB.addActionListener(e -> {
            searchField.setText("");
            refresh();
        });

        addB.addActionListener(e -> {
            try {
                String name = nameField.getText().trim();
                String priceText = priceField.getText().trim();
                String qtyText = quantityField.getText().trim();

                if (name.isEmpty() || priceText.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please enter both a name and a price.");
                    return;
                }

                double price = Double.parseDouble(priceText);
                if (price <= 0) {
                    JOptionPane.showMessageDialog(this, "Price must be greater than 0.");
                    return;
                }

                int quantity = qtyText.isEmpty() ? 1 : Integer.parseInt(qtyText);
                if (quantity <= 0) {
                    JOptionPane.showMessageDialog(this, "Quantity must be greater than 0.");
                    return;
                }

                Book newBook = new Book(name, price, quantity);
                if (app.getStore().addBook(newBook)) {
                    refresh();
                    nameField.setText("");
                    priceField.setText("");
                    quantityField.setText("");
                } else {
                    JOptionPane.showMessageDialog(this, "A book named '" + name + "' already exists.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Price and quantity must be numbers.");
            }
        });

        restockB.addActionListener(e -> {
            int r = table.getSelectedRow();
            if (r == -1) {
                JOptionPane.showMessageDialog(this, "Please select a row to restock.");
                return;
            }
            Book book = currentlyShown().get(r);
            app.getStore().restock(book, 1);
            refresh();
        });

        delB.addActionListener(e -> {
            int r = table.getSelectedRow();
            if (r != -1) {
                app.getStore().removeBook(currentlyShown().get(r));
                refresh();
            } else {
                JOptionPane.showMessageDialog(this, "Please select a row to delete.");
            }
        });

        backB.addActionListener(e -> app.switchScreen("OWNER_START"));
    }

    private List<Book> currentlyShown() {
        String query = searchField.getText().trim();
        return query.isEmpty() ? app.getStore().getBooks() : app.getStore().searchBooks(query);
    }

    public void refresh() {
        model.setRowCount(0);
        for (Book b : currentlyShown()) {
            model.addRow(new Object[]{b.getName(), b.getPrice(), b.getQuantity()});
        }
    }
}
