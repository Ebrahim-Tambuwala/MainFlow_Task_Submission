package inventoryManagementSystem.dao;

import inventoryManagementSystem.DatabaseConnection;
import inventoryManagementSystem.model.Buyer;
import inventoryManagementSystem.model.TransactionView;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BuyerDAO {

    // ================= ADD BUYER =================
    public void addBuyer(Buyer buyer) throws Exception {
        Connection con = DatabaseConnection.getConnection();
        String sql = "INSERT INTO buyers (name, email, phone, address) VALUES (?, ?, ?, ?)";
        PreparedStatement pst = con.prepareStatement(sql);

        pst.setString(1, buyer.getName());
        pst.setString(2, buyer.getEmail());
        pst.setString(3, buyer.getPhone());
        pst.setString(4, buyer.getAddress());

        pst.executeUpdate();
        pst.close();
        con.close();
    }

    // ================= UPDATE BUYER =================
    public void updateBuyer(Buyer buyer) throws Exception {
        Connection con = DatabaseConnection.getConnection();
        String sql = "UPDATE buyers SET name=?, email=?, phone=?, address=? WHERE id=?";
        PreparedStatement pst = con.prepareStatement(sql);

        pst.setString(1, buyer.getName());
        pst.setString(2, buyer.getEmail());
        pst.setString(3, buyer.getPhone());
        pst.setString(4, buyer.getAddress());
        pst.setInt(5, buyer.getId());

        pst.executeUpdate();
        pst.close();
        con.close();
    }

    // ================= DELETE BUYER =================
    public void deleteBuyer(int id) throws Exception {
        Connection con = DatabaseConnection.getConnection();
        String sql = "DELETE FROM buyers WHERE id=?";
        PreparedStatement pst = con.prepareStatement(sql);

        pst.setInt(1, id);
        pst.executeUpdate();

        pst.close();
        con.close();
    }

    // ================= COUNT TOTAL BUYERS =================
    public int getTotalBuyerCount() throws Exception {
        Connection con = DatabaseConnection.getConnection();
        String sql = "SELECT COUNT(*) FROM buyers";
        PreparedStatement pst = con.prepareStatement(sql);
        ResultSet rs = pst.executeQuery();

        int count = 0;
        if (rs.next()) {
            count = rs.getInt(1);
        }

        rs.close();
        pst.close();
        con.close();

        return count;
    }

    // ================= SEARCH BUYERS WITH PAGINATION =================
    public List<Buyer> searchBuyers(
            String keyword,
            String addressFilter,
            int limit,
            int offset
    ) throws Exception {

        List<Buyer> list = new ArrayList<>();
        Connection con = DatabaseConnection.getConnection();

        StringBuilder sql = new StringBuilder("SELECT * FROM buyers WHERE 1=1");
        List<Object> parameters = new ArrayList<>();

        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append(" AND (LOWER(name) LIKE ? OR LOWER(email) LIKE ? OR LOWER(phone) LIKE ? OR CAST(id AS CHAR) LIKE ?)");
            String value = "%" + keyword.toLowerCase() + "%";

            parameters.add(value);
            parameters.add(value);
            parameters.add(value);
            parameters.add(value);
        }

        if (addressFilter != null && !addressFilter.trim().isEmpty()) {
            sql.append(" AND LOWER(address) LIKE ?");
            parameters.add("%" + addressFilter.toLowerCase() + "%");
        }

        sql.append(" LIMIT ? OFFSET ?");
        parameters.add(limit);
        parameters.add(offset);

        PreparedStatement pst = con.prepareStatement(sql.toString());

        for (int i = 0; i < parameters.size(); i++) {
            pst.setObject(i + 1, parameters.get(i));
        }

        ResultSet rs = pst.executeQuery();

        while (rs.next()) {
            list.add(new Buyer(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("phone"),
                    rs.getString("address")
            ));
        }

        rs.close();
        pst.close();
        con.close();

        return list;
    }

    // ================= GET BUYER TRANSACTION HISTORY =================
    public List<TransactionView> getTransactionsByBuyerId(int buyerId) throws Exception {

        List<TransactionView> list = new ArrayList<>();
        Connection con = DatabaseConnection.getConnection();

        String sql =
                "SELECT t.transaction_id, b.name, t.purchase_date, t.payment_method, t.order_status, " +
                "SUM(ti.quantity * ti.unit_price) AS total_amount " +
                "FROM transactions t " +
                "JOIN buyers b ON t.buyer_id = b.id " +
                "JOIN transaction_items ti ON t.transaction_id = ti.transaction_id " +
                "WHERE t.buyer_id = ? " +
                "GROUP BY t.transaction_id";

        PreparedStatement pst = con.prepareStatement(sql);
        pst.setInt(1, buyerId);

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

    // ================= GET TOTAL AMOUNT SPENT =================
    public double getTotalAmountSpent(int buyerId) throws Exception {

        Connection con = DatabaseConnection.getConnection();

        String sql =
                "SELECT IFNULL(SUM(ti.quantity * ti.unit_price),0) AS total_spent " +
                "FROM transactions t " +
                "JOIN transaction_items ti ON t.transaction_id = ti.transaction_id " +
                "WHERE t.buyer_id = ?";

        PreparedStatement pst = con.prepareStatement(sql);
        pst.setInt(1, buyerId);

        ResultSet rs = pst.executeQuery();

        double total = 0;
        if (rs.next()) {
            total = rs.getDouble("total_spent");
        }

        rs.close();
        pst.close();
        con.close();

        return total;
    }

    // ================= GET PREFERRED PAYMENT METHOD =================
    public String getPreferredPaymentMethod(int buyerId) throws Exception {

        Connection con = DatabaseConnection.getConnection();

        String sql =
                "SELECT payment_method, COUNT(*) AS cnt " +
                "FROM transactions " +
                "WHERE buyer_id = ? " +
                "GROUP BY payment_method " +
                "ORDER BY cnt DESC LIMIT 1";

        PreparedStatement pst = con.prepareStatement(sql);
        pst.setInt(1, buyerId);

        ResultSet rs = pst.executeQuery();

        String method = "N/A";
        if (rs.next()) {
            method = rs.getString("payment_method");
        }

        rs.close();
        pst.close();
        con.close();

        return method;
    }
}