package inventoryManagementSystem.dao;

import inventoryManagementSystem.DatabaseConnection;
import inventoryManagementSystem.model.Buyer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BuyerDAO {

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

    public void deleteBuyer(int id) throws Exception {

        Connection con = DatabaseConnection.getConnection();
        String sql = "DELETE FROM buyers WHERE id=?";

        PreparedStatement pst = con.prepareStatement(sql);
        pst.setInt(1, id);
        pst.executeUpdate();
    }

    public List<Buyer> getAllBuyers() throws Exception {

        List<Buyer> list = new ArrayList<>();
        Connection con = DatabaseConnection.getConnection();
        String sql = "SELECT * FROM buyers";

        PreparedStatement pst = con.prepareStatement(sql);
        ResultSet rs = pst.executeQuery();

        while (rs.next()) {
            Buyer b = new Buyer(
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("phone"),
                    rs.getString("address")
            );
            b.setId(rs.getInt("id"));
            list.add(b);
        }
        return list;
    }
}