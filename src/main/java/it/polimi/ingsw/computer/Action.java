package it.polimi.ingsw.computer;

import it.polimi.ingsw.model.TileColor;

import java.util.List;

/**
 * This class represents an action of the game, that is the tiles that the player wants to place and the column where he wants to place them.
 */
public class Action {
    /**
     * the tiles that the player wants to place
     */
    private final List<TileColor> tiles;
    /**
     * the column where the player wants to place the tiles
     */
    private final Integer column;

    /**
     * This constructor creates an action of the game
     * @param tiles the tiles that the player wants to place
     * @param column the column where the player wants to place the tiles
     */
    public Action(List<TileColor> tiles, Integer column){
        this.tiles=tiles;
        this.column=column;
    }

    /**
     * This method returns the tiles that the player wants to place
     * @return the tiles that the player wants to place
     */
    public List<TileColor> getTiles(){
        return tiles;
    }

    /**
     * This method returns the column where the player wants to place the tiles
     * @return the column where the player wants to place the tiles
     */
    public Integer getColumn(){
        return column;
    }

    /**
     * Equals method
     * @param obj the object to compare
     * @return true if the objects are equals, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Action action)) return false;

        if(this.tiles.size()!=action.tiles.size()) return false;

        for (int i = 0; i < this.tiles.size(); i++) {
            if (!this.tiles.get(i).equals(action.tiles.get(i))) return false;
        }

        return this.column.equals(action.column);
    }
}
