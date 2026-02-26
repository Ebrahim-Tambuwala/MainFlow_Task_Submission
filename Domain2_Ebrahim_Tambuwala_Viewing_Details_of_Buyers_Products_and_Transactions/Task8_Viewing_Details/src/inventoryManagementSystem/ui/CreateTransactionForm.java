package inventoryManagementSystem.ui;

import inventoryManagementSystem.dao.ProductDAO;
import inventoryManagementSystem.dao.BuyerDAO;
import inventoryManagementSystem.dao.TransactionDAO;
import inventoryManagementSystem.model.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CreateTransactionForm extends JFrame {

    private JComboBox<Buyer> buyerBox;
    private JComboBox<Product> productBox;
    private JTextField quantityField;
    private JComboBox<String> paymentBox;

    private JTable cartTable;
    private DefaultTableModel cartModel;

    private JLabel subtotalLabel;

    private List<TransactionItem> cartItems = new ArrayList<>();
    private double subtotal = 0;

    public CreateTransactionForm() {

        setTitle("Create Transaction");
        setSize(950, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // ================= TOP =================
        JPanel topPanel = new JPanel(new GridLayout(2, 1));

        JLabel title = new JLabel("Create Transaction", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));

        JPanel buyerPanel = new JPanel();
        buyerPanel.add(new JLabel("Select Buyer:"));

        buyerBox = new JComboBox<>();
        loadBuyers();
        buyerPanel.add(buyerBox);

        topPanel.add(title);
        topPanel.add(buyerPanel);

        add(topPanel, BorderLayout.NORTH);

        // ================= CENTER =================
        JPanel centerPanel = new JPanel(new BorderLayout());

        JPanel productPanel = new JPanel();

        productPanel.add(new JLabel("Product:"));
        productBox = new JComboBox<>();
        loadProducts();
        productPanel.add(productBox);

        productPanel.add(new JLabel("Quantity:"));
        quantityField = new JTextField(5);
        productPanel.add(quantityField);

        JButton addBtn = new JButton("Add To Cart");
        productPanel.add(addBtn);

        centerPanel.add(productPanel, BorderLayout.NORTH);

        cartModel = new DefaultTableModel(
                new String[]{"Product ID", "Name", "Qty", "Unit Price", "Total"}, 0
        ) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        cartTable = new JTable(cartModel);
        centerPanel.add(new JScrollPane(cartTable), BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);

        // ================= BOTTOM =================
        JPanel bottomPanel = new JPanel(new GridLayout(3, 1));

        JPanel paymentPanel = new JPanel();
        paymentPanel.add(new JLabel("Payment Method:"));

        paymentBox = new JComboBox<>(new String[]{"Cash", "Card", "UPI"});
        paymentPanel.add(paymentBox);

        subtotalLabel = new JLabel("Subtotal: ₹0.00");
        paymentPanel.add(subtotalLabel);

        bottomPanel.add(paymentPanel);

        JButton removeBtn = new JButton("Remove Selected");
        JButton clearBtn = new JButton("Clear Cart");
        JButton saveBtn = new JButton("Save Transaction");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(removeBtn);
        buttonPanel.add(clearBtn);
        buttonPanel.add(saveBtn);

        bottomPanel.add(buttonPanel);

        add(bottomPanel, BorderLayout.SOUTH);

        // ================= ACTIONS =================
        addBtn.addActionListener(e -> addToCart());
        removeBtn.addActionListener(e -> removeFromCart());
        clearBtn.addActionListener(e -> clearCart());
        saveBtn.addActionListener(e -> saveTransaction());
    }

    // ================= LOAD DATA =================
    private void loadBuyers() {
        try {
            List<Buyer> buyers = new BuyerDAO().searchBuyers(null, null, 100, 0);
            for (Buyer b : buyers) buyerBox.addItem(b);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading buyers.");
        }
    }

    private void loadProducts() {
        try {
            List<Product> products = new ProductDAO().getAllProducts(100, 0);
            for (Product p : products) productBox.addItem(p);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading products.");
        }
    }

    // ================= ADD TO CART =================
    private void addToCart() {

        Product product = (Product) productBox.getSelectedItem();

        if (product == null) return;

        try {
            int qty = Integer.parseInt(quantityField.getText().trim());

            if (qty <= 0) {
                JOptionPane.showMessageDialog(this, "Quantity must be positive.");
                return;
            }

            if (qty > product.getQuantity()) {
                JOptionPane.showMessageDialog(this, "Insufficient stock.");
                return;
            }

            // Prevent duplicate products in cart
            for (int i = 0; i < cartItems.size(); i++) {
                if (cartItems.get(i).getProductId() == product.getId()) {
                    JOptionPane.showMessageDialog(this, "Product already added.");
                    return;
                }
            }

            double total = qty * product.getPrice();

            cartModel.addRow(new Object[]{
                    product.getId(),
                    product.getName(),
                    qty,
                    product.getPrice(),
                    total
            });

            cartItems.add(new TransactionItem(product.getId(), qty, product.getPrice()));

            subtotal += total;
            updateSubtotal();

            quantityField.setText("");

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid quantity.");
        }
    }

    // ================= REMOVE =================
    private void removeFromCart() {

        int row = cartTable.getSelectedRow();
        if (row == -1) return;

        double total = (double) cartModel.getValueAt(row, 4);
        subtotal -= total;

        cartItems.remove(row);
        cartModel.removeRow(row);

        updateSubtotal();
    }

    // ================= CLEAR =================
    private void clearCart() {
        cartItems.clear();
        cartModel.setRowCount(0);
        subtotal = 0;
        updateSubtotal();
    }

    private void updateSubtotal() {
        subtotalLabel.setText(String.format("Subtotal: ₹%.2f", subtotal));
    }

    // ================= SAVE =================
    private void saveTransaction() {

        if (cartItems.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Cart is empty.");
            return;
        }

        if (buyerBox.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Please select buyer.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Confirm transaction?",
                "Confirm",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) return;

        try {
            Buyer buyer = (Buyer) buyerBox.getSelectedItem();
            String paymentMethod = paymentBox.getSelectedItem().toString();

            Transaction transaction = new Transaction(
                    buyer.getId(),
                    paymentMethod
            );

            int transactionId = new TransactionDAO()
                    .createTransaction(transaction, cartItems);

            JOptionPane.showMessageDialog(this,
                    "Transaction Successful! ID: " + transactionId);

            clearCart();
            dispose();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error: " + e.getMessage());
        }
    }
}