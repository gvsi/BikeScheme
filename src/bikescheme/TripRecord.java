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
    private int charges;          // The charges for the hire in £
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

        logger.fine("Initialised a new trip record on " + this.startTime + " for " + user + " using bike with id " + bike.getBikeId() + ".");
    }

    // Updates and finalises the TripRecord
    public void finaliseTripRecord(DStation endDStation) {
        this.endDStation = endDStation;
        this.isActive = false;
        this.endTime = Clock.getInstance().getDateAndTime();
        
        if (this.endTime.after(this.startTime)) {
            long diff = this.endTime.getTime() - this.startTime.getTime();
            int diffMin = (int) (diff / 1000 / 60);
            if (diffMin <= 30) {
                this.charges = 1;
            } else {
                this.charges = 1 + 2 * (int) Math.ceil((diffMin - 30)/30.0);
            }
        }
    }

    public User getUser() {
        return user;
    }

    public Bike getBike() {
        return bike;
    }

    public DStation getStartDStation() {
        return startDStation;
    }

    public Date getStartTime() {
        return startTime;
    }


    public boolean isActive() {
        return isActive;
    }

}
