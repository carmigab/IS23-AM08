package it.polimi.ingsw.model;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.model.constants.AppConstants;
import it.polimi.ingsw.model.utilities.UtilityFunctions;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

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
        String file = "src/main/resources/savedMatches/Points_test.json";
        Gson json = new GsonBuilder().setPrettyPrinting().create();
        PlayerState pl;
        try {
            pl = new PlayerState(json.fromJson(new FileReader(file), PlayerState.class));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }


        // Points evaluations
        pl.evaluatePGPoints();
        pl.evaluateGroupPoints();

        pl.setFirstPoint();

        assertEquals(8+1+1+1, pl.getPoints());
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