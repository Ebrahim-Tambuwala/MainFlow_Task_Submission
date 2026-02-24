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

    private String role;

    private JTable table;
    private DefaultTableModel model;
    private ProductDAO productDAO;

    // Search Components
    private JTextField searchField, minPriceField, maxPriceField;
    private JComboBox<String> categoryBox;
    private JCheckBox inStockCheck;

    // Pagination
    private int currentPage = 1;
    private final int pageSize = 10;
    private int totalRecords = 0;

    private JLabel pageLabel;
    private JLabel resultLabel;

    public ViewProductsForm(String role) {

        this.role = role;
        productDAO = new ProductDAO();

        setTitle("Product Management");
        setSize(1100, 650);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        add(mainPanel);

        JLabel title = new JLabel("Product Management", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 22));
        mainPanel.add(title, BorderLayout.NORTH);

        // ================= SEARCH PANEL =================
        JPanel searchPanel = new JPanel(new GridLayout(2, 1));

        JPanel topSearch = new JPanel();

        searchField = new JTextField(15);
        categoryBox = new JComboBox<>(new String[]{"All", "Electronics", "Clothing", "Food", "Accessories"});
        minPriceField = new JTextField(6);
        maxPriceField = new JTextField(6);
        inStockCheck = new JCheckBox("In Stock Only");

        topSearch.add(new JLabel("Keyword:"));
        topSearch.add(searchField);
        topSearch.add(new JLabel("Category:"));
        topSearch.add(categoryBox);
        topSearch.add(new JLabel("Min Price:"));
        topSearch.add(minPriceField);
        topSearch.add(new JLabel("Max Price:"));
        topSearch.add(maxPriceField);
        topSearch.add(inStockCheck);

        JPanel bottomSearch = new JPanel();

        JButton searchBtn = new JButton("Search");
        JButton resetBtn = new JButton("Reset");
        JButton editBtn = new JButton("Edit Selected");
        JButton deleteBtn = new JButton("Delete Selected");

        searchBtn.setToolTipText("Search products");
        resetBtn.setToolTipText("Clear filters and reload");
        editBtn.setToolTipText("Edit selected product");
        deleteBtn.setToolTipText("Delete selected products");

        // 🔐 Role-based restriction
        if (role == null || !role.equalsIgnoreCase("admin")) {
            editBtn.setEnabled(false);
            deleteBtn.setEnabled(false);
        }

        bottomSearch.add(searchBtn);
        bottomSearch.add(resetBtn);
        bottomSearch.add(editBtn);
        bottomSearch.add(deleteBtn);

        searchPanel.add(topSearch);
        searchPanel.add(bottomSearch);

        mainPanel.add(searchPanel, BorderLayout.BEFORE_FIRST_LINE);

        // ================= TABLE =================
        model = new DefaultTableModel(
                new String[]{"ID", "Name", "Category", "Price", "Quantity", "Description"}, 0
        ) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(model);
        table.setRowHeight(25);
        table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 13));

        mainPanel.add(new JScrollPane(table), BorderLayout.CENTER);

        // ================= PAGINATION PANEL =================
        JPanel paginationPanel = new JPanel();

        JButton prevBtn = new JButton("Previous");
        JButton nextBtn = new JButton("Next");

        pageLabel = new JLabel();
        resultLabel = new JLabel();

        paginationPanel.add(prevBtn);
        paginationPanel.add(nextBtn);
        paginationPanel.add(pageLabel);
        paginationPanel.add(resultLabel);

        mainPanel.add(paginationPanel, BorderLayout.SOUTH);

        // ================= ACTIONS =================
        searchBtn.addActionListener(e -> {
            currentPage = 1;
            loadProducts();
        });

        resetBtn.addActionListener(e -> resetFilters());

        editBtn.addActionListener(e -> editProduct());

        deleteBtn.addActionListener(e -> deleteProduct());

        prevBtn.addActionListener(e -> {
            if (currentPage > 1) {
                currentPage--;
                loadProducts();
            }
        });

        nextBtn.addActionListener(e -> {
            if (currentPage * pageSize < totalRecords) {
                currentPage++;
                loadProducts();
            }
        });

        loadProducts();
    }

    private void loadProducts() {
        try {

            model.setRowCount(0);

            String keyword = searchField.getText();
            String category = categoryBox.getSelectedItem().toString();
            Double minPrice = minPriceField.getText().isEmpty() ? null : Double.parseDouble(minPriceField.getText());
            Double maxPrice = maxPriceField.getText().isEmpty() ? null : Double.parseDouble(maxPriceField.getText());
            Boolean inStock = inStockCheck.isSelected();

            int offset = (currentPage - 1) * pageSize;

            List<Product> products = productDAO.searchProducts(
                    keyword,
                    category,
                    minPrice,
                    maxPrice,
                    inStock,
                    pageSize,
                    offset
            );

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

            totalRecords = productDAO.getTotalProductCount();

            pageLabel.setText("Page: " + currentPage);
            resultLabel.setText("Total Records: " + totalRecords);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading products: " + e.getMessage());
        }
    }

    private void editProduct() {

        int selectedRow = table.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Select one product to edit.");
            return;
        }

        int id = (int) model.getValueAt(selectedRow, 0);
        String name = (String) model.getValueAt(selectedRow, 1);
        String category = (String) model.getValueAt(selectedRow, 2);
        double price = (double) model.getValueAt(selectedRow, 3);
        int quantity = (int) model.getValueAt(selectedRow, 4);
        String description = (String) model.getValueAt(selectedRow, 5);

        Product product = new Product(id, name, category, price, quantity, description);
        new AddProductForm(product).setVisible(true);
    }

    private void deleteProduct() {

        try {
            int[] selectedRows = table.getSelectedRows();

            if (selectedRows.length == 0) {
                JOptionPane.showMessageDialog(this, "Select at least one product.");
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Delete selected product(s)?",
                    "Confirm",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {

                for (int i = selectedRows.length - 1; i >= 0; i--) {
                    int row = selectedRows[i];
                    int id = (int) model.getValueAt(row, 0);
                    productDAO.deleteProduct(id);
                }

                loadProducts();
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error deleting products: " + e.getMessage());
        }
    }

    private void resetFilters() {

        searchField.setText("");
        categoryBox.setSelectedIndex(0);
        minPriceField.setText("");
        maxPriceField.setText("");
        inStockCheck.setSelected(false);

        currentPage = 1;
        loadProducts();
    }
}