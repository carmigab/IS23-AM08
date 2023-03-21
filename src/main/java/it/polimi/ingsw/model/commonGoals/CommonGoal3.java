package it.polimi.ingsw.model.commonGoals;

import it.polimi.ingsw.model.Shelf;
import it.polimi.ingsw.model.TileColor;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.constants.AppConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * This class implements the third common goal: four tile of the same color in the four corner of the shelf
 */
public class CommonGoal3 extends CommonGoal {
    /**
     * This method evaluate if the current player's shelf satisfies the common goal
     *
     * @param x Shelf of the current player
     * @return true if the goal has been satisfied
     */
    @Override
    public boolean evaluate(Shelf x) {
        // initializing the position to be checked, the four corners
        List<Position> posToCheck = new ArrayList<>();
        posToCheck.add(new Position(0, 0));
        posToCheck.add(new Position(AppConstants.COLS_NUMBER - 1, 0));
        posToCheck.add(new Position(0, AppConstants.ROWS_NUMBER - 1));
        posToCheck.add(new Position(AppConstants.COLS_NUMBER - 1, AppConstants.ROWS_NUMBER - 1));

        // this variable store the color of the first corner to check if the others have the same color
        TileColor color = x.getTile(posToCheck.get(0)).getColor();

        // if the first corner is empty just return false immediately
        if (color.equals(TileColor.EMPTY)) return false;

        // for each corner if the color is different from the first corner color return false
        for (Position position: posToCheck) {
            if (!x.getTile(position).getColor().equals(color)) {
                return false;
            }
        }

        // return true in the end if all went good
        return true;
    }
}
