package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CommonObjective12Test {

    @Test
    void evaluate() {
        CommonObjective12 co12=new CommonObjective12();

        Library lib = new Library();

        // creating one card per color to fill the board
        Card cBlue = new Card(CardColor.BLUE, 0);
        Card cWhite = new Card(CardColor.WHITE, 0);
        Card cYellow = new Card(CardColor.YELLOW, 0);
        Card cLightBlue = new Card(CardColor.LIGHT_BLUE, 0);
        Card cGreen = new Card(CardColor.GREEN, 0);
        Card cViolet = new Card(CardColor.VIOLET, 0);

        // check if evaluate works with an empty library
        assertFalse(co12.evaluate(lib));

        // a ladder of only 4 columns should not work (color is unimportant)
        lib.add(cBlue,0);
        lib.add(cBlue,0);
        lib.add(cBlue,0);
        lib.add(cBlue,0);
        lib.add(cBlue,1);
        lib.add(cBlue,1);
        lib.add(cBlue,1);
        lib.add(cBlue,2);
        lib.add(cBlue,2);
        lib.add(cBlue,3);

        assertFalse(co12.evaluate(lib));

        //now we add one more layer and it should work
        lib.add(cViolet,0);
        lib.add(cBlue,1);
        lib.add(cBlue,2);
        lib.add(cBlue,3);
        lib.add(cBlue,4);

        assertTrue(co12.evaluate(lib));

        //we add one more in the bottom right and should not be true anymore
        lib.add(cBlue,4);

        assertFalse(co12.evaluate(lib));

        //we complete the layer and it shouhld be true again
        lib.add(cBlue,3);
        lib.add(cBlue,2);
        lib.add(cBlue,1);
        lib.add(cBlue,0);

        assertTrue(co12.evaluate(lib));

        //we add a bit in the middle and it should be false again
        lib.add(cBlue,2);

        assertFalse(co12.evaluate(lib));

        //fast clear
        lib = new Library();

        //we do the same operations but mirrored

        lib.add(cBlue,4);
        lib.add(cBlue,4);
        lib.add(cBlue,4);
        lib.add(cBlue,4);
        lib.add(cBlue,3);
        lib.add(cBlue,3);
        lib.add(cBlue,3);
        lib.add(cBlue,2);
        lib.add(cBlue,2);
        lib.add(cBlue,1);

        assertFalse(co12.evaluate(lib));

        lib.add(cViolet,4);
        lib.add(cBlue,3);
        lib.add(cBlue,2);
        lib.add(cBlue,1);
        lib.add(cBlue,0);

        assertTrue(co12.evaluate(lib));

        lib.add(cBlue,0);

        assertFalse(co12.evaluate(lib));

        lib.add(cBlue,1);
        lib.add(cBlue,2);
        lib.add(cBlue,3);
        lib.add(cBlue,4);

        assertTrue(co12.evaluate(lib));

        lib.add(cBlue,2);

        assertFalse(co12.evaluate(lib));
    }
}