package it.polimi.ingsw.model.commonObjectives;

import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.model.CardColor;
import it.polimi.ingsw.model.Library;
import it.polimi.ingsw.model.constants.AppConstants;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test for CommonObjective5
 */
class CommonObjective5Test {
    CommonObjective5 co5 = new CommonObjective5();

    /**
     * This method test the evaluate method of CommonObjective5
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
        assertFalse(co5.evaluate(library));

        // insert some cards in the library but without filling any column
        for (int i = 0; i < 3; i++) {
            library.add(cardBlue, 0);
        }
        for (int i = 0; i < 3; i++) {
            library.add(cardYellow, 2);
        }
        for (int i = 0; i < 2; i++) {
            library.add(cardWhite, 4);
        }
        assertFalse(co5.evaluate(library));

        // fill one entire column
        for (int i = 0; i < 3; i++) {
            library.add(cardBlue, 0);
        }
        for (int i = 0; i < 2; i++) {
            library.add(cardWhite, 2);
        }
        for (int i = 0; i < 2; i++) {
            library.add(cardYellow, 4);
        }
        assertFalse(co5.evaluate(library));

        // fill 2 entire columns
        library.add(cardBlue, 2);
        library.add(cardBlue, 4);
        assertFalse(co5.evaluate(library));

        // fill 3 columns but one with 4 colors
        library.add(cardViolet, 4);
        assertFalse(co5.evaluate(library));

        // fill the forth column correctly and assert the result is true
        for (int i = 0; i < AppConstants.ROWS_NUMBER; i++) {
            library.add(cardViolet, 1);
        }
        assertTrue(co5.evaluate(library));
    }
}