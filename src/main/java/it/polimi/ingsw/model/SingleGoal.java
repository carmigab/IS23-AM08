package it.polimi.ingsw.model;

import com.google.gson.annotations.Expose;

/**
 * this class represents the specific single objective. Six of them composed one of the personal objective assigned to
 * a player
 */
public class SingleGoal {
    /**
     * this attribute is the position in the personal shelf that allows the player to complete the objective
     */
    @Expose
    private final Position position;
    /**
     * this attribute is the color of the objective
     */
    @Expose
    private final TileColor color;

    /**
     * this class is the class constructor; it creates a new SingleGoal and sets the attribute position to the
     * value of the parameter position and the attribute color to the value of the parameter color
     * @param position the position of the objective
     * @param color the color of the objective
     */

    //Doubts on the class construct: I don't know if, using a json file, I had to change the class construct //NO
    public SingleGoal(Position position, TileColor color) {
        this.position = new Position(position);
        this.color = color;
    }

    /**
     * this method returns the position of the objective
     * @return a Position, the position of the objective
     */

    public Position getPosition() {
        return position;
    }

    /**
     * this method returns the color of the objective
     * @return a TileColor, the color of the objective
     */

    public TileColor getColor() {
        return color;
    }

    /**
     *
     * @param obj
     * @return
     */
    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof SingleGoal singleGoal)) return false;
        return this.color.equals(singleGoal.color) &&
                this.position.equals(singleGoal.position);
    }
}
