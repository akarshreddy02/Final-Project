package servlets;

import DAO.SubscribeServletDAO;
import Util.EmailUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/sendNotifications")
public class NotificationServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        SubscribeServletDAO subscribeServletDAO = new SubscribeServletDAO();
        try {
            List<String> subscribedEmails = subscribeServletDAO.getAllSubscribedEmails();
            for (String email : subscribedEmails) {
                EmailUtil.sendEmail(email, "Notification", "This is a notification email.");
            }
            response.getWriter().write("Notifications sent successfully.");
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            response.getWriter().write("Failed to send notifications.");
        }
    }
}
