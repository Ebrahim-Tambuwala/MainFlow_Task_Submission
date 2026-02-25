package inventoryManagementSystem.ui;
import java.awt.print.PrinterJob;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.Graphics2D;

import inventoryManagementSystem.dao.BillDAO;
import inventoryManagementSystem.model.Bill;
import inventoryManagementSystem.model.BillItem;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.print.PrinterException;
import java.io.FileOutputStream;

import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class GenerateBillForm extends JFrame {

    private JTextField transactionField;
    private JLabel buyerLabel, emailLabel, phoneLabel, addressLabel;
    private JLabel paymentLabel, dateLabel, subtotalLabel, finalTotalLabel;

    private JTable table;
    private DefaultTableModel model;

    private JPanel billPanel;

    public GenerateBillForm() {

        setTitle("Generate Bill");
        setSize(950, 700);
        setMinimumSize(new Dimension(900, 650));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // ================= SEARCH PANEL =================
        JPanel searchPanel = new JPanel();
        searchPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        searchPanel.add(new JLabel("Transaction ID:"));
        transactionField = new JTextField(10);
        searchPanel.add(transactionField);

        JButton searchBtn = new JButton("Generate Bill");
        searchPanel.add(searchBtn);

        add(searchPanel, BorderLayout.NORTH);

        // ================= BILL PANEL =================
        billPanel = new JPanel(new BorderLayout(15, 15));
        billPanel.setBorder(new EmptyBorder(20, 30, 20, 30));

        add(new JScrollPane(billPanel), BorderLayout.CENTER);

        // ================= HEADER =================
        JPanel headerPanel = new JPanel(new GridLayout(3, 1));
        headerPanel.setBorder(new MatteBorder(0, 0, 2, 0, Color.BLACK));

        JLabel companyLabel = new JLabel("SMARTSTORE MANAGEMENT SYSTEM", SwingConstants.CENTER);
        companyLabel.setFont(new Font("Arial", Font.BOLD, 24));

        JLabel addressCompany = new JLabel("Mumbai, India | support@smartstore.com", SwingConstants.CENTER);

        JLabel invoiceLabel = new JLabel("INVOICE", SwingConstants.CENTER);
        invoiceLabel.setFont(new Font("Arial", Font.BOLD, 18));

        headerPanel.add(companyLabel);
        headerPanel.add(addressCompany);
        headerPanel.add(invoiceLabel);

        billPanel.add(headerPanel, BorderLayout.NORTH);

        // ================= CENTER =================
        JPanel centerPanel = new JPanel(new BorderLayout(15, 15));

        JPanel buyerPanel = new JPanel(new GridLayout(4, 1));
        buyerPanel.setBorder(new TitledBorder("Buyer Details"));

        buyerLabel = new JLabel("Name: ");
        emailLabel = new JLabel("Email: ");
        phoneLabel = new JLabel("Phone: ");
        addressLabel = new JLabel("Address: ");

        buyerPanel.add(buyerLabel);
        buyerPanel.add(emailLabel);
        buyerPanel.add(phoneLabel);
        buyerPanel.add(addressLabel);

        centerPanel.add(buyerPanel, BorderLayout.NORTH);

        model = new DefaultTableModel(
                new String[]{"Product Name", "Quantity", "Unit Price", "Total Price"}, 0);

        table = new JTable(model);
        table.setRowHeight(30);

        JScrollPane tableScroll = new JScrollPane(table);
        tableScroll.setBorder(new TitledBorder("Purchased Items"));

        centerPanel.add(tableScroll, BorderLayout.CENTER);

        billPanel.add(centerPanel, BorderLayout.CENTER);

        // ================= FOOTER =================
        JPanel footerPanel = new JPanel(new BorderLayout(15, 15));

        JPanel summaryPanel = new JPanel(new GridLayout(2, 1));
        summaryPanel.setBorder(new TitledBorder("Summary"));

        subtotalLabel = new JLabel("Subtotal: ₹0.0");
        finalTotalLabel = new JLabel("Final Total: ₹0.0");
        finalTotalLabel.setFont(new Font("Arial", Font.BOLD, 16));

        summaryPanel.add(subtotalLabel);
        summaryPanel.add(finalTotalLabel);

        footerPanel.add(summaryPanel, BorderLayout.WEST);

        JPanel transactionPanel = new JPanel(new GridLayout(3, 1));
        transactionPanel.setBorder(new TitledBorder("Transaction Details"));

        paymentLabel = new JLabel("Payment Method: ");
        dateLabel = new JLabel("Purchase Date: ");

        JLabel transactionIdLabel = new JLabel("Transaction ID: ");

        transactionPanel.add(transactionIdLabel);
        transactionPanel.add(paymentLabel);
        transactionPanel.add(dateLabel);

        footerPanel.add(transactionPanel, BorderLayout.CENTER);

        billPanel.add(footerPanel, BorderLayout.SOUTH);

        // ================= BUTTON PANEL =================
        JPanel buttonPanel = new JPanel();

        JButton printBtn = new JButton("Print Full Invoice");
        JButton pdfBtn = new JButton("Export PDF");

        buttonPanel.add(printBtn);
        buttonPanel.add(pdfBtn);

        add(buttonPanel, BorderLayout.SOUTH);

        // ================= ACTIONS =================
        searchBtn.addActionListener(e -> loadBill());
        printBtn.addActionListener(e -> printFullInvoice());
        pdfBtn.addActionListener(e -> exportPDF());
    }

    private void loadBill() {

        if (transactionField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter Transaction ID.");
            return;
        }

        try {
            int transactionId = Integer.parseInt(transactionField.getText());
            Bill bill = new BillDAO().getBillByTransactionId(transactionId);

            buyerLabel.setText("Name: " + bill.getBuyerName());
            emailLabel.setText("Email: " + bill.getBuyerEmail());
            phoneLabel.setText("Phone: " + bill.getBuyerPhone());
            addressLabel.setText("Address: " + bill.getBuyerAddress());

            paymentLabel.setText("Payment Method: " + bill.getPaymentMethod());
            dateLabel.setText("Purchase Date: " + bill.getPurchaseDate());

            model.setRowCount(0);

            for (BillItem item : bill.getItems()) {
                model.addRow(new Object[]{
                        item.getProductName(),
                        item.getQuantity(),
                        item.getUnitPrice(),
                        item.getTotalPrice()
                });
            }

            subtotalLabel.setText("Subtotal: ₹" + bill.getSubtotal());
            finalTotalLabel.setText("Final Total: ₹" + bill.getSubtotal());

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private void printFullInvoice() {

        PrinterJob job = PrinterJob.getPrinterJob();
        job.setJobName("Invoice Print");

        job.setPrintable((graphics, pageFormat, pageIndex) -> {

            if (pageIndex > 0) {
                return Printable.NO_SUCH_PAGE;
            }

            Graphics2D g2 = (Graphics2D) graphics;
            g2.translate(pageFormat.getImageableX(), pageFormat.getImageableY());

            billPanel.printAll(graphics);

            return Printable.PAGE_EXISTS;
        });

        if (job.printDialog()) {
            try {
                job.print();
            } catch (PrinterException e) {
                JOptionPane.showMessageDialog(this, "Print Failed");
            }
        }
    }

    private void exportPDF() {

        if (transactionField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter Transaction ID first.");
            return;
        }

        try {

            int transactionId = Integer.parseInt(transactionField.getText());
            Bill bill = new BillDAO().getBillByTransactionId(transactionId);

            JFileChooser chooser = new JFileChooser();
            chooser.setSelectedFile(new java.io.File("Invoice_" + transactionId + ".pdf"));

            if (chooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION)
                return;

            com.itextpdf.text.Document document =
                    new com.itextpdf.text.Document();

            PdfWriter.getInstance(document,
                    new FileOutputStream(chooser.getSelectedFile()));

            document.open();

            document.add(new Paragraph("SMARTSTORE MANAGEMENT SYSTEM"));
            document.add(new Paragraph("INVOICE\n\n"));

            document.add(new Paragraph("Transaction ID: " + bill.getTransactionId()));
            document.add(new Paragraph("Buyer: " + bill.getBuyerName()));
            document.add(new Paragraph("Email: " + bill.getBuyerEmail()));
            document.add(new Paragraph("Phone: " + bill.getBuyerPhone()));
            document.add(new Paragraph("Address: " + bill.getBuyerAddress()));
            document.add(new Paragraph("\n"));

            PdfPTable pdfTable = new PdfPTable(4);
            pdfTable.addCell("Product");
            pdfTable.addCell("Qty");
            pdfTable.addCell("Unit Price");
            pdfTable.addCell("Total");

            for (BillItem item : bill.getItems()) {
                pdfTable.addCell(item.getProductName());
                pdfTable.addCell(String.valueOf(item.getQuantity()));
                pdfTable.addCell(String.valueOf(item.getUnitPrice()));
                pdfTable.addCell(String.valueOf(item.getTotalPrice()));
            }

            document.add(pdfTable);

            document.add(new Paragraph("\nSubtotal: ₹" + bill.getSubtotal()));
            document.add(new Paragraph("Final Total: ₹" + bill.getSubtotal()));
            document.add(new Paragraph("Payment Method: " + bill.getPaymentMethod()));
            document.add(new Paragraph("Date: " + bill.getPurchaseDate()));

            document.close();

            JOptionPane.showMessageDialog(this, "PDF Exported Successfully!");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "PDF Export Failed.");
        }
    }
}