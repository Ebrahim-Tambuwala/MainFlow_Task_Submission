package inventoryManagementSystem.ui;

import inventoryManagementSystem.dao.BuyerDAO;
import inventoryManagementSystem.model.Buyer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;

public class ViewBuyersForm extends JFrame {

    private JTable table;
    private DefaultTableModel model;
    private BuyerDAO buyerDAO;

    public ViewBuyersForm() {

        buyerDAO = new BuyerDAO();

        setTitle("View Buyers");
        setSize(800, 450);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        mainPanel.setBackground(Color.WHITE);
        add(mainPanel);

        JLabel title = new JLabel("Buyer Management", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setForeground(new Color(30, 144, 255));
        mainPanel.add(title, BorderLayout.NORTH);

        model = new DefaultTableModel(
                new String[]{"ID", "Name", "Email", "Phone", "Address"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table non-editable
            }
        };

        table = new JTable(model);
        table.setRowHeight(25);
        table.setSelectionBackground(new Color(220, 240, 255));

        // Enable multiple row selection for bulk delete
        table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 13));

        mainPanel.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);

        JButton refreshBtn = new JButton("Refresh");
        JButton deleteBtn = new JButton("Delete Selected");

        refreshBtn.setFocusPainted(false);
        deleteBtn.setFocusPainted(false);

        buttonPanel.add(refreshBtn);
        buttonPanel.add(deleteBtn);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        refreshBtn.addActionListener(e -> loadBuyers());
        deleteBtn.addActionListener(e -> deleteBuyer());

        loadBuyers();
    }

    private void loadBuyers() {
        try {
            model.setRowCount(0);
            List<Buyer> buyers = buyerDAO.getAllBuyers();

            for (Buyer b : buyers) {
                model.addRow(new Object[]{
                        b.getId(),
                        b.getName(),
                        b.getEmail(),
                        b.getPhone(),
                        b.getAddress()
                });
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading buyers: " + e.getMessage());
        }
    }

    private void deleteBuyer() {
        try {
            int[] selectedRows = table.getSelectedRows();

            if (selectedRows.length == 0) {
                JOptionPane.showMessageDialog(this, "Please select at least one buyer.");
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to delete " + selectedRows.length + " selected buyer(s)?",
                    "Confirm Bulk Delete",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {

                // Delete from bottom to top to avoid index shifting
                for (int i = selectedRows.length - 1; i >= 0; i--) {
                    int row = selectedRows[i];
                    int id = (int) model.getValueAt(row, 0);
                    buyerDAO.deleteBuyer(id);
                }

                JOptionPane.showMessageDialog(this, "Selected buyers deleted successfully.");
                loadBuyers();
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error deleting buyers: " + e.getMessage());
        }
    }
}