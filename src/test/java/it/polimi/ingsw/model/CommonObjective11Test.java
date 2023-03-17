package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CommonObjective11Test {

    @Test
    void evaluate() {
        CommonObjective11 co11=new CommonObjective11();

        Library lib = new Library();

        // creating one card per color to fill the board
        Card cBlue = new Card(CardColor.BLUE, 0);
        Card cWhite = new Card(CardColor.WHITE, 0);
        Card cYellow = new Card(CardColor.YELLOW, 0);
        Card cLightBlue = new Card(CardColor.LIGHT_BLUE, 0);
        Card cGreen = new Card(CardColor.GREEN, 0);
        Card cViolet = new Card(CardColor.VIOLET, 0);

        // check if evaluate works with an empty library
        assertFalse(co11.evaluate(lib));

        //we did a 3x3 blue square in the bottom left, should be true
        lib.add(cBlue,0);
        lib.add(cBlue,1);
        lib.add(cBlue,2);
        lib.add(cBlue,0);
        lib.add(cBlue,1);
        lib.add(cBlue,2);
        lib.add(cBlue,0);
        lib.add(cBlue,1);
        lib.add(cBlue,2);

        assertTrue(co11.evaluate(lib));

        //Fast clear of the board
        lib=new Library();

        //now in the bottom left we put a green one, no cross so no objective
        lib.add(cGreen,0);
        lib.add(cBlue,1);
        lib.add(cBlue,2);
        lib.add(cBlue,0);
        lib.add(cBlue,1);
        lib.add(cBlue,2);
        lib.add(cBlue,0);
        lib.add(cBlue,1);
        lib.add(cBlue,2);

        assertFalse(co11.evaluate(lib));

        //Fast clear of the board
        lib=new Library();

        //we did a stack on the right with a cross, should be true
        lib.add(cViolet,2);
        lib.add(cViolet,3);
        lib.add(cViolet,4);
        lib.add(cBlue,2);
        lib.add(cYellow,3);
        lib.add(cBlue,4);
        lib.add(cYellow,2);
        lib.add(cBlue,3);
        lib.add(cYellow,4);
        lib.add(cBlue,2);
        lib.add(cYellow, 3);
        lib.add(cBlue,4);
        lib.add(cGreen,2);
        lib.add(cGreen,3);
        lib.add(cGreen,4);

        assertTrue(co11.evaluate(lib));

    }
}