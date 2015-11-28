/**
 * 
 */
package bikescheme;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Large wall display output device in Hub operations room 
 * 
 * @author pbj
 *
 */
public class BankServer extends AbstractOutputDevice {

    public BankServer(String instanceName) {
        super(instanceName);
    }
    
    /**
     * Charge users for their trips in the last 24 hours.
     * 
     * Each tuple shows the transactions.
     * The tuple fields are:
     * 
     *   User Name                   - user charged
     *   Bank authorisation code     - the card's authorisation code
     *   Amount charged              - amount of pounds the user is charged.

     * 
     * @param chargeData
     */

    public void chargeUsers(List<String> chargeData) {
        String deviceClass = "BankServer";
        String deviceInstance = getInstanceName();
        String messageName = "chargeUsers";
        
        List<String> messageArgs = new ArrayList<String>();
        String[] preludeArgs = 
            {"unordered-tuples","3",
             "User Name","Bank authorisation code","Amount charged"};
        messageArgs.addAll(Arrays.asList(preludeArgs));
        messageArgs.addAll(chargeData);
        
        super.sendEvent(
            new Event(
                Clock.getInstance().getDateAndTime(), 
                deviceClass,
                deviceInstance,
                messageName,
                messageArgs));
        
    }
}
