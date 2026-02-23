package inventoryManagementSystem.ui;

import inventoryManagementSystem.dao.ProductDAO;
import inventoryManagementSystem.model.Product;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;

public class ViewProductsForm extends JFrame {

    private JTable table;
    private DefaultTableModel model;
    private ProductDAO productDAO;

    public ViewProductsForm() {

        productDAO = new ProductDAO();

        setTitle("View Products");
        setSize(900, 500);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        mainPanel.setBackground(Color.WHITE);
        add(mainPanel);

        JLabel title = new JLabel("Product Management", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setForeground(new Color(30, 144, 255));
        mainPanel.add(title, BorderLayout.NORTH);

        model = new DefaultTableModel(
                new String[]{"ID", "Name", "Category", "Price", "Quantity", "Description"}, 0
        ) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(model);
        table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        table.setRowHeight(25);
        table.setSelectionBackground(new Color(220, 240, 255));

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

        refreshBtn.addActionListener(e -> loadProducts());
        deleteBtn.addActionListener(e -> deleteProduct());

        loadProducts();
    }

    private void loadProducts() {
        try {
            model.setRowCount(0);
            List<Product> products = productDAO.getAllProducts();

            for (Product p : products) {
                model.addRow(new Object[]{
                        p.getId(),
                        p.getName(),
                        p.getCategory(),
                        p.getPrice(),
                        p.getQuantity(),
                        p.getDescription()
                });
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading products: " + e.getMessage());
        }
    }

    private void deleteProduct() {
        try {
            int[] selectedRows = table.getSelectedRows();

            if (selectedRows.length == 0) {
                JOptionPane.showMessageDialog(this, "Please select at least one product.");
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to delete " + selectedRows.length + " selected product(s)?",
                    "Confirm Bulk Delete",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {

                // Delete from bottom to top to avoid index shifting
                for (int i = selectedRows.length - 1; i >= 0; i--) {
                    int row = selectedRows[i];
                    int id = (int) model.getValueAt(row, 0);
                    productDAO.deleteProduct(id);
                }

                JOptionPane.showMessageDialog(this, "Selected products deleted successfully.");
                loadProducts();
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error deleting products: " + e.getMessage());
        }
    }
}