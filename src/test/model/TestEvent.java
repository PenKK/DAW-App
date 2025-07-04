package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.event.Event;

import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Event class
 */
public class TestEvent {
    private Event e;
    private Event e2;
    private Date d;

    //NOTE: these tests might fail if time at which line (2) below is executed
    //is different from time that line (1) is executed.  Lines (1) and (2) must
    //run in same millisecond for this test to make sense and pass.

    @BeforeEach
    public void runBefore() {
        // Timing specific tests only work when running all tests in project, not individually
        e = new Event("Sensor open at door"); // (1)
        e2 = new Event("Sensor open at door");
        d = Calendar.getInstance().getTime(); // (2)
    }

    @Test
    public void testEvent() {
        assertEquals("Sensor open at door", e.getDescription());
        assertEquals(d, e.getDate());
    }

    @Test
    public void testToString() {
        assertEquals(d.toString() + "\n" + "Sensor open at door", e.toString());
    }

    @Test
    public void testEquals() {
        assertFalse(e.equals(null));
        assertFalse(e.equals(new Object()));
    }

    @Test
    public void testHashCode() {
        // also may not pass if not constructor not run in same millisecond
        assertEquals(e.hashCode(), e2.hashCode());
    }
}