package persistance;

import static org.junit.jupiter.api.Assertions.fail;

import java.beans.PropertyChangeSupport;
import java.io.IOException;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;

import org.junit.jupiter.api.Test;

import model.Timeline;

// Code adapted from src/test/persistance/JsonReaderTest
//     at https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
public class TestJsonReader {

    JsonReader reader;

    @Test
    void testReaderNonExistentFile() throws MidiUnavailableException {
        reader = new JsonReader("./data/test/noSuchFile.json");
        try {
            reader.read();
            fail("IOException expected");
        } catch (InvalidMidiDataException e) {
            fail("InvalidMidiDataException should not be thrown");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testNewTimeline() throws MidiUnavailableException {
        reader = new JsonReader("./data/test/testReaderNewTimeline.json");
        try {
            Timeline timeline = reader.read();
            TestUtil.assertTimelineEquals(timeline, new Timeline("New project", null));
        } catch (IOException e) {
            fail("Path should exist and be accessible");
        } catch (InvalidMidiDataException e) {
            fail("Should not have invalid midi data");
        }
    }

    @Test
    void testExtensiveTimeline() throws MidiUnavailableException {
        reader = new JsonReader("./data/test/testReaderExtensive.json");
        try {
            Timeline timeline = reader.read();
            Timeline timeline2 = new Timeline("aaaa", null);
            timeline2.setPropertyChangeSupport(new PropertyChangeSupport(timeline2));
            timeline2.getPlayer().setBPM(160);

            TestUtil.addSampleSong(timeline2);
            TestUtil.assertTimelineEquals(timeline, timeline2);
        } catch (InvalidMidiDataException e) {
            fail("InvalidMidiDataException should not be thrown");
        } catch (IOException e) {
            fail("File should exist and be accessible");
        }
    }
}
