/**
 * 
 */
package bikescheme;


/**
 * Model of a terminal with a keyboard, mouse and monitor.
 * 
 * @author pbj
 *
 */
public class HubTerminal extends AbstractIODevice {

    /**
     * 
     * @param instanceName  
     */
    public HubTerminal(String instanceName) {
        super(instanceName);   
    }
    
    // Fields and methods for device input function
    
    private AddDStationObserver observer;
    
    public void setObserver(AddDStationObserver o) {
        observer = o;
    }
    
    /** 
     *    Select device action based on input event message
     *    
     *    @param e
     */
    @Override
    public void receiveEvent(Event e) {
        
        if (e.getMessageName().equals("addDStation") 
                && e.getMessageArgs().size() == 4) {
            
            String instanceName = e.getMessageArgs().get(0);
            int eastPos = Integer.parseInt(e.getMessageArg(1));
            int northPos =  Integer.parseInt(e.getMessageArg(2));
            int numPoints =  Integer.parseInt(e.getMessageArg(3));
            
            addDStation(instanceName, eastPos, northPos, numPoints);
            
        } else {
            super.receiveEvent(e);
        } 
    }
    /**
     * Handle request to add a new docking station
     */
    public void addDStation(
            String instanceName, 
            int eastPos, 
            int northPos,
            int numPoints) {
        logger.fine(getInstanceName());
        
        
        observer.addDStation(instanceName, eastPos, northPos, numPoints);
    }
    
    
    // Insert here support for operations generating output on the 
    // touch screen display.
    
   
}
