/**
 * 
 */
package bikescheme;

/**
 * An abstract device supporting both event input and event output.
 * 
 * All input/output device classes should inherit from this class
 * and call the constructor to initialise the instanceName.
 * 
 * This class inherits the implementation of AbstractInputDevice 
 * and merges in a copy of the implementation of AbstractOutputDevice.  
 * It is needed because Java does not support multiple inheritance.
 * 
 * @author pbj
 *
 */
public abstract class AbstractIODevice 
        extends AbstractInputDevice
        implements OutputCapableDevice {

    private EventCollector collector;
    
    
    public AbstractIODevice(String instance) {
        super(instance);
    }
    
    
    // COPIES OF METHODS FROM AbstractOutputDevice
    
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
