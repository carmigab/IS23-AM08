package it.polimi.ingsw.model.commonGoals;

import it.polimi.ingsw.model.Shelf;
import it.polimi.ingsw.model.Tile;
import it.polimi.ingsw.model.TileColor;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.constants.AppConstants;

import java.util.HashSet;
import java.util.Set;

/**
 * This class implements the eighth common goal: four rows containing max three different colors
 */
public class CommonGoal8 extends CommonGoal {
    /**
     * This method evaluate if the current player's shelf satisfies the common goal
     *
     * @param shelf Shelf of the current player
     * @return true if the goal has been satisfied
     */
    @Override
    public boolean evaluate(Shelf shelf) {
        int result = 0;

        // used to store the colors found in a column so far
        Set<TileColor> differentColors;

        // looping in each row
        for (int i = 0; i < AppConstants.ROWS_NUMBER; i++) {
            differentColors = new HashSet<>();

            // execute only if the row is full (if not it's not a valid column for sure)
            if (isRowFull(shelf, i)) {
                for (int j = 0; j < AppConstants.COLS_NUMBER; j++) {
                    // add each color in the row (if not empty) to the set (if already present it will not be added)
                    Tile tile = shelf.getTile(new Position(j, i));
                    differentColors.add(tile.getColor());
                }
                // if there are less or equals than 3 colors add 1 to the valid rows
                if (differentColors.size() <= 3) result++;
            }
        }

        // return true only if there are more or equals than 3 valid rows
        return result >= 4;
    }

    /**
     * This method is used by the evaluate method to check if a given column is full
     *
     * @param shelf the shelf to which the column belong
     * @param row the column to check
     * @return true is the column is full, false elsewhere
     */
    private boolean isRowFull(Shelf shelf, int row) {
        for (int i = 0; i < AppConstants.COLS_NUMBER; i++) {
            if (shelf.getTile(new Position(i, row)).isEmpty()) return false;
        }

        return true;
    }
}
