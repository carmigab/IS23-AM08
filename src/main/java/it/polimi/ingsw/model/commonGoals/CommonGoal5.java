package it.polimi.ingsw.model.commonGoals;

import it.polimi.ingsw.model.Shelf;
import it.polimi.ingsw.model.Tile;
import it.polimi.ingsw.model.TileColor;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.constants.AppConstants;

import java.util.HashSet;
import java.util.Set;

/**
 * This class implements the fifth common goal: three column containing max three different colors
 */
public class CommonGoal5 extends CommonGoal {
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

        // looping in each column
        for (int j = 0; j < AppConstants.COLS_NUMBER; j++) {
            differentColors = new HashSet<>();

            // execute only if the column is full (if not it's not a valid column for sure)
            if (isColumnFull(shelf, j)) {
                for (int i = 0; i < AppConstants.ROWS_NUMBER; i++) {
                    // add each color in the column (if not empty) to the set (if already present it will not be added)
                    Tile card = shelf.getTile(new Position(j, i));
                    differentColors.add(card.getColor());
                }
                // if there are less or equals than 3 colors add 1 to the valid columns
                if (differentColors.size() <= 3) result++;
            }
        }

        // return true only if there are more or equals than 3 valid column
        return result >= 3;
    }

    /**
     * This method is used by the evaluate method to check if a given column is full
     *
     * @param shelf the shelf to which the column belong
     * @param column the column to check
     * @return true is the column is full, false elsewhere
     */
    private boolean isColumnFull(Shelf shelf, int column) {
        for (int i = 0; i < AppConstants.ROWS_NUMBER; i++) {
            if (shelf.getTile(new Position(column, i)).isEmpty()) return false;
        }

        return true;
    }
}
