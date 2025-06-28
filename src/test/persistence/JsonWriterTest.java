package persistence;

import model.GachaHistory;
import model.GachaPull;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the JsonWriter class.
 * We'll write GachaHistory to JSON, then read it back with JsonReader to verify.
 */
public class JsonWriterTest {

    @Test
    void testWriterInvalidFile() {
        try {
            JsonWriter writer = new JsonWriter("./data/\0illegalFileName.json");
            writer.open();
            fail("Expected FileNotFoundException was not thrown");
        } catch (FileNotFoundException e) {
            // pass
        }
    }

    @Test
    void testWriterEmptyGachaHistory() {
        try {
            GachaHistory gh = new GachaHistory();
            JsonWriter writer = new JsonWriter("./data/testWriterEmptyGachaHistory.json");
            writer.open();
            writer.write(gh);
            writer.close();

            // Now read it back and verify
            JsonReader reader = new JsonReader("./data/testWriterEmptyGachaHistory.json");
            GachaHistory reloaded = reader.read();
            assertEquals(0, reloaded.getAllPulls().size(), 
                         "Expected empty GachaHistory after writing empty data.");
        } catch (IOException e) {
            fail("IOException should not happen here.");
        }
    }

    @Test
    void testWriterGeneralGachaHistory() {
        try {
            GachaHistory gh = new GachaHistory();
            gh.addPull(new GachaPull(true, 2, 1, 10));   // desired, 2 four-stars, pullIndex=1, draw=10
            gh.addPull(new GachaPull(false, 0, 2, 10));  // undesired, 0 four-stars, pullIndex=2, draw=10

            JsonWriter writer = new JsonWriter("./data/testWriterGeneralGachaHistory.json");
            writer.open();
            writer.write(gh);
            writer.close();

            // read it back
            JsonReader reader = new JsonReader("./data/testWriterGeneralGachaHistory.json");
            GachaHistory reloaded = reader.read();
            assertEquals(2, reloaded.getAllPulls().size(), 
                         "Should have 2 pull records.");

            // check first pull
            GachaPull p1 = reloaded.getAllPulls().get(0);
            assertTrue(p1.isDesired5Star(), "First pull should be desired 5-star");
            assertEquals(2, p1.getNumberOf4Stars());
            assertEquals(1, p1.getPullIndex());
            assertEquals(10, p1.getDrawCount());

            // check second pull
            GachaPull p2 = reloaded.getAllPulls().get(1);
            assertFalse(p2.isDesired5Star(), "Second pull is undesired 5-star");
            assertEquals(0, p2.getNumberOf4Stars());
            assertEquals(2, p2.getPullIndex());
            assertEquals(10, p2.getDrawCount());

        } catch (IOException e) {
            fail("IOException should not have happened writing to valid file.");
        }
    }

    @Test
    void testWriterCloseWithoutOpen() {
        // 调用 close() 前未调用 open()，应该不抛异常
        JsonWriter writer = new JsonWriter("./data/testWriterNoOpen.json");
        // 直接关闭（此时 writer 为 null），不应抛异常
        writer.close();

        // 之后再正常使用 writer
        try {
            writer.open();
            GachaHistory gh = new GachaHistory();
            gh.addPull(new GachaPull(true, 1, 1, 10));
            writer.write(gh);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterNoOpen.json");
            GachaHistory reloaded = reader.read();
            assertEquals(1, reloaded.getAllPulls().size(), "Should have 1 pull record.");
        } catch (IOException e) {
            fail("IOException should not have been thrown when using writer after close without open.");
        }
    }

    @Test
    void testWriterOverwrite() {
        try {
            // 第一次写入空的 GachaHistory
            GachaHistory gh1 = new GachaHistory();
            JsonWriter writer1 = new JsonWriter("./data/testWriterOverwrite.json");
            writer1.open();
            writer1.write(gh1);
            writer1.close();

            JsonReader reader1 = new JsonReader("./data/testWriterOverwrite.json");
            GachaHistory reloaded1 = reader1.read();
            assertEquals(0, reloaded1.getAllPulls().size(), "Expected empty history from first write.");

            // 第二次写入有记录的 GachaHistory
            GachaHistory gh2 = new GachaHistory();
            gh2.addPull(new GachaPull(true, 2, 1, 10));
            gh2.addPull(new GachaPull(false, 0, 2, 10));
            JsonWriter writer2 = new JsonWriter("./data/testWriterOverwrite.json");
            writer2.open();
            writer2.write(gh2);
            writer2.close();

            JsonReader reader2 = new JsonReader("./data/testWriterOverwrite.json");
            GachaHistory reloaded2 = reader2.read();
            assertEquals(2, reloaded2.getAllPulls().size(), "Expected 2 pull records after overwrite.");
        } catch (IOException e) {
            fail("IOException should not have been thrown during overwrite test.");
        }
    }
}
