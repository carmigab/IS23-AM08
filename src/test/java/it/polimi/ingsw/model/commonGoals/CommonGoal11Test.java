package it.polimi.ingsw.model.commonGoals;

import it.polimi.ingsw.model.Shelf;
import it.polimi.ingsw.model.Tile;
import it.polimi.ingsw.model.TileColor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CommonGoal11Test {

    @Test
    void evaluate() {
        CommonGoal11 cg11 = new CommonGoal11();

        Shelf lib = new Shelf();

        // creating one card per color to fill the board
        Tile cBlue = new Tile(TileColor.BLUE, 0);
        Tile cWhite = new Tile(TileColor.WHITE, 0);
        Tile cYellow = new Tile(TileColor.YELLOW, 0);
        Tile cLightBlue = new Tile(TileColor.LIGHT_BLUE, 0);
        Tile cGreen = new Tile(TileColor.GREEN, 0);
        Tile cViolet = new Tile(TileColor.VIOLET, 0);

        // check if evaluate works with an empty library
        assertFalse(cg11.evaluate(lib));

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

        assertTrue(cg11.evaluate(lib));

        //Fast clear of the board
        lib=new Shelf();

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

        assertFalse(cg11.evaluate(lib));

        //Fast clear of the board
        lib=new Shelf();

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

        assertTrue(cg11.evaluate(lib));

    }
}