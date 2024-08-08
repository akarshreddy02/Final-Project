package DAOPackageTest;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import DAO.ConsumerItemDAO;
import Model.ConsumerItem;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

class ConsumerItemDAOTest {

    private ConsumerItemDAO consumerItemDAO;

    @BeforeEach
    void setUp() {
        consumerItemDAO = new ConsumerItemDAO();
    }

    @Test
    void testAddConsumerItem() throws SQLException, IOException {
        // Setup
        ConsumerItem item = new ConsumerItem(1, "Retailer1", "Item1", 10, 100.0, "2024-12-31");
        
        // Action
        consumerItemDAO.addConsumerItem(item);

        // Assert
        List<ConsumerItem> retrievedItems = consumerItemDAO.selectAllConsumerItems();
        assertFalse(retrievedItems.isEmpty());
        assertEquals(item.getItemName(), retrievedItems.get(0).getItemName());
    }

    @Test
    void testSelectAllConsumerItems() throws SQLException, IOException {
        // Setup
        ConsumerItem item1 = new ConsumerItem(1, "Retailer1", "Item1", 10, 100.0, "2024-12-31");
        ConsumerItem item2 = new ConsumerItem(2, "Retailer2", "Item2", 20, 200.0, "2024-11-30");
        consumerItemDAO.addConsumerItem(item1);
        consumerItemDAO.addConsumerItem(item2);

        // Action
        List<ConsumerItem> items = consumerItemDAO.selectAllConsumerItems();

        // Assert
        assertFalse(items.isEmpty());
        assertEquals(2, items.size());
    }

    @Test
    void testDeleteConsumerItem() throws SQLException, IOException {
        // Setup
        ConsumerItem item = new ConsumerItem(1, "Retailer1", "Item1", 10, 100.0, "2024-12-31");
        consumerItemDAO.addConsumerItem(item);

        // Action
        boolean result = consumerItemDAO.deleteConsumerItem(1);

        // Assert
        assertTrue(result);
        List<ConsumerItem> items = consumerItemDAO.selectAllConsumerItems();
        assertTrue(items.isEmpty());
    }

    @Test
    void testTransferToCharity() throws SQLException, IOException {
        // Setup
        ConsumerItem item = new ConsumerItem(1, "Retailer1", "Item1", 10, 100.0, "2024-12-31");
        consumerItemDAO.addConsumerItem(item);

        // Action
        consumerItemDAO.transferToCharity(1);

        // Assert
        List<ConsumerItem> items = consumerItemDAO.selectAllConsumerItems();
        assertTrue(items.isEmpty());
        // Assuming there's a method to retrieve items from the charity table
        // List<CharityItem> charityItems = charityItemDAO.selectAllCharityItems();
        // assertFalse(charityItems.isEmpty());
    }
}
