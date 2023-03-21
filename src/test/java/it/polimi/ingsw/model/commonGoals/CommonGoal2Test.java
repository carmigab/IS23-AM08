package it.polimi.ingsw.model.commonGoals;

import it.polimi.ingsw.model.Shelf;
import it.polimi.ingsw.model.Tile;
import it.polimi.ingsw.model.TileColor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test for CommonGoal2
 */
class CommonGoal2Test {
    CommonGoal2 cg2 = new CommonGoal2();

    // push and pop already tested in CommonGoal1

    /**
     * This method test the evaluate method of CommonGoal2
     */
    @Test
    void evaluate() {
        Shelf lib = new Shelf();

        // creating some card of different color to fill the board
        Tile cBlue = new Tile(TileColor.BLUE, 0);
        Tile cWhite = new Tile(TileColor.WHITE, 0);
        Tile cGreen = new Tile(TileColor.GREEN, 0);
        Tile cViolet = new Tile(TileColor.VIOLET, 0);

        // check if evaluate works with an empty library
        assertFalse(cg2.evaluate(lib));

        // partially fill the library in a way to not create any group of cards
        lib.add(cBlue, 0);
        lib.add(cGreen, 1);
        lib.add(cViolet, 4);
        assertFalse(cg2.evaluate(lib));

        // add other cards to create some groups but all with less than 4 cards
        lib.add(cBlue, 0);
        lib.add(cBlue, 0);
        lib.add(cGreen, 2);
        lib.add(cViolet, 4);
        assertFalse(cg2.evaluate(lib));

        // add other cards to create 4 groups of 4 cards
        lib.add(cBlue, 0);
        lib.add(cGreen, 2);
        lib.add(cGreen, 3);
        lib.add(cViolet, 4);
        lib.add(cViolet, 3);
        lib.add(cWhite, 2);
        lib.add(cWhite, 2);
        lib.add(cWhite, 3);
        lib.add(cWhite, 3);
        assertTrue(cg2.evaluate(lib));

        // try with two group of four of the same color and adjacent to assure the method count them as 1 group and not as 2
        lib = new Shelf();
        lib.add(cBlue, 0);
        lib.add(cGreen, 1);
        lib.add(cViolet, 4);
        lib.add(cBlue, 0);
        lib.add(cBlue, 0);
        lib.add(cGreen, 2);
        lib.add(cViolet, 4);
        lib.add(cBlue, 0);
        lib.add(cGreen, 2);
        lib.add(cGreen, 3);
        lib.add(cViolet, 4);
        lib.add(cViolet, 3);
        lib.add(cViolet, 2);
        lib.add(cViolet, 2);
        lib.add(cViolet, 3);
        lib.add(cViolet, 3);
        assertFalse(cg2.evaluate(lib));
    }
}