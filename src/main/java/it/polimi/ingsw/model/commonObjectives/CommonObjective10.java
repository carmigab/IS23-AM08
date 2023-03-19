package it.polimi.ingsw.model.commonObjectives;

import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.model.CardColor;
import it.polimi.ingsw.model.Library;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.constants.AppConstants;

import java.util.HashSet;
import java.util.Set;

/**
 * This class implements the tenth common objective: two rows containing five different colors
 */
public class CommonObjective10 extends CommonObjective {



    public boolean evaluate(Library library){
        int result = 0;

        // used to store the colors found in a column so far
        Set<CardColor> differentColors;

        // looping in each row
        for (int i = 0; i < AppConstants.ROWS_NUMBER; i++) {
            differentColors = new HashSet<>();

            // execute only if the row is full (if not it's not a valid column for sure )
            if (isRowFull(library, i)) {
                for (int j = 0; j < AppConstants.COLS_NUMBER; j++) {
                    // add each color in the row (if not empty) to the set (if already present it will not be added)
                    Card card = library.getCard(new Position(j, i));
                    differentColors.add(card.getColor());
                }

                if (differentColors.size() == 5) result++;
            }
        }

        // return true only if there are more or equals than 2 valid rows
        return result >= 2;
    }



    private boolean isRowFull(Library library, int row) {
        for (int i = 0; i < AppConstants.COLS_NUMBER; i++) {
            if (library.getCard(new Position(i, row)).isEmpty()) return false;
        }

        return true;
    }
}
