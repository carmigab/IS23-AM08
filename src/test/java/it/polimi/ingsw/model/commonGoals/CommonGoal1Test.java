package it.polimi.ingsw.model.commonGoals;

import it.polimi.ingsw.model.Shelf;
import it.polimi.ingsw.model.Tile;
import it.polimi.ingsw.model.TileColor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test for CommonGoal1
 */
class CommonGoal1Test {
    CommonGoal1 cg1 = new CommonGoal1();

    /**
     * This method test the methods push and pop of the class CommonGoal
     */
    @Test
    void pushAndPop() {
        cg1.push(2);
        cg1.push(4);
        cg1.push(6);

        assertEquals(6, cg1.pop());

        cg1.push(6);

        assertEquals(6, cg1.pop());
        assertEquals(4, cg1.pop());
        assertEquals(2, cg1.pop());
    }

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
        assertFalse(cg1.evaluate(lib));

        // partially fill the library in a way to not create any group of two adjacent cards of the same color and check evaluate to false
        lib.add(cBlue, 0);
        lib.add(cViolet, 0);
        lib.add(cGreen, 0);
        lib.add(cYellow, 1);
        lib.add(cWhite, 2);
        lib.add(cBlue, 2);
        assertFalse(cg1.evaluate(lib));

        // add other cards to the library to create some groups of two cards but not enough to satisfy the objective and check evaluate to false
        lib.add(cGreen, 0);
        lib.add(cYellow, 1);
        lib.add(cBlue, 2);
        assertFalse(cg1.evaluate(lib));

        // add other cards to create 4 groups of two cards and one group of 3 card to be sure that the evaluate function doesn't count cards twice
        lib.add(cGreen, 1);
        lib.add(cLightBlue, 3);
        lib.add(cLightBlue, 3);
        lib.add(cGreen, 3);
        lib.add(cGreen, 3);
        assertFalse(cg1.evaluate(lib));

        // reach 6 groups of two cards of the same color and check evaluate true
        lib.add(cViolet, 4);
        lib.add(cViolet, 4);
        assertTrue(cg1.evaluate(lib));

        // check if works also with more than 6 groups
        lib.add(cYellow, 4);
        lib.add(cYellow, 4);
        assertTrue(cg1.evaluate(lib));

        // check bad edge case
        lib = new Shelf();
        lib.add(cBlue, 0);
        lib.add(cBlue, 0);
        lib.add(cGreen, 1);
        lib.add(cBlue, 1);
        lib.add(cGreen, 2);
        lib.add(cBlue, 2);
        lib.add(cViolet, 3);
        lib.add(cViolet, 3);
        lib.add(cYellow, 4);
        lib.add(cYellow, 4);
        lib.add(cViolet, 4);
        lib.add(cViolet, 4);
        lib.add(cYellow, 4);
        lib.add(cYellow, 4);
        assertTrue(cg1.evaluate(lib));
    }
}