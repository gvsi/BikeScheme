/**
 * 
 */
package bikescheme;

/**
 * Interface for any class with objects that are receiving start registration
 * notifications from DSTouchScreen IO devices.
 * 
 * @author pbj
 *
 */
public interface StartRegObserver {
    
    void startRegReceived(String s);

}
