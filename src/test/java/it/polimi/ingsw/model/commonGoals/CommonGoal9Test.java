package it.polimi.ingsw.model.commonGoals;

import it.polimi.ingsw.model.Shelf;
import it.polimi.ingsw.model.Tile;
import it.polimi.ingsw.model.TileColor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test for CommonGoal9
 *
 */
class CommonGoal9Test {

    CommonGoal9 cg9 = new CommonGoal9();

    /**
     * This method test the evaluate method of CommonGoal9
     */
    @Test
    void evaluate() {
        Shelf newLib = new Shelf();

        // G: 4
        newLib.add(new Tile(TileColor.GREEN, 0), 0);
        newLib.add(new Tile(TileColor.GREEN, 0), 1);
        newLib.add(new Tile(TileColor.GREEN, 0), 2);
        newLib.add(new Tile(TileColor.GREEN, 0), 3);

        newLib.add(new Tile(TileColor.BLUE, 0), 0);
        newLib.add(new Tile(TileColor.BLUE, 0), 1);

        newLib.add(new Tile(TileColor.WHITE, 0), 0);
        newLib.add(new Tile(TileColor.WHITE, 0), 1);

        newLib.add(new Tile(TileColor.VIOLET, 0), 0);
        newLib.add(new Tile(TileColor.VIOLET, 0), 1);

        newLib.add(new Tile(TileColor.YELLOW, 0), 0);
        newLib.add(new Tile(TileColor.YELLOW, 0), 1);

        newLib.add(new Tile(TileColor.LIGHT_BLUE, 0), 0);


        assertEquals(cg9.evaluate(newLib), false);


        newLib.add(new Tile(TileColor.LIGHT_BLUE, 0), 1);
        assertEquals(cg9.evaluate(newLib), true);



    }
}