package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import Model.FoodItem;
import Util.DBUtil;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * FoodItemDAO.java
 * This DAO class provides CRUD database operations for the table food_items in the database.
 */
public class FoodItemDAO {
    private static final Logger LOGGER = Logger.getLogger(FoodItemDAO.class.getName());
    private DBUtil dbUtil;
    private SurplusFoodItemDAO surplusFoodItemDAO;

    private static final String INSERT_FOOD_ITEM_SQL = "INSERT INTO food_items (retailer_name, item_name, quantity, price, expiration_date) VALUES (?, ?, ?, ?, ?)";
    private static final String SELECT_FOOD_ITEM_BY_ID = "SELECT * FROM food_items WHERE item_id = ?";
    private static final String SELECT_ALL_FOOD_ITEMS = "SELECT * FROM food_items";
    private static final String DELETE_FOOD_ITEM_SQL = "DELETE FROM food_items WHERE item_id = ?";
    private static final String UPDATE_FOOD_ITEM_SQL = "UPDATE food_items SET retailer_name = ?, item_name = ?, quantity = ?, price = ?, expiration_date = ? WHERE item_id = ?";
    private static final String SELECT_FOOD_ITEMS_TO_TRANSFER = "SELECT * FROM food_items WHERE expiration_date <= DATE_ADD(CURDATE(), INTERVAL 1 WEEK)";

    public FoodItemDAO() {
        dbUtil = new DBUtil();
        surplusFoodItemDAO = new SurplusFoodItemDAO(); // Initialize the SurplusFoodItemDAO
    }

    protected Connection getConnection() throws SQLException, IOException {
        return dbUtil.getConnection();
    }

    public void addFoodItem(FoodItem foodItem) throws SQLException, IOException {
        if (isExpiringSoon(foodItem.getExpirationDate())) {
            surplusFoodItemDAO.addSurplusFoodItem(foodItem);
            LOGGER.info("Added food item to surplus: " + foodItem);
        } else {
            try (Connection connection = getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(INSERT_FOOD_ITEM_SQL)) {
                preparedStatement.setString(1, foodItem.getRetailerName());
                preparedStatement.setString(2, foodItem.getItemName());
                preparedStatement.setInt(3, foodItem.getQuantity());
                preparedStatement.setDouble(4, foodItem.getPrice());
                preparedStatement.setString(5, foodItem.getExpirationDate());
                preparedStatement.executeUpdate();
                LOGGER.info("Added food item to food_items: " + foodItem);
            }
        }
    }

    public FoodItem selectFoodItem(int itemId) throws SQLException, IOException {
        FoodItem foodItem = null;
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_FOOD_ITEM_BY_ID)) {
            preparedStatement.setInt(1, itemId);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    String retailerName = rs.getString("retailer_name");
                    String itemName = rs.getString("item_name");
                    int quantity = rs.getInt("quantity");
                    double price = rs.getDouble("price");
                    String expirationDate = rs.getString("expiration_date");

                    foodItem = new FoodItem(itemId, retailerName, itemName, quantity, price, expirationDate);
                }
            }
        }
        return foodItem;
    }

    public List<FoodItem> selectAllFoodItems() throws SQLException, IOException {
        List<FoodItem> foodItems = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_FOOD_ITEMS);
             ResultSet rs = preparedStatement.executeQuery()) {

            while (rs.next()) {
                int itemId = rs.getInt("item_id");
                String retailerName = rs.getString("retailer_name");
                String itemName = rs.getString("item_name");
                int quantity = rs.getInt("quantity");
                double price = rs.getDouble("price");
                String expirationDate = rs.getString("expiration_date");

                foodItems.add(new FoodItem(itemId, retailerName, itemName, quantity, price, expirationDate));
            }
        }
        return foodItems;
    }

    public boolean deleteFoodItem(int itemId) throws SQLException, IOException {
        boolean rowDeleted;
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_FOOD_ITEM_SQL)) {
            statement.setInt(1, itemId);
            rowDeleted = statement.executeUpdate() > 0;
        }
        return rowDeleted;
    }

    public boolean updateFoodItem(FoodItem foodItem) throws SQLException, IOException {
        boolean rowUpdated;
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_FOOD_ITEM_SQL)) {
            statement.setString(1, foodItem.getRetailerName());
            statement.setString(2, foodItem.getItemName());
            statement.setInt(3, foodItem.getQuantity());
            statement.setDouble(4, foodItem.getPrice());
            statement.setString(5, foodItem.getExpirationDate());
            statement.setInt(6, foodItem.getId());

            rowUpdated = statement.executeUpdate() > 0;
        }
        return rowUpdated;
    }

    public void transferToSurplusItems() throws SQLException, IOException {
        try (Connection connection = getConnection();
             PreparedStatement selectStatement = connection.prepareStatement(SELECT_FOOD_ITEMS_TO_TRANSFER);
             ResultSet rs = selectStatement.executeQuery()) {

            while (rs.next()) {
                FoodItem foodItem = new FoodItem(
                    rs.getInt("item_id"),
                    rs.getString("retailer_name"),
                    rs.getString("item_name"),
                    rs.getInt("quantity"),
                    rs.getDouble("price"),
                    rs.getString("expiration_date")
                );
                surplusFoodItemDAO.addSurplusFoodItem(foodItem);
                LOGGER.info("Transferred food item to surplus: " + foodItem);

                deleteFoodItem(foodItem.getId());
                LOGGER.info("Deleted food item from food_items: " + foodItem.getId());
            }
        }
    }

    private boolean isExpiringSoon(String expirationDate) {
        try {
            LocalDate today = LocalDate.now();
            LocalDate expiryDate = LocalDate.parse(expirationDate);
            return !expiryDate.isAfter(today.plusWeeks(1));
        } catch (DateTimeParseException e) {
            LOGGER.log(Level.SEVERE, "Error parsing expiration date: " + expirationDate, e);
            return false;
        }
    }
}
