package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


/**
 * Tests for the GachaPull class.
 */
public class GachaPullTest {

    private GachaPull testPull;

    @BeforeEach
    void runBefore() {
        // Create a GachaPull with:
        // desired5Star = false, numberOf4Stars = 2, pullIndex = 1, drawCount = 10.
        testPull = new GachaPull(false, 2, 1, 10);
    }

    @Test
    void testConstructor() {
        assertFalse(testPull.isDesired5Star(), "Initially, desired5Star should be false.");
        assertEquals(2, testPull.getNumberOf4Stars(), "Initial 4-star count should be 2.");
        assertEquals(1, testPull.getPullIndex(), "Initial pull index should be 1.");
        assertEquals(10, testPull.getDrawCount(), "Initial draw count should be 10.");
    }

    @Test
    void testSetDesired5Star() {
        // Toggle the desired flag from false -> true
        testPull.setDesired5Star(true);
        assertTrue(testPull.isDesired5Star(), "After setting, desired5Star should be true.");
    }

    @Test
    void testSetNumberOf4Stars() {
        // Set a new number of 4-star items
        testPull.setNumberOf4Stars(5);
        assertEquals(5, testPull.getNumberOf4Stars(), "After setting, numberOf4Stars should be 5.");

    }

    @Test
    void testSetDrawCount() {
        testPull.setDrawCount(15);
        assertEquals(15, testPull.getDrawCount(), "After setting, drawCount should be 15.");
    }

    @Test
    void testInvalidConstructorNegativeDrawCount() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new GachaPull(true, 1, 1, 0); 
            // drawCount less than 1 should trigger exception
        });
        assertEquals("Invalid number of 4-star items, pull index, or draw count.", exception.getMessage());
    }

    @Test
void testConstructorNegativeNum4Stars() {
    Exception exception = assertThrows(IllegalArgumentException.class, () -> {
        new GachaPull(true, -1, 1, 10);
    });
    assertEquals("Invalid number of 4-star items, pull index, or draw count.", exception.getMessage());
}

@Test
void testConstructorNegativePullIndex() {
    Exception exception = assertThrows(IllegalArgumentException.class, () -> {
        new GachaPull(true, 1, 0, 10);
    });
    assertEquals("Invalid number of 4-star items, pull index, or draw count.", exception.getMessage());
}

void testConstructorNegativeDrawCount() {
    Exception exception = assertThrows(IllegalArgumentException.class, () -> {
        new GachaPull(true, 1, 1, 0);
    });
    assertEquals("Invalid number of 4-star items, pull index, or draw count.", exception.getMessage());
}


@Test
void testSetDrawCountNegative() {
    Exception exception = assertThrows(IllegalArgumentException.class, () -> {
        testPull.setDrawCount(0);
    });
    assertEquals("Draw count must be at least 1.", exception.getMessage());
}

@Test
void testSetNumberOf4StarsNegative() {
    Exception exception = assertThrows(IllegalArgumentException.class, () -> {
        testPull.setNumberOf4Stars(-5);
    });
    assertEquals("Number of 4-star items cannot be negative", exception.getMessage());
}

}
