package it.polimi.ingsw.model.commonObjectives;

import it.polimi.ingsw.App;
import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.model.CardColor;
import it.polimi.ingsw.model.Library;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.constants.AppConstants;
import it.polimi.ingsw.model.utilities.UtilityFunctions;

import java.util.ArrayList;
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

        // counter for group of four cards
        int groupNum = 0;

        List<Position> group;

        // foreach card in library look if its part of a group and return the group dimension
        for (int i = 0; i < AppConstants.ROWS_NUMBER - 1; i++) {
            for (int j = 0; j < AppConstants.COLS_NUMBER - 1; j++) {
                group = findGroupContainingGivenPosition(copy, new Position(j, i));

                groupNum += group.size() >= 4 ? (containSquare(group) ? 1 : 0) : 0;
            }
        }

        return groupNum >= 2;
    }

    /**
     * This method check if a larger group contains a square
     *
     * @param group group to analyze
     * @return  true if a square is contained, false elsewhere
     */
    private boolean containSquare(List<Position> group) {
        List<Position> square;
        for (Position position : group) {
            square = new ArrayList<>();
            square.add(new Position(position.x() + 1, position.y()));
            square.add(new Position(position.x(), position.y() + 1));
            square.add(new Position(position.x() + 1, position.y() + 1));

            if (group.containsAll(square)) return true;
        }

        return false;
    }
}


