package model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestNote {
    Note note;

    @BeforeEach
    void runBefore() {
        note = new Note(60, 60, 0, 5);
    }

    @Test
    void testConstructor() {
        assertEquals(60, note.getPitch());
        assertEquals(60, note.getVelocity());
        assertEquals(0, note.getStartTick());
        assertEquals(5, note.getDurationTicks());
    }

    @Test
    void testSetters() {
        note.setDurationTicks(100);
        assertEquals(note.getDurationTicks(), 100);
        note.setDurationTicks(3);
        assertEquals(note.getDurationTicks(), 3);

        note.setPitch(100);
        assertEquals(note.getPitch(), 100);
        note.setPitch(65);
        assertEquals(note.getPitch(), 65);

        note.setVelocity(99);
        assertEquals(note.getVelocity(), 99);
        note.setVelocity(33);
        assertEquals(note.getVelocity(), 33);

        note.setStartTick(43);
        assertEquals(note.getStartTick(), 43);
        note.setStartTick(2000);
        assertEquals(note.getStartTick(), 2000);
    }

    @Test
    void testClone() {
        Note noteClone = note.clone();

        assertNotEquals(note, noteClone);
        assertEquals(note.getPitch(), noteClone.getPitch());
        assertEquals(note.getVelocity(), noteClone.getVelocity());
        assertEquals(note.getDurationTicks(), noteClone.getDurationTicks());
        assertEquals(note.getStartTick(), noteClone.getStartTick());
    }
}
