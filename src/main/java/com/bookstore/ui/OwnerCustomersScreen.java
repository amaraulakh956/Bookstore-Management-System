package com.bookstore.ui;

import com.bookstore.model.Customer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;

public class OwnerCustomersScreen extends JPanel {

    private final BookstoreApp app;
    private final JTable table;
    private final DefaultTableModel model;
    private final JTextField userField = new JTextField(10);
    private final JPasswordField passField = new JPasswordField(10);

    public OwnerCustomersScreen(BookstoreApp app) {
        this.app = app;
        setLayout(new BorderLayout());

        model = new DefaultTableModel(new String[]{"Username", "Password", "Points", "Status"}, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 2 ? Integer.class : String.class;
            }
        };
        table = new JTable(model);
        // Lets the owner click the "Points" column header to sort ascending/descending.
        table.setRowSorter(new TableRowSorter<>(model));
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel middle = new JPanel();
        middle.add(new JLabel("Username:"));
        middle.add(userField);
        middle.add(new JLabel("Password:"));
        middle.add(passField);
        JButton addB = new JButton("Add");
        middle.add(addB);

        JButton delB = new JButton("Delete");
        JButton backB = new JButton("Back");
        JPanel bottom = new JPanel();
        bottom.add(delB);
        bottom.add(backB);

        JPanel south = new JPanel(new BorderLayout());
        south.add(middle, BorderLayout.NORTH);
        south.add(bottom, BorderLayout.SOUTH);
        add(south, BorderLayout.SOUTH);

        addB.addActionListener(e -> {
            String user = userField.getText().trim();
            String pass = new String(passField.getPassword()).trim();

            if (user.isEmpty() || pass.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Both Username and Password must be filled out.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!app.getStore().addCustomer(new Customer(user, pass))) {
                JOptionPane.showMessageDialog(this,
                        "A customer with username '" + user + "' already exists.", "Duplicate Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            refresh();
            userField.setText("");
            passField.setText("");
        });

        delB.addActionListener(e -> {
            int viewRow = table.getSelectedRow();
            if (viewRow != -1) {
                int modelRow = table.convertRowIndexToModel(viewRow);
                app.getStore().removeCustomer(app.getStore().getCustomers().get(modelRow));
                refresh();
            } else {
                JOptionPane.showMessageDialog(this, "Please select a row to delete.");
            }
        });

        backB.addActionListener(e -> app.switchScreen("OWNER_START"));
    }

    public void refresh() {
        model.setRowCount(0);
        for (Customer c : app.getStore().getCustomers()) {
            model.addRow(new Object[]{c.getUsername(), c.getPassword(), c.getPoints(), c.getStatus()});
        }
    }
}
