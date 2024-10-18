package com.example.invoicer;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Launch the invoice GUI
        SwingUtilities.invokeLater(() -> new InvoiceGUI());
    }
}
