package com.bookstore.ui;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/** Collapses the three DocumentListener callbacks into a single Runnable, for live-search fields. */
class SimpleDocumentListener implements DocumentListener {

    private final Runnable onChange;

    SimpleDocumentListener(Runnable onChange) {
        this.onChange = onChange;
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        onChange.run();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        onChange.run();
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        onChange.run();
    }
}
