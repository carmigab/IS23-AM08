package it.polimi.ingsw.computer.predictiveFunction;

import it.polimi.ingsw.model.Tile;
import it.polimi.ingsw.model.TileColor;
import it.polimi.ingsw.utilities.JsonWithExposeSingleton;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;

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

        assertEquals(colorFitnessPerTile.computeCoefficient(1, 2) + colorFitnessPerTile.computeCoefficient(2, 2), colorFitnessPerTile.getColorFitnessPerTile()[4][0].get(TileColor.BLUE));
        assertEquals(colorFitnessPerTile.computeCoefficient(3, 1), colorFitnessPerTile.getColorFitnessPerTile()[4][0].get(TileColor.GREEN));
    }

    @Test
    void evaluateAction() {
        GameStateRepresentation gameStateRepresentation = null;

        try {
            gameStateRepresentation = JsonWithExposeSingleton.getJsonWithExposeSingleton().fromJson(new FileReader("src/test/resources/botTests/updateFitnessTest.json"), GameStateRepresentation.class);
        } catch (FileNotFoundException ignored) {

        }

        colorFitnessPerTile.updateColorFitnessPerTile(gameStateRepresentation);

        Action action = new Action(List.of(TileColor.BLUE), 0);

        assertEquals(colorFitnessPerTile.computeCoefficient(1, 2)
                + colorFitnessPerTile.computeCoefficient(2, 2)
                - colorFitnessPerTile.computeCoefficient(3, 1),
                colorFitnessPerTile.evaluateAction(action, gameStateRepresentation.getShelf()));
    }

    @Test
    void findFirstFreeSpaceInGivenColumn() {
        Tile[][] shelf = null;

        try {
            shelf = JsonWithExposeSingleton.getJsonWithExposeSingleton().fromJson(new FileReader("src/test/resources/botTests/findFirstFreeSpaceTest.json"), Tile[][].class);
        } catch (FileNotFoundException ignored) {

        }

        assertEquals(4, colorFitnessPerTile.findFirstFreeSpaceInGivenColumn(0, shelf));
        assertEquals(5, colorFitnessPerTile.findFirstFreeSpaceInGivenColumn(1, shelf));
        assertEquals(-1, colorFitnessPerTile.findFirstFreeSpaceInGivenColumn(2, shelf));
    }
}