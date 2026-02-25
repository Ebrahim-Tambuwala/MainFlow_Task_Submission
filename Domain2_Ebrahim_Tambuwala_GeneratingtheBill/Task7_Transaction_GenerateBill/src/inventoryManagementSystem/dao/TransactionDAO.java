package inventoryManagementSystem.dao;

import inventoryManagementSystem.DatabaseConnection;
import inventoryManagementSystem.model.Transaction;
import inventoryManagementSystem.model.TransactionItem;

import java.sql.*;
import java.util.List;

public class TransactionDAO {

    public int createTransaction(Transaction transaction, List<TransactionItem> items) throws Exception {

        Connection con = null;

        try {
            con = DatabaseConnection.getConnection();
            con.setAutoCommit(false);   // 🔥 Start DB transaction

            // 1️⃣ Insert into transactions table
            String transactionSql = "INSERT INTO transactions (buyer_id, payment_method) VALUES (?, ?)";

            PreparedStatement transactionStmt = con.prepareStatement(
                    transactionSql,
                    Statement.RETURN_GENERATED_KEYS
            );

            transactionStmt.setInt(1, transaction.getBuyerId());
            transactionStmt.setString(2, transaction.getPaymentMethod());

            transactionStmt.executeUpdate();

            // Get Generated Transaction ID
            ResultSet generatedKeys = transactionStmt.getGeneratedKeys();

            int transactionId;
            if (generatedKeys.next()) {
                transactionId = generatedKeys.getInt(1);
            } else {
                throw new Exception("Failed to create transaction.");
            }

            // 2️⃣ Insert transaction items
            String itemSql = "INSERT INTO transaction_items (transaction_id, product_id, quantity, unit_price) VALUES (?, ?, ?, ?)";

            PreparedStatement itemStmt = con.prepareStatement(itemSql);

            for (TransactionItem item : items) {

                itemStmt.setInt(1, transactionId);
                itemStmt.setInt(2, item.getProductId());
                itemStmt.setInt(3, item.getQuantity());
                itemStmt.setDouble(4, item.getUnitPrice());

                itemStmt.executeUpdate();

                // 3️⃣ Reduce Product Stock
                reduceProductStock(con, item.getProductId(), item.getQuantity());
            }

            con.commit();   // 🔥 Commit if everything successful
            return transactionId;

        } catch (Exception e) {

            if (con != null) {
                con.rollback();   // 🔥 Rollback if anything fails
            }

            throw e;

        } finally {

            if (con != null) {
                con.setAutoCommit(true);
                con.close();
            }
        }
    }

    // ================= REDUCE PRODUCT STOCK =================
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
}