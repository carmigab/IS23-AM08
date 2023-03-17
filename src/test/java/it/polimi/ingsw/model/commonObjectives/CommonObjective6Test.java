package it.polimi.ingsw.model.commonObjectives;

import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.model.CardColor;
import it.polimi.ingsw.model.Library;
import it.polimi.ingsw.model.constants.AppConstants;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test for CommonObjective6
 */
class CommonObjective6Test {
    CommonObjective6 co6 = new CommonObjective6();

    /**
     * This method test the evaluate method of CommonObjective6
     */
    @Test
    void evaluate() {
        Library library = new Library();

        // cards used to fill the library
        Card cardBlue = new Card(CardColor.BLUE, 0);
        Card cardYellow = new Card(CardColor.YELLOW, 0);
        Card cardWhite = new Card(CardColor.WHITE, 0);
        Card cardViolet = new Card(CardColor.VIOLET, 0);

        // check with empty library
        assertFalse(co6.evaluate(library));

        // fill the library with less than 8 cards
        for (int i = 0; i < 4; i++) {
            library.add(cardBlue, 1);
        }
        assertFalse(co6.evaluate(library));

        // fill the board with more than 8 cards but less than 8 are of the same color
        for (int i = 0; i < 5; i++) {
            library.add(cardYellow, 3);
        }
        assertFalse(co6.evaluate(library));

        // fill again to reach 8 blue cards
        for (int i = 0; i < 4; i++) {
            library.add(cardBlue, 4);
        }
        assertTrue(co6.evaluate(library));
    }
}