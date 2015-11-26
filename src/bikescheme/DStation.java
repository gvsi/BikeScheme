/**
 * 
 */
package bikescheme;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 *  
 * Docking Station.
 * 
 * @author pbj
 *
 */
public class DStation implements StartRegObserver {
    public static final Logger logger = Logger.getLogger("bikescheme");

    private String instanceName;
    private int eastPos;
    private int northPos;
    
    private DSTouchScreen touchScreen;
    private CardReader cardReader; 
    private KeyIssuer keyIssuer;
    private List<DPoint> dockingPoints;

    private Hub hub;
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
        touchScreen.setObserver(this);
        
        cardReader = new CardReader(instanceName + ".cr");
        
        keyIssuer = new KeyIssuer(instanceName + ".ki");
        
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
     * Dummy implementation of docking station functionality for 
     * "register user" use case.
     * 
     * Method called on docking station receiving a "start registration"
     * triggering input event at the touch screen.
     * 
     * @param personalInfo
     */
    public void startRegReceived(String personalInfo) {
        logger.fine("Starting registration of " + personalInfo + " on instance " + getInstanceName());

        String authCode = cardReader.readCard();    // Pull in non-triggering input event
        logger.fine("Read card with authCode: " + authCode);

        String keyId = keyIssuer.issueKey(); // Generate output event
        logger.fine("Issued key with id: " + keyId);

        hub.registerUser(personalInfo, keyId, authCode);
    }

    public int getDPointCount(){
        return dockingPoints.size();
    }

    public int getOccupied() {
        int i = 0;
        for (DPoint dp : dockingPoints) {
            if (dp.isOccupied())
                i++;
        }
        return i;
    }

    public Bike handleDockedBike(String bikeId) { return hub.handleDockedBike(bikeId); }

    public boolean startHire(Bike bike, DStation dStation, String keyId) {
        return hub.startHire(bike, dStation, keyId);
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
