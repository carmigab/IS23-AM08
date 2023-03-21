package it.polimi.ingsw.model.commonGoals;

import it.polimi.ingsw.model.Tile;
import it.polimi.ingsw.model.TileColor;
import it.polimi.ingsw.model.Shelf;
import it.polimi.ingsw.model.constants.AppConstants;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test for CommonGoal5
 */
class CommonGoal5Test {
    CommonGoal5 cg5 = new CommonGoal5();

    /**
     * This method test the evaluate method of CommonGoal5
     */
    @Test
    void evaluate() {
        Shelf shelf = new Shelf();

        // cards use to fill the library
        Tile cardBlue = new Tile(TileColor.BLUE, 0);
        Tile cardYellow = new Tile(TileColor.YELLOW, 0);
        Tile cardWhite = new Tile(TileColor.WHITE, 0);
        Tile cardViolet = new Tile(TileColor.VIOLET, 0);

        // check if the method does not count empty column as valid columns
        assertFalse(cg5.evaluate(shelf));

        // insert some cards in the library but without filling any column
        for (int i = 0; i < 3; i++) {
            shelf.add(cardBlue, 0);
        }
        for (int i = 0; i < 3; i++) {
            shelf.add(cardYellow, 2);
        }
        for (int i = 0; i < 2; i++) {
            shelf.add(cardWhite, 4);
        }
        assertFalse(cg5.evaluate(shelf));

        // fill one entire column
        for (int i = 0; i < 3; i++) {
            shelf.add(cardBlue, 0);
        }
        for (int i = 0; i < 2; i++) {
            shelf.add(cardWhite, 2);
        }
        for (int i = 0; i < 2; i++) {
            shelf.add(cardYellow, 4);
        }
        assertFalse(cg5.evaluate(shelf));

        // fill 2 entire columns
        shelf.add(cardBlue, 2);
        shelf.add(cardBlue, 4);
        assertFalse(cg5.evaluate(shelf));

        // fill 3 columns but one with 4 colors
        shelf.add(cardViolet, 4);
        assertFalse(cg5.evaluate(shelf));

        // fill the forth column correctly and assert the result is true
        for (int i = 0; i < AppConstants.ROWS_NUMBER; i++) {
            shelf.add(cardViolet, 1);
        }
        assertTrue(cg5.evaluate(shelf));
    }
}