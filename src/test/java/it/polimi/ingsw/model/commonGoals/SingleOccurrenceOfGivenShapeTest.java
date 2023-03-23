package it.polimi.ingsw.model.commonGoals;

import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.Shelf;
import it.polimi.ingsw.model.Tile;
import it.polimi.ingsw.model.TileColor;
import it.polimi.ingsw.model.constants.AppConstants;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

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
        List<Position> shape = new ArrayList<>();
        shape.add(new Position(0, 0));
        shape.add(new Position(0, AppConstants.ROWS_NUMBER - 1));
        shape.add(new Position(AppConstants.COLS_NUMBER - 1, 0));
        shape.add(new Position(AppConstants.COLS_NUMBER - 1, AppConstants.ROWS_NUMBER - 1));
        
        singleOccurrenceOfGivenShape = new SingleOccurrenceOfGivenShape(shape, 5, 6, true);

        // card used to fill the board
        Tile cardBlue = new Tile(TileColor.BLUE, 0);

        // check if the method doesn't consider four empty corner as objective completed
        assertFalse(singleOccurrenceOfGivenShape.evaluate(shelf));

        // fill only 1, then 2, then 3 corner and check that the method still return false
        // 1
        shelf.add(cardBlue, 0);
        assertFalse(singleOccurrenceOfGivenShape.evaluate(shelf));
        // 2
        for (int i = 0; i < AppConstants.ROWS_NUMBER - 1; i++) {
            shelf.add(cardBlue, 0);
        }
        assertFalse(singleOccurrenceOfGivenShape.evaluate(shelf));
        // 3
        shelf.add(cardBlue, 4);
        assertFalse(singleOccurrenceOfGivenShape.evaluate(shelf));

        // fil the fourth corner and check if evaluate return true
        for (int i = 0; i < AppConstants.ROWS_NUMBER - 1; i++) {
            shelf.add(cardBlue, 4);
        }
        assertTrue(singleOccurrenceOfGivenShape.evaluate(shelf));



        shelf = new Shelf();
        shape = new ArrayList<>();
        for (int i = 0; i < AppConstants.COLS_NUMBER; i++) {
            shape.add(new Position(i, i));
        }

        singleOccurrenceOfGivenShape = new SingleOccurrenceOfGivenShape(shape, 5, 5, false);

        // check with empty library
        assertFalse(singleOccurrenceOfGivenShape.evaluate(shelf));

        // fill the library with some cards but not enough to make a diagonal
        for (int i = 0; i < 5; i++) {
            shelf.add(cardBlue, 0);
        }
        assertFalse(singleOccurrenceOfGivenShape.evaluate(shelf));

        // fill the library to create the first possible diagonal
        shelf = new Shelf();
        for (int i = 0; i < AppConstants.COLS_NUMBER; i++) {
            for (int j = 0; j < AppConstants.ROWS_NUMBER - i; j++) {
                shelf.add(cardBlue, i);
            }
        }
        assertTrue(singleOccurrenceOfGivenShape.evaluate(shelf));

        // fill the library to create the second possible diagonal
        shelf = new Shelf();
        for (int i = 0; i < AppConstants.COLS_NUMBER; i++) {
            for (int j = 0; j < AppConstants.ROWS_NUMBER - 1 - i; j++) {
                shelf.add(cardBlue, i);
            }
        }
        assertTrue(singleOccurrenceOfGivenShape.evaluate(shelf));

        // fill the library to create the third possible diagonal
        shelf = new Shelf();
        for (int i = 0; i < AppConstants.COLS_NUMBER; i++) {
            for (int j = 0; j < i + 1; j++) {
                shelf.add(cardBlue, i);
            }
        }
        assertTrue(singleOccurrenceOfGivenShape.evaluate(shelf));

        // fill the library to create the fourth possible diagonal
        shelf = new Shelf();
        for (int i = 0; i < AppConstants.COLS_NUMBER; i++) {
            for (int j = 0; j < i + 2; j++) {
                shelf.add(cardBlue, i);
            }
        }
        assertTrue(singleOccurrenceOfGivenShape.evaluate(shelf));




        shelf = new Shelf();
        shape = new ArrayList<>();
        shape.add(new Position(0, 0));
        shape.add(new Position(0, 2));
        shape.add(new Position(1, 1));
        shape.add(new Position(2, 0));
        shape.add(new Position(2, 2));

        singleOccurrenceOfGivenShape = new SingleOccurrenceOfGivenShape(shape, 3, 3, true);

        // creating one card per color to fill the board
        Tile cBlue = new Tile(TileColor.BLUE, 0);
        Tile cWhite = new Tile(TileColor.WHITE, 0);
        Tile cYellow = new Tile(TileColor.YELLOW, 0);
        Tile cLightBlue = new Tile(TileColor.LIGHT_BLUE, 0);
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