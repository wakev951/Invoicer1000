package com.example.invoicer;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.properties.TextAlignment;

import java.io.FileNotFoundException;

public class PDFGenerator {
    public void generateInvoicePDF(Invoice invoice, String outputPath) {
        try {
            PdfWriter writer = new PdfWriter(outputPath);
            PdfDocument pdfDocument = new PdfDocument(writer);
            Document document = new Document(pdfDocument);

            // Invoice title
            Paragraph title = new Paragraph("Invoice")
                    .setFontSize(48)
                    .setBold()
                    .setTextAlignment(TextAlignment.LEFT);  // Change alignment to left

            // Invoice title
            Paragraph wangandwilliams = new Paragraph("Wang & \nWilliams")
                    .setFontSize(28)
                    .setBold()
                    .setTextAlignment(TextAlignment.LEFT);  // Change alignment to left

            // Outer table with 2 columns and 1 row
            Table outerTable = new Table(2);
            outerTable.setWidth(500);

// Left block - company details table
            Table leftTable = new Table(1);
            leftTable.setWidth(300);
            leftTable.addCell(new Cell().add(wangandwilliams).setBold().setHeight(80));
            leftTable.addCell(new Cell().add(new Paragraph("Address")));
            leftTable.addCell(new Cell().add(new Paragraph("City")));
            leftTable.addCell(new Cell().add(new Paragraph("Postal")));
            leftTable.addCell(new Cell().add(new Paragraph("Country")));
            leftTable.addCell(new Cell().add(new Paragraph("sales@wangandwilliams.co.nz")));

// Right block - corresponding values table
            Table rightTable = new Table(1);
            rightTable.setWidth(220);
            rightTable.addCell(new Cell().add(title).setHeight(80));

            Table invoiceDetailsTable = new Table(2);
            invoiceDetailsTable.addCell(new Cell().add(new Paragraph("Invoice No:")));
            invoiceDetailsTable.addCell(new Cell().add(new Paragraph(invoice.getInvoiceNumber())));
            invoiceDetailsTable.addCell(new Cell().add(new Paragraph("Date:")));
            invoiceDetailsTable.addCell(new Cell().add(new Paragraph(invoice.getDate())));
            invoiceDetailsTable.addCell(new Cell().add(new Paragraph("Terms:")));
            invoiceDetailsTable.addCell(new Cell().add(new Paragraph("30 Days")));
            invoiceDetailsTable.addCell(new Cell().add(new Paragraph("Due Date:")));
            invoiceDetailsTable.addCell(new Cell().add(new Paragraph("..."))); // Add the due date logic here
            rightTable.addCell(invoiceDetailsTable);

// Add the rightTable with margin or padding for extra space
            Cell rightCell = new Cell().add(rightTable).setPaddingLeft(150);  // Adds 50 units of padding to move it to the right

// Add both blocks (tables) into the outer table
            outerTable.addCell(new Cell().add(leftTable));
            //outerTable.addCell(new Cell().add(rightTable));
            outerTable.addCell(rightCell);  // Add the right table with padding

// Add the outer table to the document
            document.add(outerTable);



            // Bill to section
            document.add(new Paragraph("\nBill to:").setBold());
            Table billToTable = new Table(1);
            billToTable.addCell(new Cell().add(new Paragraph("Company: " + invoice.getCustomer().getName())));
            billToTable.addCell(new Cell().add(new Paragraph("Address: " + invoice.getCustomer().getAddress())));
            billToTable.addCell(new Cell().add(new Paragraph("City: " + invoice.getCustomer().getCity())));
            billToTable.addCell(new Cell().add(new Paragraph("Postal Code: " + invoice.getCustomer().getPostalCode())));
            document.add(billToTable);



            // Add some spacing below the title
            document.add(new Paragraph(" "));  // Add a blank paragraph for spacing
//
//            // Invoice details table
//            Table invoiceDetailsTable = new Table(2);
//            invoiceDetailsTable.setWidth(500);
//            invoiceDetailsTable.addCell(new Cell().add(new Paragraph("Invoice No:")));
//            invoiceDetailsTable.addCell(new Cell().add(new Paragraph(invoice.getInvoiceNumber())));
//            invoiceDetailsTable.addCell(new Cell().add(new Paragraph("Date:")));
//            invoiceDetailsTable.addCell(new Cell().add(new Paragraph(invoice.getDate())));
//            invoiceDetailsTable.addCell(new Cell().add(new Paragraph("Terms:")));
//            invoiceDetailsTable.addCell(new Cell().add(new Paragraph("30 Days")));
//            invoiceDetailsTable.addCell(new Cell().add(new Paragraph("Due Date:")));
//            invoiceDetailsTable.addCell(new Cell().add(new Paragraph("..."))); // You can add the due date logic here
//            document.add(invoiceDetailsTable);



            // Add a space before items
            document.add(new Paragraph("\n"));

            // Create a table for items
            Table itemsTable = new Table(new float[]{3, 1, 1, 1}); // Define column widths
            itemsTable.setWidth(500); // Set width in points

            // Table header
            itemsTable.addHeaderCell(new Cell().add(new Paragraph("Description")).setBold());
            itemsTable.addHeaderCell(new Cell().add(new Paragraph("Quantity")).setBold());
            itemsTable.addHeaderCell(new Cell().add(new Paragraph("Unit Price")).setBold());
            itemsTable.addHeaderCell(new Cell().add(new Paragraph("Total Price")).setBold());

            // Add items to the table
            for (InvoiceItem item : invoice.getItems()) {
                itemsTable.addCell(new Cell().add(new Paragraph(item.getDescription())));
                itemsTable.addCell(new Cell().add(new Paragraph(String.valueOf(item.getQuantity()))));
                itemsTable.addCell(new Cell().add(new Paragraph(String.format("%.2f", item.getUnitPrice()))));
                itemsTable.addCell(new Cell().add(new Paragraph(String.format("%.2f", item.getTotalPrice()))));
            }

            document.add(itemsTable);

            // Add subtotal, tax, and total amounts (right justified)
            double subtotal = invoice.getSubtotal();
            double tax = invoice.getTax();
            double total = invoice.getTotalAmount();

            // Create a new table for totals
            Table totalsTable = new Table(2);
            totalsTable.setWidth(500);
            totalsTable.addCell(new Cell().add(new Paragraph("Subtotal:")).setTextAlignment(TextAlignment.RIGHT));
            totalsTable.addCell(new Cell().add(new Paragraph(String.format("%.2f", subtotal))));
            totalsTable.addCell(new Cell().add(new Paragraph("Tax:")).setTextAlignment(TextAlignment.RIGHT));
            totalsTable.addCell(new Cell().add(new Paragraph(String.format("%.2f", tax))));
            totalsTable.addCell(new Cell().add(new Paragraph("Total Amount:")).setTextAlignment(TextAlignment.RIGHT));
            totalsTable.addCell(new Cell().add(new Paragraph(String.format("%.2f", total))));
            document.add(totalsTable);

            // Footer
            document.add(new Paragraph("\nThank you for your business!")
                    .setFontSize(12)
                    .setTextAlignment(TextAlignment.CENTER));

            document.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
