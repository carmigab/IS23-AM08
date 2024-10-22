package it.polimi.ingsw.model;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

/**
 * this class represents the single tile of the game
 */

public class Tile implements Serializable {
    /**
     * this attribute is the color of the tile;
     */
    @Expose
    private TileColor color;
    /**
     * this attribute is used to show the correct image of the tile in the Gui
     */
    @Expose
    private final int sprite;

    /**
     * this method is the class constructor
     *
     * @param color  : set the attribute "color" to color value
     * @param sprite : set the attribute "sprite" to sprite value
     */
    public Tile(TileColor color, int sprite) {
        this.color = color;
        this.sprite = sprite;
    }

    /**
     *  this method is an alternative class constructor that returns a new tile, copy of the tile passed
     *  as parameter
     * @param copy tile we want the copy
     */
    public Tile(Tile copy){
        this.color = copy.color;
        this.sprite = copy.sprite;
    }

    /**
     * this method return the value of the attribute color
     * @return a TileColor, the color of the tile (also empty or invalid)
     */
    public TileColor getColor() {return color;}

    /**
     * * this method return the value of the attribute sprite
     * @return an int, the sprite of the tile
     */
    public int getSprite() {return sprite;}

    /**
     * this method return true if the tile is empty
     * @return true if the tile is empty
     */
    public boolean isEmpty(){
        return this.color == TileColor.EMPTY;
    }

    /**
     * this method return true if the tile is invalid
     * @return true if the tile is invalid
     */
    public boolean isInvalid(){
        return this.color == TileColor.INVALID;
    }

    /**
     * this method set the tile color to empty
     */
    public void setEmpty(){this.color = TileColor.EMPTY;}

    /**
     * this method set the tile color to invalid
     */
    public void setInvalid(){this.color = TileColor.INVALID;}

    /**
     * this method overrides the method equals. it assumes that two tiles are equals if they have the same color;
     * if the parameter isn't a tile, the method return false
     * @param obj object to be compared with "this"
     * @return a boolean : true if the cards are equals
     */
    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Tile c)) return false;
        return this.color == c.color;
    }
}
