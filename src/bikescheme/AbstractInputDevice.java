/**
 * 
 */
package bikescheme;

import java.util.logging.Logger;

/**
 * An abstract device supporting event input, but not event output.
 * 
 * All input-only device classes should inherit from this class
 * and call the constructor to initialise the instanceName.
 * 
 * @author pbj
 *
 */
public abstract class AbstractInputDevice implements InputCapableDevice {
    private static final String LS = System.getProperty("line.separator");
    public static final Logger logger = Logger.getLogger("bikescheme");
    
    private String instanceName;
    private EventDistributor distributor;
    
    public AbstractInputDevice(String instance) {
        instanceName = instance;
    }
    
    public String getInstanceName() {
        return instanceName;
    }
     
    public EventDistributor getDistributor() {
        return distributor;
    }

    public void setDistributor(EventDistributor distributor) {
        this.distributor = distributor;
    }
    
    public void addDistributorLinks(EventDistributor distributor) {
        setDistributor(distributor);
        distributor.addInputCapableDevice(this);
    }

    /**
     * Dummy method for receiving triggering input events from distributor
     * 
     * @param e
     */
    public void receiveEvent(Event e) {
        String errorMessage = 
                this.getClass().getSimpleName() + " " + e.getDeviceInstance() 
                + " received unrecognised event: " + LS
                + e;
        
        logger.warning(errorMessage);
        
        throw new RuntimeException(errorMessage);
        
    }


    

}
