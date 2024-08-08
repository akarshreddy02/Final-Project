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

import DAO.CharityItemDAO;
import DAO.SurplusFoodItemDAO;
import Model.FoodItem;

/**
 * Servlet implementation class CharitableItemsServlet
 */

public class CharitableItemsServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private CharityItemDAO charityItemDAO;
    private SurplusFoodItemDAO surplusFoodItemDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        charityItemDAO = new CharityItemDAO();
        surplusFoodItemDAO = new SurplusFoodItemDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        try {
            if (action == null) {
                action = "listCharity";
            }

            switch (action) {
                case "transfer":
                    transferSurplusToCharity(request, response);
                    break;
                case "listSurplus":
                    listSurplusItems(request, response);
                    break;
                case "listCharity":
                    listCharityAllItems(request, response);
                    break;
                case "delete":
                    deleteCharityItem(request, response);
                    break;
                case "transferToConsumer":
                    transferCharityToConsumer(request, response);
                    break;
                case "claim":
                    deleteCharityItem(request, response);
                    break;
                default:
                    listCharityAllItems(request, response);
                    break;
            }
        } catch (SQLException ex) {
            throw new ServletException(ex);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String action = request.getParameter("action");

            if ("add".equalsIgnoreCase(action)) {
                addCharityItem(request, response);
            } else {
                listCharityAllItems(request, response);
            }
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    private void listSurplusItems(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        List<FoodItem> surplusItems = surplusFoodItemDAO.selectAllSurplusFoodItems();
        request.setAttribute("surplusItems", surplusItems);
        RequestDispatcher dispatcher = request.getRequestDispatcher("surplusitems.jsp");
        dispatcher.forward(request, response);
    }

    private void listCharityAllItems(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        List<FoodItem> charityItems = charityItemDAO.selectAllCharityItems();
        request.setAttribute("charityItems", charityItems);
        RequestDispatcher dispatcher = request.getRequestDispatcher("charitable_items.jsp");
        dispatcher.forward(request, response);
    }

    private void transferSurplusToCharity(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        int itemId = parseInt(request.getParameter("item_id"));

        FoodItem surplusItem = surplusFoodItemDAO.selectSurplusFoodItem(itemId);

        if (surplusItem != null) {
            FoodItem charityItem = new FoodItem(
                surplusItem.getId(),
                surplusItem.getRetailerName(),
                surplusItem.getItemName(),
                surplusItem.getQuantity(),
                surplusItem.getExpirationDate()
            );
            charityItemDAO.addCharityItem(charityItem);
            surplusFoodItemDAO.deleteSurplusFoodItem(itemId);
        }
        listSurplusItems(request, response);
    }

    private void addCharityItem(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        String retailerName = request.getParameter("retailer_name");
        String itemName = request.getParameter("item_name");
        int quantity = parseInt(request.getParameter("quantity"));
        String expirationDate = request.getParameter("expiration_date");

        FoodItem foodItem = new FoodItem(retailerName, itemName, quantity, 0.0, expirationDate);
        charityItemDAO.addCharityItem(foodItem);

        listCharityAllItems(request, response);
    }
    
    private void deleteCharityItem(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        int itemId = parseInt(request.getParameter("item_id"));
        charityItemDAO.deleteCharityItem(itemId);
        listCharityAllItems(request, response);
    }
    
    private void transferCharityToConsumer(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        int itemId = parseInt(request.getParameter("item_id"));
        FoodItem charityItem = charityItemDAO.selectCharityItem(itemId);
        if (charityItem != null) {
            charityItemDAO.transferToConsumer(charityItem);
            charityItemDAO.deleteCharityItem(itemId);
        }
        listCharityAllItems(request, response);
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