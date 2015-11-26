/**
 * 
 */
package bikescheme;

import java.util.logging.Logger;

/**
 *  
 * Docking Point for a Docking Station.
 * 
 * @author pbj
 *
 */
public class DPoint implements KeyInsertionObserver, BikeDockingObserver {
    public static final Logger logger = Logger.getLogger("bikescheme");

    private KeyReader keyReader;
    private BikeSensor bikeSensor;
    private OKLight okLight;
    private String instanceName;
    private int index;
    private Bike currentBike;
    private DStation dStation;

    /**
     * 
     * Construct a Docking Point object with a key reader and green ok light
     * interface devices.
     * 
     * @param instanceName a globally unique name
     * @param index of reference to this docking point  in owning DStation's
     *  list of its docking points.
     */
    public DPoint(String instanceName, int index, DStation dStation) {

     // Construct and make connections with interface devices

        keyReader = new KeyReader(instanceName + ".kr");
        keyReader.setObserver(this);
        bikeSensor = new BikeSensor(instanceName + ".bs");
        bikeSensor.setObserver(this);
        okLight = new OKLight(instanceName + ".ok");
        this.instanceName = instanceName;
        this.index = index;
        this.dStation = dStation;
        this.currentBike = null;
    }
       
    public void setDistributor(EventDistributor d) {
        keyReader.addDistributorLinks(d);
        bikeSensor.addDistributorLinks(d);
    }
    
    public void setCollector(EventCollector c) {
        okLight.setCollector(c);
    }

    public boolean isOccupied() {
        return currentBike != null;
    }
    
    public String getInstanceName() {
        return instanceName;
    }
    public int getIndex() {
        return index;
    }
    
    /**
     * Dummy implementation of docking point functionality on key insertion.
     *
     * Here, just flash the OK light.
     */
    public void keyInserted(String keyId) {
        logger.fine(getInstanceName());

        okLight.flash();
    }

    /**
     * Whenever a bike is docked:
     *  1: If it already exists it is a ReturnBike scenario
     *  2: If it does not exist it is a AddBike scenario
     */

    @Override
    public void bikeDocked(String bikeId) {
        this.currentBike = dStation.handleDockedBike(bikeId);

    }
}
