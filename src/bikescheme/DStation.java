/**
 * 
 */
package bikescheme;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

/**
 *  
 * Docking Station.
 * 
 * @author pbj
 *
 */
public class DStation implements StartRegObserver, ViewActivityObserver {
    public static final Logger logger = Logger.getLogger("bikescheme");

    private String instanceName;
    private int eastPos;
    private int northPos;
    
    private DSTouchScreen touchScreen;
    private CardReader cardReader; 
    private KeyIssuer keyIssuer;
    private KeyReader keyReader;
    private List<DPoint> dockingPoints;

    private HubInterface hub;
    /**
     * 
     * Construct a Docking Station object with touch screen, card reader
     * and key issuer interface devices and a connection to a number of
     * docking points.
     * 
     * If the instance name is <foo>, then the Docking Points are named
     * <foo>.1 ... <foo>.<numPoints> . 
     * 
     * @param instanceName
     */
    public DStation(
            String instanceName,
            int eastPos,
            int northPos,
            int numPoints,
            Hub hub) {
        
     // Construct and make connections with interface devices
        
        this.instanceName = instanceName;
        this.eastPos = eastPos;
        this.northPos = northPos;
        this.hub = hub;

        touchScreen = new DSTouchScreen(instanceName + ".ts");
        touchScreen.setRegObserver(this);
        touchScreen.setViewActivityObserver(this);

        cardReader = new CardReader(instanceName + ".cr");
        
        keyIssuer = new KeyIssuer(instanceName + ".ki");
        keyReader = new KeyReader(instanceName + ".kr");

        dockingPoints = new ArrayList<DPoint>();
        
        for (int i = 1; i <= numPoints; i++) {
            DPoint dp = new DPoint(instanceName + "." + i, i - 1, this);
            dockingPoints.add(dp);
        }
    }
       
    void setDistributor(EventDistributor d) {
        touchScreen.addDistributorLinks(d); 
        cardReader.addDistributorLinks(d);
        for (DPoint dp : dockingPoints) {
            dp.setDistributor(d);
        }
        keyReader.addDistributorLinks(d);
    }
    
    void setCollector(EventCollector c) {
        touchScreen.setCollector(c);
        cardReader.setCollector(c);
        keyIssuer.setCollector(c);
        for (DPoint dp : dockingPoints) {
            dp.setCollector(c);
        }
    }
    
    /** 
     * Implementation of docking station functionality for
     * "register user" use case.
     * 
     * Method called on docking station receiving a "start registration"
     * triggering input event at the touch screen.
     * 
     * @param personalInfo
     */
    public void startRegReceived(String personalInfo) {
        logger.fine("Starting registration of " + personalInfo + " on instance " + getInstanceName());

        touchScreen.showPrompt("Please authorise your bank card");

        String authCode = cardReader.readCard();    // Pull in non-triggering input event
        logger.fine("Read card with authCode: " + authCode);

        String keyId = keyIssuer.issueKey(false); // Generate output event
        logger.fine("Issued key with id: " + keyId);

        hub.registerUser(personalInfo, keyId, authCode);
    }

    public int getDPointCount(){
        return dockingPoints.size();
    }

    /**
     * Gets the number of occupied dockingPoints for viewOccupancy purposes
     */
    public int getOccupied() {
        int i = 0;
        for (DPoint dp : dockingPoints) {
            if (dp.isOccupied())
                i++;
        }
        return i;
    }

    public Date getBikeDockingTime(String bikeId) { return hub.getBikeDockingTime(bikeId); }

    public Bike handleDockedBike(String bikeId) { return hub.handleDockedBike(bikeId, this); }

    public boolean handleKeyInserted(Bike bike, DStation dStation, String keyId) {
        return hub.handleKeyInserted(bike, dStation, keyId);
    }

    public void viewActivityReceived() {
        logger.fine("Initiating generation of user report...");
        // Prompt user to insert key
        touchScreen.showPrompt("Please insert key into Terminal");

        String keyId = keyReader.waitForKeyInsertion();
        ArrayList userActivity = hub.generateUserActivity(keyId);
        touchScreen.showUserActivity(userActivity);
    }

    public String getInstanceName() {
        return instanceName;
    }
    
    public int getEastPos() {
        return eastPos;
    }
    
    public int getNorthPos() {
        return northPos;
    }

}
