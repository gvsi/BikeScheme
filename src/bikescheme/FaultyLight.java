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
public class FaultyLight extends AbstractOutputDevice {

    private boolean isOn;
    public FaultyLight(String instanceName) {
        super(instanceName);
        isOn = false;
    }
    
    public void turnOn() {

        String deviceClass = "FaultyLight";
        String deviceInstance = getInstanceName();
        String messageName = "on";
        List<String> valueList = new ArrayList<String>();
 
        super.sendEvent(
            new Event(
                Clock.getInstance().getDateAndTime(), 
                deviceClass,
                deviceInstance,
                messageName,
                valueList));
        
    }

    public void turnOff() {

        String deviceClass = "FaultyLight";
        String deviceInstance = getInstanceName();
        String messageName = "off";
        List<String> valueList = new ArrayList<String>();

        super.sendEvent(
                new Event(
                        Clock.getInstance().getDateAndTime(),
                        deviceClass,
                        deviceInstance,
                        messageName,
                        valueList));

    }

    public boolean isOn() {
        return isOn;
    }
}
