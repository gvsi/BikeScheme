package bikescheme;

/**
 *
 * Interface for any class with objects that receive bikeDocked
 * notifications from a BikeSensor device.
 *
 * @author gvsi
 *
 */
public interface FindFreePointObserver {
    void findFreePointReceived();
}

