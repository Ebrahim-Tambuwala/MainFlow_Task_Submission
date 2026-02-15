package task4.home;

import javax.swing.JOptionPane;
import task4.login.LoginForm;
import task4.users.UserList;
public class HomePage extends javax.swing.JFrame {

    private String username;

    public HomePage(String username) {
        initComponents();
        this.username = username;
        setLocationRelativeTo(null);
        lblWelcome.setText("Welcome, " + username);
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        lblTitle = new javax.swing.JLabel();
        lblWelcome = new javax.swing.JLabel();
        btnProfile = new javax.swing.JButton();
        btnUsers = new javax.swing.JButton();
        btnLogout = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Home");

        lblTitle.setFont(new java.awt.Font("Tahoma", 1, 18));
        lblTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTitle.setText("SmartStore Dashboard");

        lblWelcome.setFont(new java.awt.Font("Tahoma", 0, 14));
        lblWelcome.setText("Welcome");

        btnProfile.setText("View Profile");
        btnProfile.addActionListener(evt ->
            JOptionPane.showMessageDialog(this,
                "Profile feature will be added later")
        );

        btnUsers.setText("View Users");
        btnUsers.addActionListener(evt ->{
            new task4.users.UserList().setVisible(true);
        });

        btnLogout.setText("Logout");
        btnLogout.addActionListener(evt -> {
            new LoginForm().setVisible(true);
            this.dispose();
        });

        javax.swing.GroupLayout layout =
            new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);

        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                .addComponent(lblTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 300,
                        javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(lblWelcome)
                .addComponent(btnProfile, javax.swing.GroupLayout.PREFERRED_SIZE, 150,
                        javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(btnUsers, javax.swing.GroupLayout.PREFERRED_SIZE, 150,
                        javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(btnLogout, javax.swing.GroupLayout.PREFERRED_SIZE, 150,
                        javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        layout.setVerticalGroup(
            layout.createSequentialGroup()
                .addGap(20)
                .addComponent(lblTitle)
                .addGap(20)
                .addComponent(lblWelcome)
                .addGap(20)
                .addComponent(btnProfile)
                .addGap(10)
                .addComponent(btnUsers)
                .addGap(10)
                .addComponent(btnLogout)
                .addGap(20)
        );

        pack();
    }

    // Variables declaration
    private javax.swing.JLabel lblTitle;
    private javax.swing.JLabel lblWelcome;
    private javax.swing.JButton btnProfile;
    private javax.swing.JButton btnUsers;
    private javax.swing.JButton btnLogout;
}
