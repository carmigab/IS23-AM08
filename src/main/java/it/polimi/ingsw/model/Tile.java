package it.polimi.ingsw.model;

/**
 * this class represent the single tile of the game
 */

public class Tile {
    /**
     * this attribute is the color of the tile;
     */
    private TileColor color;
    /**
     * this attribute is used to show the correct image of the tile
     */
    private final int sprite;

    /**
     * this method is the class constructor
     *
     * @param color  : set the attribute "color" to color value
     * @param sprite : set the attribute "sprite" to color value
     */
    public Tile(TileColor color, int sprite) {
        this.color = color;
        this.sprite = sprite;
    }

    /**
     * this method is an alternative class constructor that returns a new tile, copy of the tile passed as parameter
     */
    public Tile(Tile copy){
        this.color = copy.color;
        this.sprite = copy.sprite;
    }

    /**
     * this method return the value of the attribute color
     * @return a TileColor, the color of the tile
     */
    public TileColor getColor() {return color;}

    /**
     * * this method return the value of the attribute sprite
     * @return an int, the sprite of the tile
     */
    public int getSprite() {return sprite;}

    /**
     * this method return true if the tile is empty
     * @return a boolean
     */
    public boolean isEmpty(){
        return this.color == TileColor.EMPTY;
    }

    /**
     * this method return true if the tile is empty
     * @return a boolean
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
     * @param obj
     * @return a boolean
     */
    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Tile)) return false;
        Tile c = (Tile) obj;
        return this.color == c.color;
    }
}
