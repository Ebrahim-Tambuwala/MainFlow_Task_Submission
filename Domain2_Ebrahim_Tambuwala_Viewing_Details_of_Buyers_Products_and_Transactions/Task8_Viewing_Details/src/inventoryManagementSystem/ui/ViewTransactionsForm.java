package inventoryManagementSystem.ui;

import inventoryManagementSystem.dao.TransactionDAO;
import inventoryManagementSystem.model.TransactionView;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;

public class ViewTransactionsForm extends JFrame {
    private String role;
    private JTable table;
    private DefaultTableModel model;
    private TransactionDAO transactionDAO;

    private JTextField searchField;

    public ViewTransactionsForm(String role) {
        this.role = role;

        transactionDAO = new TransactionDAO();

        setTitle("View Transactions");
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // ================= TOP PANEL =================
        JPanel topPanel = new JPanel();

        topPanel.add(new JLabel("Search (Buyer Name / Transaction ID):"));
        searchField = new JTextField(20);
        topPanel.add(searchField);

        JButton searchBtn = new JButton("Search");
        JButton refreshBtn = new JButton("Refresh");

        topPanel.add(searchBtn);
        topPanel.add(refreshBtn);

        add(topPanel, BorderLayout.NORTH);

        // ================= TABLE =================
        model = new DefaultTableModel(
                new String[]{"Transaction ID", "Buyer Name", "Date",
                        "Payment Method", "Order Status", "Total Amount"}, 0
        ) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(model);
        table.setRowHeight(25);
        table.setAutoCreateRowSorter(true);

        add(new JScrollPane(table), BorderLayout.CENTER);

        // ================= ACTIONS =================
        searchBtn.addActionListener(e -> searchTransactions());
        refreshBtn.addActionListener(e -> loadAllTransactions());

        loadAllTransactions();
    }

    // ================= LOAD ALL =================
    private void loadAllTransactions() {

        try {
            model.setRowCount(0);

            List<TransactionView> list =
                    transactionDAO.getAllTransactions(1000, 0);

            for (TransactionView t : list) {
                model.addRow(new Object[]{
                        t.getTransactionId(),
                        t.getBuyerName(),
                        t.getPurchaseDate(),
                        t.getPaymentMethod(),
                        t.getOrderStatus(),
                        "₹" + t.getTotalAmount()
                });
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error loading transactions: " + e.getMessage());
        }
    }

    // ================= SEARCH =================
    private void searchTransactions() {

        String keyword = searchField.getText().trim();

        if (keyword.isEmpty()) {
            loadAllTransactions();
            return;
        }

        try {

            model.setRowCount(0);

            List<TransactionView> list =
                    transactionDAO.searchTransactions(keyword);

            for (TransactionView t : list) {
                model.addRow(new Object[]{
                        t.getTransactionId(),
                        t.getBuyerName(),
                        t.getPurchaseDate(),
                        t.getPaymentMethod(),
                        t.getOrderStatus(),
                        "₹" + t.getTotalAmount()
                });
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error searching transactions: " + e.getMessage());
        }
    }
}