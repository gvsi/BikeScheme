/**
 * 
 */
package bikescheme;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Model of a touch screen input & output device.
 * 
 * @author pbj
 *
 */
public class DSTouchScreen extends AbstractIODevice {

    
    /**
     *
     * @param instanceName
     */
    public DSTouchScreen(String instanceName) {
        super(instanceName);
    }


    /**
     *    Select device action based on input event message
     *
     *    @param e
     */
    @Override
    public void receiveEvent(Event e) {

        if (e.getMessageName().equals("startReg")
                && e.getMessageArgs().size() == 1) {

            String personalDetails = e.getMessageArg(0);
            startReg(personalDetails);

        } else if (e.getMessageName().equals("viewActivity")
                    && e.getMessageArgs().size() == 0) {

            viewActivity();

        } else if (e.getMessageName().equals("findFreePoints")
                && e.getMessageArgs().size() == 0) {

            findFreePoints();

        } else {
            super.receiveEvent(e);
        }
    }

    /*
     *
     * SUPPORT FOR startReg TRIGGERING INPUT MESSAGE
     *
     */

    private StartRegObserver startRegObserver;

    public void setRegObserver(StartRegObserver o) {
        startRegObserver = o;
    }

    /**
     * Model user starting a user registration operation and entering their
     * personal details.  Pass details on to the registered observer.
     *
     * @param keyId
     */
    public void startReg(String personalDetails) {
        startRegObserver.startRegReceived(personalDetails);
    }

    /*
     *
     * SUPPORT FOR viewActivity TRIGGERING INPUT MESSAGE
     *
     */

    private ViewActivityObserver viewActivityObserver;

    public void setViewActivityObserver(ViewActivityObserver o) {
        viewActivityObserver = o;
    }

    /**
     * Model user selecting a "view activity" option to see their completed
     * trips since the previous midnight.
     *
     * @param keyId
     */
        public void viewActivity() {

        viewActivityObserver.viewActivityReceived();
    }

    private FindFreePointObserver findFreePointObserver;

    public void setFindFreePointObserver(FindFreePointObserver o) {
        findFreePointObserver = o;
    }

    /**
     * Model user selecting a "view activity" option to see their completed
     * trips since the previous midnight.
     *
     * @param keyId
     */
    public void findFreePoints() {

        findFreePointObserver.findFreePointReceived();
    }

    /*
     *
     * SUPPORT FOR showPrompt OUTPUT MESSAGE
     *
     */

    public void showPrompt(String prompt) {
        logger.fine("Showing prompt message to user: \"" + prompt + "\"");

        String deviceClass = "DSTouchScreen";
        String deviceInstance = getInstanceName();
        String messageName = "viewPrompt";

        List<String> valueList = new ArrayList<String>();
        valueList.add(prompt);


        super.sendEvent(
            new Event(
                Clock.getInstance().getDateAndTime(),
                deviceClass,
                deviceInstance,
                messageName,
                valueList));
    }

    /*
     *
     * SUPPORT FOR showPrompt OUTPUT MESSAGE
     *
     */

    public void showUserActivity(List<String> activityData) {
        logger.fine("Showing user activity to user");

        String deviceClass = "DSTouchScreen";
        String deviceInstance = getInstanceName();
        String messageName = "viewUserActivity";

        List<String> messageArgs = new ArrayList<String>();
        String[] preludeArgs =
            {"ordered-tuples","4",
             "HireTime","HireDS","ReturnDS","Duration (min)"};
        messageArgs.addAll(Arrays.asList(preludeArgs));
        messageArgs.addAll(activityData);

        super.sendEvent(
            new Event(
                Clock.getInstance().getDateAndTime(),
                deviceClass,
                deviceInstance,
                messageName,
                messageArgs));

    }

    /*
     *
     * SUPPORT FOR showPrompt OUTPUT MESSAGE
     *
     */

    public void showFreePoints(List<String> freeDockingPoints) {
        logger.fine("Showing free docking points to user");

        String deviceClass = "DSTouchScreen";
        String deviceInstance = getInstanceName();
        String messageName = "viewFreePoints";

        List<String> messageArgs = new ArrayList<String>();
        String[] preludeArgs =
                {"ordered-tuples","5",
                        "DSName","East","North","#Occupied","#DPoints"};
        messageArgs.addAll(Arrays.asList(preludeArgs));
        messageArgs.addAll(freeDockingPoints);

        super.sendEvent(
                new Event(
                        Clock.getInstance().getDateAndTime(),
                        deviceClass,
                        deviceInstance,
                        messageName,
                        messageArgs));

    }




}
