package Util;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBUtil {
    private static Connection connection;

    public static Connection getConnection() throws IOException, SQLException {
        if (connection == null || connection.isClosed()) {
            Properties props = new Properties();
            try (FileInputStream fis = new FileInputStream("C:\\Users\\lucky\\OneDrive\\Documents\\NetBeansProjects\\project\\data\\database.properties")) {
                props.load(fis);
            }

            String db = props.getProperty("db");
            String name = props.getProperty("name");
            String host = props.getProperty("host");
            String pass = props.getProperty("pass");
            String port = props.getProperty("port");
            String user = props.getProperty("user");

            String url = "jdbc:" + db + "://" + host + ":" + port + "/" + name;

            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                System.err.println("MySQL JDBC Driver not found.");
                e.printStackTrace();
                throw new SQLException("MySQL JDBC Driver not found.", e);
            }

            try {
                connection = DriverManager.getConnection(url, user, pass);
            } catch (SQLException e) {
                System.err.println("Connection failed: " + e.getMessage());
                throw e; 
            }
        }
        return connection;
    }
}
