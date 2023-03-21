package it.polimi.ingsw.model.commonGoals;

import it.polimi.ingsw.model.Tile;
import it.polimi.ingsw.model.TileColor;
import it.polimi.ingsw.model.Shelf;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.constants.AppConstants;

import java.util.HashSet;
import java.util.Set;

public class CommonGoal9 extends CommonGoal {



    public boolean evaluate(Shelf shelf){
        int result = 0;

        // used to store the colors found in a column so far
        Set<TileColor> differentColors;

        // looping in each row
        for (int i = 0; i < AppConstants.COLS_NUMBER; i++) {
            differentColors = new HashSet<>();

            // execute only if the row is full (if not it's not a valid column for sure )
            if (isColumnFull(shelf, i)) {
                for (int j = 0; j < AppConstants.ROWS_NUMBER; j++) {
                    // add each color in the row (if not empty) to the set (if already present it will not be added)
                    Tile tile = shelf.getTile(new Position(i, j));
                    differentColors.add(tile.getColor());
                }

                if (differentColors.size() == 6) result++;
            }
        }

        // return true only if there are more or equals than 2 valid rows
        return result >= 2;
    }



    private boolean isColumnFull(Shelf shelf, int col) {
        for (int i = 0; i < AppConstants.ROWS_NUMBER; i++) {
            if (shelf.getTile(new Position(col, i)).isEmpty()) return false;
        }

        return true;
    }
}

