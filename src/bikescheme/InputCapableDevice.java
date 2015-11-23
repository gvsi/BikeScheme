package bikescheme;

/**
 * 
 * An interface that must be implemented by any device class accepting 
 * input events.
 * 
 * Implementations should define a constructor taking an instance name as 
 * argument.
 * 
 * @author pbj
 *
 */
public interface InputCapableDevice {

    String getInstanceName();

    EventDistributor getDistributor();

    void setDistributor(EventDistributor distributor);

    void addDistributorLinks(EventDistributor distributor);

    void receiveEvent(Event e);

}