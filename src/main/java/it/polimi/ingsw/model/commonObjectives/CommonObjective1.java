package it.polimi.ingsw.model.commonObjectives;

import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.model.Library;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.constants.AppConstants;
import it.polimi.ingsw.model.utilities.UtilityFunctions;

import java.util.List;

import static it.polimi.ingsw.model.utilities.UtilityFunctions.findGroupContainingGivenPosition;

/**
 * This class implements the first common objective: six group of two cards of the same color (different groups can have different colors)
 */
public class CommonObjective1 extends CommonObjective {

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
                    // find the dimension of the group of which c is part and add 1 if the group is made of 4 or more cards
                    groupNum += UtilityFunctions.findGroupSize(copy, new Position(j, i)) >= 2 ? 1 : 0;
                }
            }
        }

        return groupNum >= 6;
    }
}
