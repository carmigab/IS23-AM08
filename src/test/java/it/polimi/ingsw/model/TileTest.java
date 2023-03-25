package it.polimi.ingsw.model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

/**
 * Unit test for Tile class
 */
public class TileTest {



    /**
     * This method tests the getColor method of the Tile class
     */
    @Test
    void getColor() {
        Tile c = new Tile(TileColor.GREEN, 2);
        assertEquals(TileColor.GREEN, c.getColor());
    }

    /**
     * This method tests the getSprite method of the Tile class
     */
    @Test
    void getSprite() {
        Tile c = new Tile(TileColor.GREEN, 2);
        assertEquals(2, c.getSprite());
    }

    @Test
    void isEmpty(){
        Tile c = new Tile(TileColor.EMPTY, 2);
        assertTrue(c.isEmpty());
        c.setInvalid();
        assertFalse(c.isEmpty());
    }


    @Test
    void isInvalid(){
        Tile c = new Tile(TileColor.INVALID, 2);
        assertTrue(c.isInvalid());
        c.setEmpty();
        assertFalse(c.isInvalid());
    }

    @Test
    void setEmpty(){
        Tile c = new Tile(TileColor.INVALID, 2);
        c.setEmpty();
        assertTrue(c.isEmpty());
    }

    @Test
    void setInvalid(){
        Tile c = new Tile(TileColor.INVALID, 2);
        c.setInvalid();
        assertTrue(c.isInvalid());
    }

    @Test
    void equals(){
        Tile c = new Tile(TileColor.INVALID, 2);
        String s = "";
        assertNotEquals(c, s);
        Tile c1 = new Tile(TileColor.INVALID, 2);
        assertEquals(c, c1);
        c1.setEmpty();
        assertNotEquals(c, c1);
    }


}