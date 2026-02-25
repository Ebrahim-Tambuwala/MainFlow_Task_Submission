package inventoryManagementSystem.ui;

import inventoryManagementSystem.dao.BuyerDAO;
import inventoryManagementSystem.model.Buyer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;

import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;

public class ViewBuyersForm extends JFrame {

    private String role;

    private JTable table;
    private DefaultTableModel model;
    private BuyerDAO buyerDAO;

    private JTextField searchField;
    private JTextField addressField;

    private int currentPage = 1;
    private final int pageSize = 10;
    private int totalRecords = 0;

    private JLabel pageLabel;
    private JLabel resultLabel;

    public ViewBuyersForm(String role) {

        this.role = role;
        buyerDAO = new BuyerDAO();

        setTitle("Buyer Management");
        setSize(1000, 600);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        add(mainPanel);

        JLabel title = new JLabel("Buyer Management", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 22));
        mainPanel.add(title, BorderLayout.NORTH);

        // ================= SEARCH PANEL =================
        JPanel searchPanel = new JPanel(new GridLayout(2, 1));

        JPanel topSearch = new JPanel();

        searchField = new JTextField(15);
        addressField = new JTextField(10);

        topSearch.add(new JLabel("Keyword:"));
        topSearch.add(searchField);
        topSearch.add(new JLabel("Address Filter:"));
        topSearch.add(addressField);

        JPanel bottomSearch = new JPanel();

        JButton searchBtn = new JButton("Search");
        JButton resetBtn = new JButton("Reset");
        JButton editBtn = new JButton("Edit Selected");
        JButton deleteBtn = new JButton("Delete Selected");

        searchBtn.setToolTipText("Search buyers");
        resetBtn.setToolTipText("Clear filters");
        editBtn.setToolTipText("Edit selected buyer");
        deleteBtn.setToolTipText("Delete selected buyers");

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
                new String[]{"ID", "Name", "Email", "Phone", "Address"}, 0
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
            loadBuyers();
        });

        resetBtn.addActionListener(e -> resetFilters());

        editBtn.addActionListener(e -> editBuyer());

        deleteBtn.addActionListener(e -> deleteBuyer());

        prevBtn.addActionListener(e -> {
            if (currentPage > 1) {
                currentPage--;
                loadBuyers();
            }
        });

        nextBtn.addActionListener(e -> {
            if (currentPage * pageSize < totalRecords) {
                currentPage++;
                loadBuyers();
            }
        });

        loadBuyers();
    }

    private void loadBuyers() {
        try {

            model.setRowCount(0);

            String keyword = searchField.getText();
            String addressFilter = addressField.getText();

            int offset = (currentPage - 1) * pageSize;

            List<Buyer> buyers = buyerDAO.searchBuyers(
                    keyword,
                    addressFilter,
                    pageSize,
                    offset
            );

            for (Buyer b : buyers) {
                model.addRow(new Object[]{
                        b.getId(),
                        b.getName(),
                        b.getEmail(),
                        b.getPhone(),
                        b.getAddress()
                });
            }

            totalRecords = buyerDAO.getTotalBuyerCount();

            pageLabel.setText("Page: " + currentPage);
            resultLabel.setText("Total Records: " + totalRecords);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading buyers: " + e.getMessage());
        }
    }

    private void editBuyer() {

        int selectedRow = table.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Select one buyer to edit.");
            return;
        }

        int id = (int) model.getValueAt(selectedRow, 0);
        String name = (String) model.getValueAt(selectedRow, 1);
        String email = (String) model.getValueAt(selectedRow, 2);
        String phone = (String) model.getValueAt(selectedRow, 3);
        String address = (String) model.getValueAt(selectedRow, 4);

        Buyer buyer = new Buyer(id, name, email, phone, address);
        new AddBuyerForm(buyer).setVisible(true);
    }

    private void deleteBuyer() {

        try {
            int[] selectedRows = table.getSelectedRows();

            if (selectedRows.length == 0) {
                JOptionPane.showMessageDialog(this, "Select at least one buyer.");
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Delete selected buyer(s)?",
                    "Confirm",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {

                for (int i = selectedRows.length - 1; i >= 0; i--) {
                    int row = selectedRows[i];
                    int id = (int) model.getValueAt(row, 0);
                    buyerDAO.deleteBuyer(id);
                }

                loadBuyers();
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error deleting buyers: " + e.getMessage());
        }
    }

    private void resetFilters() {
        searchField.setText("");
        addressField.setText("");
        currentPage = 1;
        loadBuyers();
    }
}