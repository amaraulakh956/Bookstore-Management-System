package com.bookstore.ui;

import javax.swing.table.DefaultTableModel;

/**
 * Table model for the customer's book-selection screen.
 * Columns: Book Name, Book Price, Quantity, Select (checkbox).
 */
public class BookTableModel extends DefaultTableModel {

    public BookTableModel() {
        super(new String[]{"Book Name", "Book Price", "Quantity", "Select"}, 0);
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if (columnIndex == 3) return Boolean.class;
        return String.class;
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return column == 3;
    }
}
