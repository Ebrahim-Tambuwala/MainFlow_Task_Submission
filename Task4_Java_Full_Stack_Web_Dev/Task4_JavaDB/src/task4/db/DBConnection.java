package task4.db;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {

    public static Connection getConnection() {
        Connection con = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/user_auth?useSSL=false&allowPublicKeyRetrieval=true",
                "root",
                "Ebbo@5405"
            );

        } catch (Exception e) {
            e.printStackTrace();
        }

        return con;
    }

    public static void main(String[] args) {
        Connection con = getConnection();

        if (con != null) {
            System.out.println("Database Connected Successfully");
        } else {
            System.out.println("Database Connection Failed");
        }
    }
}
