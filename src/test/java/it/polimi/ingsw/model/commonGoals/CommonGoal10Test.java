package it.polimi.ingsw.model.commonGoals;

import it.polimi.ingsw.model.Shelf;
import it.polimi.ingsw.model.Tile;
import it.polimi.ingsw.model.TileColor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test for CommonGoal10
 *
 */
class CommonGoal10Test {

    CommonGoal10 cg10 = new CommonGoal10();

    /**
     * This method test the evaluate method of CommonGoal10
     */
    @Test
    void evaluate() {
        Shelf newLib = new Shelf();

        // G: 4
        newLib.add(new Tile(TileColor.GREEN, 0), 0);
        newLib.add(new Tile(TileColor.GREEN, 0), 0);
        newLib.add(new Tile(TileColor.GREEN, 0), 0);
        newLib.add(new Tile(TileColor.GREEN, 0), 0);

        newLib.add(new Tile(TileColor.BLUE, 0), 1);
        newLib.add(new Tile(TileColor.BLUE, 0), 1);

        newLib.add(new Tile(TileColor.WHITE, 0), 2);
        newLib.add(new Tile(TileColor.WHITE, 0), 2);

        newLib.add(new Tile(TileColor.VIOLET, 0), 3);
        newLib.add(new Tile(TileColor.VIOLET, 0), 3);

        newLib.add(new Tile(TileColor.YELLOW, 0), 4);


        assertEquals(cg10.evaluate(newLib), false);


        newLib.add(new Tile(TileColor.YELLOW, 0), 4);
        assertEquals(cg10.evaluate(newLib), true);


    }
}