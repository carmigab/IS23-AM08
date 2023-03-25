package it.polimi.ingsw.model;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * This is a set of tests for the class PlayerState
 */
class PlayerStateTest {

    PlayerState player = new PlayerState("Bill1", null);


    /**
     * This method tests the getNickname method
     */
    @Test
    void getNickname() {
        assertEquals("Bill1", player.getNickname());
    }


    /**
     * This method tests the getPoints method
     */
    @Test
    void getPoints() {
        // to do: integrate in the test the evaluation functions
        // player.evaluatePGPoints();
        // player.evaluateGroupPoints();
        player.addCGPoints(68);
        player.setFirstPoint();

        assertEquals(69, player.getPoints());
    }


    /**
     * This method tests the isCGDone method
     */
    @Test
    void isCODone() {
        player.setCGDone(0);
        assertTrue(player.isCGDone(0));
        assertFalse(player.isCGDone(1));
    }
}