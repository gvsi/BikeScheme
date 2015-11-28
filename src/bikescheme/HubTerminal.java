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
    
    private AddDStationObserver addStationObserver;
    private IssueMasterKeyObserver issueMasterKeyOserver;
    private ViewStatsObserver viewStatsObserver;
    
    public void setAddStationObserver(AddDStationObserver o) {
        addStationObserver = o;
    }

    public void setIssueMasterKeyObserver(IssueMasterKeyObserver o) {
        issueMasterKeyOserver = o;
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
            
        } else if (e.getMessageName().equals("issueMasterKey")
                && e.getMessageArgs().size() == 0){

            issueMasterKey();

        }  else if (e.getMessageName().equals("viewStats")
                && e.getMessageArgs().size() == 0){

            viewStats();

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
        logger.fine("DStation " + instanceName + " created.");


        addStationObserver.addDStation(instanceName, eastPos, northPos, numPoints);
    }

    /**
     * Handle request to create a new master key
     */
    private void issueMasterKey() {
        logger.fine("Initiating master key dispense procedure.");

        issueMasterKeyOserver.issueMasterKey();
    }

    /**
     * Handle request to view stats of the schema
     */
    private void viewStats() {
        logger.fine("Initiating master key dispense procedure.");

        viewStatsObserver.viewStats();
    }
    
    
    // Insert here support for operations generating output on the 
    // touch screen display.

}
