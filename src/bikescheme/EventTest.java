package bikescheme;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class EventTest {

    // Check that when the objects are from different classes they are not equal
    @Test
    public void equalsTest1() {
        Event e = new Event("1 00:00, X, X, X");
        String s = new String("1 00:00, X, X, X");

        assertNotEquals(e,s);
    }

    // Check the first 4 arguments
    @Test
    public void equalsTest2() {
        Event e1 = new Event("1 00:00, X, X, X");
        Event e2 = new Event("1 00:00, X, X, X");
        Event e3 = new Event("1 00:00, Y, X, X");
        Event e4 = new Event("1 00:00, X, Y, X");
        Event e5 = new Event("1 00:00, X, X, Y");
        Event e6 = new Event("1 00:01, X, X, X");

        assertEquals(e1, e2);
        assertNotEquals(e1, e3);
        assertNotEquals(e1, e4);
        assertNotEquals(e1, e5);
        assertNotEquals(e1, e6);
    }


    // Check the case when the events message arguments are not well formatted tuples
    @Test
    public void equalsTest3() {
        Event e1 = new Event("1 00:00, X, X, M1");
        Event e2 = new Event("1 00:00, X, X, M1");
        Event e3 = new Event("1 00:00, X, X, M2");
        Event e4 = new Event("1 00:00, X, X, M1, M2");

        assertEquals(e1, e2);
        assertNotEquals(e1, e3);
        assertNotEquals(e1, e4);
    }

    // Test message arguments for equal length
    @Test
    public void equalsTest4() {
        Event e1 = new Event("1 00:00, X, X, X, unordered-tuples, 2, H1, H2, M1.1, M1.2, M2.1, M2.2");
        Event e2 = new Event("1 00:00, X, X, X, unordered-tuples, 2, H1, H2, M1.1, M1.2, M2.1, M2.2");
        Event e3 = new Event("1 00:00, X, X, X, unordered-tuples, 2, H1, H2, M1.1, M1.2");

        assertEquals(e1, e2);
        assertNotEquals(e1, e3);
    }

    // Test if message arguments are tuple complete
    @Test
    public void equalsTest5() {
        Event e1 = new Event("1 00:00, X, X, X, unordered-tuples, 2, H1, H2, M1.1, M1.2, M2.1, M2.2");
        Event e2 = new Event("1 00:00, X, X, X, unordered-tuples, 2, H1, H2, M1.1, M1.2, M2.1");

        assertNotEquals(e1, e2);
    }

    // Test if message arguments have header tuple
    @Test
    public void equalsTest6() {
        Event e1 = new Event("1 00:00, X, X, X, unordered-tuples, 2, H1, H2, M1.1, M1.2, M2.1, M2.2");
        Event e2 = new Event("1 00:00, X, X, X, unordered-tuples");

        assertNotEquals(e1, e2);
    }

    // Check leading arguments (first two args and header tuple
    // of each arg list) for equality.
    @Test
    public void equalsTest7() {
        Event e1 = new Event("1 00:00, X, X, X, unordered-tuples, 2, H1, H2");
        Event e2 = new Event("1 00:00, X, X, X, unordered-tuples, 2, H1, H2");
        Event e3 = new Event("1 00:00, X, X, X, unordered-tuples, 2, H1,  X");
        Event e4 = new Event("1 00:00, X, X, X, unordered-tuples, 2,  X, H2");

        assertEquals(e1, e2);
        assertNotEquals(e1, e3);
        assertNotEquals(e1, e4);
    }

    // Testing - O(n^2) multiset equality test for the tuples
    @Test
    public void equalsTest8() {
        Event e1 = new Event("1 00:00, X, X, X, unordered-tuples, 2, H1, H2, M1.1, M1.2, M2.1, M2.2");
        Event e2 = new Event("1 00:00, X, X, X, unordered-tuples, 2, H1, H2, M1.1, M1.2, M2.1, M2.2");
        Event e3 = new Event("1 00:00, X, X, X, unordered-tuples, 2, H1, H2, M1.1, M1.2, X, M2.2");
        Event e4 = new Event("1 00:00, X, X, X, unordered-tuples, 2, H1, H2, M1.1, M1.2, M2.1, X");
        Event e5 = new Event("1 00:00, X, X, X, unordered-tuples, 2, H1, H2, X, M1.2, M2.1, M2.2");
        Event e6 = new Event("1 00:00, X, X, X, unordered-tuples, 2, H1, H2, M1.1, X, M2.1, M2.2");

        assertEquals(e1, e2);
        assertNotEquals(e1, e3);
        assertNotEquals(e1, e4);
        assertNotEquals(e1, e5);
        assertNotEquals(e1, e6);
    }

    // Testing different sizes of event lists
    @Test
    public void listEqualsTest1() {
        List<Event> es1 = new ArrayList<>();
        List<Event> es2 = new ArrayList<>();
        List<Event> es3 = new ArrayList<>();

        Event e1 = new Event("1 00:00, X, X, X");
        Event e2 = new Event("1 00:00, X, X, X");

        es1.add(e1);
        es1.add(e2);

        es2.add(e1);
        es2.add(e2);

        es3.add(e1);

        assertEquals(es1, es2);
        assertNotEquals(es1, es3);
    }

    // Testing different dates and times of event lists
    @Test
    public void listEqualsTest2() {
        List<Event> es1 = new ArrayList<>();
        List<Event> es2 = new ArrayList<>();
        List<Event> es3 = new ArrayList<>();

        es1.add(new Event("1 00:00, X, X, X"));
        es1.add(new Event("1 00:00, X, X, X"));

        es2.add(new Event("1 00:00, X, X, X"));
        es2.add(new Event("1 00:00, X, X, X"));

        es3.add(new Event("1 00:00, X, X, X"));
        es3.add(new Event("1 10:00, X, X, X"));

        assertEquals(es1, es2);
        assertNotEquals(es1, es3);
    }

    // Test the equality check for range over all event lists
    @Test
    public void listEqualsTest3() {
        List<Event> es1 = new ArrayList<>();
        List<Event> es2 = new ArrayList<>();
        List<Event> es3 = new ArrayList<>();
        List<Event> es4 = new ArrayList<>();
        List<Event> es5 = new ArrayList<>();

        es1.add(new Event("1 00:00, X, X, X, unordered-tuples, 2, H1, H2, M1.1, M1.2, M2.1, M2.2"));
        es1.add(new Event("1 00:00, X, X, X, unordered-tuples, 2, H1, H2, M1.1, M1.2, M2.1, M2.2"));
        es1.add(new Event("1 00:00, X, X, X, unordered-tuples, 2, H1, H2, M1.1, M1.2, M2.1, M2.2"));

        es2.add(new Event("1 00:00, X, X, X, unordered-tuples, 2, H1, H2, M1.1, M1.2, M2.1, M2.2"));
        es2.add(new Event("1 00:00, X, X, X, unordered-tuples, 2, H1, H2, M1.1, M1.2, M2.1, M2.2"));
        es2.add(new Event("1 00:00, X, X, X, unordered-tuples, 2, H1, H2, M1.1, M1.2, M2.1, M2.2"));

        es3.add(new Event("1 00:00, X, X, X, unordered-tuples, 2, H1, H2, X, M1.2, M2.1, M2.2"));
        es3.add(new Event("1 00:00, X, X, X, unordered-tuples, 2, H1, H2, M1.1, M1.2, M2.1, M2.2"));
        es3.add(new Event("1 00:00, X, X, X, unordered-tuples, 2, H1, H2, M1.1, M1.2, M2.1, M2.2"));

        es4.add(new Event("1 00:00, X, X, X, unordered-tuples, 2, H1, H2, M1.1, M1.2, M2.1, M2.2"));
        es4.add(new Event("1 00:00, X, X, X, unordered-tuples, 2, H1, H2, M1.1, X, M2.1, M2.2"));
        es4.add(new Event("1 00:00, X, X, X, unordered-tuples, 2, H1, H2, M1.1, M1.2, M2.1, M2.2"));

        es5.add(new Event("1 00:00, X, X, X, unordered-tuples, 2, H1, H2, M1.1, M1.2, M2.1, M2.2"));
        es5.add(new Event("1 00:00, X, X, X, unordered-tuples, 2, H1, H2, M1.1, M1.2, M2.1, M2.2"));
        es5.add(new Event("1 00:00, X, X, X, unordered-tuples, 2, H1, H2, M1.1, M1.2, X, M2.2"));



        assertEquals(es1, es2);
        assertNotEquals(es1, es3);
        assertNotEquals(es1, es4);
        assertNotEquals(es1, es5);

    }

    
}
