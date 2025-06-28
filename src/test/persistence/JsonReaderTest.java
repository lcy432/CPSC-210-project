package persistence;

import model.GachaHistory;
import model.GachaPull;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the JsonReader class.
 * Aims to cover reading from valid/invalid files, empty arrays, missing fields, etc.
 */
public class JsonReaderTest {

    // Adjust these paths according to your actual project setup:
    private static final String NON_EXISTENT_FILE = "./data/NoSuchFile.json";
    private static final String EMPTY_FILE = "./data/EmptyGachaHistory.json";
    private static final String GENERAL_FILE = "./data/GeneralGachaHistory.json";
    private static final String MISSING_PULL_RECORDS = "./data/MissingPullRecords.json";
    private static final String INVALID_JSON_STRUCTURE = "./data/InvalidJsonStructure.json";

    /**
     * If needed, you can do some global setup here. 
     * For example, verifying that the test data files exist (except the non-existent one).
     */
    @BeforeAll
    static void checkTestFiles() {
        // Optional: check if the files exist, log warnings, etc.
        // Not strictly necessary for coverage, but can help debug file path issues.
    }

    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader(NON_EXISTENT_FILE);
        assertThrows(IOException.class, reader::read, 
            "Expected IOException for a non-existent file path.");
    }

    @Test
    void testReaderEmptyGachaHistory() {
        // This file has a valid JSON structure but no pulls: { "pullRecords": [] }
        JsonReader reader = new JsonReader(EMPTY_FILE);
        try {
            GachaHistory history = reader.read();
            assertNotNull(history, "Should not return null");
            assertEquals(0, history.getAllPulls().size(),
                    "Expected empty GachaHistory if file has no pulls");
        } catch (IOException e) {
            fail("IOException should not have been thrown for an existing empty file");
        }
    }

    @Test
    void testReaderGeneralGachaHistory() {
        // This file has a valid JSON with 2+ GachaPull records
        JsonReader reader = new JsonReader(GENERAL_FILE);
        try {
            GachaHistory history = reader.read();
            List<GachaPull> pulls = history.getAllPulls();

            assertNotNull(pulls, "Pull list should not be null");
            assertFalse(pulls.isEmpty(), "Pull list should not be empty");
            // Example: expecting 2 pulls
            assertEquals(2, pulls.size(), "Expected 2 pulls in test file");

            // Check first pull
            GachaPull p1 = pulls.get(0);
            assertTrue(p1.isDesired5Star(), "First pull should be desired 5-star");
            assertEquals(2, p1.getNumberOf4Stars());
            assertEquals(1, p1.getPullIndex());
            assertEquals(10, p1.getDrawCount());

            // Check second pull
            GachaPull p2 = pulls.get(1);
            assertFalse(p2.isDesired5Star(), "Second pull is undesired 5-star");
            assertEquals(0, p2.getNumberOf4Stars());
            assertEquals(2, p2.getPullIndex());
            assertEquals(10, p2.getDrawCount());

        } catch (IOException e) {
            fail("IOException should not have been thrown for a valid file");
        }
    }

    @Test
    void testReaderMissingPullRecordsKey() {
        // Suppose parseGachaHistory checks if "pullRecords" is present
        // If it's not present, let's see if it returns an empty history or does something else
        JsonReader reader = new JsonReader(MISSING_PULL_RECORDS);
        try {
            GachaHistory history = reader.read();
            // If your parse logic returns empty GachaHistory in that scenario:
            assertNotNull(history);
            assertEquals(0, history.getAllPulls().size(),
                    "Expected empty GachaHistory if 'pullRecords' key is missing");
        } catch (IOException e) {
            fail("IOException should not have been thrown for missing 'pullRecords' key");
        }
    }

    @Test
    void testReaderInvalidJsonStructure() {
        JsonReader reader = new JsonReader(INVALID_JSON_STRUCTURE);
        // You may let org.json throw a JSONException, or wrap it as IOException, or other.
        // Check your actual code's behavior. If you let it bubble up as a RuntimeException,
        // then we test for that.
        assertThrows(RuntimeException.class, reader::read,
            "Invalid JSON structure should throw an exception (JSONException or similar).");
    }

    /**
     * Additional test for extra coverage:
     * if your parseGachaPull or parseGachaHistory has any conditional branches 
     * about missing fields, you can place them here.
     */
    @Test
    void testReaderPartialPullData() {
        // e.g. a file that has "desired5Star" but missing "numberOf4Stars"
        // If your code is designed to throw an exception, we can test that:
        JsonReader reader = new JsonReader("./data/PartialPullData.json");
        assertThrows(RuntimeException.class, reader::read,
            "Missing required fields should cause an exception in parseGachaPull");
    }
} 