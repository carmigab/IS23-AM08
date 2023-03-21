package it.polimi.ingsw.model.commonGoals;

import it.polimi.ingsw.model.Shelf;
import it.polimi.ingsw.model.Tile;
import it.polimi.ingsw.model.TileColor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CommonGoal12Test {

    @Test
    void evaluate() {
        CommonGoal12 cg12 = new CommonGoal12();

        Shelf lib = new Shelf();

        // creating one card per color to fill the board
        Tile cBlue = new Tile(TileColor.BLUE, 0);
        Tile cWhite = new Tile(TileColor.WHITE, 0);
        Tile cYellow = new Tile(TileColor.YELLOW, 0);
        Tile cLightBlue = new Tile(TileColor.LIGHT_BLUE, 0);
        Tile cGreen = new Tile(TileColor.GREEN, 0);
        Tile cViolet = new Tile(TileColor.VIOLET, 0);

        // check if evaluate works with an empty library
        assertFalse(cg12.evaluate(lib));

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

        assertFalse(cg12.evaluate(lib));

        //now we add one more layer and it should work
        lib.add(cViolet,0);
        lib.add(cBlue,1);
        lib.add(cBlue,2);
        lib.add(cBlue,3);
        lib.add(cBlue,4);

        assertTrue(cg12.evaluate(lib));

        //we add one more in the bottom right and should not be true anymore
        lib.add(cBlue,4);

        assertFalse(cg12.evaluate(lib));

        //we complete the layer and it shouhld be true again
        lib.add(cBlue,3);
        lib.add(cBlue,2);
        lib.add(cBlue,1);
        lib.add(cBlue,0);

        assertTrue(cg12.evaluate(lib));

        //we add a bit in the middle and it should be false again
        lib.add(cBlue,2);

        assertFalse(cg12.evaluate(lib));

        //fast clear
        lib = new Shelf();

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

        assertFalse(cg12.evaluate(lib));

        lib.add(cViolet,4);
        lib.add(cBlue,3);
        lib.add(cBlue,2);
        lib.add(cBlue,1);
        lib.add(cBlue,0);

        assertTrue(cg12.evaluate(lib));

        lib.add(cBlue,0);

        assertFalse(cg12.evaluate(lib));

        lib.add(cBlue,1);
        lib.add(cBlue,2);
        lib.add(cBlue,3);
        lib.add(cBlue,4);

        assertTrue(cg12.evaluate(lib));

        lib.add(cBlue,2);

        assertFalse(cg12.evaluate(lib));
    }
}