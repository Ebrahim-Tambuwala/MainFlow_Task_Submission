package inventoryManagementSystem.ui;

import inventoryManagementSystem.dao.ProductDAO;
import inventoryManagementSystem.model.Product;

import javax.swing.*;
import java.awt.*;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;

public class AddProductForm extends JFrame {

    private JTextField nameField, categoryField, priceField, quantityField;
    private JTextArea descriptionArea;
    private JButton saveBtn;

    private Product existingProduct = null;   // Used for Update Mode

    // ================= DEFAULT CONSTRUCTOR (ADD MODE) =================
    public AddProductForm() {
        initializeUI();
    }

    // ================= UPDATE MODE CONSTRUCTOR =================
    public AddProductForm(Product product) {
        this();
        this.existingProduct = product;

        setTitle("Edit Product");
        saveBtn.setText("Update Product");

        // Pre-fill fields
        nameField.setText(product.getName());
        categoryField.setText(product.getCategory());
        priceField.setText(String.valueOf(product.getPrice()));
        quantityField.setText(String.valueOf(product.getQuantity()));
        descriptionArea.setText(product.getDescription());
    }

    // ================= UI INITIALIZATION =================
    private void initializeUI() {

        setTitle("Add Product");
        setSize(500, 500);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        mainPanel.setBackground(Color.WHITE);
        add(mainPanel);

        JLabel title = new JLabel("Product Form", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setForeground(new Color(30, 144, 255));
        mainPanel.add(title, BorderLayout.NORTH);

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

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);

        saveBtn = new JButton("Add Product");
        saveBtn.setBackground(new Color(30, 144, 255));
        saveBtn.setForeground(Color.WHITE);
        saveBtn.setFocusPainted(false);
        saveBtn.setToolTipText("Save product details");

        buttonPanel.add(saveBtn);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        saveBtn.addActionListener(e -> saveProduct());
    }

    // ================= SAVE LOGIC (ADD + UPDATE) =================
    private void saveProduct() {

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

            Product product;

            // ADD MODE
            if (existingProduct == null) {

                product = new Product(name, category, price, quantity, description);
                new ProductDAO().addProduct(product);

                JOptionPane.showMessageDialog(this, "Product Added Successfully!");
                clearFields();
            }

            // UPDATE MODE
            else {

                product = new Product(
                        existingProduct.getId(),
                        name,
                        category,
                        price,
                        quantity,
                        description
                );

                new ProductDAO().updateProduct(product);

                JOptionPane.showMessageDialog(this, "Product Updated Successfully!");
                dispose();   // Close after update
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Price must be a number and Quantity must be an integer.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error: " + ex.getMessage());
        }
    }

    // ================= CLEAR FORM =================
    private void clearFields() {
        nameField.setText("");
        categoryField.setText("");
        priceField.setText("");
        quantityField.setText("");
        descriptionArea.setText("");
    }
}