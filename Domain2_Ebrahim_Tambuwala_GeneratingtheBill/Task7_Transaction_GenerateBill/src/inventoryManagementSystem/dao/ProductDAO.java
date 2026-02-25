package inventoryManagementSystem.dao;

import inventoryManagementSystem.DatabaseConnection;
import inventoryManagementSystem.model.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {

    // ================= ADD PRODUCT =================
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

    // ================= UPDATE PRODUCT =================
    public void updateProduct(Product product) throws Exception {

        Connection con = DatabaseConnection.getConnection();

        String sql = "UPDATE products SET name=?, category=?, price=?, quantity=?, description=? WHERE id=?";

        PreparedStatement pst = con.prepareStatement(sql);

        pst.setString(1, product.getName());
        pst.setString(2, product.getCategory());
        pst.setDouble(3, product.getPrice());
        pst.setInt(4, product.getQuantity());
        pst.setString(5, product.getDescription());
        pst.setInt(6, product.getId());

        pst.executeUpdate();
    }

    // ================= DELETE PRODUCT =================
    public void deleteProduct(int id) throws Exception {

        Connection con = DatabaseConnection.getConnection();
        String sql = "DELETE FROM products WHERE id=?";

        PreparedStatement pst = con.prepareStatement(sql);
        pst.setInt(1, id);

        pst.executeUpdate();
    }

    // ================= GET ALL PRODUCTS =================
    public List<Product> getAllProducts(int limit, int offset) throws Exception {

        List<Product> list = new ArrayList<>();
        Connection con = DatabaseConnection.getConnection();

        String sql = "SELECT * FROM products LIMIT ? OFFSET ?";
        PreparedStatement pst = con.prepareStatement(sql);

        pst.setInt(1, limit);
        pst.setInt(2, offset);

        ResultSet rs = pst.executeQuery();

        while (rs.next()) {

            Product p = new Product(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("category"),
                    rs.getDouble("price"),
                    rs.getInt("quantity"),
                    rs.getString("description")
            );

            list.add(p);
        }

        return list;
    }

    // ================= COUNT TOTAL PRODUCTS (For Pagination) =================
    public int getTotalProductCount() throws Exception {

        Connection con = DatabaseConnection.getConnection();
        String sql = "SELECT COUNT(*) FROM products";

        PreparedStatement pst = con.prepareStatement(sql);
        ResultSet rs = pst.executeQuery();

        if (rs.next()) {
            return rs.getInt(1);
        }

        return 0;
    }

    // ================= ADVANCED SEARCH WITH FILTERS + PAGINATION =================
    public List<Product> searchProducts(
            String keyword,
            String category,
            Double minPrice,
            Double maxPrice,
            Boolean inStockOnly,
            int limit,
            int offset
    ) throws Exception {

        List<Product> list = new ArrayList<>();
        Connection con = DatabaseConnection.getConnection();

        StringBuilder sql = new StringBuilder("SELECT * FROM products WHERE 1=1");

        List<Object> parameters = new ArrayList<>();

        // Keyword Search (Case Insensitive)
        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append(" AND (LOWER(name) LIKE ? OR LOWER(category) LIKE ? OR CAST(id AS CHAR) LIKE ?)");
            String searchValue = "%" + keyword.toLowerCase() + "%";
            parameters.add(searchValue);
            parameters.add(searchValue);
            parameters.add(searchValue);
        }

        // Category Filter
        if (category != null && !category.equals("All")) {
            sql.append(" AND category = ?");
            parameters.add(category);
        }

        // Min Price
        if (minPrice != null) {
            sql.append(" AND price >= ?");
            parameters.add(minPrice);
        }

        // Max Price
        if (maxPrice != null) {
            sql.append(" AND price <= ?");
            parameters.add(maxPrice);
        }

        // In-Stock Filter
        if (inStockOnly != null && inStockOnly) {
            sql.append(" AND quantity > 0");
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

            Product p = new Product(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("category"),
                    rs.getDouble("price"),
                    rs.getInt("quantity"),
                    rs.getString("description")
            );

            list.add(p);
        }

        return list;
    }
}