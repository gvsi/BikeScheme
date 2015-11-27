package bikescheme;

/**
 * Created by gvsi on 26/11/2015.
 */
public interface HubInterface {
    public Bike handleDockedBike(String bikeId, DStation dStation);
    public boolean handleKeyInserted(Bike bike, DStation dStation, String keyId);
    public void registerUser(String name, String keyId, String authCode);
}
