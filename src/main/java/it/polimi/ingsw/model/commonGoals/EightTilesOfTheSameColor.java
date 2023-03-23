package it.polimi.ingsw.model.commonGoals;

import it.polimi.ingsw.model.Tile;
import it.polimi.ingsw.model.Shelf;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.constants.AppConstants;

/**
 * This class implements the sixth common goal: 8 cards of the same color anywhere in the library
 */
public class EightTilesOfTheSameColor extends CommonGoal {
    /**
     * This method evaluate if the current player's library satisfies the commoon goal
     *
     * @param library Shelf of the current player
     * @return true if the goal has been satisfied
     */
    @Override
    public boolean evaluate(Shelf library) {
        // store the number of card of each color in the board
        int colorCounters[] = new int[AppConstants.TOTAL_COLORS];

        // for each card check if it's empty and if not add 1 to the corresponding color counter
        for (int i = 0; i < AppConstants.ROWS_NUMBER; i++) {
            for (int j = 0; j < AppConstants.COLS_NUMBER; j++) {
                Tile card = library.getTile(new Position(j, i));

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
