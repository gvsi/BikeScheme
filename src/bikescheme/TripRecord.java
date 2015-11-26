package bikescheme;

import java.util.Date;
import java.util.logging.Logger;

/**
 * Created by gvsi on 25/11/2015.
 */
public class TripRecord {
    public static final Logger logger = Logger.getLogger("bikescheme");

    private Bike bike;
    private User user;
    private DStation startDStation;
    private DStation endDStation;
    private Date startTime;
    private Date endTime;
    private float charges;
    private boolean isActive;

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

    public void finaliseTripRecord(DStation endDStation) {
        this.endDStation = endDStation;
        this.isActive = false;
        this.endTime = Clock.getInstance().getDateAndTime();

        // TODO
        this.charges = 10.5f;
    }



}
