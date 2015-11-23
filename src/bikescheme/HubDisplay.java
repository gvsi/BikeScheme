/**
 * 
 */
package bikescheme;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Large wall display output device in Hub operations room 
 * 
 * @author pbj
 *
 */
public class HubDisplay extends AbstractOutputDevice {
    
    public HubDisplay(String instanceName) {
        super(instanceName);
    }
    
    /**
     * Generate presentation on display of occupancy of all
     * docking stations, highlighting those with 
     * high (> 85%) or low (< 15%) occupancy.
     * 
     * Each tuple shows the status at one of the stations.
     * The tuple fields are:
     * 
     *   DSName    - docking station name
     *   East      - position in metres East (+) or West (-) 
     *   North     - position in metres North (+) or South (-)
     *   Status    - HIGH, OK or LOW.
     *   #Occupied - number of docking points occupied 
     *   #DPoints  - total number of docking points
     * 
     * @param occupancyData
     */
    public void showOccupancy(List<String> occupancyData) {
        logger.fine(getInstanceName());
        
        String deviceClass = "HubDisplay";
        String deviceInstance = getInstanceName();
        String messageName = "viewOccupancy";
        
        List<String> messageArgs = new ArrayList<String>();
        String[] preludeArgs = 
            {"unordered-tuples","6",
             "DSName","East","North","Status","#Occupied","#DPoints"};
        messageArgs.addAll(Arrays.asList(preludeArgs));
        messageArgs.addAll(occupancyData);
        
        super.sendEvent(
            new Event(
                Clock.getInstance().getDateAndTime(), 
                deviceClass,
                deviceInstance,
                messageName,
                messageArgs));
        
    }
}
