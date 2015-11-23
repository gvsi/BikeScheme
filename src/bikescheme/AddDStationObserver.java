/**
 * 
 */
package bikescheme;

/**
 * 
 * Interface for an object that is handling requests to add a new docking
 * station.
 * 
 * @author pbj
 *
 */
public interface AddDStationObserver {
    public void addDStation(
            String instanceName, 
            int eastPos, 
            int northPos,
            int numPoints);
}
