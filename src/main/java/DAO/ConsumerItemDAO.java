/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

/**
 *
 * @author lijoy
 */
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import Model.ConsumerItem;
import Util.DBUtil;
import java.io.IOException;

public class ConsumerItemDAO {
    private DBUtil dbUtil;

    private static final String INSERT_CONSUMER_ITEM_SQL = "INSERT INTO consumer_items (item_id, retailer_name, item_name, quantity, price, expiration_date) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String SELECT_CONSUMER_ITEM_BY_ID = "SELECT * FROM consumer_items WHERE item_id = ?";
    private static final String SELECT_ALL_CONSUMER_ITEMS = "SELECT * FROM consumer_items";
    private static final String DELETE_CONSUMER_ITEM_SQL = "DELETE FROM consumer_items WHERE item_id = ?";
    private static final String INSERT_CHARITY_ITEM_SQL = "INSERT INTO charitable_items (item_id, retailer_name, item_name, quantity, expiration_date) VALUES (?, ?, ?, ?, ?)";

    public ConsumerItemDAO() {
        dbUtil = new DBUtil();
    }

    protected Connection getConnection() throws SQLException, IOException {
        return dbUtil.getConnection();
    }

    public void addConsumerItem(ConsumerItem consumerItem) throws SQLException, IOException {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_CONSUMER_ITEM_SQL)) {
            preparedStatement.setInt(1, consumerItem.getId());
            preparedStatement.setString(2, consumerItem.getRetailerName());
            preparedStatement.setString(3, consumerItem.getItemName());
            preparedStatement.setInt(4, consumerItem.getQuantity());
            preparedStatement.setDouble(5, consumerItem.getPrice());
            preparedStatement.setString(6, consumerItem.getExpirationDate());
            preparedStatement.executeUpdate();
        }
    }

    public List<ConsumerItem> selectAllConsumerItems() throws SQLException, IOException {
    List<ConsumerItem> consumerItems = new ArrayList<>();
    String query = "SELECT * FROM consumer_items";

    try (Connection connection = getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(query);
         ResultSet rs = preparedStatement.executeQuery()) {

        while (rs.next()) {
            int itemId = rs.getInt("item_id");
            String retailerName = rs.getString("retailer_name");
            String itemName = rs.getString("item_name");
            int quantity = rs.getInt("quantity");
            double price = rs.getDouble("price");
            String expirationDate = rs.getString("expiration_date");
            System.out.println("Consumer Items Retrieved: " + consumerItems.size());
            System.out.println(itemId + quantity );

            consumerItems.add(new ConsumerItem(itemId, retailerName, itemName, quantity, price, expirationDate));
        }
    }
    System.out.println("Consumer Items Retrieved: " + consumerItems.size()); // Debug statement
    return consumerItems;
}



    public boolean deleteConsumerItem(int id) throws SQLException, IOException {
        boolean rowDeleted;
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_CONSUMER_ITEM_SQL)) {
            preparedStatement.setInt(1, id);
            rowDeleted = preparedStatement.executeUpdate() > 0;
        }
        return rowDeleted;
    }
    
    public void transferToCharity(int id) throws SQLException, IOException {
        Connection connection = null;
        PreparedStatement selectStmt = null;
        PreparedStatement insertStmt = null;
        PreparedStatement deleteStmt = null;
        ResultSet rs = null;

        try {
            connection = getConnection();
            connection.setAutoCommit(false);
            selectStmt = connection.prepareStatement(SELECT_CONSUMER_ITEM_BY_ID);
            selectStmt.setInt(1, id);
            rs = selectStmt.executeQuery();

            if (rs.next()) {
                insertStmt = connection.prepareStatement(INSERT_CHARITY_ITEM_SQL);
                insertStmt.setInt(1, rs.getInt("item_id"));
                insertStmt.setString(2, rs.getString("retailer_name"));
                insertStmt.setString(3, rs.getString("item_name"));
                insertStmt.setInt(4, rs.getInt("quantity"));
                insertStmt.setString(5, rs.getString("expiration_date"));
                insertStmt.executeUpdate();

                deleteStmt = connection.prepareStatement(DELETE_CONSUMER_ITEM_SQL);
                deleteStmt.setInt(1, id);
                deleteStmt.executeUpdate();
            }

            connection.commit();
        } catch (SQLException | IOException e) {
            if (connection != null) {
                connection.rollback();
            }
            throw e;
        } finally {
            if (rs != null) rs.close();
            if (selectStmt != null) selectStmt.close();
            if (insertStmt != null) insertStmt.close();
            if (deleteStmt != null) deleteStmt.close();
            if (connection != null) connection.close();
        }
    }
}

