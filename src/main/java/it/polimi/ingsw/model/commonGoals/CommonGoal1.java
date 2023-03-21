package it.polimi.ingsw.model.commonGoals;

import it.polimi.ingsw.model.Tile;
import it.polimi.ingsw.model.Shelf;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.constants.AppConstants;
import it.polimi.ingsw.model.utilities.UtilityFunctions;

/**
 * This class implements the first common goal: six group of two tiles of the same color (different groups can have different colors)
 */
public class CommonGoal1 extends CommonGoal {

    /**
     * This method evaluate if the current player's shelf satisfies the common goal
     *
     * @param x Shelf of the current player
     * @return true if the goal has been satisfied
     */
    @Override
    public boolean evaluate(Shelf x) {
        Shelf copy = new Shelf(x);

        // counter for group of two tiles
        int groupNum = 0;

        // foreach tile in shelf look if its part of a group and return the group dimension
        for (int i = 0; i < AppConstants.ROWS_NUMBER; i++) {
            for (int j = 0; j < AppConstants.COLS_NUMBER; j++) {
                // select the current tile
                Tile c = copy.getTile(new Position(j, i));

                // execute the algorithm only if the current tile is not empty
                if (!c.isEmpty()) {
                    // find the dimension of the group of which c is part and add 1 if the group is made of 4 or more tiles
                    groupNum += UtilityFunctions.findGroupSize(copy, new Position(j, i)) >= 2 ? 1 : 0;
                }
            }
        }

        return groupNum >= 6;
    }
}
