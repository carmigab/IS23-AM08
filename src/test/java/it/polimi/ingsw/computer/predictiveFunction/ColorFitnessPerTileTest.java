package it.polimi.ingsw.computer.predictiveFunction;

import it.polimi.ingsw.utilities.JsonWithExposeSingleton;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;

import static org.junit.jupiter.api.Assertions.*;

class ColorFitnessPerTileTest {

    private ColorFitnessPerTile colorFitnessPerTile;

    @BeforeEach
    void setUp() {
        colorFitnessPerTile = new ColorFitnessPerTile();
    }

    @Test
    void updateColorFitnessPerTile() {
        GameStateRepresentation gameStateRepresentation = null;

        try {
            gameStateRepresentation = JsonWithExposeSingleton.getJsonWithExposeSingleton().fromJson(new FileReader("src/test/resources/botTests/updateFitnessTest.json"), GameStateRepresentation.class);
        } catch (FileNotFoundException ignored) {

        }

        colorFitnessPerTile.updateColorFitnessPerTile(gameStateRepresentation);
    }

    @Test
    void evaluateAction() {
    }

    @Test
    void findFirstFreeSpaceInGivenColumn() {
    }
}