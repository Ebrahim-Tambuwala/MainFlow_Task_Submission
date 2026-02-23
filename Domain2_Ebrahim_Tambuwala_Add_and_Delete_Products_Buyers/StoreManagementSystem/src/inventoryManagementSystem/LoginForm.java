package inventoryManagementSystem;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class LoginForm extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginForm() {

        setTitle("SmartStore Login");

        setSize(400, 320);           // Fixed size
        setResizable(false);         // Prevent resizing
        setLocationRelativeTo(null); // Always center
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        getContentPane().setBackground(Color.WHITE);

        // Main Panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        add(mainPanel);

        // Title
        JLabel titleLabel = new JLabel("SmartStore Login", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        titleLabel.setForeground(new Color(30, 144, 255));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Form Panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(4, 1, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        usernameField = new JTextField();
        usernameField.setBorder(BorderFactory.createTitledBorder("Username"));

        passwordField = new JPasswordField();
        passwordField.setBorder(BorderFactory.createTitledBorder("Password"));

        JButton loginBtn = new JButton("Login");
        loginBtn.setBackground(new Color(30, 144, 255));
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setFocusPainted(false);

        JButton exitBtn = new JButton("Exit");

        formPanel.add(usernameField);
        formPanel.add(passwordField);
        formPanel.add(loginBtn);
        formPanel.add(exitBtn);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Button Actions
        loginBtn.addActionListener(e -> login());
        exitBtn.addActionListener(e -> System.exit(0));
    }

    private void login() {

        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter username and password.");
            return;
        }

        try {
            Connection con = DatabaseConnection.getConnection();

            String sql = "SELECT * FROM users WHERE username=? AND password=?";
            PreparedStatement pst = con.prepareStatement(sql);

            pst.setString(1, username);
            pst.setString(2, password);

            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                JOptionPane.showMessageDialog(this, "Login Successful!");
                new HomePage(username).setVisible(true);
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid Credentials");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Database Error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        new LoginForm().setVisible(true);
    }
}