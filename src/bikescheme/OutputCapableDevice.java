package bikescheme;


/**
 * An interface that must be implemented by any device class generating 
 * output events.
 * 
 * Implementations should define a constructor taking an instance name as 
 * argument.
 * 
 * @author pbj
 *
 */
public interface OutputCapableDevice {

    String getInstanceName();

    void setCollector(EventCollector c);

    EventCollector getCollector();

    void sendEvent(Event e);

}