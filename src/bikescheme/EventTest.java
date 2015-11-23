package bikescheme;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

public class EventTest {

    @Test
    public void test1() {
        assertEquals(
                new Event("1 00:00,C,i,m, a, b, c, d"),
                new Event("1 00:00,C,i,m, a, b, c, d") );
    }
    @Test
    public void test2() {
        assertNotEquals(
                new Event("1 00:00,C,i,m, a, b, c, d"),
                new Event("1 00:00,C,i,m, a, b, c, e") );
    }
    
}
