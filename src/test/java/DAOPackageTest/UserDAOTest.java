
package DAOPackageTest;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import DAO.UserDAO;
import java.io.IOException;
import java.sql.SQLException;

class UserDAOTest {

    private UserDAO userDAO;

    @BeforeEach
    void setUp() {
        userDAO = new UserDAO();
    }

    @Test
    void testRegisterUser() throws SQLException, IOException {
        // Setup
        String uname = "testuser";
        String uemail = "testuser@example.com";
        String upwd = "password";
        String umobile = "1234567890";
        String userType = "admin";

        // Action
        int result = userDAO.registerUser(uname, uemail, upwd, umobile, userType);

        // Assert
        assertEquals(1, result);
    }
}

