package it.polimi.ingsw.model.commonObjectives;

import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.model.CardColor;
import it.polimi.ingsw.model.Library;
import it.polimi.ingsw.model.commonObjectives.CommonObjective3;
import it.polimi.ingsw.model.constants.AppConstants;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test for CommonObjective3
 */
class CommonObjective3Test {
    CommonObjective3 co3 = new CommonObjective3();

    /**
     * This method test the evaluate method of CommonObjective3
     */
    @Test
    void evaluate() {
        Library lib = new Library();

        // card used to fill the board
        Card card = new Card(CardColor.BLUE, 0);

        // check if the method doesn't consider four empty corner as objective completed
        assertFalse(co3.evaluate(lib));

        // fill only 1, then 2, then 3 corner and check that the method still return false
        // 1
        lib.add(card, 0);
        assertFalse(co3.evaluate(lib));
        // 2
        for (int i = 0; i < AppConstants.ROWS_NUMBER - 1; i++) {
            lib.add(card, 0);
        }
        assertFalse(co3.evaluate(lib));
        // 3
        lib.add(card, 4);
        assertFalse(co3.evaluate(lib));

        // fil the fourth corner and check if evaluate return true
        for (int i = 0; i < AppConstants.ROWS_NUMBER - 1; i++) {
            lib.add(card, 4);
        }
        assertTrue(co3.evaluate(lib));
    }
}