package bikescheme;

/**
 *
 * Interface the HubInterface to provide safe access to the Hub's methods
 *
 * @author gvsi
 *
 */
public interface HubInterface {
    public Bike handleDockedBike(String bikeId, DStation dStation);
    public boolean startHire(Bike bike, DStation dStation, String keyId);
    public void registerUser(String name, String keyId, String authCode);
}
