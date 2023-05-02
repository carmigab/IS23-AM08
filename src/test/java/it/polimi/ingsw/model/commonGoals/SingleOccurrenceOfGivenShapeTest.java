package it.polimi.ingsw.model.commonGoals;

import com.google.gson.Gson;
import it.polimi.ingsw.UtilityTestFunctions;
import it.polimi.ingsw.constants.ModelConstants;
import it.polimi.ingsw.model.Shelf;
import it.polimi.ingsw.model.Tile;
import it.polimi.ingsw.model.TileColor;
import it.polimi.ingsw.utilities.JsonWithExposeSingleton;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test for SingleOccurrenceOfGivenShape
 */
class SingleOccurrenceOfGivenShapeTest {
    SingleOccurrenceOfGivenShape singleOccurrenceOfGivenShape;

    /**
     * This method test the evaluate method of NGroupOfMTiles
     */
    @Test
    void evaluate() {
        Shelf shelf = new Shelf();
        Gson jsonLoader= JsonWithExposeSingleton.getJsonWithExposeSingleton();

        SingleOccurrenceOfGivenShapeConfiguration singleOccurrenceOfGivenShapeConfiguration = jsonLoader.fromJson(UtilityTestFunctions.getReaderFromFileNameRelativePath("commonGoals3and7and11.json", this.getClass()), SingleOccurrenceOfGivenShapeConfiguration.class);


        singleOccurrenceOfGivenShape = singleOccurrenceOfGivenShapeConfiguration.getGoalAt(0);

        // card used to fill the board
        Tile cardBlue = new Tile(TileColor.BLUE, 0);

        // check if the method doesn't consider four empty corner as objective completed
        assertFalse(singleOccurrenceOfGivenShape.evaluate(shelf));

        // fill only 1, then 2, then 3 corner and check that the method still return false
        // 1
        shelf.add(cardBlue, 0);
        assertFalse(singleOccurrenceOfGivenShape.evaluate(shelf));
        // 2
        for (int i = 0; i < ModelConstants.ROWS_NUMBER - 1; i++) {
            shelf.add(cardBlue, 0);
        }
        assertFalse(singleOccurrenceOfGivenShape.evaluate(shelf));
        // 3
        shelf.add(cardBlue, 4);
        assertFalse(singleOccurrenceOfGivenShape.evaluate(shelf));

        // fil the fourth corner and check if evaluate return true
        for (int i = 0; i < ModelConstants.ROWS_NUMBER - 1; i++) {
            shelf.add(cardBlue, 4);
        }
        assertTrue(singleOccurrenceOfGivenShape.evaluate(shelf));



        shelf = new Shelf();

        singleOccurrenceOfGivenShape = singleOccurrenceOfGivenShapeConfiguration.getGoalAt(1);

        // check with empty library
        assertFalse(singleOccurrenceOfGivenShape.evaluate(shelf));

        // fill the library with some cards but not enough to make a diagonal
        for (int i = 0; i < 5; i++) {
            shelf.add(cardBlue, 0);
        }
        assertFalse(singleOccurrenceOfGivenShape.evaluate(shelf));

        // fill the library to create the first possible diagonal
        shelf = new Shelf();
        for (int i = 0; i < ModelConstants.COLS_NUMBER; i++) {
            for (int j = 0; j < ModelConstants.ROWS_NUMBER - i; j++) {
                shelf.add(cardBlue, i);
            }
        }
        assertTrue(singleOccurrenceOfGivenShape.evaluate(shelf));

        // fill the library to create the second possible diagonal
        shelf = new Shelf();
        for (int i = 0; i < ModelConstants.COLS_NUMBER; i++) {
            for (int j = 0; j < ModelConstants.ROWS_NUMBER - 1 - i; j++) {
                shelf.add(cardBlue, i);
            }
        }
        assertTrue(singleOccurrenceOfGivenShape.evaluate(shelf));

        // fill the library to create the third possible diagonal
        shelf = new Shelf();
        for (int i = 0; i < ModelConstants.COLS_NUMBER; i++) {
            for (int j = 0; j < i + 1; j++) {
                shelf.add(cardBlue, i);
            }
        }
        assertTrue(singleOccurrenceOfGivenShape.evaluate(shelf));

        // fill the library to create the fourth possible diagonal
        shelf = new Shelf();
        for (int i = 0; i < ModelConstants.COLS_NUMBER; i++) {
            for (int j = 0; j < i + 2; j++) {
                shelf.add(cardBlue, i);
            }
        }
        assertTrue(singleOccurrenceOfGivenShape.evaluate(shelf));




        shelf = new Shelf();

        singleOccurrenceOfGivenShape = singleOccurrenceOfGivenShapeConfiguration.getGoalAt(2);

        // creating one card per color to fill the board
        Tile cBlue = new Tile(TileColor.BLUE, 0);
        Tile cYellow = new Tile(TileColor.YELLOW, 0);
        Tile cGreen = new Tile(TileColor.GREEN, 0);
        Tile cViolet = new Tile(TileColor.VIOLET, 0);

        // check if evaluate works with an empty library
        assertFalse(singleOccurrenceOfGivenShape.evaluate(shelf));

        //we did a 3x3 blue square in the bottom left, should be true
        shelf.add(cBlue,0);
        shelf.add(cBlue,1);
        shelf.add(cBlue,2);
        shelf.add(cBlue,0);
        shelf.add(cBlue,1);
        shelf.add(cBlue,2);
        shelf.add(cBlue,0);
        shelf.add(cBlue,1);
        shelf.add(cBlue,2);

        assertTrue(singleOccurrenceOfGivenShape.evaluate(shelf));

        //Fast clear of the board
        shelf=new Shelf();

        //now in the bottom left we put a green one, no cross so no objective
        shelf.add(cGreen,0);
        shelf.add(cBlue,1);
        shelf.add(cBlue,2);
        shelf.add(cBlue,0);
        shelf.add(cBlue,1);
        shelf.add(cBlue,2);
        shelf.add(cBlue,0);
        shelf.add(cBlue,1);
        shelf.add(cBlue,2);

        assertFalse(singleOccurrenceOfGivenShape.evaluate(shelf));

        //Fast clear of the board
        shelf=new Shelf();

        //we did a stack on the right with a cross, should be true
        shelf.add(cViolet,2);
        shelf.add(cViolet,3);
        shelf.add(cViolet,4);
        shelf.add(cBlue,2);
        shelf.add(cYellow,3);
        shelf.add(cBlue,4);
        shelf.add(cYellow,2);
        shelf.add(cBlue,3);
        shelf.add(cYellow,4);
        shelf.add(cBlue,2);
        shelf.add(cYellow, 3);
        shelf.add(cBlue,4);
        shelf.add(cGreen,2);
        shelf.add(cGreen,3);
        shelf.add(cGreen,4);

        assertTrue(singleOccurrenceOfGivenShape.evaluate(shelf));
    }
}