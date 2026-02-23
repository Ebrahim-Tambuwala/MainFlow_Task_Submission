package inventoryManagementSystem.dao;

import inventoryManagementSystem.DatabaseConnection;
import inventoryManagementSystem.model.Product;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {

    public void addProduct(Product product) throws Exception {

        Connection con = DatabaseConnection.getConnection();
        String sql = "INSERT INTO products (name, category, price, quantity, description) VALUES (?, ?, ?, ?, ?)";

        PreparedStatement pst = con.prepareStatement(sql);
        pst.setString(1, product.getName());
        pst.setString(2, product.getCategory());
        pst.setDouble(3, product.getPrice());
        pst.setInt(4, product.getQuantity());
        pst.setString(5, product.getDescription());

        pst.executeUpdate();
    }

    public void deleteProduct(int id) throws Exception {
        Connection con = DatabaseConnection.getConnection();
        String sql = "DELETE FROM products WHERE id=?";
        PreparedStatement pst = con.prepareStatement(sql);
        pst.setInt(1, id);
        pst.executeUpdate();
    }

    public List<Product> getAllProducts() throws Exception {

        List<Product> list = new ArrayList<>();
        Connection con = DatabaseConnection.getConnection();
        String sql = "SELECT * FROM products";

        PreparedStatement pst = con.prepareStatement(sql);
        ResultSet rs = pst.executeQuery();

        while (rs.next()) {
            Product p = new Product(
                    rs.getString("name"),
                    rs.getString("category"),
                    rs.getDouble("price"),
                    rs.getInt("quantity"),
                    rs.getString("description")
            );

            // Set ID manually
            java.lang.reflect.Field field = p.getClass().getDeclaredField("id");
            field.setAccessible(true);
            field.set(p, rs.getInt("id"));

            list.add(p);
        }
        return list;
    }
}