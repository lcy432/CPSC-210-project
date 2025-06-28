package model;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

/**
 * Tests for the GachaHistory class.
 */


public class GachaHistoryTest {

    private GachaHistory testHistory;
    private GachaPull pullA;
    private GachaPull pullB;

    @BeforeEach
    void runBefore() {
        // Create an empty GachaHistory
        testHistory = new GachaHistory();

        // Create a couple of sample pulls to add or remove
        pullA = new GachaPull(true, 1, 1, 10);
        pullB = new GachaPull(false, 3, 2, 20);
    }


    @Test
    void testConstructor() {
        List<GachaPull> pulls = testHistory.getAllPulls();
        assertNotNull(pulls, "Should not return null list");
        assertEquals(0, pulls.size(), 
        "Expected an empty pull list at start");
    }

    @Test
    void testAddPull() {
        testHistory.addPull(pullA);
        assertEquals(1, testHistory.getAllPulls().size(), 
        "Expected history size of 1 after adding one pull");
        assertTrue(testHistory.getAllPulls().contains(pullA), 
        "History should contain the newly added pull");
    }

    @Test
    void testRemovePullExists() {
        testHistory.addPull(pullA);
        testHistory.removePull(pullA);
        assertEquals(0, testHistory.getAllPulls().size(), 
        "Expected history to be empty after removing the pull");
    }

    @Test
    void testRemovePullNotExists() {
        testHistory.removePull(pullA);
        assertEquals(0, testHistory.getAllPulls().size(), 
        "Removing a non-existent pull should do nothing");
    }

    @Test
void testCalculateFiveStarRate() {
    // pullA: desired 5-star (drawCount = 10) contributes 1/10 = 0.1
    // pullB: undesired 5-star (drawCount = 20) contributes 0
    // Total desired count = 1, total draws = 10 + 20 = 30, so expected rate = 1/30 ≈ 0.0333
    testHistory.addPull(pullA);
    testHistory.addPull(pullB);
    double rate = testHistory.calculateFiveStarRate();
    assertEquals(0.0333, rate, 0.001, 
    "Expected desired 5-star rate to be approximately 0.0333");
}

    @Test
    void testCalculateFourStarAvg() {
        testHistory.addPull(pullA); // 1 four-star
        testHistory.addPull(pullB); // 3 four-stars
        double avg = testHistory.calculateFourStarAvg();
        assertEquals(2.0, avg, 0.001, 
        "Expected four-star average to be 2.0 (i.e., (1+3)/2)");
    }

    @Test
void testIsAboveThreshold() {
    // For a pull with desired true and drawCount = 10, rate = 1/10 = 0.1
    GachaPull pull = new GachaPull(true, 1, 1, 10);
    testHistory.addPull(pull);
    assertTrue(testHistory.isAboveThreshold(0.05), 
    "Expected rate (0.1) to be above threshold 0.05");
    assertFalse(testHistory.isAboveThreshold(0.1), 
    "Expected rate (0.1) not to be above threshold equal to actual rate");
}

@Test
void testCalculateFiveStarRateEmpty() {
    // When there are no pulls, the rate should be 0.0.
    assertEquals(0.0, testHistory.calculateFiveStarRate(), 0.001, 
    "Expected rate to be 0.0 when no pulls exist");
}

@Test
void testCalculateFourStarAvgEmpty() {
    // When there are no pulls, the average should be 0.0.
    assertEquals(0.0, testHistory.calculateFourStarAvg(), 0.001, 
    "Expected average to be 0.0 when no pulls exist");
}

@Test
void testIsAboveThresholdNegative() {
    Exception exception = assertThrows(IllegalArgumentException.class, () -> {
        testHistory.isAboveThreshold(-0.1);
    });
    assertEquals("Threshold cannot be negative", exception.getMessage());
}


}

