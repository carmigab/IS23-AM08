package it.polimi.ingsw.model.commonObjectives;

import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.model.Library;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.constants.AppConstants;

import java.util.List;

import static it.polimi.ingsw.model.utilities.UtilityFunctions.findGroupContainingGivenPosition;

/**
 * This class implements the first common objective: six group of two cards of the same color (different groups can have different colors)
 */
public class CommonObjective1 extends CommonObjective{

    /**
     * This method evaluate if the current player's library satisfies the common objective
     *
     * @param x Library of the current player
     * @return true if the objective has been satisfied
     */
    @Override
    public boolean evaluate(Library x) {
        Library copy = new Library(x);

        // variable used to store each group and to count how many groups of two cards it contains
        List<Position> group;

        // counter for group of two cards
        int groupNum = 0;

        // foreach Card in library find the group containing it and analyze the group to find how many groups of two cards it contains
        for (int i = 0; i < AppConstants.ROWS_NUMBER; i++) {
            for (int j = 0; j < AppConstants.COLS_NUMBER; j++) {
                group = findGroupContainingGivenPosition(copy, new Position(j, i));
            }
        }

        return groupNum >= 6;
    }

    /**
     * This is a private method used by the evaluate method to check if two card have the same color
     *
     * @param c1 the first card
     * @param c2 the second card
     * @return true if c1 has the same color of c2
     */
    private boolean sameColor (Card c1, Card c2) {
        return c1.getColor().equals(c2.getColor());
    }
}
