/**
 * 
 */
package bikescheme;

import com.sun.xml.internal.bind.v2.TODO;

import java.util.*;
import java.util.logging.Logger;

/**
 *  
 * Hub system.
 *
 * 
 * @author pbj
 *
 */
public class Hub implements AddDStationObserver {
    public static final Logger logger = Logger.getLogger("bikescheme");

    private HubTerminal terminal;
    private HubDisplay display;
    private Map<String,DStation> dockingStationMap;
    private ArrayList<User> userList;
    private ArrayList<Key> keyList;
    private ArrayList<TripRecord> tripRecordsList;
    private ArrayList<Bike> bikeList;
    
    /**
     * 
     * Construct a hub system with an operator terminal, a wall display 
     * and connections to a number of docking stations (initially 0). 
     * 
     * Schedule update of the hub wall display every 5 minutes with
     * docking station occupancy data.
     * 
     */
    public Hub() {

        // Construct and make connections with interface devices
        terminal = new HubTerminal("ht");
        terminal.setObserver(this);
        display = new HubDisplay("hd");
        dockingStationMap = new HashMap<String,DStation>();
        userList = new ArrayList<>();
        keyList = new ArrayList<>();
        tripRecordsList = new ArrayList<>();
        bikeList = new ArrayList<>();

        // Schedule timed notification for generating updates of 
        // hub display. 

        // The idiom of an anonymous class is used here, to make it easy
        // for hub code to process multiple timed notification, if needed.
         
        Clock.getInstance().scheduleNotification(
                new TimedNotificationObserver() {

                    /** 
                     * Generate a display of station occupancy data.
                     */
                    @Override
                    public void processTimedNotification() {

                        ArrayList<String> occupancyArray = new ArrayList<>();

                        /**
                         * Check DStations for more than 85% or less than 15%
                         * and displays them accordingly.
                         */
                        for (String key : dockingStationMap.keySet()) {

                            String status = "";
                            if((float)dockingStationMap.get(key).getOccupied()/dockingStationMap.get(key).getDPointCount() < 0.15){
                                status = "LOW";
                            }else if((float)dockingStationMap.get(key).getOccupied()/dockingStationMap.get(key).getDPointCount() > 0.85){
                                status = "HIGH";
                            }

                            if(status.length() > 0) {
                                occupancyArray.add(key);
                                occupancyArray.add((Integer.toString(dockingStationMap.get(key).getEastPos())));
                                occupancyArray.add((Integer.toString(dockingStationMap.get(key).getNorthPos())));
                                occupancyArray.add(status);
                                occupancyArray.add((Integer.toString(dockingStationMap.get(key).getOccupied())));
                                occupancyArray.add((Integer.toString(dockingStationMap.get(key).getDPointCount())));
                            }
                        }

                        display.showOccupancy(occupancyArray);
                    }

                },
                Clock.getStartDate(), 
                0, 
                5);

    }

    public void setDistributor(EventDistributor d) {
        
        // The clock device is connected to the EventDistributor here, even
        // though the clock object is not constructed here, 
        // as no distributor is available to the Clock constructor.
        Clock.getInstance().addDistributorLinks(d);
        terminal.addDistributorLinks(d);
    }
    
    public void setCollector(EventCollector c) {
        display.setCollector(c); 
        terminal.setCollector(c);
    }

    public void registerUser(String name, String keyId, String authCode) {
        Key key = new Key(keyId);
        keyList.add(key);
        userList.add(new User(name, key, authCode));

        logger.fine("Added user " + name + " and key with id " + keyId + " to the system");
    }

    /**
     * 
     */
    @Override
    public void addDStation(
            String instanceName, 
            int eastPos, 
            int northPos,
            int numPoints) {
        
        DStation newDStation = 
                new DStation(instanceName, eastPos, northPos, numPoints, this);
        dockingStationMap.put(instanceName, newDStation);
        
        // Now connect up DStation to event distributor and collector.
        
        EventDistributor d = terminal.getDistributor();
        EventCollector c = display.getCollector();
        
        newDStation.setDistributor(d);
        newDStation.setCollector(c);
    }

    public Bike handleDockedBike(String bikeId) {
        logger.fine("Checking if bike with id " + bikeId + " exists.");

        for (Bike bike : bikeList) {
            if(bike.getBikeId().equals((bikeId))){
                // If the bike already exists end the trip (ReturnBike).
                logger.fine("Bike with id " + bikeId + " exists.");
                endTrip(bike);
                return bike;
            }
        }

        // If a bike does not exist create a new one (AddBike).
        return addBike(bikeId);
    }

    private void endTrip(Bike bike) {
        //TODO
        // Return a bike code
    }

    private Bike addBike(String bikeId) {
        logger.fine("Bike with id " + bikeId + " does not exist.");

        Bike newBike = new Bike(bikeId);
        bikeList.add(newBike);

        logger.fine("New bike with id " + newBike.getBikeId() + " created.");
        return newBike;
    }

    public boolean startHire(Bike bike, DStation dStation, String keyId) {
        Key key = new Key(keyId);
        User user = getUser(key);

        if (user != null && !userHasActiveHire(user)) {
            TripRecord tr = new TripRecord(bike, user, dStation);
            return true;
        }
        return false;
    }

    private User getUser(Key key) {
        logger.fine("Expected key " + key.getKeyId());
        for (User u : userList) {
            logger.fine("User found with keyId " + u.getKey().getKeyId());
            if (u.getKey().equals(key)) {
                return u;
            }
        }
        return null;
    }

    private boolean userHasActiveHire(User u) {
        for (TripRecord t : tripRecordsList) {
            if (t.getUser().equals(u) && t.isActive()) {
                return true;
            }
        }
        return false;
    }

    public DStation getDStation(String instanceName) {
        return dockingStationMap.get(instanceName);
    }
}
