package inventoryManagementSystem.dao;

import inventoryManagementSystem.DatabaseConnection;
import inventoryManagementSystem.model.Bill;
import inventoryManagementSystem.model.BillItem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BillDAO {

    public Bill getBillByTransactionId(int transactionId) throws Exception {

        Connection con = DatabaseConnection.getConnection();

        // ================= 1️⃣ FETCH TRANSACTION + BUYER =================
        String transactionSql =
                "SELECT t.transaction_id, t.payment_method, t.purchase_date, " +
                "b.name, b.email, b.phone, b.address " +
                "FROM transactions t " +
                "JOIN buyers b ON t.buyer_id = b.id " +
                "WHERE t.transaction_id = ?";

        PreparedStatement transactionStmt = con.prepareStatement(transactionSql);
        transactionStmt.setInt(1, transactionId);

        ResultSet transactionRs = transactionStmt.executeQuery();

        if (!transactionRs.next()) {
            throw new Exception("Transaction not found.");
        }

        String buyerName = transactionRs.getString("name");
        String buyerEmail = transactionRs.getString("email");
        String buyerPhone = transactionRs.getString("phone");
        String buyerAddress = transactionRs.getString("address");
        String paymentMethod = transactionRs.getString("payment_method");
        Timestamp purchaseDate = transactionRs.getTimestamp("purchase_date");

        // ================= 2️⃣ FETCH TRANSACTION ITEMS =================
        String itemsSql =
                "SELECT p.name, ti.quantity, ti.unit_price " +
                "FROM transaction_items ti " +
                "JOIN products p ON ti.product_id = p.id " +
                "WHERE ti.transaction_id = ?";

        PreparedStatement itemsStmt = con.prepareStatement(itemsSql);
        itemsStmt.setInt(1, transactionId);

        ResultSet itemsRs = itemsStmt.executeQuery();

        List<BillItem> items = new ArrayList<>();

        while (itemsRs.next()) {

            String productName = itemsRs.getString("name");
            int quantity = itemsRs.getInt("quantity");
            double unitPrice = itemsRs.getDouble("unit_price");

            items.add(new BillItem(productName, quantity, unitPrice));
        }

        // ================= 3️⃣ BUILD BILL OBJECT =================
        return new Bill(
                transactionId,
                buyerName,
                buyerEmail,
                buyerPhone,
                buyerAddress,
                paymentMethod,
                purchaseDate,
                items
        );
    }
}