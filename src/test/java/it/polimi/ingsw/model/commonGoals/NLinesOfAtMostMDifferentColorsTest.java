package it.polimi.ingsw.model.commonGoals;

import it.polimi.ingsw.model.Shelf;
import it.polimi.ingsw.model.Tile;
import it.polimi.ingsw.model.TileColor;
import it.polimi.ingsw.model.constants.AppConstants;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test for NLinesOfAtMostMDifferentColors
 */
class NLinesOfAtMostMDifferentColorsTest {
    NLinesOfAtMostMDifferentColors nLinesOfAtMostMDifferentColors;
    
    /**
     * This method test the evaluate method of NGroupOfMTiles
     */
    @Test
    void evaluate() {
        Shelf shelf = new Shelf();
        nLinesOfAtMostMDifferentColors = new NLinesOfAtMostMDifferentColors(3, 3, true);

        // cards use to fill the shelf
        Tile cardBlue = new Tile(TileColor.BLUE, 0);
        Tile cardYellow = new Tile(TileColor.YELLOW, 0);
        Tile cardWhite = new Tile(TileColor.WHITE, 0);
        Tile cardViolet = new Tile(TileColor.VIOLET, 0);

        // check if the method does not count empty column as valid columns
        assertFalse(nLinesOfAtMostMDifferentColors.evaluate(shelf));

        // insert some cards in the shelf but without filling any column
        for (int i = 0; i < 3; i++) {
            shelf.add(cardBlue, 0);
        }
        for (int i = 0; i < 3; i++) {
            shelf.add(cardYellow, 2);
        }
        for (int i = 0; i < 2; i++) {
            shelf.add(cardWhite, 4);
        }
        assertFalse(nLinesOfAtMostMDifferentColors.evaluate(shelf));

        // fill one entire column
        for (int i = 0; i < 3; i++) {
            shelf.add(cardBlue, 0);
        }
        for (int i = 0; i < 2; i++) {
            shelf.add(cardWhite, 2);
        }
        for (int i = 0; i < 2; i++) {
            shelf.add(cardYellow, 4);
        }
        assertFalse(nLinesOfAtMostMDifferentColors.evaluate(shelf));

        // fill 2 entire columns
        shelf.add(cardBlue, 2);
        shelf.add(cardBlue, 4);
        assertFalse(nLinesOfAtMostMDifferentColors.evaluate(shelf));

        // fill 3 columns but one with 4 colors
        shelf.add(cardViolet, 4);
        assertFalse(nLinesOfAtMostMDifferentColors.evaluate(shelf));

        // fill the forth column correctly and assert the result is true
        for (int i = 0; i < AppConstants.ROWS_NUMBER; i++) {
            shelf.add(cardViolet, 1);
        }
        assertTrue(nLinesOfAtMostMDifferentColors.evaluate(shelf));


        shelf = new Shelf();
        nLinesOfAtMostMDifferentColors = new NLinesOfAtMostMDifferentColors(4, 3, false);

        // check if the method does not count empty column as valid columns
        assertFalse(nLinesOfAtMostMDifferentColors.evaluate(shelf));

        // insert some cards in the shelf but without filling any row
        for (int i = 0; i < 3; i++) {
            shelf.add(cardBlue, i);
        }
        for (int i = 0; i < 3; i++) {
            shelf.add(cardYellow, i);
        }
        for (int i = 0; i < 2; i++) {
            shelf.add(cardWhite, i);
        }
        assertFalse(nLinesOfAtMostMDifferentColors.evaluate(shelf));

        // fill one entire row
        for (int i = 0; i < 2; i++) {
            shelf.add(cardBlue, 3 + i);
        }
        shelf.add(cardWhite, 3);
        shelf.add(cardYellow, 2);

        assertFalse(nLinesOfAtMostMDifferentColors.evaluate(shelf));

        // fill 2 entire rows
        shelf.add(cardBlue, 4);
        assertFalse(nLinesOfAtMostMDifferentColors.evaluate(shelf));

        // fill 3 rows but one with 4 colors
        shelf.add(cardBlue, 3);
        shelf.add(cardViolet, 4);
        assertFalse(nLinesOfAtMostMDifferentColors.evaluate(shelf));

        // fill the forth row correctly and assert the result is still false
        for (int i = 0; i < AppConstants.COLS_NUMBER; i++) {
            shelf.add(cardBlue, i);
        }
        assertFalse(nLinesOfAtMostMDifferentColors.evaluate(shelf));

        // fill the fifth color to have 4 valid rows and assert true
        for (int i = 0; i < AppConstants.COLS_NUMBER; i++) {
            shelf.add(cardViolet, i);
        }
        assertTrue(nLinesOfAtMostMDifferentColors.evaluate(shelf));


        shelf = new Shelf();
        nLinesOfAtMostMDifferentColors = new NLinesOfAtMostMDifferentColors(2, 6, true);

        // G: 4
        shelf.add(new Tile(TileColor.GREEN, 0), 0);
        shelf.add(new Tile(TileColor.GREEN, 0), 1);
        shelf.add(new Tile(TileColor.GREEN, 0), 2);
        shelf.add(new Tile(TileColor.GREEN, 0), 3);

        shelf.add(new Tile(TileColor.BLUE, 0), 0);
        shelf.add(new Tile(TileColor.BLUE, 0), 1);

        shelf.add(new Tile(TileColor.WHITE, 0), 0);
        shelf.add(new Tile(TileColor.WHITE, 0), 1);

        shelf.add(new Tile(TileColor.VIOLET, 0), 0);
        shelf.add(new Tile(TileColor.VIOLET, 0), 1);

        shelf.add(new Tile(TileColor.YELLOW, 0), 0);
        shelf.add(new Tile(TileColor.YELLOW, 0), 1);

        shelf.add(new Tile(TileColor.LIGHT_BLUE, 0), 0);


        assertEquals(nLinesOfAtMostMDifferentColors.evaluate(shelf), false);


        shelf.add(new Tile(TileColor.LIGHT_BLUE, 0), 1);
        assertEquals(nLinesOfAtMostMDifferentColors.evaluate(shelf), true);



        shelf = new Shelf();
        nLinesOfAtMostMDifferentColors = new NLinesOfAtMostMDifferentColors(2, 5, false);

        // G: 4
        shelf.add(new Tile(TileColor.GREEN, 0), 0);
        shelf.add(new Tile(TileColor.GREEN, 0), 0);
        shelf.add(new Tile(TileColor.GREEN, 0), 0);
        shelf.add(new Tile(TileColor.GREEN, 0), 0);

        shelf.add(new Tile(TileColor.BLUE, 0), 1);
        shelf.add(new Tile(TileColor.BLUE, 0), 1);

        shelf.add(new Tile(TileColor.WHITE, 0), 2);
        shelf.add(new Tile(TileColor.WHITE, 0), 2);

        shelf.add(new Tile(TileColor.VIOLET, 0), 3);
        shelf.add(new Tile(TileColor.VIOLET, 0), 3);

        shelf.add(new Tile(TileColor.YELLOW, 0), 4);


        assertEquals(nLinesOfAtMostMDifferentColors.evaluate(shelf), false);


        shelf.add(new Tile(TileColor.YELLOW, 0), 4);
        assertEquals(nLinesOfAtMostMDifferentColors.evaluate(shelf), true);
    }
}