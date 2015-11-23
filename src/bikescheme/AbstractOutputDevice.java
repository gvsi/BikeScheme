/**
 * 
 */
package bikescheme;

import java.util.logging.Logger;

/**
 * An abstract device supporting event output, but not event input.
 * 
 * All output-only device classes should inherit from this class
 * and should themselves have a constructor which takes an instance name as 
 * argument.
 * 
 * @author pbj
 *
 */
public abstract class AbstractOutputDevice implements OutputCapableDevice {
    
    public static final Logger logger = Logger.getLogger("bikescheme");
    
    private String instanceName;
    private EventCollector collector;
    
    public AbstractOutputDevice(String instanceName) {
        this.instanceName = instanceName;
    }
    
    public String getInstanceName() {
        return instanceName;
    }
    
    // FOLLOWING METHODS COPIED IN AbstractIODevice
    
    public void setCollector(EventCollector c) {
        collector = c;
    }
    
    public EventCollector getCollector() {
        return collector;
    }
    
    public void sendEvent(Event e) {
        collector.collectEvent(e);
    }

}
