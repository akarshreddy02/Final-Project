package DAOPackageTest;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import DAO.FoodItemDAO;
import Model.FoodItem;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

class FoodItemDAOTest {

    private FoodItemDAO foodItemDAO;

    @BeforeEach
    void setUp() {
        foodItemDAO = new FoodItemDAO();
    }

    @Test
    void testAddFoodItem() throws SQLException, IOException {
        // Setup
        FoodItem item = new FoodItem("Retailer1", "Item1", 10, 100.0, "2024-12-31");
        
        // Action
        foodItemDAO.addFoodItem(item);

        // Assert
        List<FoodItem> retrievedItems = foodItemDAO.selectAllFoodItems();
        assertFalse(retrievedItems.isEmpty());
        assertEquals(item.getItemName(), retrievedItems.get(0).getItemName());
    }

    @Test
    void testSelectAllFoodItems() throws SQLException, IOException {
        // Setup
        FoodItem item1 = new FoodItem("Retailer1", "Item1", 10, 100.0, "2024-12-31");
        FoodItem item2 = new FoodItem("Retailer2", "Item2", 20, 200.0, "2024-11-30");
        foodItemDAO.addFoodItem(item1);
        foodItemDAO.addFoodItem(item2);

        // Action
        List<FoodItem> items = foodItemDAO.selectAllFoodItems();

        // Assert
        assertFalse(items.isEmpty());
        assertEquals(2, items.size());
    }

    @Test
    void testSelectFoodItem() throws SQLException, IOException {
        // Setup
        FoodItem item = new FoodItem("Retailer1", "Item1", 10, 100.0, "2024-12-31");
        foodItemDAO.addFoodItem(item);

        // Action
        FoodItem retrievedItem = foodItemDAO.selectFoodItem(1);

        // Assert
        assertNotNull(retrievedItem);
        assertEquals(item.getItemName(), retrievedItem.getItemName());
    }

    @Test
    void testDeleteFoodItem() throws SQLException, IOException {
        // Setup
        FoodItem item = new FoodItem("Retailer1", "Item1", 10, 100.0, "2024-12-31");
        foodItemDAO.addFoodItem(item);

        // Action
        boolean result = foodItemDAO.deleteFoodItem(1);

        // Assert
        assertTrue(result);
        List<FoodItem> items = foodItemDAO.selectAllFoodItems();
        assertTrue(items.isEmpty());
    }

    @Test
    void testUpdateFoodItem() throws SQLException, IOException {
        // Setup
        FoodItem item = new FoodItem("Retailer1", "Item1", 10, 100.0, "2024-12-31");
        foodItemDAO.addFoodItem(item);
        FoodItem updatedItem = new FoodItem(1, "Retailer1", "UpdatedItem", 10, 150.0, "2024-12-31");

        // Action
        boolean result = foodItemDAO.updateFoodItem(updatedItem);

        // Assert
        assertTrue(result);
        FoodItem retrievedItem = foodItemDAO.selectFoodItem(1);
        assertEquals(updatedItem.getItemName(), retrievedItem.getItemName());
    }

    @Test
    void testTransferToSurplusItems() throws SQLException, IOException {
        // Setup
        FoodItem item = new FoodItem("Retailer1", "Item1", 10, 100.0, "2024-12-01"); // Expiring soon
        foodItemDAO.addFoodItem(item);

        // Action
        foodItemDAO.transferToSurplusItems();

        // Assert
        List<FoodItem> items = foodItemDAO.selectAllFoodItems();
        assertTrue(items.isEmpty());
        // Assuming there's a method to retrieve items from the surplus table
        // List<SurplusFoodItem> surplusItems = surplusFoodItemDAO.selectAllSurplusFoodItems();
        // assertFalse(surplusItems.isEmpty());
    }
}
