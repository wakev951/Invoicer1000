package com.example.invoicer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InvoiceGUI extends JFrame {
    private JTextField customerNameField;
    private JTextField customerEmailField;
    private JTextField customerAddressField;
    private JTextField customerCityField;
    private JTextField customerPostalCodeField;
    private JTextField itemDescriptionField;
    private JTextField itemQuantityField;
    private JTextField itemUnitPriceField;
    private JTextArea invoiceItemsArea;

    private Invoice invoice;

    public InvoiceGUI() {
        setTitle("Invoice Generator");
        setSize(500, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        // Customer information
        JLabel customerLabel = new JLabel("Customer Name:");
        customerNameField = new JTextField(20);
        JLabel emailLabel = new JLabel("Customer Email:");
        customerEmailField = new JTextField(20);
        JLabel addressLabel = new JLabel("Customer Address:");
        customerAddressField = new JTextField(20);
        JLabel cityLabel = new JLabel("City:");
        customerCityField = new JTextField(15);
        JLabel postalCodeLabel = new JLabel("Postal Code:");
        customerPostalCodeField = new JTextField(10);

        // Item details
        JLabel itemLabel = new JLabel("Item Description:");
        itemDescriptionField = new JTextField(15);
        JLabel quantityLabel = new JLabel("Quantity:");
        itemQuantityField = new JTextField(5);
        JLabel unitPriceLabel = new JLabel("Unit Price:");
        itemUnitPriceField = new JTextField(5);

        // Invoice items display area
        invoiceItemsArea = new JTextArea(10, 40);
        invoiceItemsArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(invoiceItemsArea);

        // Buttons
        JButton addItemButton = new JButton("Add Item");
        JButton generateInvoiceButton = new JButton("Generate Invoice");

        // Action listener for Add Item button
        addItemButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addItem();
            }
        });

        // Action listener for Generate Invoice button
        generateInvoiceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateInvoice();
            }
        });

        // Add components to the frame
        add(customerLabel);
        add(customerNameField);
        add(emailLabel);
        add(customerEmailField);
        add(addressLabel);
        add(customerAddressField);
        add(cityLabel);
        add(customerCityField);
        add(postalCodeLabel);
        add(customerPostalCodeField);
        add(itemLabel);
        add(itemDescriptionField);
        add(quantityLabel);
        add(itemQuantityField);
        add(unitPriceLabel);
        add(itemUnitPriceField);
        add(addItemButton);
        add(scrollPane);
        add(generateInvoiceButton);

        initializeTestInvoice();

        setVisible(true);
    }

    private void addItem() {
        String description = itemDescriptionField.getText();
        int quantity;
        double unitPrice;

        // Validate quantity and unit price inputs
        try {
            quantity = Integer.parseInt(itemQuantityField.getText());
            unitPrice = Double.parseDouble(itemUnitPriceField.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter valid quantity and unit price.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (invoice == null) {
            // Create a new invoice if it doesn't exist
            Customer customer = new Customer(
                    customerNameField.getText(),
                    customerEmailField.getText(),
                    customerAddressField.getText(),
                    customerCityField.getText(),
                    customerPostalCodeField.getText()
            );
            invoice = new Invoice("INV-001", "2024-10-18", customer);
        }

        InvoiceItem item = new InvoiceItem(description, quantity, unitPrice);
        invoice.addItem(item);

        // Update the display area
        invoiceItemsArea.append(item.getDescription() + " (Qty: " + item.getQuantity() + ", Price: $" + item.getUnitPrice() + ")\n");

        // Clear input fields
        itemDescriptionField.setText("");
        itemQuantityField.setText("");
        itemUnitPriceField.setText("");
    }

    private void generateInvoice() {
        if (invoice == null) {
            JOptionPane.showMessageDialog(this, "Please add at least one item before generating an invoice.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        PDFGenerator pdfGenerator = new PDFGenerator();
        String outputPath = "Invoice.pdf"; // Change the path as needed
        pdfGenerator.generateInvoicePDF(invoice, outputPath);

        JOptionPane.showMessageDialog(this, "Invoice generated: " + outputPath);
    }

    private void initializeTestInvoice() {
        // Create a new customer with test data
        Customer customer = new Customer("John Doe", "john.doe@example.com", "123 Main St", "Anytown", "12345");
        invoice = new Invoice("INV-001", "2024-10-18", customer);

        // Add test items
        invoice.addItem(new InvoiceItem("Widget A", 2, 15.99));
        invoice.addItem(new InvoiceItem("Gadget B", 1, 29.99));
        invoice.addItem(new InvoiceItem("Thingamajig C", 5, 9.99));

        // Update the display area with test items
        for (InvoiceItem item : invoice.getItems()) {
            invoiceItemsArea.append(item.getDescription() + " (Qty: " + item.getQuantity() + ", Price: $" + item.getUnitPrice() + ")\n");
        }
    }
}
