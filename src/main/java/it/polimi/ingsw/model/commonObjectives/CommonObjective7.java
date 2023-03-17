package it.polimi.ingsw.model.commonObjectives;

import it.polimi.ingsw.model.CardColor;
import it.polimi.ingsw.model.Library;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.constants.AppConstants;

/**
 * This class implements the seventh common objective: a diagonal of card of the same color
 */
public class CommonObjective7 extends CommonObjective{
    /**
     * This method evaluate if the current player's library satisfies the common objective
     *
     * @param library Library of the current player
     * @return true if the objective has been satisfied
     */
    @Override
    public boolean evaluate(Library library) {
        // return true only if at lest one of the 4 possible diagonals return is valid
        return validDiagonal(library, new Position(0, 0), true) ||
                validDiagonal(library, new Position(0, 1), true) ||
                validDiagonal(library, new Position(AppConstants.COLS_NUMBER - 1, 0), true) ||
                validDiagonal(library, new Position(AppConstants.COLS_NUMBER - 1, 1), true);
    }

    /**
     * This method is used by the evaluate method to check if a diagonal is valid for the goal
     *
     * @param library Library of the current player
     * @param startingPosition starting position of the diagonal
     * @param goingRight true = from top left to bot right, false = from top right to bot left
     * @return true if the diagonal analyzed is valid
     */
    private boolean validDiagonal(Library library, Position startingPosition, boolean goingRight) {
        CardColor color = library.getCard(startingPosition).getColor();

        // if starting card is empty the diagonal is invalid for sure
        if (color.equals(CardColor.EMPTY) || color.equals(CardColor.INVALID)) return false;

        // choosing diagonal direction, true = from top left to bot right, false = from top right to bot left
        if (goingRight == true) {
            for (int i = 0; i < AppConstants.COLS_NUMBER; i++) {
                // if one of the card on the diagonal has a different color return false
                if (!library.getCard(new Position(startingPosition.x() + i, startingPosition.y() + i)).getColor().equals(color)) return false;
            }
        }
        else {
            for (int i = 0; i < AppConstants.COLS_NUMBER; i++) {
                // if one of the card on the diagonal has a different color return false
                if (!library.getCard(new Position(startingPosition.x() - i, startingPosition.y() + i)).getColor().equals(color)) return false;
            }
        }

        return true;
    }
}
