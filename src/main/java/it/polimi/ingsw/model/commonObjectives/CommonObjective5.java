package it.polimi.ingsw.model.commonObjectives;

import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.model.CardColor;
import it.polimi.ingsw.model.Library;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.constants.AppConstants;

import java.util.HashSet;
import java.util.Set;

/**
 * This class implements the fifth common objective: three column containing max three different colors
 */
public class CommonObjective5 extends CommonObjective{
    /**
     * This method evaluate if the current player's library satisfies the common objective
     *
     * @param library Library of the current player
     * @return true if the objective has been satisfied
     */
    @Override
    public boolean evaluate(Library library) {
        int result = 0;

        // used to store the colors found in a column so far
        Set<CardColor> differentColors;

        // looping in each column
        for (int j = 0; j < AppConstants.COLS_NUMBER; j++) {
            differentColors = new HashSet<>();

            // execute only if the column is full (if not it's not a valid column for sure)
            if (isColumnFull(library, j)) {
                for (int i = 0; i < AppConstants.ROWS_NUMBER; i++) {
                    // add each color in the column (if not empty) to the set (if already present it will not be added)
                    Card card = library.getCard(new Position(j, i));
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
     * @param library the library to which the column belong
     * @param column the column to check
     * @return true is the column is full, false elsewhere
     */
    private boolean isColumnFull(Library library, int column) {
        for (int i = 0; i < AppConstants.ROWS_NUMBER; i++) {
            if (library.getCard(new Position(column, i)).isEmpty()) return false;
        }

        return true;
    }
}
