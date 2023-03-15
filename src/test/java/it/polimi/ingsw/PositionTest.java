package it.polimi.ingsw;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test for Position class
 */
public class PositionTest {

    Position p = new Position(5, 5);

    /**
     * This method tests the getX method of the Position class
     */
    @Test
    void getX() {
        assertEquals(5, p.x());
    }

    /**
     * This method tests the getY method of the Position class
     */
    @Test
    void getY() {
        assertEquals(5, p.y());
    }
}