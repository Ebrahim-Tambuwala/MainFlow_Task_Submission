package inventoryManagementSystem.ui;

import inventoryManagementSystem.dao.ProductDAO;
import inventoryManagementSystem.model.Product;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;

public class ViewProductsForm extends JFrame {

    private String role;

    private JTable table;
    private DefaultTableModel model;
    private ProductDAO productDAO;

    private JTextField searchField, minPriceField, maxPriceField;
    private JComboBox<String> categoryBox;
    private JCheckBox inStockCheck;

    private int currentPage = 1;
    private final int pageSize = 10;
    private int totalRecords = 0;

    private JLabel pageLabel;
    private JLabel resultLabel;

    private JButton prevBtn;
    private JButton nextBtn;

    public ViewProductsForm(String role) {

        this.role = role;
        productDAO = new ProductDAO();

        setTitle("Product Management");
        setSize(1150, 650);
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
        categoryBox = new JComboBox<>(new String[]{"All", "Electronics", "Clothing", "Food", "Accessories", "Storage"});
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
                new String[]{"Sr No", "ID", "Name", "Category", "Price", "Quantity", "Description"}, 0
        ) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(model);
        table.setRowHeight(25);
        table.setAutoCreateRowSorter(true); // Enable sorting

        // ===== Stock Color Indicator =====
        table.getColumnModel().getColumn(5).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus,
                                                           int row, int column) {

                Component c = super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column);

                int quantity = Integer.parseInt(value.toString());

                if (!isSelected) {
                    if (quantity == 0) {
                        c.setBackground(Color.RED);
                        c.setForeground(Color.WHITE);
                    } else if (quantity <= 10) {
                        c.setBackground(Color.YELLOW);
                        c.setForeground(Color.BLACK);
                    } else {
                        c.setBackground(Color.GREEN);
                        c.setForeground(Color.BLACK);
                    }
                }

                return c;
            }
        });

        mainPanel.add(new JScrollPane(table), BorderLayout.CENTER);

        // ===== Double Click For Detailed View =====
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    showProductDetails();
                }
            }
        });

        // ================= PAGINATION =================
        JPanel paginationPanel = new JPanel();

        prevBtn = new JButton("Previous");
        nextBtn = new JButton("Next");

        pageLabel = new JLabel();
        resultLabel = new JLabel();

        paginationPanel.add(prevBtn);
        paginationPanel.add(nextBtn);
        paginationPanel.add(pageLabel);
        paginationPanel.add(resultLabel);

        mainPanel.add(paginationPanel, BorderLayout.SOUTH);

        // ===== ACTIONS =====
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

    // ================= LOAD PRODUCTS =================
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

            int srNoStart = offset + 1;

            for (int i = 0; i < products.size(); i++) {
                Product p = products.get(i);
                model.addRow(new Object[]{
                        srNoStart + i,
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

            boolean hasNextPage = currentPage * pageSize < totalRecords;
            resultLabel.setText("Total Records: " + totalRecords);

            prevBtn.setEnabled(currentPage > 1);
            nextBtn.setEnabled(hasNextPage);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading products: " + e.getMessage());
        }
    }

    // ================= PRODUCT DETAILS =================
    private void showProductDetails() {

        int viewRow = table.getSelectedRow();
        if (viewRow == -1) return;

        int row = table.convertRowIndexToModel(viewRow);

        String name = model.getValueAt(row, 2).toString();
        String category = model.getValueAt(row, 3).toString();
        String price = model.getValueAt(row, 4).toString();
        String quantity = model.getValueAt(row, 5).toString();
        String description = model.getValueAt(row, 6).toString();

        JOptionPane.showMessageDialog(this,
                "Name: " + name +
                        "\nCategory: " + category +
                        "\nPrice: ₹" + price +
                        "\nStock: " + quantity +
                        "\n\nDescription:\n" + description,
                "Product Details",
                JOptionPane.INFORMATION_MESSAGE);
    }

    // ================= EDIT PRODUCT =================
    private void editProduct() {

        int viewRow = table.getSelectedRow();
        if (viewRow == -1) {
            JOptionPane.showMessageDialog(this, "Select one product to edit.");
            return;
        }

        int row = table.convertRowIndexToModel(viewRow);

        int id = (int) model.getValueAt(row, 1);
        String name = (String) model.getValueAt(row, 2);
        String category = (String) model.getValueAt(row, 3);
        double price = (double) model.getValueAt(row, 4);
        int quantity = (int) model.getValueAt(row, 5);
        String description = (String) model.getValueAt(row, 6);

        Product product = new Product(id, name, category, price, quantity, description);
        new AddProductForm(product).setVisible(true);
    }

    // ================= DELETE PRODUCT =================
    private void deleteProduct() {

        int[] viewRows = table.getSelectedRows();
        if (viewRows.length == 0) {
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
            try {
                for (int i = viewRows.length - 1; i >= 0; i--) {
                    int modelRow = table.convertRowIndexToModel(viewRows[i]);
                    int id = (int) model.getValueAt(modelRow, 1);
                    productDAO.deleteProduct(id);
                }
                loadProducts();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error deleting products: " + e.getMessage());
            }
        }
    }

    // ================= RESET FILTERS =================
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