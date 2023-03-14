package it.polimi.ingsw;

/**
 * this class represent the position of a specific card in the game board
 */
public class Position {
    /**
     * this attribute represent the row of the position
     */
    private final int x;
    /**
     * this attribute represent the column of the position
     */
    private final int y;

    /**
     * this method is the class constructor
     * @param x : set attribute x to x value
     * @param y : set attribute y to y value
     */
    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * this method return the value of the attribute x;
     * @return an int.
     */
    public int getX() {
        return x;
    }

    /**
     * this method return the value of the attribute y;
     * @return an int.
     */

    public int getY() {
        return y;
    }
}
