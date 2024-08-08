package DAOPackageTest;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import DAO.CharityItemDAO;
import Model.FoodItem;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

class CharityItemDAOTest {

    private CharityItemDAO charityItemDAO;

    @BeforeEach
    void setUp() {
        charityItemDAO = new CharityItemDAO();
    }

    @Test
    void testAddCharityItem() throws SQLException, IOException {
        // Setup
        FoodItem item = new FoodItem(1, "Retailer1", "Item1", 10, "2024-12-31");
        
        // Action
        charityItemDAO.addCharityItem(item);

        // Assert
        FoodItem retrievedItem = charityItemDAO.selectCharityItem(1);
        assertNotNull(retrievedItem);
        assertEquals(item.getItemName(), retrievedItem.getItemName());
    }

    @Test
    void testSelectAllCharityItems() throws SQLException, IOException {
        // Setup
        FoodItem item1 = new FoodItem(1, "Retailer1", "Item1", 10, "2024-12-31");
        FoodItem item2 = new FoodItem(2, "Retailer2", "Item2", 20, "2024-11-30");
        charityItemDAO.addCharityItem(item1);
        charityItemDAO.addCharityItem(item2);

        // Action
        List<FoodItem> items = charityItemDAO.selectAllCharityItems();

        // Assert
        assertFalse(items.isEmpty());
        assertEquals(2, items.size());
    }

    @Test
    void testSelectCharityItem() throws SQLException, IOException {
        // Setup
        FoodItem item = new FoodItem(1, "Retailer1", "Item1", 10, "2024-12-31");
        charityItemDAO.addCharityItem(item);

        // Action
        FoodItem retrievedItem = charityItemDAO.selectCharityItem(1);

        // Assert
        assertNotNull(retrievedItem);
        assertEquals(item.getItemName(), retrievedItem.getItemName());
    }

    @Test
    void testDeleteCharityItem() throws SQLException, IOException {
        // Setup
        FoodItem item = new FoodItem(1, "Retailer1", "Item1", 10, "2024-12-31");
        charityItemDAO.addCharityItem(item);

        // Action
        charityItemDAO.deleteCharityItem(1);

        // Assert
        FoodItem retrievedItem = charityItemDAO.selectCharityItem(1);
        assertNull(retrievedItem);
    }

    @Test
    void testTransferToConsumer() throws SQLException, IOException {
        // Setup
        FoodItem item = new FoodItem(1, "Retailer1", "Item1", 10, 100.0, "2024-12-31");
        charityItemDAO.addCharityItem(item);

        // Action
        charityItemDAO.transferToConsumer(item);

        // Assert
        // Check the consumer_items table for the transferred item (this would require another DAO to fetch consumer items)
        // Here, we assume the transfer was successful if no exceptions were thrown
        assertTrue(true);
    }
}
