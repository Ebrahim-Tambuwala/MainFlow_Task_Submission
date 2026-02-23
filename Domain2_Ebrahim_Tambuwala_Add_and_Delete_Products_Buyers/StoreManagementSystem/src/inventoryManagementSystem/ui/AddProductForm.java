package inventoryManagementSystem.ui;

import inventoryManagementSystem.dao.ProductDAO;
import inventoryManagementSystem.model.Product;

import javax.swing.*;
import java.awt.*;

public class AddProductForm extends JFrame {

    private JTextField nameField, categoryField, priceField, quantityField;
    private JTextArea descriptionArea;

    public AddProductForm() {

        setTitle("Add Product");
        setSize(500, 480);
        setResizable(false);                     // Prevent distortion
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        mainPanel.setBackground(Color.WHITE);
        add(mainPanel);

        // Title
        JLabel title = new JLabel("Add New Product", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setForeground(new Color(30, 144, 255));
        mainPanel.add(title, BorderLayout.NORTH);

        // Form panel
        JPanel formPanel = new JPanel(new GridLayout(5, 1, 15, 15));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        formPanel.setBackground(Color.WHITE);

        nameField = new JTextField();
        nameField.setBorder(BorderFactory.createTitledBorder("Product Name"));

        categoryField = new JTextField();
        categoryField.setBorder(BorderFactory.createTitledBorder("Category"));

        priceField = new JTextField();
        priceField.setBorder(BorderFactory.createTitledBorder("Price"));

        quantityField = new JTextField();
        quantityField.setBorder(BorderFactory.createTitledBorder("Quantity"));

        descriptionArea = new JTextArea(3, 20);
        descriptionArea.setBorder(BorderFactory.createTitledBorder("Description"));

        formPanel.add(nameField);
        formPanel.add(categoryField);
        formPanel.add(priceField);
        formPanel.add(quantityField);
        formPanel.add(new JScrollPane(descriptionArea));

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);

        JButton addBtn = new JButton("Add Product");
        addBtn.setBackground(new Color(30, 144, 255));
        addBtn.setForeground(Color.WHITE);
        addBtn.setFocusPainted(false);

        buttonPanel.add(addBtn);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        addBtn.addActionListener(e -> addProduct());
    }

    private void addProduct() {

        String name = nameField.getText().trim();
        String category = categoryField.getText().trim();
        String priceText = priceField.getText().trim();
        String quantityText = quantityField.getText().trim();
        String description = descriptionArea.getText().trim();

        if (name.isEmpty() || category.isEmpty() || priceText.isEmpty() || quantityText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all required fields.");
            return;
        }

        try {
            double price = Double.parseDouble(priceText);
            int quantity = Integer.parseInt(quantityText);

            if (price < 0 || quantity < 0) {
                JOptionPane.showMessageDialog(this, "Price and Quantity must be positive values.");
                return;
            }

            Product product = new Product(name, category, price, quantity, description);
            new ProductDAO().addProduct(product);

            JOptionPane.showMessageDialog(this, "Product Added Successfully!");

            // Clear fields
            nameField.setText("");
            categoryField.setText("");
            priceField.setText("");
            quantityField.setText("");
            descriptionArea.setText("");

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Price must be a number and Quantity must be an integer.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }
}