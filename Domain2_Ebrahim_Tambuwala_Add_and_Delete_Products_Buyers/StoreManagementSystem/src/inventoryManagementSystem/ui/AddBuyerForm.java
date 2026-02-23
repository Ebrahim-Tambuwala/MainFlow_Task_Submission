package inventoryManagementSystem.ui;

import inventoryManagementSystem.dao.BuyerDAO;
import inventoryManagementSystem.model.Buyer;

import javax.swing.*;
import java.awt.*;

public class AddBuyerForm extends JFrame {

    private JTextField nameField, emailField, phoneField;
    private JTextArea addressArea;

    public AddBuyerForm() {

        setTitle("Add Buyer");
        setSize(450, 420);
        setResizable(false);                 // Prevent distortion
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        mainPanel.setBackground(Color.WHITE);
        add(mainPanel);

        // Title
        JLabel title = new JLabel("Add New Buyer", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setForeground(new Color(30, 144, 255));
        mainPanel.add(title, BorderLayout.NORTH);

        // Form panel
        JPanel formPanel = new JPanel(new GridLayout(4, 1, 15, 15));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        formPanel.setBackground(Color.WHITE);

        nameField = new JTextField();
        nameField.setBorder(BorderFactory.createTitledBorder("Name"));

        emailField = new JTextField();
        emailField.setBorder(BorderFactory.createTitledBorder("Email"));

        phoneField = new JTextField();
        phoneField.setBorder(BorderFactory.createTitledBorder("Phone"));

        addressArea = new JTextArea(3, 20);
        addressArea.setBorder(BorderFactory.createTitledBorder("Address"));

        formPanel.add(nameField);
        formPanel.add(emailField);
        formPanel.add(phoneField);
        formPanel.add(new JScrollPane(addressArea));

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);

        JButton addBtn = new JButton("Add Buyer");
        addBtn.setBackground(new Color(30, 144, 255));
        addBtn.setForeground(Color.WHITE);
        addBtn.setFocusPainted(false);

        buttonPanel.add(addBtn);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        addBtn.addActionListener(e -> addBuyer());
    }

    private void addBuyer() {

        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();
        String address = addressArea.getText().trim();

        if (name.isEmpty() || email.isEmpty() || phone.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all required fields.");
            return;
        }

        if (!email.contains("@")) {
            JOptionPane.showMessageDialog(this, "Invalid email format.");
            return;
        }

        try {
            Buyer buyer = new Buyer(name, email, phone, address);
            new BuyerDAO().addBuyer(buyer);

            JOptionPane.showMessageDialog(this, "Buyer Added Successfully!");

            // Clear fields
            nameField.setText("");
            emailField.setText("");
            phoneField.setText("");
            addressArea.setText("");

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }
}