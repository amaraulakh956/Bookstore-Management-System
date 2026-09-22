package com.bookstore.ui;

import com.bookstore.model.Customer;
import com.bookstore.model.Owner;
import com.bookstore.model.User;

import javax.swing.*;
import java.awt.*;

public class LoginScreen extends JPanel {

    private final JTextField userField = new JTextField(15);
    private final JPasswordField passField = new JPasswordField(15);

    public LoginScreen(BookstoreApp app) {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(new JLabel("Welcome to the BookStore App"), gbc);
        gbc.gridwidth = 1;

        gbc.gridy = 1;
        add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        add(userField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        add(passField, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        JButton loginBtn = new JButton("Login");
        add(loginBtn, gbc);

        gbc.gridy = 4;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        add(new JLabel("Hint: owner login is admin / admin"), gbc);

        loginBtn.addActionListener(e -> {
            String username = userField.getText();
            String password = new String(passField.getPassword());

            User u = app.getStore().login(username, password);

            if (u instanceof Owner) {
                clearFields();
                app.switchScreen("OWNER_START");
            } else if (u instanceof Customer c) {
                app.getCustomerStart().setCustomer(c);
                app.getCustomerStart().refreshTable();
                clearFields();
                app.switchScreen("CUSTOMER_START");
            } else {
                JOptionPane.showMessageDialog(this, "Invalid credentials. Please try again.");
                passField.setText("");
            }
        });
    }

    private void clearFields() {
        userField.setText("");
        passField.setText("");
    }
}
