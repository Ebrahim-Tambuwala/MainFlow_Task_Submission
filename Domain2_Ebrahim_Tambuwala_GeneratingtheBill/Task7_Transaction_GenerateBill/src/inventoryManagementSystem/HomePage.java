package inventoryManagementSystem;

import inventoryManagementSystem.ui.*;
import javax.swing.*;
import java.awt.*;

public class HomePage extends JFrame {

    private String username;
    private String role;

    public HomePage(String username, String role) {

        this.username = username;
        this.role = role;

        setTitle("SmartStore Dashboard");
        setSize(500, 600);   // Increased height
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        panel.setBackground(Color.WHITE);
        add(panel);

        // ================= TITLE SECTION =================
        JLabel lblTitle = new JLabel("SmartStore Dashboard", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitle.setForeground(new Color(30, 144, 255));

        JLabel lblWelcome = new JLabel(
                "Welcome, " + username + " (" + role.toUpperCase() + ")",
                SwingConstants.CENTER
        );
        lblWelcome.setFont(new Font("Arial", Font.PLAIN, 14));

        JPanel topPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        topPanel.setBackground(Color.WHITE);
        topPanel.add(lblTitle);
        topPanel.add(lblWelcome);

        panel.add(topPanel, BorderLayout.NORTH);

        // ================= BUTTONS PANEL =================
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(7, 1, 15, 15));  // Increased rows
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(30, 0, 0, 0));

        JButton btnAddProduct = new JButton("Add Product");
        JButton btnViewProducts = new JButton("View Products");
        JButton btnAddBuyer = new JButton("Add Buyer");
        JButton btnViewBuyers = new JButton("View Buyers");

        JButton btnCreateTransaction = new JButton("Create Transaction");
        JButton btnGenerateBill = new JButton("Generate Bill");

        JButton btnLogout = new JButton("Logout");

        JButton[] buttons = {
                btnAddProduct,
                btnViewProducts,
                btnAddBuyer,
                btnViewBuyers,
                btnCreateTransaction,
                btnGenerateBill,
                btnLogout
        };

        for (JButton btn : buttons) {
            btn.setFocusPainted(false);
            btn.setPreferredSize(new Dimension(200, 40));
        }

        // ================= NAVIGATION =================
        btnAddProduct.addActionListener(e ->
                new AddProductForm().setVisible(true)
        );

        btnViewProducts.addActionListener(e ->
                new ViewProductsForm(role).setVisible(true)
        );

        btnAddBuyer.addActionListener(e ->
                new AddBuyerForm().setVisible(true)
        );

        btnViewBuyers.addActionListener(e ->
                new ViewBuyersForm(role).setVisible(true)
        );

        btnCreateTransaction.addActionListener(e ->
                new CreateTransactionForm().setVisible(true)
        );

        btnGenerateBill.addActionListener(e ->
                new GenerateBillForm().setVisible(true)
        );

        btnLogout.addActionListener(e -> {
            new LoginForm().setVisible(true);
            this.dispose();
        });

        // ================= ADD BUTTONS =================
        for (JButton btn : buttons) {
            buttonPanel.add(btn);
        }

        panel.add(buttonPanel, BorderLayout.CENTER);
    }
}