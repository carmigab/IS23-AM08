package it.polimi.ingsw.model.commonObjectives;

import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.model.CardColor;
import it.polimi.ingsw.model.Library;
import it.polimi.ingsw.model.constants.AppConstants;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test for CommonObjective8
 */
class CommonObjective8Test {
    CommonObjective8 co8 = new CommonObjective8();

    /**
     * This method test the evaluate method of CommonObjective8
     */
    @Test
    void evaluate() {
        Library library = new Library();

        // cards use to fill the library
        Card cardBlue = new Card(CardColor.BLUE, 0);
        Card cardYellow = new Card(CardColor.YELLOW, 0);
        Card cardWhite = new Card(CardColor.WHITE, 0);
        Card cardViolet = new Card(CardColor.VIOLET, 0);

        // check if the method does not count empty column as valid columns
        assertFalse(co8.evaluate(library));

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
        assertFalse(co8.evaluate(library));

        // fill one entire row
        for (int i = 0; i < 2; i++) {
            library.add(cardBlue, 3 + i);
        }
        library.add(cardWhite, 3);
        library.add(cardYellow, 2);

        assertFalse(co8.evaluate(library));

        // fill 2 entire rows
        library.add(cardBlue, 4);
        assertFalse(co8.evaluate(library));

        // fill 3 rows but one with 4 colors
        library.add(cardBlue, 3);
        library.add(cardViolet, 4);
        assertFalse(co8.evaluate(library));

        // fill the forth row correctly and assert the result is still false
        for (int i = 0; i < AppConstants.COLS_NUMBER; i++) {
            library.add(cardBlue, i);
        }
        assertFalse(co8.evaluate(library));

        // fill the fifth color to have 4 valid rows and assert true
        for (int i = 0; i < AppConstants.COLS_NUMBER; i++) {
            library.add(cardViolet, i);
        }
        assertTrue(co8.evaluate(library));
    }
}