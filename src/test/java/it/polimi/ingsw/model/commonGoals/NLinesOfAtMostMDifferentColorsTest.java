package it.polimi.ingsw.model.commonGoals;

import com.google.gson.Gson;
import it.polimi.ingsw.model.Shelf;
import it.polimi.ingsw.model.Tile;
import it.polimi.ingsw.model.TileColor;
import it.polimi.ingsw.model.constants.AppConstants;
import it.polimi.ingsw.model.utilities.JsonWithExposeSingleton;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;

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
        Gson jsonLoader= JsonWithExposeSingleton.getJsonWithExposeSingleton();
        Reader fileReader= null;
        try {
            fileReader = new FileReader(AppConstants.FILE_CONFIG_NLINESOFATMOSTMDIFFERENTCOLORS);
        }
        catch(FileNotFoundException e){
            System.out.println("error");
        }
        NLinesOfAtMostMDifferentColorsConfiguration nLinesOfAtMostMDifferentColorsConfiguration = jsonLoader.fromJson(fileReader, NLinesOfAtMostMDifferentColorsConfiguration.class);


        nLinesOfAtMostMDifferentColors = nLinesOfAtMostMDifferentColorsConfiguration.getGoalAt(0);

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
        nLinesOfAtMostMDifferentColors = nLinesOfAtMostMDifferentColorsConfiguration.getGoalAt(1);

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
        nLinesOfAtMostMDifferentColors = nLinesOfAtMostMDifferentColorsConfiguration.getGoalAt(2);

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

        shelf.add(new Tile(TileColor.CYAN, 0), 0);


        assertFalse(nLinesOfAtMostMDifferentColors.evaluate(shelf));


        shelf.add(new Tile(TileColor.CYAN, 0), 1);
        assertTrue(nLinesOfAtMostMDifferentColors.evaluate(shelf));



        shelf = new Shelf();
        nLinesOfAtMostMDifferentColors = nLinesOfAtMostMDifferentColorsConfiguration.getGoalAt(3);

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


        assertFalse(nLinesOfAtMostMDifferentColors.evaluate(shelf));


        shelf.add(new Tile(TileColor.YELLOW, 0), 4);
        assertTrue(nLinesOfAtMostMDifferentColors.evaluate(shelf));
    }
}