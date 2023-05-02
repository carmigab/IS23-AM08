package it.polimi.ingsw.model.commonGoals;

import it.polimi.ingsw.constants.ModelConstants;
import it.polimi.ingsw.model.Shelf;
import it.polimi.ingsw.model.Position;

import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.utilities.UtilityFunctionsModel.findGroupContainingGivenPosition;

/**
 * This class implements the fourth common goal: two squares 2x2 of the same color
 */
public class TwoSquares extends CommonGoal {
    /**
     * This method evaluate if the current player's shelf satisfies the common goal
     *
     * @param x Shelf of the current player
     * @return true if the goal has been satisfied
     */
    @Override
    public boolean evaluate(Shelf x) {
        Shelf copy = new Shelf(x);

        // counter for group of four tiles
        int groupNum = 0;

        List<Position> group;

        // foreach tile in shelf look if its part of a group and return the group dimension
        for (int i = 0; i < ModelConstants.ROWS_NUMBER - 1; i++) {
            for (int j = 0; j < ModelConstants.COLS_NUMBER - 1; j++) {
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

    /**
     *
     * @param obj
     * @return
     */
    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof TwoSquares twoSquares)) return false;
        return true;
    }
}


