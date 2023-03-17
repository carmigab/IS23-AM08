package it.polimi.ingsw.model;

import it.polimi.ingsw.App;
import it.polimi.ingsw.model.constants.AppConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * This class implements the third common objective: four card of the same color in the four corner of the shelf
 */
public class CommonObjective3 extends CommonObjective{
    /**
     * This method evaluate if the current player's library satisfies the common objective
     *
     * @param x Library of the current player
     * @return true if the objective has been satisfied
     */
    @Override
    public boolean evaluate(Library x) {
        // initializing the position to be checked, the four corners
        List<Position> posToCheck = new ArrayList<>();
        posToCheck.add(new Position(0, 0));
        posToCheck.add(new Position(AppConstants.COLS_NUMBER - 1, 0));
        posToCheck.add(new Position(0, AppConstants.ROWS_NUMBER - 1));
        posToCheck.add(new Position(AppConstants.COLS_NUMBER - 1, AppConstants.ROWS_NUMBER - 1));

        // this variable store the color of the first corner to check if the others have the same color
        CardColor color = x.getCard(posToCheck.get(0)).getColor();

        // if the first corner is empty just return false immediately
        if (color.equals(CardColor.EMPTY)) return false;

        // for each corner if the color is different from the first corner color return false
        for (Position position: posToCheck) {
            if (!x.getCard(position).getColor().equals(color)) {
                return false;
            }
        }

        // return true in the end if all went good
        return true;
    }
}
