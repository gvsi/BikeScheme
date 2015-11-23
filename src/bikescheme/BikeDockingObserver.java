/**
 * 
 */
package bikescheme;

/**
 * 
 * Interface for any class with objects that receive bikeDocked
 * notifications from a BikeSensor device.
 * 
 * @author pbj
 *
 */
public interface BikeDockingObserver {
    public void bikeDocked(String bikeId);
}
