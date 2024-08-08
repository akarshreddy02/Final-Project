package DAO;

import Util.DBUtil;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserDAO {
    private DBUtil dbConnection;

    public UserDAO() {
        dbConnection = new DBUtil();
    }

    public int registerUser(String uname, String uemail, String upwd, String umobile, String userType) throws IOException, SQLException {
        String sql = "INSERT INTO users (uname, uemail, upwd, umobile, userType) VALUES (?, ?, ?, ?, ?)";
        int rowCount = 0;

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, uname);
            pstmt.setString(2, uemail);
            pstmt.setString(3, upwd);
            pstmt.setString(4, umobile);
            pstmt.setString(5, userType);
            rowCount = pstmt.executeUpdate();
        }
        
        return rowCount;
    }
}
