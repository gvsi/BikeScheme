package bikescheme;

import java.util.Date;
import java.util.logging.Logger;

/**
 * Created by gvsi on 25/11/2015.
 */
public class TripRecord {
    public static final Logger logger = Logger.getLogger("bikescheme");

    private Bike bike;              // The bike used in the trip

    private User user;              // The user who hire the bike
    private DStation startDStation; // The starting DStation
    private DStation endDStation;   // The DStation where the user returns the bike
    private Date startTime;         // The time of the start of the hire
    private Date endTime;           // The time of the end of the hire
    private float charges;          // The charges for the hire in £

    private boolean isActive;       // Flags whether the hire is still active or not

    public TripRecord(Bike bike, User user, DStation startDStation) {

        this.bike = bike;
        this.user = user;
        this.startDStation = startDStation;
        this.startTime = Clock.getInstance().getDateAndTime();
        this.endDStation = null;
        this.endTime = null;
        this.isActive = true;
        this.charges = 0;

        logger.fine("Initialised a new trip at" + this.startTime + " record for user " + user.getUserId() + " using bike " + bike.getBikeId() + ".");
    }

    // Updates and finalises the TripRecord
    public void finaliseTripRecord(DStation endDStation) {
        this.endDStation = endDStation;
        this.isActive = false;
        this.endTime = Clock.getInstance().getDateAndTime();

        // TODO
        this.charges = 10.5f;
    }

    public User getUser() {
        return user;
    }

    public boolean isActive() {
        return isActive;
    }

}
