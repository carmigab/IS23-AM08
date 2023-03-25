package it.polimi.ingsw.model.commonGoals;

import com.google.gson.annotations.Expose;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.Shelf;
import it.polimi.ingsw.model.TileColor;
import it.polimi.ingsw.model.constants.AppConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * This class implements the third, seventh and eleventh common goal: check if a given shape is present in the board (four corners, diagonal, cross)
 */
public class SingleOccurrenceOfGivenShape extends CommonGoal{
    @Expose
    private final List<Position> shape;
    @Expose
    private final int shapeXSize;
    @Expose
    private final int shapeYSize;
    @Expose
    private final boolean isSymmetric;

    /**
     * Constructor
     *
     * @param shape       list of the starting position of the shape to check
     * @param shapeXSize  x size of the shape
     * @param shapeYSize  y size of the shape
     * @param isSymmetric if true the shape is symmetric and the method doesn't need to search the shape from both sides of the shelf
     */
    public SingleOccurrenceOfGivenShape(List<Position> shape, int shapeXSize, int shapeYSize, boolean isSymmetric) {
        this.shape = shape;
        this.shapeXSize = shapeXSize;
        this.shapeYSize = shapeYSize;
        this.isSymmetric = isSymmetric;
    }

    /**
     * This method evaluate if the current player's shelf satisfies the common goal
     *
     * @param shelf Shelf of the current player
     * @return true if the goal has been satisfied
     */
    @Override
    public boolean evaluate(Shelf shelf) {
        List<Position> shiftingShape;
        List<Position> shiftingShapeSymmetric;

        for (int i = 0; i <= AppConstants.ROWS_NUMBER - shapeYSize; i++) {
            for (int j = 0; j <= AppConstants.COLS_NUMBER - shapeXSize; j++) {
                shiftingShape = new ArrayList<>();
                shiftingShapeSymmetric = new ArrayList<>();


                for (Position position : shape) {
                    shiftingShape.add(new Position(position.x() + j, position.y() + i));

                    if (!isSymmetric) shiftingShapeSymmetric.add(new Position(AppConstants.COLS_NUMBER - 1 - position.x() - j, position.y() + i));
                }

                if (sameColor(shiftingShape, shelf) || sameColor(shiftingShapeSymmetric, shelf)) return true;
            }
        }

        return false;
    }

    /**
     * This method is used by the evaluate function to check if the given shape of tiles is made by only one color
     *
     * @param shape list of position to check in the shelf
     * @param shelf shelf of the player to look at
     * @return true if the color of all the tiles in shape are of the same color
     */
    private boolean sameColor(List<Position> shape, Shelf shelf) {
        if (shape.isEmpty()) return false;

        if (shelf.getTile(shape.get(0)).isEmpty()) return false;
        TileColor color = shelf.getTile(shape.get(0)).getColor();

        for (Position position : shape) {
            if (!shelf.getTile(position).getColor().equals(color)) return false;
        }

        return true;
    }
}
