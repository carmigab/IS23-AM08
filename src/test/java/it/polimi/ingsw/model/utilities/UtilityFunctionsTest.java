package it.polimi.ingsw.model.utilities;

import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.model.CardColor;
import it.polimi.ingsw.model.Library;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.constants.AppConstants;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test for UtilityFunctions
 */
class UtilityFunctionsTest {

    /**
     * This method test if the method findGroupSize works properly
     */
    @Test
    void findGroupSize() {
        Library library = new Library();
        Library copy = new Library(library.getMyLibrary());
        Card card = new Card(CardColor.BLUE, 0);

        // initializing the library with a group of only one card and check if the method return 1 and if the library has not been changed except for the card valued
        library.add(card, 0);
        assertEquals(1, UtilityFunctions.findGroupSize(library, new Position(0, 5)));
        for (int i = 0; i < AppConstants.ROWS_NUMBER; i++) {
            for (int j = 0; j < AppConstants.COLS_NUMBER; j++) {
                Position position = new Position(j, i);
                assertEquals(copy.getCard(position), library.getCard(position));
            }
        }

        // add more cards to create a group of 2, then of 3, 4, 5, 6 and 7
        library.add(card, 0);
        library.add(card, 0);
        assertEquals(2, UtilityFunctions.findGroupSize(library, new Position(0, 5)));
        for (int i = 0; i < AppConstants.ROWS_NUMBER; i++) {
            for (int j = 0; j < AppConstants.COLS_NUMBER; j++) {
                Position position = new Position(j, i);
                assertEquals(copy.getCard(position), library.getCard(position));
            }
        }

        library.add(card, 0);
        library.add(card, 0);
        library.add(card, 1);
        assertEquals(3, UtilityFunctions.findGroupSize(library, new Position(0, 5)));
        for (int i = 0; i < AppConstants.ROWS_NUMBER; i++) {
            for (int j = 0; j < AppConstants.COLS_NUMBER; j++) {
                Position position = new Position(j, i);
                assertEquals(copy.getCard(position), library.getCard(position));
            }
        }

        library.add(card, 0);
        library.add(card, 0);
        library.add(card, 1);
        library.add(card, 1);
        assertEquals(4, UtilityFunctions.findGroupSize(library, new Position(0, 5)));
        for (int i = 0; i < AppConstants.ROWS_NUMBER; i++) {
            for (int j = 0; j < AppConstants.COLS_NUMBER; j++) {
                Position position = new Position(j, i);
                assertEquals(copy.getCard(position), library.getCard(position));
            }
        }

        library.add(card, 0);
        library.add(card, 0);
        library.add(card, 1);
        library.add(card, 1);
        library.add(card, 0);
        assertEquals(5, UtilityFunctions.findGroupSize(library, new Position(0, 5)));
        for (int i = 0; i < AppConstants.ROWS_NUMBER; i++) {
            for (int j = 0; j < AppConstants.COLS_NUMBER; j++) {
                Position position = new Position(j, i);
                assertEquals(copy.getCard(position), library.getCard(position));
            }
        }

        library.add(card, 0);
        library.add(card, 0);
        library.add(card, 1);
        library.add(card, 1);
        library.add(card, 0);
        library.add(card, 0);
        assertEquals(6, UtilityFunctions.findGroupSize(library, new Position(0, 5)));
        for (int i = 0; i < AppConstants.ROWS_NUMBER; i++) {
            for (int j = 0; j < AppConstants.COLS_NUMBER; j++) {
                Position position = new Position(j, i);
                assertEquals(copy.getCard(position), library.getCard(position));
            }
        }

        library.add(card, 0);
        library.add(card, 0);
        library.add(card, 1);
        library.add(card, 1);
        library.add(card, 0);
        library.add(card, 0);
        library.add(card, 2);
        assertEquals(7, UtilityFunctions.findGroupSize(library, new Position(0, 5)));
        for (int i = 0; i < AppConstants.ROWS_NUMBER; i++) {
            for (int j = 0; j < AppConstants.COLS_NUMBER; j++) {
                Position position = new Position(j, i);
                assertEquals(copy.getCard(position), library.getCard(position));
            }
        }

        // refill the library with more than one group one near the other to be sure that findGroupSize does not accidentally modify one group while finding the other
        Card card1 = new Card(CardColor.GREEN, 0);
        library.add(card1, 2);
        library.add(card1, 2);
        library.add(card1, 2);
        library.add(card1, 1);
        copy = new Library(library.getMyLibrary());
        library.add(card, 0);
        library.add(card, 0);
        library.add(card, 1);
        library.add(card, 1);
        library.add(card, 0);
        assertEquals(5, UtilityFunctions.findGroupSize(library, new Position(0, 5)));
        for (int i = 0; i < AppConstants.ROWS_NUMBER; i++) {
            for (int j = 0; j < AppConstants.COLS_NUMBER; j++) {
                Position position = new Position(j, i);
                assertEquals(copy.getCard(position), library.getCard(position));
            }
        }
    }
}