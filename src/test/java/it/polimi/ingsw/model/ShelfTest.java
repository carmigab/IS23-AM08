package it.polimi.ingsw.model;

import it.polimi.ingsw.model.constants.AppConstants;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test for Shelf class
 */
public class ShelfTest {
    Shelf lib;

    /**
     * This method initialize an empty library to perform each method test
     */
    @org.junit.jupiter.api.Test
    @BeforeEach
    public void initLib() {
        lib = new Shelf();
    }

    /**
     * This method tests the add and GetCard method of the library class
     */
    @org.junit.jupiter.api.Test
    void addAndGetCard() {
        Tile c = new Tile(TileColor.CYAN, 0);
        lib.add(c, 4);
        //assertEquals(lib.getCard(new Position(AppConstants.ROWS_NUMBER - 1, 4)), c);
    }


    /**
     * This method tests the isFull method of the library class
     */
    @org.junit.jupiter.api.Test
    void isFull() {
        Tile c = new Tile(TileColor.CYAN, 0);
        for (int i = 0; i < AppConstants.COLS_NUMBER; i++) {
            for (int j = 0; j < AppConstants.ROWS_NUMBER; j++) {
                lib.add(c, i);
            }
        }
        assertTrue(lib.isFull());
    }

    /**
     * This method tests the getFreeSpaces method of the library class
     */
    @org.junit.jupiter.api.Test
    void getFreeSpaces() {
        Tile c = new Tile(TileColor.CYAN, 0);
        for (int i = 0; i < AppConstants.ROWS_NUMBER; i++) {
            assertEquals(lib.getFreeSpaces(0), AppConstants.ROWS_NUMBER - i);
            lib.add(c, 0);
        }
        assertEquals(lib.getFreeSpaces(0), 0);
    }

    /**
     * This method tests the evaluateGroupPoints method of the library class
     */
    @org.junit.jupiter.api.Test
    void evaluateGroupPoints() {
        Shelf newLib = new Shelf();

        // G: 4
        newLib.add(new Tile(TileColor.GREEN, 0), 0);
        newLib.add(new Tile(TileColor.GREEN, 0), 0);
        newLib.add(new Tile(TileColor.GREEN, 0), 0);
        newLib.add(new Tile(TileColor.GREEN, 0), 0);

        // B: 7
        newLib.add(new Tile(TileColor.BLUE, 0), 1);
        newLib.add(new Tile(TileColor.BLUE, 0), 1);
        newLib.add(new Tile(TileColor.BLUE, 0), 1);
        newLib.add(new Tile(TileColor.BLUE, 0), 1);

        newLib.add(new Tile(TileColor.BLUE, 0), 2);
        newLib.add(new Tile(TileColor.BLUE, 0), 3);
        newLib.add(new Tile(TileColor.BLUE, 0), 4);

        // W: 3
        newLib.add(new Tile(TileColor.WHITE, 0), 3);
        newLib.add(new Tile(TileColor.WHITE, 0), 3);
        newLib.add(new Tile(TileColor.WHITE, 0), 3);

        // V: 1
        newLib.add(new Tile(TileColor.VIOLET, 0), 4);

        assertEquals(13, newLib.evaluateGroupPoints());


    }
}