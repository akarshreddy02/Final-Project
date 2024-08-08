package DAO;

import Model.CharityItem;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import Model.FoodItem;
import Util.DBUtil;
import java.io.IOException;

public class CharityItemDAO {
    private DBUtil dbUtil;

    private static final String INSERT_CHARITY_ITEM_SQL = "INSERT INTO charitable_items (item_id, retailer_name, item_name, quantity, expiration_date) VALUES (?, ?, ?, ?, ?)";
    private static final String SELECT_ALL_CHARITY_ITEMS = "SELECT * FROM charitable_items";
    private static final String SELECT_CHARITY_ITEM_BY_ID = "SELECT * FROM charitable_items WHERE item_id = ?";
    private static final String DELETE_CHARITY_ITEM_SQL = "DELETE FROM charitable_items WHERE item_id = ?";
    private static final String INSERT_CONSUMER_ITEM_SQL = "INSERT INTO consumer_items (item_id, retailer_name, item_name, quantity, price, expiration_date) VALUES (?, ?, ?, ?, ?, ?)";

    public CharityItemDAO() {
        dbUtil = new DBUtil();
    }

    protected Connection getConnection() throws SQLException, IOException {
        return dbUtil.getConnection();
    }

    public void addCharityItem(FoodItem foodItem) throws SQLException, IOException {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_CHARITY_ITEM_SQL)) {
            preparedStatement.setInt(1, foodItem.getId());
            preparedStatement.setString(2, foodItem.getRetailerName());
            preparedStatement.setString(3, foodItem.getItemName());
            preparedStatement.setInt(4, foodItem.getQuantity());
            preparedStatement.setString(5, foodItem.getExpirationDate());

            preparedStatement.executeUpdate();
        }
    }

    public List<FoodItem> selectAllCharityItems() throws SQLException, IOException {
    List<FoodItem> charityItems = new ArrayList<>();
    try (Connection connection = getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_CHARITY_ITEMS);
         ResultSet rs = preparedStatement.executeQuery()) {

        while (rs.next()) {
            int itemId = rs.getInt("item_id");
            String retailerName = rs.getString("retailer_name");
            String itemName = rs.getString("item_name");
            int quantity = rs.getInt("quantity");
            String expirationDate = rs.getString("expiration_date");

            FoodItem foodItem = new FoodItem(itemId, retailerName, itemName, quantity, expirationDate);
            System.out.println("Retrieved item: " + foodItem); // Debugging statement
            charityItems.add(foodItem);
        }
    } catch (SQLException | IOException e) {
        e.printStackTrace();
    }
    return charityItems;
}


    public FoodItem selectCharityItem(int id) throws SQLException, IOException {
        FoodItem foodItem = null;
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_CHARITY_ITEM_BY_ID)) {
            preparedStatement.setInt(1, id);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    String retailerName = rs.getString("retailer_name");
                    String itemName = rs.getString("item_name");
                    int quantity = rs.getInt("quantity");
                    String expirationDate = rs.getString("expiration_date");

                    foodItem = new FoodItem(id, retailerName, itemName, quantity, expirationDate);
                }
            }
        }
        return foodItem;
    }
    public void deleteCharityItem(int itemId) throws SQLException, IOException {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_CHARITY_ITEM_SQL)) {
            preparedStatement.setInt(1, itemId);
            preparedStatement.executeUpdate();
        }
    }
    
     public void transferToConsumer(FoodItem foodItem) throws SQLException, IOException {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_CONSUMER_ITEM_SQL)) {
            preparedStatement.setInt(1, foodItem.getId());
            preparedStatement.setString(2, foodItem.getRetailerName());
            preparedStatement.setString(3, foodItem.getItemName());
            preparedStatement.setInt(4, foodItem.getQuantity());
            preparedStatement.setDouble(5, foodItem.getPrice()/2);
            preparedStatement.setString(6, foodItem.getExpirationDate());

            preparedStatement.executeUpdate();
        }
    }

    public CharityItem getCharityItem(int itemId) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }
}

   