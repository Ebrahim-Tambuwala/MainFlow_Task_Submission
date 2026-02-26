package inventoryManagementSystem.dao;

import inventoryManagementSystem.DatabaseConnection;
import inventoryManagementSystem.model.Transaction;
import inventoryManagementSystem.model.TransactionItem;
import inventoryManagementSystem.model.TransactionView;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAO {

    // ==========================================================
    // 1️⃣ CREATE TRANSACTION (Task 7)
    // ==========================================================
    public int createTransaction(Transaction transaction, List<TransactionItem> items) throws Exception {

        Connection con = null;

        try {
            con = DatabaseConnection.getConnection();
            con.setAutoCommit(false);

            String transactionSql =
                    "INSERT INTO transactions (buyer_id, payment_method, order_status) VALUES (?, ?, ?)";

            PreparedStatement transactionStmt =
                    con.prepareStatement(transactionSql, Statement.RETURN_GENERATED_KEYS);

            transactionStmt.setInt(1, transaction.getBuyerId());
            transactionStmt.setString(2, transaction.getPaymentMethod());
            transactionStmt.setString(3, "Completed");

            transactionStmt.executeUpdate();

            ResultSet generatedKeys = transactionStmt.getGeneratedKeys();

            int transactionId;
            if (generatedKeys.next()) {
                transactionId = generatedKeys.getInt(1);
            } else {
                throw new Exception("Failed to create transaction.");
            }

            String itemSql =
                    "INSERT INTO transaction_items (transaction_id, product_id, quantity, unit_price) VALUES (?, ?, ?, ?)";

            PreparedStatement itemStmt = con.prepareStatement(itemSql);

            for (TransactionItem item : items) {

                itemStmt.setInt(1, transactionId);
                itemStmt.setInt(2, item.getProductId());
                itemStmt.setInt(3, item.getQuantity());
                itemStmt.setDouble(4, item.getUnitPrice());

                itemStmt.executeUpdate();

                reduceProductStock(con, item.getProductId(), item.getQuantity());
            }

            con.commit();
            return transactionId;

        } catch (Exception e) {

            if (con != null) {
                con.rollback();
            }

            throw e;

        } finally {

            if (con != null) {
                con.setAutoCommit(true);
                con.close();
            }
        }
    }

    // ==========================================================
    // 2️⃣ REDUCE STOCK
    // ==========================================================
    private void reduceProductStock(Connection con, int productId, int quantity) throws Exception {

        String checkSql = "SELECT quantity FROM products WHERE id = ?";
        PreparedStatement checkStmt = con.prepareStatement(checkSql);
        checkStmt.setInt(1, productId);

        ResultSet rs = checkStmt.executeQuery();

        if (rs.next()) {

            int currentStock = rs.getInt("quantity");

            if (currentStock < quantity) {
                throw new Exception("Insufficient stock for product ID: " + productId);
            }

            String updateSql = "UPDATE products SET quantity = quantity - ? WHERE id = ?";
            PreparedStatement updateStmt = con.prepareStatement(updateSql);

            updateStmt.setInt(1, quantity);
            updateStmt.setInt(2, productId);

            updateStmt.executeUpdate();

        } else {
            throw new Exception("Product not found: " + productId);
        }
    }

    // ==========================================================
    // 3️⃣ GET ALL TRANSACTIONS (Task 8)
    // ==========================================================
    public List<TransactionView> getAllTransactions(int limit, int offset) throws Exception {

        List<TransactionView> list = new ArrayList<>();
        Connection con = DatabaseConnection.getConnection();

        String sql =
                "SELECT t.transaction_id, b.name, t.purchase_date, t.payment_method, t.order_status, " +
                "SUM(ti.quantity * ti.unit_price) AS total_amount " +
                "FROM transactions t " +
                "JOIN buyers b ON t.buyer_id = b.id " +
                "JOIN transaction_items ti ON t.transaction_id = ti.transaction_id " +
                "GROUP BY t.transaction_id " +
                "ORDER BY t.purchase_date DESC " +
                "LIMIT ? OFFSET ?";

        PreparedStatement pst = con.prepareStatement(sql);
        pst.setInt(1, limit);
        pst.setInt(2, offset);

        ResultSet rs = pst.executeQuery();

        while (rs.next()) {
            list.add(new TransactionView(
                    rs.getInt("transaction_id"),
                    rs.getString("name"),
                    rs.getTimestamp("purchase_date"),
                    rs.getString("payment_method"),
                    rs.getString("order_status"),
                    rs.getDouble("total_amount")
            ));
        }

        rs.close();
        pst.close();
        con.close();

        return list;
    }

    // ==========================================================
    // 4️⃣ SEARCH TRANSACTIONS
    // ==========================================================
    public List<TransactionView> searchTransactions(String keyword) throws Exception {

        List<TransactionView> list = new ArrayList<>();
        Connection con = DatabaseConnection.getConnection();

        String sql =
                "SELECT t.transaction_id, b.name, t.purchase_date, t.payment_method, t.order_status, " +
                "SUM(ti.quantity * ti.unit_price) AS total_amount " +
                "FROM transactions t " +
                "JOIN buyers b ON t.buyer_id = b.id " +
                "JOIN transaction_items ti ON t.transaction_id = ti.transaction_id " +
                "WHERE LOWER(b.name) LIKE ? OR CAST(t.transaction_id AS CHAR) LIKE ? " +
                "GROUP BY t.transaction_id";

        PreparedStatement pst = con.prepareStatement(sql);
        pst.setString(1, "%" + keyword.toLowerCase() + "%");
        pst.setString(2, "%" + keyword + "%");

        ResultSet rs = pst.executeQuery();

        while (rs.next()) {
            list.add(new TransactionView(
                    rs.getInt("transaction_id"),
                    rs.getString("name"),
                    rs.getTimestamp("purchase_date"),
                    rs.getString("payment_method"),
                    rs.getString("order_status"),
                    rs.getDouble("total_amount")
            ));
        }

        rs.close();
        pst.close();
        con.close();

        return list;
    }

    // ==========================================================
    // 5️⃣ DATE RANGE FILTER
    // ==========================================================
    public List<TransactionView> getTransactionsBetweenDates(Date from, Date to) throws Exception {

        List<TransactionView> list = new ArrayList<>();
        Connection con = DatabaseConnection.getConnection();

        String sql =
                "SELECT t.transaction_id, b.name, t.purchase_date, t.payment_method, t.order_status, " +
                "SUM(ti.quantity * ti.unit_price) AS total_amount " +
                "FROM transactions t " +
                "JOIN buyers b ON t.buyer_id = b.id " +
                "JOIN transaction_items ti ON t.transaction_id = ti.transaction_id " +
                "WHERE t.purchase_date BETWEEN ? AND ? " +
                "GROUP BY t.transaction_id";

        PreparedStatement pst = con.prepareStatement(sql);
        pst.setDate(1, from);
        pst.setDate(2, to);

        ResultSet rs = pst.executeQuery();

        while (rs.next()) {
            list.add(new TransactionView(
                    rs.getInt("transaction_id"),
                    rs.getString("name"),
                    rs.getTimestamp("purchase_date"),
                    rs.getString("payment_method"),
                    rs.getString("order_status"),
                    rs.getDouble("total_amount")
            ));
        }

        rs.close();
        pst.close();
        con.close();

        return list;
    }
}