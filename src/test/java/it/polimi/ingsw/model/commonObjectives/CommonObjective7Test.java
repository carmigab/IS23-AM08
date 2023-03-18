package it.polimi.ingsw.model.commonObjectives;

import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.model.CardColor;
import it.polimi.ingsw.model.Library;
import it.polimi.ingsw.model.constants.AppConstants;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test for CommonObjective7
 */
class CommonObjective7Test {
    CommonObjective6 co7 = new CommonObjective6();

    /**
     * This method test the evaluate method of CommonObjective7
     */
    @Test
    void evaluate() {
        Library library = new Library();

        // cards used to fill the library
        Card cardBlue = new Card(CardColor.BLUE, 0);

        // check with empty library
        assertFalse(co7.evaluate(library));

        // fill the library with some cards but not enough to make a diagonal
        for (int i = 0; i < 5; i++) {
            library.add(cardBlue, 0);
        }
        assertFalse(co7.evaluate(library));

        // fill the library to create the first possible diagonal
        library = new Library();
        for (int i = 0; i < AppConstants.COLS_NUMBER; i++) {
            for (int j = 0; j < AppConstants.ROWS_NUMBER - i; j++) {
                library.add(cardBlue, i);
            }
        }
        assertTrue(co7.evaluate(library));

        // fill the library to create the second possible diagonal
        library = new Library();
        for (int i = 0; i < AppConstants.COLS_NUMBER; i++) {
            for (int j = 0; j < AppConstants.ROWS_NUMBER - 1 - i; j++) {
                library.add(cardBlue, i);
            }
        }
        assertTrue(co7.evaluate(library));

        // fill the library to create the third possible diagonal
        library = new Library();
        for (int i = 0; i < AppConstants.COLS_NUMBER; i++) {
            for (int j = 0; j < i + 1; j++) {
                library.add(cardBlue, i);
            }
        }
        assertTrue(co7.evaluate(library));

        // fill the library to create the fourth possible diagonal
        library = new Library();
        for (int i = 0; i < AppConstants.COLS_NUMBER; i++) {
            for (int j = 0; j < i + 2; j++) {
                library.add(cardBlue, i);
            }
        }
        assertTrue(co7.evaluate(library));
    }
}