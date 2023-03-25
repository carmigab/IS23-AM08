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
 * Unit test for NGroupOfMTiles
 */
class NGroupsOfSizeMTest {
    NGroupsOfSizeM nGroupsOfSizeM;
    
    /**
     * This method test the evaluate method of NGroupOfMTiles
     */     
    @Test
    void evaluate() {
        Shelf shelf = new Shelf();

        Gson jsonLoader= JsonWithExposeSingleton.getJsonWithExposeSingleton();
        Reader fileReader= null;
        try {
            fileReader = new FileReader(AppConstants.FILE_CONFIG_NGROUPOFSIZEM);
        }
        catch(FileNotFoundException e){
            System.out.println(e);
        }
        NGroupsOfSizeMConfiguration nGroupsOfSizeMConfiguration = jsonLoader.fromJson(fileReader, NGroupsOfSizeMConfiguration.class);


        nGroupsOfSizeM = nGroupsOfSizeMConfiguration.getGoalAt(0);

        // creating one card per color to fill the board
        Tile cBlue = new Tile(TileColor.BLUE, 0);
        Tile cWhite = new Tile(TileColor.WHITE, 0);
        Tile cYellow = new Tile(TileColor.YELLOW, 0);
        Tile cLightBlue = new Tile(TileColor.LIGHT_BLUE, 0);
        Tile cGreen = new Tile(TileColor.GREEN, 0);
        Tile cViolet = new Tile(TileColor.VIOLET, 0);

        // check if evaluate works with an empty library
        assertFalse(nGroupsOfSizeM.evaluate(shelf));

        // partially fill the library in a way to not create any group of two adjacent cards of the same color and check evaluate to false
        shelf.add(cBlue, 0);
        shelf.add(cViolet, 0);
        shelf.add(cGreen, 0);
        shelf.add(cYellow, 1);
        shelf.add(cWhite, 2);
        shelf.add(cBlue, 2);
        assertFalse(nGroupsOfSizeM.evaluate(shelf));

        // add other cards to the library to create some groups of two cards but not enough to satisfy the objective and check evaluate to false
        shelf.add(cGreen, 0);
        shelf.add(cYellow, 1);
        shelf.add(cBlue, 2);
        assertFalse(nGroupsOfSizeM.evaluate(shelf));

        // add other cards to create 4 groups of two cards and one group of 3 card to be sure that the evaluate function doesn't count cards twice
        shelf.add(cGreen, 1);
        shelf.add(cLightBlue, 3);
        shelf.add(cLightBlue, 3);
        shelf.add(cGreen, 3);
        shelf.add(cGreen, 3);
        assertFalse(nGroupsOfSizeM.evaluate(shelf));

        // reach 6 groups of two cards of the same color and check evaluate true
        shelf.add(cViolet, 4);
        shelf.add(cViolet, 4);
        assertTrue(nGroupsOfSizeM.evaluate(shelf));

        // check if works also with more than 6 groups
        shelf.add(cYellow, 4);
        shelf.add(cYellow, 4);
        assertTrue(nGroupsOfSizeM.evaluate(shelf));

        // check bad edge case
        shelf = new Shelf();
        shelf.add(cBlue, 0);
        shelf.add(cBlue, 0);
        shelf.add(cGreen, 1);
        shelf.add(cBlue, 1);
        shelf.add(cGreen, 2);
        shelf.add(cBlue, 2);
        shelf.add(cViolet, 3);
        shelf.add(cViolet, 3);
        shelf.add(cYellow, 4);
        shelf.add(cYellow, 4);
        shelf.add(cViolet, 4);
        shelf.add(cViolet, 4);
        shelf.add(cYellow, 4);
        shelf.add(cYellow, 4);
        assertTrue(nGroupsOfSizeM.evaluate(shelf));
        
        // four groups of four tiles
        shelf = new Shelf();
        nGroupsOfSizeM = nGroupsOfSizeMConfiguration.getGoalAt(1);

        // check if evaluate works with an empty library
        assertFalse(nGroupsOfSizeM.evaluate(shelf));

        // partially fill the library in a way to not create any group of cards
        shelf.add(cBlue, 0);
        shelf.add(cGreen, 1);
        shelf.add(cViolet, 4);
        assertFalse(nGroupsOfSizeM.evaluate(shelf));

        // add other cards to create some groups but all with less than 4 cards
        shelf.add(cBlue, 0);
        shelf.add(cBlue, 0);
        shelf.add(cGreen, 2);
        shelf.add(cViolet, 4);
        assertFalse(nGroupsOfSizeM.evaluate(shelf));

        // add other cards to create 4 groups of 4 cards
        shelf.add(cBlue, 0);
        shelf.add(cGreen, 2);
        shelf.add(cGreen, 3);
        shelf.add(cViolet, 4);
        shelf.add(cViolet, 3);
        shelf.add(cWhite, 2);
        shelf.add(cWhite, 2);
        shelf.add(cWhite, 3);
        shelf.add(cWhite, 3);
        assertTrue(nGroupsOfSizeM.evaluate(shelf));

        // try with two group of four of the same color and adjacent to assure the method count them as 1 group and not as 2
        shelf = new Shelf();
        shelf.add(cBlue, 0);
        shelf.add(cGreen, 1);
        shelf.add(cViolet, 4);
        shelf.add(cBlue, 0);
        shelf.add(cBlue, 0);
        shelf.add(cGreen, 2);
        shelf.add(cViolet, 4);
        shelf.add(cBlue, 0);
        shelf.add(cGreen, 2);
        shelf.add(cGreen, 3);
        shelf.add(cViolet, 4);
        shelf.add(cViolet, 3);
        shelf.add(cViolet, 2);
        shelf.add(cViolet, 2);
        shelf.add(cViolet, 3);
        shelf.add(cViolet, 3);
        assertFalse(nGroupsOfSizeM.evaluate(shelf));
    }
}