package servlets;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import DAO.FoodItemDAO;
import Model.FoodItem;
import java.util.logging.Level;
import java.util.logging.Logger;


public class AddFoodItemServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private FoodItemDAO foodItemDAO;
    private Timer timer;

    @Override
    public void init() throws ServletException {
        super.init();
        foodItemDAO = new FoodItemDAO();
        timer = new Timer(true);

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    foodItemDAO.transferToSurplusItems();
                } catch (IOException | SQLException ex) {
                    Logger.getLogger(AddFoodItemServlet.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }, 0, 10000);
    }

    @Override
    public void destroy() {
        super.destroy();
        if (timer != null) {
            timer.cancel();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        if (action == null) {
            action = "list";
        }

        try {
            switch (action) {
                case "new":
                    showNewForm(request, response);
                    break;
                case "insert":
                    insertFoodItem(request, response);
                    break;
                case "update":
                    updateFoodItem(request, response);
                    break;
                case "delete":
                    deleteFoodItem(request, response);
                    break;
                case "edit":
                    showEditForm(request, response);
                    break;
                case "list":
                default:
                    listFoodItems(request, response);
                    break;
            }
        } catch (SQLException ex) {
            throw new ServletException(ex);
        }
    }

    private void listFoodItems(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        List<FoodItem> listFoodItems = foodItemDAO.selectAllFoodItems();
        request.setAttribute("listFoodItems", listFoodItems);
        RequestDispatcher dispatcher = request.getRequestDispatcher("retailer.jsp");
        dispatcher.forward(request, response);
    }

    private void showNewForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("food-item-form.jsp");
        dispatcher.forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        FoodItem existingFoodItem = foodItemDAO.selectFoodItem(id);
        RequestDispatcher dispatcher = request.getRequestDispatcher("food-item-form.jsp");
        request.setAttribute("foodItem", existingFoodItem);
        dispatcher.forward(request, response);
    }

    private void insertFoodItem(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        String retailerName = request.getParameter("retailer_name");
        String itemName = request.getParameter("item_name");
        int quantity = parseInt(request.getParameter("quantity"));
        double price = parseDouble(request.getParameter("price"));
        String expirationDate = request.getParameter("expiration_date");

        FoodItem newFoodItem = new FoodItem(retailerName, itemName, quantity, price, expirationDate);
        foodItemDAO.addFoodItem(newFoodItem);
        response.sendRedirect("AddFoodItemServlet?action=list");
    }

    private void updateFoodItem(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        int id = Integer.parseInt(request.getParameter("id"));
        String retailerName = request.getParameter("retailer_name");
        String itemName = request.getParameter("item_name");
        int quantity = parseInt(request.getParameter("quantity"));
        double price = parseDouble(request.getParameter("price"));
        String expirationDate = request.getParameter("expiration_date");

        FoodItem foodItem = new FoodItem(id, retailerName, itemName, quantity, price, expirationDate);
        foodItemDAO.updateFoodItem(foodItem);
        response.sendRedirect("AddFoodItemServlet?action=list");
    }

    private void deleteFoodItem(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        int id = Integer.parseInt(request.getParameter("id"));
        foodItemDAO.deleteFoodItem(id);
        response.sendRedirect("AddFoodItemServlet?action=list");
    }

    private int parseInt(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private double parseDouble(String value) {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }
}
