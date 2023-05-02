package it.polimi.ingsw.model.utilities;

import it.polimi.ingsw.constants.ModelConstants;
import it.polimi.ingsw.model.Shelf;
import it.polimi.ingsw.model.Tile;
import it.polimi.ingsw.model.TileColor;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.utilities.UtilityFunctionsModel;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test for UtilityFunctionsModel
 */
class UtilityFunctionsTest {

    /**
     * This method test if the method findGroupSize works properly
     */
    @Test
    void findGroupSize() {
        Shelf shelf = new Shelf();
        Shelf copy = new Shelf(shelf);
        Tile card = new Tile(TileColor.BLUE, 0);

        // initializing the library with a group of only one card and check if the method return 1 and if the library has not been changed except for the card valued
        shelf.add(card, 0);
        assertEquals(1, UtilityFunctionsModel.findGroupSize(shelf, new Position(0, 5)));
        for (int i = 0; i < ModelConstants.ROWS_NUMBER; i++) {
            for (int j = 0; j < ModelConstants.COLS_NUMBER; j++) {
                Position position = new Position(j, i);
                assertEquals(copy.getTile(position), shelf.getTile(position));
            }
        }

        // add more cards to create a group of 2, then of 3, 4, 5, 6 and 7
        shelf.add(card, 0);
        shelf.add(card, 0);
        assertEquals(2, UtilityFunctionsModel.findGroupSize(shelf, new Position(0, 5)));
        for (int i = 0; i < ModelConstants.ROWS_NUMBER; i++) {
            for (int j = 0; j < ModelConstants.COLS_NUMBER; j++) {
                Position position = new Position(j, i);
                assertEquals(copy.getTile(position), shelf.getTile(position));
            }
        }

        shelf.add(card, 0);
        shelf.add(card, 0);
        shelf.add(card, 1);
        assertEquals(3, UtilityFunctionsModel.findGroupSize(shelf, new Position(0, 5)));
        for (int i = 0; i < ModelConstants.ROWS_NUMBER; i++) {
            for (int j = 0; j < ModelConstants.COLS_NUMBER; j++) {
                Position position = new Position(j, i);
                assertEquals(copy.getTile(position), shelf.getTile(position));
            }
        }

        shelf.add(card, 0);
        shelf.add(card, 0);
        shelf.add(card, 1);
        shelf.add(card, 1);
        assertEquals(4, UtilityFunctionsModel.findGroupSize(shelf, new Position(0, 5)));
        for (int i = 0; i < ModelConstants.ROWS_NUMBER; i++) {
            for (int j = 0; j < ModelConstants.COLS_NUMBER; j++) {
                Position position = new Position(j, i);
                assertEquals(copy.getTile(position), shelf.getTile(position));
            }
        }

        shelf.add(card, 0);
        shelf.add(card, 0);
        shelf.add(card, 1);
        shelf.add(card, 1);
        shelf.add(card, 0);
        assertEquals(5, UtilityFunctionsModel.findGroupSize(shelf, new Position(0, 5)));
        for (int i = 0; i < ModelConstants.ROWS_NUMBER; i++) {
            for (int j = 0; j < ModelConstants.COLS_NUMBER; j++) {
                Position position = new Position(j, i);
                assertEquals(copy.getTile(position), shelf.getTile(position));
            }
        }

        shelf.add(card, 0);
        shelf.add(card, 0);
        shelf.add(card, 1);
        shelf.add(card, 1);
        shelf.add(card, 0);
        shelf.add(card, 0);
        assertEquals(6, UtilityFunctionsModel.findGroupSize(shelf, new Position(0, 5)));
        for (int i = 0; i < ModelConstants.ROWS_NUMBER; i++) {
            for (int j = 0; j < ModelConstants.COLS_NUMBER; j++) {
                Position position = new Position(j, i);
                assertEquals(copy.getTile(position), shelf.getTile(position));
            }
        }

        shelf.add(card, 0);
        shelf.add(card, 0);
        shelf.add(card, 1);
        shelf.add(card, 1);
        shelf.add(card, 0);
        shelf.add(card, 0);
        shelf.add(card, 2);
        assertEquals(7, UtilityFunctionsModel.findGroupSize(shelf, new Position(0, 5)));
        for (int i = 0; i < ModelConstants.ROWS_NUMBER; i++) {
            for (int j = 0; j < ModelConstants.COLS_NUMBER; j++) {
                Position position = new Position(j, i);
                assertEquals(copy.getTile(position), shelf.getTile(position));
            }
        }

        // refill the library with more than one group one near the other to be sure that findGroupSize does not accidentally modify one group while finding the other
        Tile card1 = new Tile(TileColor.GREEN, 0);
        shelf.add(card1, 2);
        shelf.add(card1, 2);
        shelf.add(card1, 2);
        shelf.add(card1, 1);
        copy = new Shelf(shelf);
        shelf.add(card, 0);
        shelf.add(card, 0);
        shelf.add(card, 1);
        shelf.add(card, 1);
        shelf.add(card, 0);
        assertEquals(5, UtilityFunctionsModel.findGroupSize(shelf, new Position(0, 5)));
        for (int i = 0; i < ModelConstants.ROWS_NUMBER; i++) {
            for (int j = 0; j < ModelConstants.COLS_NUMBER; j++) {
                Position position = new Position(j, i);
                assertEquals(copy.getTile(position), shelf.getTile(position));
            }
        }
    }
}