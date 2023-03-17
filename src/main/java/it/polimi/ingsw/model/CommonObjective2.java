package it.polimi.ingsw.model;

import it.polimi.ingsw.model.constants.AppConstants;
import it.polimi.ingsw.model.utilities.UtilityFunctions;

import java.util.ArrayDeque;

/**
 * This class implements the second common objective: four groups made by four cards of the same color
 */
public class CommonObjective2 extends CommonObjective{
    /**
     * This method evaluate if the current player's library satisfies the common objective
     *
     * @param x Library of the current player
     * @return true if the objective has been satisfied
     */
    @Override
    public boolean evaluate(Library x) {
        Library copy = new Library(x);

        // counter for group of two cards
        int groupNum = 0;

        // foreach card in library look if its part of a group and return the group dimension
        for (int i = 0; i < AppConstants.ROWS_NUMBER; i++) {
            for (int j = 0; j < AppConstants.COLS_NUMBER; j++) {
                // select the current card
                Card c = copy.getCard(new Position(j, i));

                // execute the algorithm only if the current card is not empty
                if (!c.isEmpty()) {
                    // find the dimension of the group of which c is part and return its dimension divided by 4
                    groupNum += UtilityFunctions.findGroupSize(copy, new Position(j, i)) / 4;
                }
            }
        }

        return groupNum >= 4;
    }
}
