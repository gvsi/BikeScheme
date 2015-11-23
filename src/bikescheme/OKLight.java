/**
 * 
 */
package bikescheme;

import java.util.ArrayList;
import java.util.List;

/**
 * Green OK Light output device.
 * 
 * @author pbj
 *
 */
public class OKLight extends AbstractOutputDevice {
    
    public OKLight(String instanceName) {
        super(instanceName);
    }
    
    public void flash() {
        logger.fine(getInstanceName());
        
        String deviceClass = "OKLight";
        String deviceInstance = getInstanceName();
        String messageName = "flashed";
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
