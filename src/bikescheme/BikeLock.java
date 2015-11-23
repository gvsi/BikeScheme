/**
 * 
 */
package bikescheme;

import java.util.ArrayList;
import java.util.List;

/**
 * Bike lock at a docking point
 * 
 * @author pbj
 *
 */
public class BikeLock extends AbstractOutputDevice {
    
    public BikeLock(String instanceName) {
        super(instanceName);
    }
    
    public void lock() {
        logger.fine(getInstanceName());
        
        String deviceClass = "BikeLock";
        String deviceInstance = getInstanceName();
        String messageName = "locked";
        List<String> valueList = new ArrayList<String>();
 
        super.sendEvent(
            new Event(
                Clock.getInstance().getDateAndTime(), 
                deviceClass,
                deviceInstance,
                messageName,
                valueList));
        
    }
    
    public void unlock() {
        logger.fine(getInstanceName());
        
        String deviceClass = "BikeLock";
        String deviceInstance = getInstanceName();
        String messageName = "unlocked";
        List<String> valueList = new ArrayList<String>();
 
        super.sendEvent(
            new Event(
                Clock.getInstance().getDateAndTime(), 
                deviceClass,
                deviceInstance,
                messageName,
                valueList));
        
    }
}
