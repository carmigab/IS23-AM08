package it.polimi.ingsw.model.commonGoals;

import it.polimi.ingsw.model.Tile;
import it.polimi.ingsw.model.TileColor;
import it.polimi.ingsw.model.Shelf;
import it.polimi.ingsw.model.constants.AppConstants;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test for CommonGoal3
 */
class CommonGoal3Test {
    CommonGoal3 cg3 = new CommonGoal3();

    /**
     * This method test the evaluate method of CommonGoal3
     */
    @Test
    void evaluate() {
        Shelf lib = new Shelf();

        // card used to fill the board
        Tile card = new Tile(TileColor.BLUE, 0);

        // check if the method doesn't consider four empty corner as objective completed
        assertFalse(cg3.evaluate(lib));

        // fill only 1, then 2, then 3 corner and check that the method still return false
        // 1
        lib.add(card, 0);
        assertFalse(cg3.evaluate(lib));
        // 2
        for (int i = 0; i < AppConstants.ROWS_NUMBER - 1; i++) {
            lib.add(card, 0);
        }
        assertFalse(cg3.evaluate(lib));
        // 3
        lib.add(card, 4);
        assertFalse(cg3.evaluate(lib));

        // fil the fourth corner and check if evaluate return true
        for (int i = 0; i < AppConstants.ROWS_NUMBER - 1; i++) {
            lib.add(card, 4);
        }
        assertTrue(cg3.evaluate(lib));
    }
}