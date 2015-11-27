package bikescheme;

import java.util.Date;

/**
 * Created by martin on 27/11/15.
 */
public class FaultButton extends AbstractInputDevice {

    public FaultButton(String instance) {
        super(instance);
    }

    private FaultButtonObserver observer;

    /**
     * @param o
     */
    public void setObserver(FaultButtonObserver o) {
        observer = o;
    }

    /**
     *    Select device action based on input event message
     *
     *    @param e
     */
    @Override
    public void receiveEvent(Event e) {

        if (e.getMessageName().equals("reportFault")
                && e.getMessageArgs().size() == 0) {

            logger.fine("Report fault button pressed on DPoint " + e.getDeviceInstance());
            press(e.getDate());

        } else {
            super.receiveEvent(e);
        }
    }

    private void press(Date date) {
        observer.pressed(date);
    }


}
