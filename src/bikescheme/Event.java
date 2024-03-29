/**
 * 
 */
package bikescheme;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * An event is a message exchanged between an interface device and the world
 * outside the system.
 * 
 * The components of an event object are:
 * 
 * 1. A time at which the event is exchanged.
 * 
 * 2. The name of the Java class for the input-capable device to receive the
 *    event from the outside world, or the name of the Java class for the
 *    output-capable device sending the event to the outside world.
 * 
 * 3. An instance name to distinguish different devices of the same class.
 * 
 * 4. A name of the message.
 * 
 * 5. 0 or more message arguments, each represented using a string.
 * 
 * 
 * For convenience, a concrete syntax is supported for defining an event as a 
 * single string, and methods are provided for converting back and forth
 * between Event objects and these strings. 
 * 
 * In the concrete syntax, the string is composed of fields which are
 * separated by "," characters.  There is one field for each component 1-4
 * above and one for each message argument.
 * 
 * Whitespace (spaces, linebreaks) around each "," and at the beginning and of
 * the event string is ignored.
 * 
 * Dates are of the form
 * 
 *    d HH:mm
 * 
 * where
 * 
 *   d is the day (1-31), HH is the hour (00-23), mm is the minute (00-59).
 * 
 * To keep the syntax simple, month and year information is not tracked.
 * 
 * The message arguments are sometimes a list of tuples.  For example, if
 * information on a nearby docking point is stored in 5-tuples with 
 * components
 * 
 * - docking-station-name
 * - North-location
 * - East-location
 * - number-free-points
 * - total-number-points
 * 
 * then the information for 2 nearby stations might be displayed using 
 * the sequence of message arguments.
 * 
 *   DSName,    East,    North,    FreeDPs,   TotalDPs,
 *   A,        500,    -200,      3,          10,
 *   B,        300,     500,     7,           8
 * 
 * Tuple list are always expected to have a header tuple such as here, to
 * make the lists legible.
 * 
 * When tests compare events that have list-of-tuples arguments, the ordering of 
 * the tuples after the header tuple needs to be ignored.  To enable this, if arguments are a 
 * list of tuples, 2 special arguments are given first, the string "unordered-tuples"
 * and an integer for the tuple size.  
 * 
 * These arguments are also used to 
 * trigger printing of tuples on separate lines to make them easier to read.
 * Printing also recognises the special first argument "ordered-tuples", again 
 * with the second argument indicating the tuple size.
 * 
 * 
 * @author pbj
 *
 *
 *
 *
 *
 */
public class Event {
    
        
    
    private Date date;
    private String deviceClass;
    private String deviceInstance;
    private String messageName;
    private List<String> messageArgs;
    
    
    /** 
     * Construct an Event object by parsing string representation of the event
     * 
     * 
     * @param s
     */
    public Event(String eventString) {
        
        
        List<String> eventFields = splitIntoFields(eventString);
        
        date = Clock.parse(eventFields.get(0));
        deviceClass = eventFields.get(1);
        deviceInstance = eventFields.get(2);
        messageName = eventFields.get(3);
        messageArgs = eventFields.subList(4, eventFields.size());
        
        
    }
    /** Construct an Event object from its components
     * 
     * @return
     */
    public Event(
            Date date, 
            String deviceClass,
            String deviceInstance,
            String messageName,
            List<String> messageArgs) {
        this.date = date;
        this.deviceClass = deviceClass;
        this.deviceInstance = deviceInstance;
        this.messageName = messageName;
        this.messageArgs = messageArgs;
   }
    
    public Date getDate() {
        return date;
    }
    
    public String getDeviceClass() {
        return deviceClass;
    }
    
    public String getDeviceInstance() {
        return deviceInstance;
    }
    
    public List<String> getMessageArgs() {
        return messageArgs;
    }
    
    public String getMessageArg(int i) {
        return messageArgs.get(i);        
    }
    
    public int getNumMessageArgs() {
        return messageArgs.size();        
    }
    
    public String getMessageName() {
        return messageName;
    }
    
    
    public String toString() {
        StringBuilder eventStrings = new StringBuilder();
        eventStrings.append(Clock.format(date));
        eventStrings.append(", ");
        eventStrings.append(deviceClass);
        eventStrings.append(", ");
        eventStrings.append(deviceInstance);
        eventStrings.append(", ");
        eventStrings.append(messageName);
        
        boolean hasTupleListArgs = false;
        
        if (messageArgs.size() >= 2 
                && (messageArgs.get(0).equals("unordered-tuples")
                    || messageArgs.get(0).equals("ordered-tuples"))) {
            int tupleSize = 0;
            try {
                tupleSize = Integer.parseInt(messageArgs.get(1));
            } catch (NumberFormatException e) {
               
            }
            if (tupleSize > 0) {
                hasTupleListArgs = true;
            
                eventStrings.append(", ");
                eventStrings.append(messageArgs.get(0));
                eventStrings.append(", ");
                eventStrings.append(messageArgs.get(1));
                
                // Determine maximum width for field at each tuple position
                int[] fieldWidths = new int[tupleSize];
                for (int i = 2; i < messageArgs.size(); i++ ) {
                    int tupleIndex = (i - 2) % tupleSize; 
                    if (messageArgs.get(i).length() > fieldWidths[tupleIndex]) {
                        fieldWidths[tupleIndex] = messageArgs.get(i).length();
                    }
                }
                
                // Generate formatted tuples
                for (int i = 2; i < messageArgs.size(); i++) {
                    eventStrings.append(", ");
                    
                    // Insert linebreak before tuple start
                    if ((i - 2) % tupleSize == 0) {
                        eventStrings.append(System.getProperty("line.separator"));
                        eventStrings.append("    ");                    
                    }
                    
                    
                    // Add left padding to fieldWidth
                    int padding = fieldWidths[(i - 2) % tupleSize] 
                            - messageArgs.get(i).length();
                    for (int k = 0; k < padding; k++) {
                        eventStrings.append(" ");
                    }
                    
                    eventStrings.append(messageArgs.get(i));
                    
                }
            }
        }
        if (! hasTupleListArgs) {
            for (String s : messageArgs) {
                eventStrings.append(", ");
                eventStrings.append(s);
            }
        }
        return eventStrings.toString();
    }
    
    
    
    /**
     * Equality method treats message arguments that are lists of tuples
     * as sets of tuples.
     */
    @Override 
    public boolean equals(Object o) {
        
        if (! (o instanceof Event)) {
            return false;
        }
        Event e = (Event) o;
        if (! (date.equals(e.date)
              && deviceClass.equals(e.deviceClass)
              && deviceInstance.equals(e.deviceInstance)
              && messageName.equals(e.messageName) )) {
            return false;
        }
        
        // Fall-back result for when don't have well-formed tuple lists
        boolean messageArgsEqual = messageArgs.equals(e.messageArgs);
            
        if (messageArgs.size() >= 2 && messageArgs.get(0).equals("unordered-tuples")) {
            int tupleSize = 0;
            try {
                tupleSize = Integer.parseInt(messageArgs.get(1));
            } catch (NumberFormatException ex) {
               
            }
            if (tupleSize >= 1) {
                boolean argsEqualLength = messageArgs.size() == e.messageArgs.size();
                boolean lastTupleComplete = (messageArgs.size() - 2) % tupleSize == 0;
                boolean headerTuplePresent = messageArgs.size() >= 2 + tupleSize;
                
                if (argsEqualLength && lastTupleComplete && headerTuplePresent) {
                    // Tuple lists are well-formed
                    List<List<String> > tuples = new ArrayList<List <String> >();
                    List<List<String> > eTuples = new ArrayList<List <String> >();

                    // Check leading arguments (first two args and header tuple
                    // of each arg list) for equality.

                        List<String> leadingArgs = messageArgs.subList(0, 2 + tupleSize);
                        List<String> eLeadingArgs = e.messageArgs.subList(0, 2 + tupleSize);
                        if (! leadingArgs.equals(eLeadingArgs) ){
                            return false;
                        }

                    // Gather rest of tuples from each arg list.


                    for (int i = 2 + tupleSize; i < messageArgs.size(); i = i + tupleSize) {

                        tuples.add(messageArgs.subList(i, i + tupleSize));
                        eTuples.add(e.messageArgs.subList(i, i + tupleSize));
                    }

                    // O(n^2) multiset equality test for rest of tuples
                    for (List<String> tuple : tuples) {

                        int countInTuples = Collections.frequency(tuples, tuple);
                        int countInETuples = Collections.frequency(eTuples, tuple);

                        if (countInTuples != countInETuples)  {
                            return false;
                        }
                    }
                    return true;
                }   
            }
        }
        return messageArgsEqual;
                
    }
    
    private static List<String> splitIntoFields(String s) {
        return Arrays.asList(s.trim().split("\\p{Space}*,\\p{Space}*"));
    }
    
    /**
     * 
     * Compare 2 event lists, using multiset equality for sub-ranges with 
     * equal timestamps.  Assumes lists are ordered in time. 
     * 
     * @param s1
     * @param s2
     */
    public static boolean listEqual(List<Event> es1, List<Event> es2) {
        if (es1.size() != es2.size()) {
            return false;
        }
        int i = 0;
        while (i < es1.size()) {
            // set j so that es1.get(i).date, ... es1.get(j-1).date are same
            // and either j - 1 is last element or es.get(i).date != es1.get(j)
            int j = i + 1;
            while (j < es1.size() && es1.get(j).date.equals(es1.get(i).date)) {
                j++;
            }
            // Do multiset equality check for range i .. j -1 of es1 and es2.
            for (int k = i; k < j; k++) {
               int eventKCountInEs1 = Collections.frequency(es1.subList(i, j), es1.get(k));
               int eventKCountInEs2 = Collections.frequency(es2.subList(i, j), es1.get(k));
               if (eventKCountInEs1 != eventKCountInEs2) {
                   return false;
               }
                        
            }
            i = j;
            
        }
        // If we get to here, lists must be the same, modulo rearranging 
        // events with equal dates.
        return true;
        
    }
   
    
}
