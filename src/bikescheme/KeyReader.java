/*
 * Copyright: pbj
 * 
 */
package bikescheme;

import java.util.List;

/**
 * Input device for reading contactless keys
 * 
 * @author pbj
 *
 */
public class KeyReader extends AbstractInputDevice {


    private KeyInsertionObserver observer;
    
    /**
     * @param instanceName  
     */
    public KeyReader(String instanceName) {
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
    public void setObserver(KeyInsertionObserver o) {
        observer = o;
    }
    
    /** 
     *    Select device action based on input event message
     *    
     *    @param e
     */
    @Override
    public void receiveEvent(Event e) {
        
        if (e.getMessageName().equals("insertKey") 
                && e.getMessageArgs().size() == 1) {
            
            String keyId = e.getMessageArg(0);
            insertKey(keyId);
            
        } else {
            super.receiveEvent(e);
        }
    }
    
    /**
     * Model insert key operation on a key reader object
     * 
     * @param keyId
     */
    public void insertKey(String keyId) {
        logger.fine(getInstanceName());
        
        observer.keyInserted(keyId);
    }

    /*
     * 
     *  METHOD FOR HANDLING NON-TRIGGERING INPUT
     *  
     */
    
    /**
     * Fetch a non-triggering input event indicating that a key has
     * been inserted into the key reader.
     * 
     * @return the key Id.
     */
    public String waitForKeyInsertion() {
        String deviceClass = "KeyReader";
        String deviceInstance = getInstanceName();
        String messageName = "keyInsertion";
        
        logger.fine(deviceInstance);
        
        List<String> messageArgs = 
                getDistributor().fetchMatchingEvent(
                        deviceClass, 
                        deviceInstance, 
                        messageName);
        return messageArgs.get(0);
    }
  


}
