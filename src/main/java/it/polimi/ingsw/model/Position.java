package it.polimi.ingsw.model;

/**
 * this class represent the position of a specific card in the game board
 */
public class Position {
    private int x;
    private int y;

    /**
     * This is the constructor of the class
     * @param x this attribute represent the row of the position
     * @param y this attribute represent the column of the position
     */
    public Position (int x, int y){
        this.x=x;
        this.y=y;
    }

    /**
     * This is the copy constructor of the class
     * @param p position to be copied
     */
    public Position (Position p){
        this.x=p.x;
        this.y=p.y;
    }

    /**
     * this method return the value of the attribute x;
     *
     * @return an int.
     */
    public int x() {
        return x;
    }

    /**
     * this method return the value of the attribute y;
     *
     * @return an int.
     */
    public int y() {
        return y;
    }

    /**
     * this method overrides the method equals. it assumes that two positions are equals if they have the same x and y values;
     * if the parameter isn't a position, the method return false
     * @param obj
     * @return a boolean
     */
    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Position)) return false;
        Position position = (Position) obj;
        return this.x == position.x && this.y == position.y;
    }
}
