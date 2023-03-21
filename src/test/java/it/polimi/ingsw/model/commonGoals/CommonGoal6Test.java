package it.polimi.ingsw.model.commonGoals;

import it.polimi.ingsw.model.Shelf;
import it.polimi.ingsw.model.Tile;
import it.polimi.ingsw.model.TileColor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test for CommonGoal6
 */
class CommonGoal6Test {
    CommonGoal6 cg6 = new CommonGoal6();

    /**
     * This method test the evaluate method of CommonGoal6
     */
    @Test
    void evaluate() {
        Shelf library = new Shelf();

        // cards used to fill the library
        Tile cardBlue = new Tile(TileColor.BLUE, 0);
        Tile cardYellow = new Tile(TileColor.YELLOW, 0);

        // check with empty library
        assertFalse(cg6.evaluate(library));

        // fill the library with less than 8 cards
        for (int i = 0; i < 4; i++) {
            library.add(cardBlue, 1);
        }
        assertFalse(cg6.evaluate(library));

        // fill the board with more than 8 cards but less than 8 are of the same color
        for (int i = 0; i < 5; i++) {
            library.add(cardYellow, 3);
        }
        assertFalse(cg6.evaluate(library));

        // fill again to reach 8 blue cards
        for (int i = 0; i < 4; i++) {
            library.add(cardBlue, 4);
        }
        assertTrue(cg6.evaluate(library));
    }
}