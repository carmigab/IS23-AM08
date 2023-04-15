package it.polimi.ingsw.model;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

/**
 * this class represents the specific single objective. Six of them composed one of the personal objective
 * assigned to a player
 */
public class SingleGoal implements Serializable {
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
     * this method overrides the method equals. it assumes that two SingleGoal are equals if they have the
     * same position and the same color; if the parameter isn't a SingleGoal, return false
     * @param obj object to be compared with "this"
     * @return a boolean
     */
    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof SingleGoal singleGoal)) return false;
        return this.color.equals(singleGoal.color) &&
                this.position.equals(singleGoal.position);
    }
}
