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

/**
 * SurplusFoodItemDAO.java
 * This DAO class provides database operations for the table surplus_food_items in the database.
 */
public class SurplusFoodItemDAO {
    private DBUtil dbUtil;

    private static final String INSERT_SURPLUS_FOOD_ITEM_SQL = "INSERT INTO surplusfooditems (item_id, retailer_name, item_name, quantity, price, expiration_date) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String SELECT_SURPLUS_FOOD_ITEM_BY_ID = "SELECT * FROM surplusfooditems WHERE item_id = ?";
    private static final String SELECT_ALL_SURPLUS_FOOD_ITEMS = "SELECT * FROM surplusfooditems";
    private static final String UPDATE_SURPLUS_FOOD_ITEM_SQL = "UPDATE surplusfooditems SET retailer_name = ?, item_name = ?, quantity = ?, price = ?, expiration_date = ? WHERE item_id = ?";
    private static final String DELETE_SURPLUS_FOOD_ITEM_SQL = "DELETE FROM surplusfooditems WHERE item_id = ?";
    private static final String INSERT_CONSUMER_FOOD_ITEM_SQL = "INSERT INTO consumer_items (item_id, retailer_name, item_name, quantity, price, expiration_date) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String SELECT_SURPLUS_FOOD_ITEM_SQL = "SELECT * FROM surplusfooditems WHERE item_id = ?";
    
    public SurplusFoodItemDAO() {
        dbUtil = new DBUtil();
    }

    protected Connection getConnection() throws SQLException, IOException {
        return dbUtil.getConnection();
    }

    public void addSurplusFoodItem(FoodItem foodItem) throws SQLException, IOException {
        try (Connection connection = getConnection();
             PreparedStatement checkStatement = connection.prepareStatement(SELECT_SURPLUS_FOOD_ITEM_BY_ID)) {
            checkStatement.setInt(1, foodItem.getId());
            try (ResultSet rs = checkStatement.executeQuery()) {
                if (!rs.next()) {
                    try (PreparedStatement insertStatement = connection.prepareStatement(INSERT_SURPLUS_FOOD_ITEM_SQL)) {
                        insertStatement.setInt(1, foodItem.getId());
                        insertStatement.setString(2, foodItem.getRetailerName());
                        insertStatement.setString(3, foodItem.getItemName());
                        insertStatement.setInt(4, foodItem.getQuantity());
                        insertStatement.setDouble(5, foodItem.getPrice());
                        insertStatement.setString(6, foodItem.getExpirationDate());
                        insertStatement.executeUpdate();
                    }
                }
            }
        }
    }

    public List<FoodItem> selectAllSurplusFoodItems() throws SQLException, IOException {
        List<FoodItem> surplusFoodItems = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_SURPLUS_FOOD_ITEMS);
             ResultSet rs = preparedStatement.executeQuery()) {

            while (rs.next()) {
                int itemId = rs.getInt("item_id");
                String retailerName = rs.getString("retailer_name");
                String itemName = rs.getString("item_name");
                int quantity = rs.getInt("quantity");
                double price = rs.getDouble("price");
                String expirationDate = rs.getString("expiration_date");

                surplusFoodItems.add(new FoodItem(itemId, retailerName, itemName, quantity, price, expirationDate));
            }
        }
        return surplusFoodItems;
    }

    public FoodItem selectSurplusFoodItem(int id) throws SQLException, IOException {
        FoodItem foodItem = null;
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_SURPLUS_FOOD_ITEM_BY_ID)) {
            preparedStatement.setInt(1, id);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    String retailerName = rs.getString("retailer_name");
                    String itemName = rs.getString("item_name");
                    int quantity = rs.getInt("quantity");
                    double price = rs.getDouble("price");
                    String expirationDate = rs.getString("expiration_date");

                    foodItem = new FoodItem(id, retailerName, itemName, quantity, price, expirationDate);
                }
            }
        }
        return foodItem;
    }

    public boolean updateSurplusFoodItem(FoodItem foodItem) throws SQLException, IOException {
        boolean rowUpdated;
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_SURPLUS_FOOD_ITEM_SQL)) {
            preparedStatement.setString(1, foodItem.getRetailerName());
            preparedStatement.setString(2, foodItem.getItemName());
            preparedStatement.setInt(3, foodItem.getQuantity());
            preparedStatement.setDouble(4, foodItem.getPrice());
            preparedStatement.setString(5, foodItem.getExpirationDate());
            preparedStatement.setInt(6, foodItem.getId());

            rowUpdated = preparedStatement.executeUpdate() > 0;
        }
        return rowUpdated;
    }

    public boolean deleteSurplusFoodItem(int id) throws SQLException, IOException {
        boolean rowDeleted;
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_SURPLUS_FOOD_ITEM_SQL)) {
            preparedStatement.setInt(1, id);
            rowDeleted = preparedStatement.executeUpdate() > 0;
        }
        return rowDeleted;
    }

    public void sendToConsumer(int id) throws SQLException, IOException {
        String selectSQL = SELECT_SURPLUS_FOOD_ITEM_SQL;
        String insertSQL = INSERT_CONSUMER_FOOD_ITEM_SQL;
        String deleteSQL = DELETE_SURPLUS_FOOD_ITEM_SQL;

        try (Connection connection = getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement selectStatement = connection.prepareStatement(selectSQL);
                 PreparedStatement insertStatement = connection.prepareStatement(insertSQL);
                 PreparedStatement deleteStatement = connection.prepareStatement(deleteSQL)) {

                selectStatement.setInt(1, id);
                try (ResultSet rs = selectStatement.executeQuery()) {
                    if (rs.next()) {
                        int itemId = rs.getInt("item_id");
                        String retailerName = rs.getString("retailer_name");
                        String itemName = rs.getString("item_name");
                        int quantity = rs.getInt("quantity");
                        double price = rs.getDouble("price");
                        String expirationDate = rs.getString("expiration_date");

                        insertStatement.setInt(1, itemId);
                        insertStatement.setString(2, retailerName);
                        insertStatement.setString(3, itemName);
                        insertStatement.setInt(4, quantity);
                        insertStatement.setDouble(5, price/2);
                        insertStatement.setString(6, expirationDate);
                        insertStatement.executeUpdate();

                        deleteStatement.setInt(1, id);
                        deleteStatement.executeUpdate();
                    }
                }

                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                throw e;
            }
        }
    }
}
