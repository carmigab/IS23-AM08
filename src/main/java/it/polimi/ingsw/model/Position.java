package it.polimi.ingsw.model;

/**
 * this class represent the position of a specific card in the game board
 *
 * @param x this attribute represent the row of the position
 * @param y this attribute represent the column of the position
 */
public record Position(int x, int y) {
    /**
     * this method is the class constructor
     *
     * @param x : set attribute x to x value
     * @param y : set attribute y to y value
     */
    public Position {
    }

    /**
     * this method return the value of the attribute x;
     *
     * @return an int.
     */
    @Override
    public int x() {
        return x;
    }

    /**
     * this method return the value of the attribute y;
     *
     * @return an int.
     */

    @Override
    public int y() {
        return y;
    }
}
