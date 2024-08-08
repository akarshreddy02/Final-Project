package servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import Util.DBUtil;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.SQLException;


public class loginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(loginServlet.class.getName());
    
    private static final String SELECT_USERS_SQL = "SELECT * FROM users WHERE uemail = ? AND upwd = ? AND userType = ?";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String uemail = request.getParameter("username");
        String upwd = request.getParameter("password");
        String userType = request.getParameter("userType");
        HttpSession session = request.getSession();
        RequestDispatcher dispatcher;

        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            conn = DBUtil.getConnection();
            String sql = SELECT_USERS_SQL;
            pst = conn.prepareStatement(sql);
            pst.setString(1, uemail);
            pst.setString(2, upwd);
            pst.setString(3, userType);

            rs = pst.executeQuery();

            if (rs.next()) {
                session.setAttribute("name", rs.getString("uname"));
                session.setAttribute("userType", userType);

                dispatcher = request.getRequestDispatcher("home.jsp");
            } else {
                request.setAttribute("status", "failed");
                dispatcher = request.getRequestDispatcher("login.jsp");
            }
            dispatcher.forward(request, response);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Database error: {0}", e.getMessage());
            request.setAttribute("status", "error");
            dispatcher = request.getRequestDispatcher("login.jsp");
            dispatcher.forward(request, response);
        } finally {
            try {
                if (rs != null) rs.close();
                if (pst != null) pst.close();
                if (conn != null) conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}