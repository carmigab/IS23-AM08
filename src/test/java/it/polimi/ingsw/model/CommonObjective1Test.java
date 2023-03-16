package it.polimi.ingsw.model;

import it.polimi.ingsw.model.constants.AppConstants;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test for CommonObjective1
 */
class CommonObjective1Test {
    CommonObjective1 co1 = new CommonObjective1();

    /**
     * This method test the methods push and pop of the class CommonObjective
     */
    @Test
    void pushAndPop() {
        co1.push(2);
        co1.push(4);
        co1.push(6);

        assertEquals(6, co1.pop());

        co1.push(6);

        assertEquals(6, co1.pop());
        assertEquals(4, co1.pop());
        assertEquals(2, co1.pop());
    }

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
        assertFalse(co1.evaluate(lib));

        // partially fill the library in a way to not create any group of two adjacent cards of the same color and check evaluate to false
        lib.add(cBlue, 0);
        lib.add(cViolet, 0);
        lib.add(cGreen, 0);
        lib.add(cYellow, 1);
        lib.add(cWhite, 2);
        lib.add(cBlue, 2);
        assertFalse(co1.evaluate(lib));

        // add other cards to the library to create some groups of two cards but not enough to satisfy the objective and check evaluate to false
        lib.add(cGreen, 0);
        lib.add(cYellow, 1);
        lib.add(cBlue, 2);
        assertFalse(co1.evaluate(lib));

        // add other cards to create 4 groups of two cards and one group of 3 card to be sure that the evaluate function doesn't count cards twice
        lib.add(cGreen, 1);
        lib.add(cLightBlue, 3);
        lib.add(cLightBlue, 3);
        lib.add(cGreen, 3);
        lib.add(cGreen, 3);
        assertFalse(co1.evaluate(lib));

        // reach 6 groups of two cards of the same color and check evaluate true
        lib.add(cViolet, 4);
        lib.add(cViolet, 4);
        assertTrue(co1.evaluate(lib));

        // check if works also with more than 6 groups
        lib.add(cYellow, 4);
        lib.add(cYellow, 4);
        assertTrue(co1.evaluate(lib));
    }
}