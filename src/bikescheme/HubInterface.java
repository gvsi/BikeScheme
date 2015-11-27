package bikescheme;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by gvsi on 26/11/2015.
 */
public interface HubInterface {
    Bike handleDockedBike(String bikeId, DStation dStation);
    boolean handleKeyInserted(Bike bike, DStation dStation, String keyId);
    void registerUser(String name, String keyId, String authCode);
    ArrayList<String> generateUserActivity(String keyId);
    ArrayList<String> findFreePoints(DStation dStation, String keyId);
    Date getBikeDockingTime(String bikeId);
}
