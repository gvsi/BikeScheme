/**
 * 
 */
package bikescheme;

import java.util.ArrayList;
import java.util.List;

/**
 * Device for reading bank cards and PINs and checking card authorisation.
 * Device has small screen for prompts to user such as "insert card",
 * "enter PIN" and "remove card".  Device has some wireless connection to 
 * a bank card processing centre which is used for checking card authorisations.
 * 
 * Device is modeled at an abstract level with a single prompt output operation
 * and a single checking card non-triggering input operation.
 * 
 * @author pbj
 *
 */
public class CardReader extends AbstractIODevice {

  
    
    /**
     * 
     * @param instanceName  
     */
    public CardReader(String instanceName) {
        super(instanceName);   
    }
    
     /**
     * Generate an output event requesting bank card insertion and PIN entry 
     */
    public void requestCard() {
        
        String deviceClass = "CardReader";
        String deviceInstance = getInstanceName();
        String messageName = "enterCardAndPin";
        List<String> valueList = new ArrayList<String>();
        
        logger.fine(deviceInstance);
        
        super.sendEvent(
            new Event(
                Clock.getInstance().getDateAndTime(), 
                deviceClass,
                deviceInstance,
                messageName,
                valueList));
        
    }
    
    
    /**
     * Fetch a non-triggering input event modeling the reading of a 
     * card and PIN and the authorisation of the card.
     * 
     * @return an authorisation code for the card.
     */
    public String checkCard() {
        String deviceClass = "CardReader";
        String deviceInstance = getInstanceName();
        String messageName = "checkCard";
        
        logger.fine(deviceInstance);
        
        List<String> messageArgs = 
                getDistributor().fetchMatchingEvent(
                        deviceClass, 
                        deviceInstance, 
                        messageName);
        return messageArgs.get(0);
    }
  
    
    
    
    /**
     * A single method combining the above operations of requesting and 
     * checking a card.
     * 
     * @return an authorisation code for the card read
     */
    public String readCard() {
        requestCard();
        return checkCard();
    }

}
