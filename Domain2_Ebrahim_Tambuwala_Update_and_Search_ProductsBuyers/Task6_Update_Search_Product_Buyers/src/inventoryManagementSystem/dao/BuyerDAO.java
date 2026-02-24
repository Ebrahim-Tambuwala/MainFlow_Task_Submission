package inventoryManagementSystem.dao;

import inventoryManagementSystem.DatabaseConnection;
import inventoryManagementSystem.model.Buyer;

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
    }

    // ================= DELETE BUYER =================
    public void deleteBuyer(int id) throws Exception {

        Connection con = DatabaseConnection.getConnection();
        String sql = "DELETE FROM buyers WHERE id=?";

        PreparedStatement pst = con.prepareStatement(sql);
        pst.setInt(1, id);

        pst.executeUpdate();
    }

    // ================= COUNT TOTAL BUYERS (For Pagination) =================
    public int getTotalBuyerCount() throws Exception {

        Connection con = DatabaseConnection.getConnection();
        String sql = "SELECT COUNT(*) FROM buyers";

        PreparedStatement pst = con.prepareStatement(sql);
        ResultSet rs = pst.executeQuery();

        if (rs.next()) {
            return rs.getInt(1);
        }

        return 0;
    }

    // ================= ADVANCED SEARCH WITH PAGINATION =================
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

        // Keyword Search (Case Insensitive)
        if (keyword != null && !keyword.trim().isEmpty()) {

            sql.append(" AND (LOWER(name) LIKE ? OR LOWER(email) LIKE ? OR LOWER(phone) LIKE ? OR CAST(id AS CHAR) LIKE ?)");
            String value = "%" + keyword.toLowerCase() + "%";

            parameters.add(value);
            parameters.add(value);
            parameters.add(value);
            parameters.add(value);
        }

        // Address Filter
        if (addressFilter != null && !addressFilter.equals("All")) {
            sql.append(" AND LOWER(address) LIKE ?");
            parameters.add("%" + addressFilter.toLowerCase() + "%");
        }

        // Pagination
        sql.append(" LIMIT ? OFFSET ?");
        parameters.add(limit);
        parameters.add(offset);

        PreparedStatement pst = con.prepareStatement(sql.toString());

        for (int i = 0; i < parameters.size(); i++) {
            pst.setObject(i + 1, parameters.get(i));
        }

        ResultSet rs = pst.executeQuery();

        while (rs.next()) {

            Buyer b = new Buyer(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("phone"),
                    rs.getString("address")
            );

            list.add(b);
        }

        return list;
    }
}