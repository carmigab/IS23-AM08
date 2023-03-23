package it.polimi.ingsw.model.commonGoals;

import it.polimi.ingsw.model.Tile;
import it.polimi.ingsw.model.TileColor;
import it.polimi.ingsw.model.Shelf;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test for TwoSquares
 */
class TwoSquaresTest {
    TwoSquares cg4 = new TwoSquares();

    /**
     * This method test the evaluate method of CommonGoal1
     */
    @Test
    void evaluate() {
        Shelf lib = new Shelf();

        // creating one card per color to fill the board
        Tile cBlue = new Tile(TileColor.BLUE, 0);
        Tile cWhite = new Tile(TileColor.WHITE, 0);
        Tile cYellow = new Tile(TileColor.YELLOW, 0);
        Tile cLightBlue = new Tile(TileColor.LIGHT_BLUE, 0);
        Tile cGreen = new Tile(TileColor.GREEN, 0);
        Tile cViolet = new Tile(TileColor.VIOLET, 0);

        // check if evaluate works with an empty library
        assertFalse(cg4.evaluate(lib));

        // fill with some cards without forming any square
        lib.add(cBlue, 0);
        lib.add(cBlue, 0);
        lib.add(cBlue, 1);
        lib.add(cGreen, 2);
        lib.add(cGreen, 3);
        assertFalse(cg4.evaluate(lib));

        // form one square
        lib.add(cBlue, 1);
        assertFalse(cg4.evaluate(lib));

        // form 2 squares but of the same color and adjacent
        lib.add(cBlue, 0);
        lib.add(cBlue, 0);
        lib.add(cBlue, 1);
        lib.add(cBlue, 1);
        assertFalse(cg4.evaluate(lib));

        // form a second square
        lib.add(cGreen, 2);
        lib.add(cGreen, 3);
        assertTrue(cg4.evaluate(lib));
    }
}