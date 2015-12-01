package bikescheme;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

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

        assertEquals(e1, e2);
    }

    // Test if message arguments have header tuple
    @Test
    public void equalsTest6() {
        Event e1 = new Event("1 00:00, X, X, X, unordered-tuples, 2, H1, H2, M1.1, M1.2, M2.1, M2.2");
        Event e2 = new Event("1 00:00, X, X, X, unordered-tuples");

        assertNotEquals(e1, e2);
    }




    
}
