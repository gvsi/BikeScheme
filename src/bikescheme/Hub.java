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

        // Schedule timed notification for generating updates of 
        // hub display. 

        // The idiom of an anonymous class is used here, to make it easy
        // for hub code to process multiple timed notification, if needed.
         
        Clock.getInstance().scheduleNotification(
                new TimedNotificationObserver() {

                    /** 
                     * Generate dummy display of station occupancy data.
                     */
                    @Override
                    public void processTimedNotification() {
                        logger.fine("");

                        ArrayList<String> occupancyArray = new ArrayList<>();

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

        logger.fine(""+userList.get(userList.size()-1).getUserId());
        logger.fine(""+keyList.get(keyList.size()-1).getKeyId());
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
        logger.fine("");
        
        DStation newDStation = 
                new DStation(instanceName, eastPos, northPos, numPoints, this);
        dockingStationMap.put(instanceName, newDStation);
        
        // Now connect up DStation to event distributor and collector.
        
        EventDistributor d = terminal.getDistributor();
        EventCollector c = display.getCollector();
        
        newDStation.setDistributor(d);
        newDStation.setCollector(c);
    }

    public boolean handleDockedBike(String bikeId) {
        boolean bikeExists = false;

        for (Bike bike : bikeList) {
            if(bike.getBikeId().equals((bikeId))){
                bikeExists = true;
                break;
            }
        }

        // If a bike exist end a trip record else add a new bike to the system
        if(bikeExists){
            returnBike();
        }else{
            addBike();
        }

        return bikeExists;
    }

    private void returnBike() {
        //TODO
        // Return a bike code 
    }

    private void addBike() {
        Bike newBike = new Bike();
        bikeList.add(newBike);
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
        for (User u : userList) {
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
