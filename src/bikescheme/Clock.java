/**
 * 
 */
package bikescheme;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 
 * This is a singleton class.
 * 
 * The clock object holds current time, as set by the most recent input event.
 * 
 * It also manages observers who want to receive notifications at regular
 * intervals of time.  Observers must implement the 
 * TimedNotificationObserver interface and use the schedule() method to 
 * register with the clock. 
 * 
 * Before the first input event the clock is set to 1 Jan 1970 00:00
 * 
 * The class assumes that elsewhere in the system the values of date objects 
 * are never mutated.
 * 
 * Various static utility methods are provided, including:
 * 
 * - Clock getInstance()  to fetch the singleton instance
 * - String format(Date) 
 * - Date parse(String)
 *
 * 
 * @author pbj
 *
 */

public class Clock extends AbstractInputDevice {
 
    private class TimedNotification {
        private TimedNotificationObserver observer;
        private long start;
        private long interval;
        
        public TimedNotification(
                TimedNotificationObserver observer,
                Date startDate,
                int intervalHours,
                int intervalMinutes
                ) {
            this.observer = observer;
            this.start = startDate.getTime();
            this.interval = 1000 * 60 * (intervalMinutes + 60 * intervalHours);
        }
        
        /**
         * Send notification to observer if it is time to do so.
         */
        public void sendNotification() {
            if ( (dateAndTime.getTime() - start) % interval == 0) {
                observer.processTimedNotification();
            }     
        }
        
                
    }
        
    private static final DateFormat dateFormat;
    private static final Date startDate;
    private static final Date errorDate;
    private static Clock instance;
       
    private List<TimedNotification> timedNotifications;
    private Date dateAndTime;
   
    static {  
        dateFormat = new SimpleDateFormat("d HH:mm");

        startDate = new Date(0L); // 1 Jan 1970, 00:00
        
        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.set(1999, 12, 31, 23, 59);
        errorDate = cal.getTime();   
        
         
        instance = new Clock();
        
    }
    
    private Clock() {
        super("clk");  // Set instance name to "clk"
        timedNotifications = new ArrayList<TimedNotification>();
        dateAndTime = startDate;
    }
        
    /**
     * Set date and time. 
     * 
     * @param d
     */
    public void setDateAndTime(Date d) {
        dateAndTime = d;
        
    }
    
    /**
     * Get current simulation date and time as a Date object
     *  
     * @return
     */
    public Date getDateAndTime() {
        return dateAndTime;
    }

    /**
     * Get current simulation date and time as a Calendar object
     *  
     * @return
     */
    
    public Calendar getDateAndTimeAsCalendar() {
  
        Calendar c = Calendar.getInstance();
        c.setTime(dateAndTime);
        return c;
    }
    
 
    private void sendNotifications() {
        logger.fine(getInstanceName());
        
        for (TimedNotification t : timedNotifications) {
            t.sendNotification();
        }
    }
    
    
    /**
     * Select device action based on input event message
     *   
     * @param e
     */
    @Override
    public void receiveEvent(Event e) {
        if (e.getMessageName().equals("tick") 
                && e.getMessageArgs().size() == 0) {
            
            sendNotifications();
            
        } else {
            super.receiveEvent(e);;
        }
    }
       
    public static Clock getInstance() {
        return instance;
    }
     
    public static String format(Date d) {
        return dateFormat.format(d);
    }
    
    public static Date parse(String s) {
        // Catch exception and return error value to avoid lots of 
        // throws clauses for ParseException
        try {
            return dateFormat.parse(s);
        } catch (ParseException e) {     
            return errorDate; 
        }
    }
    
    public static Date getStartDate() {
        return startDate;
    }
    
    public static Date getErrorDate() {
        return errorDate;
    }
    public static int minutesBetween(Date startDate, Date endDate) {
        long minutes = 
                (endDate.getTime() - startDate.getTime()) / (1000 * 60);
        return (int) minutes;
    }
    
    /**
     * Schedule a notification to start at given time and repeat every
     * intervalHours:intervalMinutes.
     * 
     * Notifications are sent out at times in the future.  A notification
     * is not sent out if start time is same as current time.
     * 
     * out immediately.
     * @param task
     * @param startTime
     * @param intervalHours
     * @param intervalMinutes
     */
    public void scheduleNotification(
            TimedNotificationObserver task, 
            Date startTime, 
            int intervalHours,
            int intervalMinutes) {
        
        timedNotifications.add(
                new TimedNotification(
                        task,
                        startTime,
                        intervalHours,
                        intervalMinutes));       
    }
}
