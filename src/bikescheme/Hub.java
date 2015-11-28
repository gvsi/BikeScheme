/**
 * 
 */
package bikescheme;

import com.sun.xml.internal.bind.v2.TODO;
import com.sun.xml.internal.fastinfoset.algorithm.IntegerEncodingAlgorithm;

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
public class Hub implements HubInterface, AddDStationObserver, IssueMasterKeyObserver, ViewStatsObserver {
    public static final Logger logger = Logger.getLogger("bikescheme");

    private HubTerminal terminal;
    private HubDisplay display;
    private Map<String,DStation> dockingStationMap;
    private ArrayList<User> userList;
    private ArrayList<Key> keyList;
    private ArrayList<TripRecord> tripRecordsList;
    private ArrayList<Bike> bikeList;
    private KeyIssuer masterKeyIssuer;
    private BankServer bankServer;
    
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
        terminal.setAddStationObserver(this);
        terminal.setIssueMasterKeyObserver(this);
        display = new HubDisplay("hd");
        bankServer = new BankServer("hbs");
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
                        updateOccupancyDisplay();
                    }

                },
                Clock.getStartDate(),
                0,
                5);

        Clock.getInstance().scheduleNotification(
                new TimedNotificationObserver() {

                    /**
                     * Generate a display of station occupancy data.
                     */
                    @Override
                    public void processTimedNotification() {

                        ArrayList<String> chargeArray = new ArrayList<>();


                        Calendar lastMidnight = Clock.getInstance().getDateAndTimeAsCalendar();
                        lastMidnight.add(Calendar.DAY_OF_MONTH, -1);
                        lastMidnight.set(Calendar.HOUR_OF_DAY, 0);
                        lastMidnight.set(Calendar.MINUTE, 0);
                        lastMidnight.set(Calendar.SECOND, 0);
                        lastMidnight.set(Calendar.MILLISECOND, 0);


                        for (TripRecord tr : tripRecordsList){
                            if (tr.getEndTime().after(lastMidnight.getTime()) && !tr.isActive()) {
                                if(chargeArray.contains(tr.getUser().getName())){
                                    int userChargeIndex = chargeArray.indexOf(tr.getUser().getName()) + 2;
                                    chargeArray.set(userChargeIndex + 2, chargeArray.get(userChargeIndex) + tr.getCharges());
                                }else {
                                    chargeArray.add(tr.getUser().getName()); // User Name
                                    chargeArray.add(tr.getUser().getName()); // Bank authorisation code
                                    chargeArray.add(Integer.toString(tr.getCharges())); // Amount charged
                                }

                            }
                        }


                        bankServer.chargeUsers(chargeArray);
                    }

                },
                Clock.getStartDate(),
                24,
                0);

    }

    public void updateOccupancyDisplay() {
        ArrayList<String> occupancyArray = new ArrayList<>();

        /**
         * Check DStations for more than 85% or less than 15%
         * and displays them accordingly.
         */
        for (String key : dockingStationMap.keySet()) {

            String status = "";
            if ((float)dockingStationMap.get(key).getOccupied()/dockingStationMap.get(key).getDPointCount() < 0.15) {
                status = "LOW";
            } else if((float)dockingStationMap.get(key).getOccupied()/dockingStationMap.get(key).getDPointCount() > 0.85) {
                status = "HIGH";
            }

            if (status.length() > 0) {
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

    /**
     * Registers a new user into the scheme
     */
    public void registerUser(String name, String keyId, String authCode) {
        Key key = new Key(keyId, false);
        keyList.add(key);
        userList.add(new User(name, key, authCode));

        logger.fine("Added user " + name + " and key with id " + keyId + " to the system");
    }

    /**
     * Add a DStation to the system
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

    @Override
    public void viewStats() {
        ArrayList<String> stats = new ArrayList<>();

        // first day with a TripRecord and change time to midnight
        Calendar firstDay = Calendar.getInstance();
        firstDay.setTime(tripRecordsList.get(0).getStartTime());
        firstDay.set(Calendar.HOUR_OF_DAY, 0);
        firstDay.set(Calendar.MINUTE, 0);
        firstDay.set(Calendar.SECOND, 0);
        firstDay.set(Calendar.MILLISECOND, 0);

        // initialise currentDay to firstDay
        Calendar currentDay = firstDay;

        // while the currentDay is before the current time
        while (currentDay.before(Clock.getInstance().getDateAndTimeAsCalendar())) {
            int numberOfTrips = 0;
            Set<User> users = new HashSet<>();
            double totalDistanceTravelled = 0;
            int totalTripTime = 0;

            for (TripRecord tr : tripRecordsList) {
                Calendar tripEndDate = Calendar.getInstance();
                tripEndDate.setTime(tr.getEndTime());

                // get TripRecords of the day
                if (tripEndDate.get(Calendar.DAY_OF_YEAR) == currentDay.get(Calendar.DAY_OF_YEAR)) {

                    // number of journeys on the day
                    numberOfTrips++;

                    // number of unique users on the day
                    users.add(tr.getUser());

                    // total distance travelled on the day
                    totalDistanceTravelled += Math.sqrt(Math.pow(tr.getStartDStation().getEastPos() - tr.getEndDStation().getEastPos(), 2) +
                                                        Math.pow(tr.getStartDStation().getNorthPos() - tr.getEndDStation().getNorthPos(), 2));

                    // total distance travelled on the day
                    totalTripTime += Clock.minutesBetween(tr.getStartTime(), tr.getEndTime());

                }
            }

            // Add stats of the trip if there are trips on that day
            if (numberOfTrips > 0) {
                stats.add(Clock.format(currentDay.getTime())); // Day
                stats.add(Integer.toString(numberOfTrips));    // #Journeys
                stats.add(Integer.toString(users.size()));     // #Users
                stats.add(Double.toString(totalDistanceTravelled));     // TotalDistanceTravelled
                stats.add(Float.toString((float) totalTripTime / numberOfTrips));  // AverageJourneyTime
            }

            // check next day
            currentDay.add(Calendar.DATE, 1);
        }

        display.showStats(stats);
    }

    /**
     * Handles a docked bike trigger (either AddBike or HireBike).
     */
    public Bike handleDockedBike(String bikeId, DStation dStation) {
        logger.fine("Checking if bike with id " + bikeId + " exists.");

        for (Bike bike : bikeList) {
            if(bike.getBikeId().equals((bikeId))){
                // If the bike already exists end the trip (ReturnBike).
                logger.fine("Bike with id " + bikeId + " exists.");
                endTrip(bike, dStation);
                return bike;
            }
        }

        // If a bike does not exist create a new one (AddBike).
        return addBike(bikeId);
    }


    private void endTrip(Bike bike, DStation endDStation) {
        logger.fine("Ending trip of bike with id " + bike.getBikeId() + "...");

        TripRecord tr = getActiveTripRecord(bike);
        if (tr != null) {
            tr.finaliseTripRecord(endDStation);
        }
    }

    private Bike addBike(String bikeId) {
        logger.fine("Bike with id " + bikeId + " does not exist.");

        Bike newBike = new Bike(bikeId);
        bikeList.add(newBike);

        logger.fine("New bike with id " + newBike.getBikeId() + " created.");
        return newBike;
    }

    /**
     * Handle the docking point key insertion:
     * If the key is a master key remove the bike and do not create a new record
     * else create a new record and start hire.
     */
    public boolean handleKeyInserted(Bike bike, DStation dStation, String keyId) {

        Key key = getKey(keyId);

        if(key.isMasterKey()) {
            logger.fine("Removing bike with id " + bike.getBikeId() + ".");
            bikeList.remove(bike);
            return false;
        }

        User user = getUser (key);
        if (user != null && !userHasActiveHire(user)) {
            logger.fine("Creating new trip record.");
            TripRecord tr = new TripRecord(bike, user, dStation);
            tripRecordsList.add(tr);
            return true;
        }

        return false;
    }

    /**
     * Gets the Key associated with a keyId.
     */
    private Key getKey(String keyId) {

        for (Key k : keyList) {
            if (k.getKeyId().equals(keyId)) {
                return k;
            }
        }

        return null;
    }

    /**
     * Gets the owner User of a key.
     */
    private User getUser(Key key) {
        logger.fine("Finding user with key with keyId " + key.getKeyId() + "...");

        for (User u : userList) {
            if (u.getKey().equals(key)) {
                logger.fine("Found! Key with id " + key.getKeyId() + " belongs to " + u + ".");
                return u;
            }
        }
        logger.fine("No user found");
        return null;
    }

    /**
     * Checks whether a User has active hires.
     */
    private boolean userHasActiveHire(User u) {
        logger.fine("Checking whether user " + u + " has active hires or not...");
        for (TripRecord t : tripRecordsList) {
            if (t.getUser().equals(u) && t.isActive()) {
                logger.fine("User has active hires!");
                return true;
            }
        }
        logger.fine("User does not have active hires!");
        return false;
    }

    private TripRecord getActiveTripRecord(Bike b) {
        logger.fine("Getting active TripRecord for Bike with id " + b.getBikeId() + "...");
        for (TripRecord tr : tripRecordsList) {
            if (tr.isActive() && tr.getBike().equals(b)) {
                logger.fine("Found trip record started in " + tr.getStartDStation().getInstanceName() + " on " +
                        tr.getStartTime() + " by user " + tr.getUser());
                return tr;
            } else {
                logger.warning("Error in trip record search! No active TripRecord with " + b.getBikeId() + " found!");
            }
        }
        return null;
    }

    public DStation getDStation(String instanceName) {
        return dockingStationMap.get(instanceName);
    }

    /**
     * Issue master key for personnel (from IssueMasterKeyObserver)
     */
    @Override
    public void issueMasterKey() {
        masterKeyIssuer = new KeyIssuer("mki");
        EventCollector c = terminal.getCollector();
        masterKeyIssuer.setCollector(c);

        String keyId = masterKeyIssuer.issueKey(true);

        Key key = new Key(keyId, true);
        keyList.add(key);

        logger.fine("Master key with id " + keyId + " issued.");
    }

    /**
     * Returning the last time a particular bike
     * was docked.
     */
    @Override
    public Date getBikeDockingTime(String bikeId) {
        ArrayList<TripRecord> reversedTripRecordList = tripRecordsList;
        Collections.reverse(reversedTripRecordList);

        for (TripRecord t : reversedTripRecordList) {
            if (t.getBike().getBikeId().equals(bikeId) && !t.isActive()) {
                return t.getEndTime();
            }
        }

        return null;
    }


    /**
     * Generate user activity to display at a DStation.
     */
    @Override
    public ArrayList<String> generateUserActivity(String keyId) {
        logger.fine("Generating user activity summary for user with key id " + keyId);
        Key key = getKey(keyId);
        if (key != null) {
            User user = getUser(key);

            if (user != null) {
                ArrayList<String> userActivity = new ArrayList<>();

                // Calculate midnight of today
                Calendar midnight = Clock.getInstance().getDateAndTimeAsCalendar();
                midnight.set(Calendar.HOUR_OF_DAY, 0);
                midnight.set(Calendar.MINUTE, 0);
                midnight.set(Calendar.SECOND, 0);
                midnight.set(Calendar.MILLISECOND, 0);

                // Add details of all the trips completed since prior midnight
                for (TripRecord tr : tripRecordsList) {
                    if (tr.getEndTime().after(midnight.getTime()) && !tr.isActive() && tr.getUser().equals(user)) {
                        userActivity.add(Clock.format(tr.getStartTime())); // HireTime
                        userActivity.add(tr.getStartDStation().getInstanceName()); // HireDS
                        userActivity.add(tr.getEndDStation().getInstanceName()); // ReturnDS
                        userActivity.add(Integer.toString(Clock.minutesBetween(tr.getStartTime(), tr.getEndTime()))); // Duration (min)
                    }
                }
                return userActivity;
            } else {
                logger.severe("Could not find owner user for key " + keyId);

            }
        } else {
            logger.severe("Could not find key with id " + keyId);
        }
        return null;
    }

    /**
     * Find the 5 closest DStations with available docking points closest to a given DStation.
     */
    @Override
    public ArrayList<String> findFreePoints(DStation dStation, String keyId) {
        Key key = getKey(keyId);
        User user = getUser(key);
        if (key == null || user == null || !userHasActiveHire(user)) {
            logger.warning("Error! Invalid key, or user, or no active trips.");
            return null;
        }

        Map<DStation, Double> freeDPoints = new HashMap<>();
        ArrayList<String> closestFreePoints = new ArrayList<>();

        // Calculate distances
        for (DStation ds : dockingStationMap.values()) {
            // If the DStation is not fully occupied

            if (ds.getOccupied() != ds.getDPointCount()) {
                double distance = Math.sqrt(Math.pow(ds.getEastPos() - dStation.getEastPos(), 2) +
                                            Math.pow(ds.getNorthPos() - dStation.getNorthPos(), 2));
                freeDPoints.put(ds, distance);
            }
        }

        // Find the 5 closest DStations
        int counter = 0;
        while (counter < 5 && freeDPoints.size() > 0) {
            int minDistance = Integer.MAX_VALUE;
            DStation closestDStation = null;
            for (DStation ds : freeDPoints.keySet()) {
                if (freeDPoints.get(ds) < minDistance) {
                    minDistance = freeDPoints.get(ds).intValue();
                    closestDStation = ds;
                }
            }

            if (closestDStation != null) {
                closestFreePoints.add(closestDStation.getInstanceName());
                closestFreePoints.add((Integer.toString(closestDStation.getEastPos())));
                closestFreePoints.add((Integer.toString(closestDStation.getNorthPos())));
                closestFreePoints.add((Integer.toString(closestDStation.getOccupied())));
                closestFreePoints.add((Integer.toString(closestDStation.getDPointCount())));
                counter++;
            }
            freeDPoints.remove(closestDStation);
        }

        return closestFreePoints;

    }
}
