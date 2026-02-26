package inventoryManagementSystem.ui;

import inventoryManagementSystem.dao.BuyerDAO;
import inventoryManagementSystem.model.Buyer;

import javax.swing.*;
import java.awt.*;

public class AddBuyerForm extends JFrame {

    private JTextField nameField, emailField, phoneField;
    private JTextArea addressArea;
    private JButton saveBtn;

    private Buyer existingBuyer = null;

    public AddBuyerForm() {
        initializeUI();
    }

    public AddBuyerForm(Buyer buyer) {
        this();
        this.existingBuyer = buyer;

        setTitle("Edit Buyer");
        saveBtn.setText("Update Buyer");

        nameField.setText(buyer.getName());
        emailField.setText(buyer.getEmail());
        phoneField.setText(buyer.getPhone());
        addressArea.setText(buyer.getAddress());
    }

    private void initializeUI() {

        setTitle("Add Buyer");
        setSize(450, 450);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        mainPanel.setBackground(Color.WHITE);
        add(mainPanel);

        JLabel title = new JLabel("Buyer Form", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setForeground(new Color(30, 144, 255));
        mainPanel.add(title, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(4, 1, 15, 15));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        formPanel.setBackground(Color.WHITE);

        nameField = new JTextField();
        nameField.setBorder(BorderFactory.createTitledBorder("Name *"));

        emailField = new JTextField();
        emailField.setBorder(BorderFactory.createTitledBorder("Email *"));

        phoneField = new JTextField();
        phoneField.setBorder(BorderFactory.createTitledBorder("Phone *"));

        addressArea = new JTextArea(3, 20);
        addressArea.setBorder(BorderFactory.createTitledBorder("Address"));

        formPanel.add(nameField);
        formPanel.add(emailField);
        formPanel.add(phoneField);
        formPanel.add(new JScrollPane(addressArea));

        mainPanel.add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);

        saveBtn = new JButton("Add Buyer");
        saveBtn.setBackground(new Color(30, 144, 255));
        saveBtn.setForeground(Color.WHITE);
        saveBtn.setFocusPainted(false);

        buttonPanel.add(saveBtn);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        saveBtn.addActionListener(e -> saveBuyer());
    }

    private void saveBuyer() {

        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();
        String address = addressArea.getText().trim();

        if (name.isEmpty() || email.isEmpty() || phone.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all required fields.");
            return;
        }

        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            JOptionPane.showMessageDialog(this, "Invalid email format.");
            return;
        }

        if (!phone.matches("\\d{10,15}")) {
            JOptionPane.showMessageDialog(this, "Phone must contain 10–15 digits.");
            return;
        }

        try {

            BuyerDAO dao = new BuyerDAO();

            if (existingBuyer == null) {

                Buyer buyer = new Buyer(name, email, phone, address);
                dao.addBuyer(buyer);

                JOptionPane.showMessageDialog(this, "Buyer Added Successfully!");
                clearFields();
            } else {

                Buyer buyer = new Buyer(
                        existingBuyer.getId(),
                        name,
                        email,
                        phone,
                        address
                );

                dao.updateBuyer(buyer);

                JOptionPane.showMessageDialog(this, "Buyer Updated Successfully!");
                dispose();
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void clearFields() {
        nameField.setText("");
        emailField.setText("");
        phoneField.setText("");
        addressArea.setText("");
    }
}