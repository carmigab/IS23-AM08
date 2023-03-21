package it.polimi.ingsw.model.commonGoals;

import it.polimi.ingsw.model.Shelf;
import it.polimi.ingsw.model.TileColor;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.constants.AppConstants;

/**
 * This class implements the seventh common goal: a diagonal of tile of the same color
 */
public class CommonGoal7 extends CommonGoal {
    /**
     * This method evaluate if the current player's shelf satisfies the common goal
     *
     * @param shelf Shelf of the current player
     * @return true if the goal has been satisfied
     */
    @Override
    public boolean evaluate(Shelf shelf) {
        // return true only if at lest one of the 4 possible diagonals return is valid
        return validDiagonal(shelf, new Position(0, 0), true) ||
                validDiagonal(shelf, new Position(0, 1), true) ||
                validDiagonal(shelf, new Position(AppConstants.COLS_NUMBER - 1, 0), true) ||
                validDiagonal(shelf, new Position(AppConstants.COLS_NUMBER - 1, 1), true);
    }

    /**
     * This method is used by the evaluate method to check if a diagonal is valid for the goal
     *
     * @param shelf Shelf of the current player
     * @param startingPosition starting position of the diagonal
     * @param goingRight true = from top left to bot right, false = from top right to bot left
     * @return true if the diagonal analyzed is valid
     */
    private boolean validDiagonal(Shelf shelf, Position startingPosition, boolean goingRight) {
        TileColor color = shelf.getTile(startingPosition).getColor();

        // if starting tile is empty the diagonal is invalid for sure
        if (color.equals(TileColor.EMPTY) || color.equals(TileColor.INVALID)) return false;

        // choosing diagonal direction, true = from top left to bot right, false = from top right to bot left
        if (goingRight == true) {
            for (int i = 0; i < AppConstants.COLS_NUMBER; i++) {
                // if one of the tile on the diagonal has a different color return false
                if (!shelf.getTile(new Position(startingPosition.x() + i, startingPosition.y() + i)).getColor().equals(color)) return false;
            }
        }
        else {
            for (int i = 0; i < AppConstants.COLS_NUMBER; i++) {
                // if one of the tile on the diagonal has a different color return false
                if (!shelf.getTile(new Position(startingPosition.x() - i, startingPosition.y() + i)).getColor().equals(color)) return false;
            }
        }

        return true;
    }
}
