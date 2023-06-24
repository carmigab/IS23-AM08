package it.polimi.ingsw.model;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

/**
 * this class represent the position of a specific tile in the game board
 */
public class Position implements Serializable {

    /**
     * this attribute represents the row of the position
     */
    @Expose
    private int x;

    /**
     * this attribute represents the column of the position
     */
    @Expose
    private int y;

    /**
     * This is the constructor of the class; it receives two int : the row and the column of the position
     * @param x this attribute represent the row of the position
     * @param y this attribute represent the column of the position
     */
    public Position (int x, int y){
        this.x=x;
        this.y=y;
    }

    /**
     * This is the class constructor used to create a copy of an existing position
     * @param p position has to be copied
     */
    public Position (Position p){
        this.x=p.x;
        this.y=p.y;
    }

    /**
     * this method return the value of the attribute x;
     *
     * @return an int: the value of the attribute x (row of the position).
     */
    public int x() {
        return x;
    }

    /**
     * this method return the value of the attribute y;
     * @return an int: the value of the attribute y (column of the position).
     */
    public int y() {
        return y;
    }

    /**
     * this method overrides the method equals. it assumes that two positions are equals if they have the
     * same x and y values;
     * if the parameter isn't a position, the method return false
     * @param obj object to be compared with "this"
     * @return a boolean : true if the positions are equals
     */
    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Position position)) return false;
        return this.x == position.x && this.y == position.y;
    }
}
