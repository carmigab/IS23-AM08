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
        /**
         * to do: integrate in the test the evaluation functions
         */
        // player.evaluatePOPoints();
        // player.evaluateGroupPoints();
        player.addCOPoints(68);
        player.setFirstPoint();

        assertEquals(69, player.getPoints());
    }


    /**
     * This method tests the isCODone method
     */
    @Test
    void isCODone() {
        player.setCODone(0);
        assertEquals(true, player.isCODone(0));
        assertEquals(false, player.isCODone(1));
    }
}