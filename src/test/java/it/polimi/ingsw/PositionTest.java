package it.polimi.ingsw;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PositionTest {

    Position p = new Position(5, 5);

    @Test
    void getX() {
        assertEquals(5, p.getX());
    }

    @Test
    void getY() {
        assertEquals(5, p.getY());
    }
}