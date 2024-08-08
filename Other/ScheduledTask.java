package Other;

import DAO.FoodItemDAO;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Timer;
import java.util.TimerTask;

public class ScheduledTask {

    private Timer timer;

    public void start() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    FoodItemDAO dao = new FoodItemDAO();
                    dao.transferToSurplusItems();
                } catch (SQLException | IOException e) {
                    e.printStackTrace();
                }
            }
        }, 0, 10 * 1000);
    }

    public void cancel() {
        if (timer != null) {
            timer.cancel();
        }
    }
}
