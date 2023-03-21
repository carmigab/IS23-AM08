package it.polimi.ingsw.model.commonGoals;

import it.polimi.ingsw.model.Tile;
import it.polimi.ingsw.model.TileColor;
import it.polimi.ingsw.model.Shelf;
import it.polimi.ingsw.model.constants.AppConstants;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test for CommonGoal8
 */
class CommonGoal8Test {
    CommonGoal8 cg8 = new CommonGoal8();

    /**
     * This method test the evaluate method of CommonGoal8
     */
    @Test
    void evaluate() {
        Shelf library = new Shelf();

        // cards use to fill the library
        Tile cardBlue = new Tile(TileColor.BLUE, 0);
        Tile cardYellow = new Tile(TileColor.YELLOW, 0);
        Tile cardWhite = new Tile(TileColor.WHITE, 0);
        Tile cardViolet = new Tile(TileColor.VIOLET, 0);

        // check if the method does not count empty column as valid columns
        assertFalse(cg8.evaluate(library));

        // insert some cards in the library but without filling any row
        for (int i = 0; i < 3; i++) {
            library.add(cardBlue, i);
        }
        for (int i = 0; i < 3; i++) {
            library.add(cardYellow, i);
        }
        for (int i = 0; i < 2; i++) {
            library.add(cardWhite, i);
        }
        assertFalse(cg8.evaluate(library));

        // fill one entire row
        for (int i = 0; i < 2; i++) {
            library.add(cardBlue, 3 + i);
        }
        library.add(cardWhite, 3);
        library.add(cardYellow, 2);

        assertFalse(cg8.evaluate(library));

        // fill 2 entire rows
        library.add(cardBlue, 4);
        assertFalse(cg8.evaluate(library));

        // fill 3 rows but one with 4 colors
        library.add(cardBlue, 3);
        library.add(cardViolet, 4);
        assertFalse(cg8.evaluate(library));

        // fill the forth row correctly and assert the result is still false
        for (int i = 0; i < AppConstants.COLS_NUMBER; i++) {
            library.add(cardBlue, i);
        }
        assertFalse(cg8.evaluate(library));

        // fill the fifth color to have 4 valid rows and assert true
        for (int i = 0; i < AppConstants.COLS_NUMBER; i++) {
            library.add(cardViolet, i);
        }
        assertTrue(cg8.evaluate(library));
    }
}