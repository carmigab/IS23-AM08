package it.polimi.ingsw.model;

/**
 * this class represents the specific single objective. Six of them composed one of the personal objective assigned to
 * a player
 */
public class SingleObjective {
    /**
     * this attribute is the position in the personal library that allows the player to complete the objective
     */
    private final Position position;
    /**
     * this attribute is the color of the objective
     */
    private final CardColor color;

    /**
     * this class is the class constructor; it creates a new SingleObjective and sets the attribute position to the
     * value of the parameter position and the attribute color to the value of the parameter color
     * @param position the position of the objective
     * @param color the color of the objective
     */

    //Doubts on the class construct: i don't know if, using a json file, i had to change the class construct
    public SingleObjective(Position position, CardColor color) {
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
     * @return a CardColor, the color of the objective
     */

    public CardColor getColor() {
        return color;
    }
}
