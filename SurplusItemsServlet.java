package servlets;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import DAO.SurplusFoodItemDAO;
import DAO.CharityItemDAO;
import Model.FoodItem;

/**
 * Servlet implementation class SurplusItemsServlet
 */

public class SurplusItemsServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private SurplusFoodItemDAO surplusFoodItemDAO;
    private CharityItemDAO charityItemDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        surplusFoodItemDAO = new SurplusFoodItemDAO();
        charityItemDAO = new CharityItemDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        try {
            if (action == null) {
                action = "list";
            }

            switch (action) {
                case "edit":
                    showEditForm(request, response);
                    break;
                case "update":
                    updateSurplusFoodItem(request, response);
                    break;
                case "delete":
                    deleteSurplusFoodItem(request, response);
                    break;
                case "sendToConsumer":
                    sendToConsumer(request, response);
                    break;
                case "list":
                default:
                    listSurplusItems(request, response);
                    break;
            }
        } catch (SQLException ex) {
            throw new ServletException(ex);
        }
    }

    private void listSurplusItems(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        List<FoodItem> listSurplusFoodItems = surplusFoodItemDAO.selectAllSurplusFoodItems();
        request.setAttribute("listSurplusFoodItems", listSurplusFoodItems);
        RequestDispatcher dispatcher = request.getRequestDispatcher("surplusitems.jsp");
        dispatcher.forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        FoodItem existingFoodItem = surplusFoodItemDAO.selectSurplusFoodItem(id);
        RequestDispatcher dispatcher = request.getRequestDispatcher("surplusitems.jsp");
        request.setAttribute("foodItem", existingFoodItem);
        dispatcher.forward(request, response);
    }

    private void updateSurplusFoodItem(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        int id = Integer.parseInt(request.getParameter("id"));
        String retailerName = request.getParameter("retailer_name");
        String itemName = request.getParameter("item_name");
        int quantity = parseInt(request.getParameter("quantity"));
        double price = parseDouble(request.getParameter("price"));
        String expirationDate = request.getParameter("expiration_date");

        FoodItem foodItem = new FoodItem(id, retailerName, itemName, quantity, price, expirationDate);
        surplusFoodItemDAO.updateSurplusFoodItem(foodItem);

        response.sendRedirect("SurplusItemsServlet?action=list");
    }

    private void deleteSurplusFoodItem(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        int id = Integer.parseInt(request.getParameter("id"));
        surplusFoodItemDAO.deleteSurplusFoodItem(id);
        response.sendRedirect("SurplusItemsServlet?action=list");
    }

    private void sendToConsumer(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        int id = Integer.parseInt(request.getParameter("id"));
        surplusFoodItemDAO.sendToConsumer(id);
        response.sendRedirect("SurplusItemsServlet?action=list");
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