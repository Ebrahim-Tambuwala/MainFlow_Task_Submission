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
        setSize(450, 470);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        panel.setBackground(Color.WHITE);
        add(panel);

        // Title Section
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

        // Buttons Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(5, 1, 15, 15));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(30, 0, 0, 0));

        JButton btnAddProduct = new JButton("Add Product");
        JButton btnViewProducts = new JButton("View Products");
        JButton btnAddBuyer = new JButton("Add Buyer");
        JButton btnViewBuyers = new JButton("View Buyers");
        JButton btnLogout = new JButton("Logout");

        JButton[] buttons = {
                btnAddProduct, btnViewProducts,
                btnAddBuyer, btnViewBuyers,
                btnLogout
        };

        for (JButton btn : buttons) {
            btn.setFocusPainted(false);
            btn.setPreferredSize(new Dimension(200, 40));
        }

        // Navigation with role passing
        btnAddProduct.addActionListener(e -> new AddProductForm().setVisible(true));

        btnViewProducts.addActionListener(e ->
                new ViewProductsForm(role).setVisible(true)
        );

        btnAddBuyer.addActionListener(e -> new AddBuyerForm().setVisible(true));

        btnViewBuyers.addActionListener(e ->
                new ViewBuyersForm(role).setVisible(true)
        );

        btnLogout.addActionListener(e -> {
            new LoginForm().setVisible(true);
            this.dispose();
        });

        buttonPanel.add(btnAddProduct);
        buttonPanel.add(btnViewProducts);
        buttonPanel.add(btnAddBuyer);
        buttonPanel.add(btnViewBuyers);
        buttonPanel.add(btnLogout);

        panel.add(buttonPanel, BorderLayout.CENTER);
    }
}