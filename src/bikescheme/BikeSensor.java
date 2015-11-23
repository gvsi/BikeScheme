/*
 * Copyright: pbj
 * 
 */
package bikescheme;

/**
 * Input device for reading contactless keys
 * 
 * @author pbj
 *
 */
public class BikeSensor extends AbstractInputDevice {


    private BikeDockingObserver observer;
    
    /**
     * @param instanceName  
     */
    public BikeSensor(String instanceName) {
        super(instanceName);   
    }
    
    /*
     * 
     *  METHODS FOR HANDLING TRIGGERING INPUT
     *  
     */
    
    /**
     * @param o
     */
    public void setObserver(BikeDockingObserver o) {
        observer = o;
    }
    
    /** 
     *    Select device action based on input event message
     *    
     *    @param e
     */
    @Override
    public void receiveEvent(Event e) {
        
        if (e.getMessageName().equals("dockBike") 
                && e.getMessageArgs().size() == 1) {
            
            String bikeId = e.getMessageArg(0);
            dockBike(bikeId);
            
        } else {
            super.receiveEvent(e);
        }
    }
    
    /**
     * Model insert key operation on a key reader object
     * 
     * @param keyId
     */
    public void dockBike(String keyId) {
        logger.fine(getInstanceName());
        
        observer.bikeDocked(keyId);
    }

    /*
     * 
     *  METHODS FOR HANDLING NON-TRIGGERING INPUT
     *  
     */
    
 


}
