package it.polimi.ingsw.model.commonGoals;

import com.google.gson.annotations.Expose;
import it.polimi.ingsw.constants.ModelConstants;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.Shelf;
import it.polimi.ingsw.model.Tile;
import it.polimi.ingsw.utilities.UtilityFunctionsModel;

/**
 * This class implements the first and second common goal: N group of M tiles of the same color (different groups can have different colors)
 */
public class NGroupsOfSizeM extends CommonGoal{
    @Expose
    private final int n;
    @Expose
    private final int m;

    /**
     * Constructor
     *
     * @param n number of groups
     * @param m number of tiles per group
     */
    public NGroupsOfSizeM(int n, int m) {
        this.n = n;
        this.m = m;
    }

    /**
     * This method evaluate if the current player's shelf satisfies the common goal
     *
     * @param shelf Shelf of the current player
     * @return true if the goal has been satisfied
     */
    @Override
    public boolean evaluate(Shelf shelf) {
        Shelf copy = new Shelf(shelf);

        // counter for group of m tiles
        int groupNum = 0;

        // foreach tile in shelf look if its part of a group and return the group dimension
        for (int i = 0; i < ModelConstants.ROWS_NUMBER; i++) {
            for (int j = 0; j < ModelConstants.COLS_NUMBER; j++) {
                // select the current tile
                Tile c = copy.getTile(new Position(j, i));

                // execute the algorithm only if the current tile is not empty
                if (!c.isEmpty()) {
                    // find the dimension of the group of which c is part and add 1 if the group is made of m or more tiles
                    groupNum += UtilityFunctionsModel.findGroupSize(copy, new Position(j, i)) >= m ? 1 : 0;
                }
            }
        }

        return groupNum >= n;
    }

    /**
     *
     * @param obj
     * @return
     */
    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof NGroupsOfSizeM nGroupsOfSizeM)) return false;
        return this.n == nGroupsOfSizeM.n &&
                this.m == nGroupsOfSizeM.m;
    }
}
