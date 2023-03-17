package it.polimi.ingsw.model.commonObjectives;

import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.model.Library;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.constants.AppConstants;

/**
 * This class implements the sixth common objective: 8 cards of the same color anywhere in the library
 */
public class CommonObjective6 extends CommonObjective{
    /**
     * This method evaluate if the current player's library satisfies the common objective
     *
     * @param library Library of the current player
     * @return true if the objective has been satisfied
     */
    @Override
    public boolean evaluate(Library library) {
        // store the number of card of each color in the board
        int colorCounters[] = new int[AppConstants.TOTAL_COLORS];

        // for each card check if it's empty and if not add 1 to the corresponding color counter
        for (int i = 0; i < AppConstants.ROWS_NUMBER; i++) {
            for (int j = 0; j < AppConstants.COLS_NUMBER; j++) {
                Card card = library.getCard(new Position(j, i));

                if (!card.isEmpty() && !card.isInvalid()) {
                    colorCounters[card.getColor().ordinal()]++;
                }
            }
        }

        // return true only if there is at least one color with counter >= 8
        for (int i = 0; i < colorCounters.length; i++) {
            if (colorCounters[i] >= 8) return true;
        }

        return false;
    }
}
