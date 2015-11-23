/**
 * 
 */
package bikescheme;

/**
 * Interface for objects with a run() method that must be invoked
 * at regular scheduled times.
 *  
 * @author pbj
 *
 */
public interface TimedNotificationObserver {
    
    void processTimedNotification();

}
