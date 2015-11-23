/**
 * 
 */
package bikescheme;

import java.util.ArrayList;
import java.util.List;

/**
 * Output device for issuing contactless electronic keys for use by bike hire 
 * scheme users.
 * 
 * @author pbj
 *
 */
public class KeyIssuer extends AbstractOutputDevice {
    int keyNum;
    
    public KeyIssuer(String instanceName) {
        super(instanceName);
        keyNum = 1;
    }
    
    public String issueKey() {
        logger.fine(getInstanceName());
        
        String deviceClass = "KeyIssuer";
        String deviceInstance = getInstanceName();
        String messageName = "keyIssued";
        String newKeyId = getInstanceName() + "-" + keyNum;
        keyNum++;
        
        List<String> valueList = new ArrayList<String>();
        valueList.add(newKeyId); 
        
        
        super.sendEvent(
            new Event(
                Clock.getInstance().getDateAndTime(), 
                deviceClass,
                deviceInstance,
                messageName,
                valueList));
        
        return newKeyId;
        
    }
}
