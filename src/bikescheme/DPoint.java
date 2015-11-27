/**
 * 
 */
package bikescheme;

import java.util.Calendar;
import java.util.Date;
import java.util.logging.Logger;

/**
 *  
 * Docking Point for a Docking Station.
 * 
 * @author pbj
 *
 */
public class DPoint implements KeyInsertionObserver, BikeDockingObserver, FaultButtonObserver {
    public static final Logger logger = Logger.getLogger("bikescheme");

    private KeyReader keyReader;
    private BikeSensor bikeSensor;
    private OKLight okLight;
    private String instanceName;
    private int index;
    private Bike currentBike;
    private BikeLock bikeLock;
    private DStation dStation;
    private FaultButton fButton;
    private boolean hasFaultyBike;

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

        fButton = new FaultButton(instanceName + ".fb");
        fButton.setObserver(this);
        keyReader = new KeyReader(instanceName + ".kr");
        keyReader.setObserver(this);
        bikeLock = new BikeLock(instanceName + ".bl");
        bikeSensor = new BikeSensor(instanceName + ".bs");
        bikeSensor.setObserver(this);
        okLight = new OKLight(instanceName + ".ok");

        this.instanceName = instanceName;
        this.index = index;
        this.dStation = dStation;
        this.currentBike = null;
        this.hasFaultyBike = false;
    }
       
    public void setDistributor(EventDistributor d) {
        fButton.addDistributorLinks(d);
        keyReader.addDistributorLinks(d);
        bikeSensor.addDistributorLinks(d);
    }
    
    public void setCollector(EventCollector c) {
        okLight.setCollector(c);
        bikeLock.setCollector(c);
    }

    public boolean isOccupied() {
        return this.currentBike != null;
    }
    
    public String getInstanceName() {
        return instanceName;
    }

    public int getIndex() {
        return index;
    }

    /**
     * Start hire on key insertion.
     *
     */
    public void keyInserted(String keyId) {
        if (isOccupied()) {
            handleKeyInserted(keyId);
        }else{
            logger.warning("Trying to hire unoccupied DPoint " + getInstanceName());
        }
    }


    /**
     * Handle startHire use case.
     * Send a message to the dStation, which sends a message to the hub to start a hire.
     * If everything is successful, unlock the bike and flash the light.
     */
    private void handleKeyInserted(String keyId) {
        if (dStation.handleKeyInserted(currentBike, this.dStation, keyId) && !hasFaultyBike) {
            logger.fine("Start of hire successful for bike " + currentBike.getBikeId() + " with key " + keyId + "at dStation " + dStation.getInstanceName());
            bikeLock.unlock();
            okLight.flash();
        }else{
            logger.fine("Bike " + currentBike.getBikeId() + " removed from the DPoint " + dStation.getInstanceName() + " using Master Key with id " + keyId);
            bikeLock.unlock();
            okLight.flash();
            hasFaultyBike = false;
        }

        // Updates the occupancy data in the hub display
        dStation.updateOccupancyHubDisplay();
    }


    /**
     * Whenever a bike is docked:
     *  1: If it already exists it is a ReturnBike scenario
     *  2: If it does not exist it is a AddBike scenario
     */

    @Override
    public void bikeDocked(String bikeId) {
        logger.fine("Start docking " + bikeId + " on " + getInstanceName());


        if(!isOccupied()) {
            this.currentBike = dStation.handleDockedBike(bikeId);
            bikeLock.lock();
            dStation.updateOccupancyHubDisplay();
            logger.fine("Bike with id " + bikeId + " locked on " + getInstanceName());
        }else{
            logger.warning("Trying to dock a bike on occupied DPoint " + getInstanceName());
        }
    }

    @Override
    public void pressed(Date pressingTime) {
        if(isOccupied()) {
            Date dockingTime = dStation.getBikeDockingTime(currentBike.getBikeId());

            long diff = pressingTime.getTime() - dockingTime.getTime();
            int diffMin = (int) (diff / 1000 / 60);

            if (diffMin < 2) {
                hasFaultyBike = true;
                logger.fine("Bike with id " + currentBike.getBikeId() + " reported as faulty on DPoint " + instanceName);
            } else {
                logger.warning("Button pressed 2 minutes or more after docking.");
            }
        }else{
            logger.warning("DPoint " + instanceName + " is not occupied.");
        }
    }
}
