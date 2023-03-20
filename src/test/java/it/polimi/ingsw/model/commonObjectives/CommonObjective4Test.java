package it.polimi.ingsw.model.commonObjectives;

import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.model.CardColor;
import it.polimi.ingsw.model.Library;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test for CommonObjective4
 */
class CommonObjective4Test {
    CommonObjective4 co4 = new CommonObjective4();

    /**
     * This method test the evaluate method of CommonObjective1
     */
    @Test
    void evaluate() {
        Library lib = new Library();

        // creating one card per color to fill the board
        Card cBlue = new Card(CardColor.BLUE, 0);
        Card cWhite = new Card(CardColor.WHITE, 0);
        Card cYellow = new Card(CardColor.YELLOW, 0);
        Card cLightBlue = new Card(CardColor.LIGHT_BLUE, 0);
        Card cGreen = new Card(CardColor.GREEN, 0);
        Card cViolet = new Card(CardColor.VIOLET, 0);

        // check if evaluate works with an empty library
        assertFalse(co4.evaluate(lib));

        // fill with some cards without forming any square
        lib.add(cBlue, 0);
        lib.add(cBlue, 0);
        lib.add(cBlue, 1);
        lib.add(cGreen, 2);
        lib.add(cGreen, 3);
        assertFalse(co4.evaluate(lib));

        // form one square
        lib.add(cBlue, 1);
        assertFalse(co4.evaluate(lib));

        // form 2 squares but of the same color and adjacent
        lib.add(cBlue, 0);
        lib.add(cBlue, 0);
        lib.add(cBlue, 1);
        lib.add(cBlue, 1);
        assertFalse(co4.evaluate(lib));

        // form a second square
        lib.add(cGreen, 2);
        lib.add(cGreen, 3);
        assertTrue(co4.evaluate(lib));
    }
}