package it.polimi.ingsw.model.commonObjectives;

import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.model.CardColor;
import it.polimi.ingsw.model.Library;
import it.polimi.ingsw.model.commonObjectives.CommonObjective2;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test for CommonObjective2
 */
class CommonObjective2Test {
    CommonObjective2 co2 = new CommonObjective2();

    // push and pop already tested in CommonObjective1

    /**
     * This method test the evaluate method of CommonObjective2
     */
    @Test
    void evaluate() {
        Library lib = new Library();

        // creating some card of different color to fill the board
        Card cBlue = new Card(CardColor.BLUE, 0);
        Card cWhite = new Card(CardColor.WHITE, 0);
        Card cGreen = new Card(CardColor.GREEN, 0);
        Card cViolet = new Card(CardColor.VIOLET, 0);

        // check if evaluate works with an empty library
        assertFalse(co2.evaluate(lib));

        // partially fill the library in a way to not create any group of cards
        lib.add(cBlue, 0);
        lib.add(cGreen, 1);
        lib.add(cViolet, 4);
        assertFalse(co2.evaluate(lib));

        // add other cards to create some groups but all with less than 4 cards
        lib.add(cBlue, 0);
        lib.add(cBlue, 0);
        lib.add(cGreen, 2);
        lib.add(cViolet, 4);
        assertFalse(co2.evaluate(lib));

        // add other cards to create 4 groups of 4 cards
        lib.add(cBlue, 0);
        lib.add(cGreen, 2);
        lib.add(cGreen, 3);
        lib.add(cViolet, 4);
        lib.add(cViolet, 3);
        lib.add(cWhite, 2);
        lib.add(cWhite, 2);
        lib.add(cWhite, 3);
        lib.add(cWhite, 3);
        assertTrue(co2.evaluate(lib));

        // try with two group of four of the same color and adjacent to assure the method count them as 1 group and not as 2
        lib = new Library();
        lib.add(cBlue, 0);
        lib.add(cGreen, 1);
        lib.add(cViolet, 4);
        lib.add(cBlue, 0);
        lib.add(cBlue, 0);
        lib.add(cGreen, 2);
        lib.add(cViolet, 4);
        lib.add(cBlue, 0);
        lib.add(cGreen, 2);
        lib.add(cGreen, 3);
        lib.add(cViolet, 4);
        lib.add(cViolet, 3);
        lib.add(cViolet, 2);
        lib.add(cViolet, 2);
        lib.add(cViolet, 3);
        lib.add(cViolet, 3);
        assertFalse(co2.evaluate(lib));
    }
}