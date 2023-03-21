package it.polimi.ingsw.model.commonGoals;

import it.polimi.ingsw.model.Shelf;
import it.polimi.ingsw.model.Tile;
import it.polimi.ingsw.model.TileColor;
import it.polimi.ingsw.model.constants.AppConstants;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test for CommonGoal7
 */
class CommonGoal7Test {
    CommonGoal6 cg7 = new CommonGoal6();

    /**
     * This method test the evaluate method of CommonGoal7
     */
    @Test
    void evaluate() {
        Shelf library = new Shelf();

        // cards used to fill the library
        Tile cardBlue = new Tile(TileColor.BLUE, 0);

        // check with empty library
        assertFalse(cg7.evaluate(library));

        // fill the library with some cards but not enough to make a diagonal
        for (int i = 0; i < 5; i++) {
            library.add(cardBlue, 0);
        }
        assertFalse(cg7.evaluate(library));

        // fill the library to create the first possible diagonal
        library = new Shelf();
        for (int i = 0; i < AppConstants.COLS_NUMBER; i++) {
            for (int j = 0; j < AppConstants.ROWS_NUMBER - i; j++) {
                library.add(cardBlue, i);
            }
        }
        assertTrue(cg7.evaluate(library));

        // fill the library to create the second possible diagonal
        library = new Shelf();
        for (int i = 0; i < AppConstants.COLS_NUMBER; i++) {
            for (int j = 0; j < AppConstants.ROWS_NUMBER - 1 - i; j++) {
                library.add(cardBlue, i);
            }
        }
        assertTrue(cg7.evaluate(library));

        // fill the library to create the third possible diagonal
        library = new Shelf();
        for (int i = 0; i < AppConstants.COLS_NUMBER; i++) {
            for (int j = 0; j < i + 1; j++) {
                library.add(cardBlue, i);
            }
        }
        assertTrue(cg7.evaluate(library));

        // fill the library to create the fourth possible diagonal
        library = new Shelf();
        for (int i = 0; i < AppConstants.COLS_NUMBER; i++) {
            for (int j = 0; j < i + 2; j++) {
                library.add(cardBlue, i);
            }
        }
        assertTrue(cg7.evaluate(library));
    }
}