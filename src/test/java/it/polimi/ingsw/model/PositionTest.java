package it.polimi.ingsw.model;

import it.polimi.ingsw.model.Position;
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

    /**
     * this method tests the equals method of the Position class
     */
    @Test
    void equals(){
        Position p= new Position(5,5);
        String s = new String();
        assertFalse(p.equals(s));
        Position p1 = new Position(5,5);
        assertTrue(p.equals(p1));
        Position p2 = new Position(4,5);
        assertFalse(p.equals(p2));
    }
}