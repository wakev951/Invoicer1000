package com.example.invoicer;

import java.util.ArrayList;
import java.util.List;

public class Invoice {
    private String invoiceNumber;
    private String date;
    private Customer customer;
    private List<InvoiceItem> items;
    private final double taxRate = 0.15; // Tax rate as a percentage (e.g., 0.15 for 15%)

    public Invoice(String invoiceNumber, String date, Customer customer) {
        this.invoiceNumber = invoiceNumber;
        this.date = date;
        this.customer = customer;
        this.items = new ArrayList<>();
    }

    public void addItem(InvoiceItem item) {
        items.add(item);
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public String getDate() {
        return date;
    }

    public Customer getCustomer() {
        return customer;
    }

    public List<InvoiceItem> getItems() {
        return items;
    }

    public double getSubtotal() {
        return items.stream().mapToDouble(InvoiceItem::getTotalPrice).sum();
    }

    public double getTax() {
        return getSubtotal() * taxRate; // Calculate tax based on subtotal
    }

    public double getTotalAmount() {
        return getSubtotal() + getTax(); // Total amount including tax
    }
}
