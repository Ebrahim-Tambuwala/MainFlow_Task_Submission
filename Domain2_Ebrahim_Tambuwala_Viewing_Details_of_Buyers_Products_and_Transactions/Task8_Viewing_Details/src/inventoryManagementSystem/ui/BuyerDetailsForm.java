package inventoryManagementSystem.ui;

import inventoryManagementSystem.dao.BuyerDAO;
import inventoryManagementSystem.model.Buyer;
import inventoryManagementSystem.model.TransactionView;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;

public class BuyerDetailsForm extends JFrame {

    private Buyer buyer;
    private JTable table;
    private DefaultTableModel model;

    public BuyerDetailsForm(Buyer buyer) {

        this.buyer = buyer;

        setTitle("Buyer Details - " + buyer.getName());
        setSize(900, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // ================= BUYER INFO =================
        JPanel infoPanel = new JPanel(new GridLayout(5, 1));
        infoPanel.setBorder(BorderFactory.createTitledBorder("Buyer Information"));

        infoPanel.add(new JLabel("Name: " + buyer.getName()));
        infoPanel.add(new JLabel("Email: " + buyer.getEmail()));
        infoPanel.add(new JLabel("Phone: " + buyer.getPhone()));
        infoPanel.add(new JLabel("Address: " + buyer.getAddress()));

        add(infoPanel, BorderLayout.NORTH);

        // ================= TABLE =================
        model = new DefaultTableModel(
                new String[]{"Transaction ID", "Date",
                        "Payment", "Status", "Total Amount"}, 0
        ) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(model);
        table.setRowHeight(25);

        add(new JScrollPane(table), BorderLayout.CENTER);

        // ================= LOAD DATA =================
        loadTransactions();
    }

    private void loadTransactions() {

        try {

            BuyerDAO dao = new BuyerDAO();

            List<TransactionView> transactions =
                    dao.getTransactionsByBuyerId(buyer.getId());

            double totalSpent = dao.getTotalAmountSpent(buyer.getId());
            String preferredPayment =
                    dao.getPreferredPaymentMethod(buyer.getId());

            for (TransactionView t : transactions) {
                model.addRow(new Object[]{
                        t.getTransactionId(),
                        t.getPurchaseDate(),
                        t.getPaymentMethod(),
                        t.getOrderStatus(),
                        "₹" + t.getTotalAmount()
                });
            }

            JOptionPane.showMessageDialog(this,
                    "Total Spent: ₹" + totalSpent +
                            "\nPreferred Payment: " + preferredPayment);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error loading buyer details: " + e.getMessage());
        }
    }
}