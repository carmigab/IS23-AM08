package it.polimi.ingsw.model.commonGoals;

import com.google.gson.annotations.Expose;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.Shelf;
import it.polimi.ingsw.model.Tile;
import it.polimi.ingsw.model.TileColor;
import it.polimi.ingsw.model.constants.AppConstants;

import java.util.HashSet;
import java.util.Set;

/**
 * This class implements the fifth, eighth, ninth and tenth common goal: N lines of tiles of at most M different colors
 */
public class NLinesOfAtMostMDifferentColors extends CommonGoal{
    @Expose
    private final int n;
    @Expose
    private final int m;
    @Expose
    private final boolean isColumn;

    /**
     * Constructor
     *
     * @param n        lines
     * @param m        max different colors
     * @param isColumn if true indicate that the line is column, if false is a row
     */
    public NLinesOfAtMostMDifferentColors(int n, int m, boolean isColumn) {
        this.n = n;
        this.m = m;
        this.isColumn = isColumn;
    }

    /**
     * This method evaluate if the current player's shelf satisfies the common goal
     *
     * @param shelf Shelf of the current player
     * @return true if the goal has been satisfied
     */
    @Override
    public boolean evaluate(Shelf shelf) {
        int result = 0;
        int jMax = isColumn ? AppConstants.COLS_NUMBER : AppConstants.ROWS_NUMBER;
        int iMax = !isColumn ? AppConstants.COLS_NUMBER : AppConstants.ROWS_NUMBER;

        // used to store the colors found in a line so far
        Set<TileColor> differentColors;

        // looping in each line
        for (int j = 0; j < jMax; j++) {
            differentColors = new HashSet<>();

            // execute only if the line is full (if not it's not a valid line for sure)
            if (isLineFull(shelf, j, isColumn)) {
                for (int i = 0; i < iMax; i++) {
                    // add each color in the line (if not empty) to the set (if already present it will not be added)
                    Tile card = shelf.getTile(new Position(isColumn ? j : i, isColumn ? i : j));
                    differentColors.add(card.getColor());
                }


                if (m > 3) {
                    // if there are exactly m colors add 1 to the valid lines
                    if (differentColors.size() == m) result++;
                }
                else
                    // if there are less or equals than m colors add 1 to the valid lines
                    if (differentColors.size() <= m) result++;
            }
        }

        // return true only if there are more or equals than 3 valid column
        return result >= n;
    }

    /**
     * This method is used by the evaluate method to check if a given line is full
     *
     * @param shelf the shelf to which the column belong
     * @param line the line to check
     * @param isColumn if true loop in the column if false in the row
     * @return true if the given line is full
     */
    private boolean isLineFull(Shelf shelf, int line, boolean isColumn) {
        int iMax = isColumn ? AppConstants.ROWS_NUMBER : AppConstants.COLS_NUMBER;
        for (int i = 0; i < iMax; i++) {
            if (shelf.getTile(new Position(isColumn ? line : i, !isColumn ? line : i)).isEmpty()) return false;
        }

        return true;
    }
}
