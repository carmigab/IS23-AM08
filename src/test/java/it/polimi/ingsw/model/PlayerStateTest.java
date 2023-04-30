package it.polimi.ingsw.model;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.UtilityTestFunctions;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.Objects;

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
        // Loading json playerstate
        Gson json = new GsonBuilder().setPrettyPrinting().create();
        PlayerState pl = new PlayerState(json.fromJson(UtilityTestFunctions.getReaderFromFileNameRelativePath("Points_test.json", this.getClass()), PlayerState.class));


        // Points evaluations
        pl.evaluatePGPoints();
        pl.evaluateGroupPoints();

        pl.addCGPoints(0, 0);
        pl.addCGPoints(0, 1);

        pl.setFirstPoint();

        // assert
        assertEquals(8+1+1, pl.getPoints());
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

    /**
     * This method tests the getShelf method
     */
    @Test
    void getShelf() {
        Gson json = new GsonBuilder().setPrettyPrinting().create();
        PlayerState pl = new PlayerState(json.fromJson(UtilityTestFunctions.getReaderFromFileNameRelativePath("Points_test.json", this.getClass()), PlayerState.class));

        PlayerState pl2 = new PlayerState(pl);

        assertEquals(pl, pl2);
    }
}