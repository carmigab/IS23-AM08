package it.polimi.ingsw.model.commonObjectives;

import it.polimi.ingsw.App;
import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.model.Library;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.constants.AppConstants;

import java.util.List;

import static it.polimi.ingsw.model.utilities.UtilityFunctions.findGroupContainingGivenPosition;

/**
 * This class implements the fourth common objective: two squares 2x2 of the same color
 */
public class CommonObjective4 extends CommonObjective{
    /**
     * This method evaluate if the current player's library satisfies the common objective
     *
     * @param x Library of the current player
     * @return true if the objective has been satisfied
     */
    @Override
    public boolean evaluate(Library x) {
        Library copy = new Library(x);

        // variable used to store each group and to check if it's a square
        List<Position> group;

        // counter for group of two cards
        int groupNum = 0;

        // loop in the library to check every possible square
        for (int i = 0; i < AppConstants.ROWS_NUMBER - 1; i++) {
            for (int j = 0; j < AppConstants.COLS_NUMBER - 1; j++) {
                // extract the group containing the given position
                group = findGroupContainingGivenPosition(x, new Position(j, i));

            }
        }

        return false;
    }
}
