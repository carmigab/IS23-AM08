package it.polimi.ingsw.computer.predictiveFunction;

import com.google.gson.annotations.Expose;
import it.polimi.ingsw.model.Tile;

/**
 * This class represents a state of the game, that is the board and the shelf.
 */
public class GameStateRepresentation {

    @Expose
    public final Tile[][] board;

    @Expose
    public final Tile[][] shelf;

    /**
     * This constructor creates a state of the game
     *
     * @param board the board of the game
     * @param shelf the shelf of the game
     */
    public GameStateRepresentation(Tile[][] board, Tile[][] shelf) {
        this.board = board;
        this.shelf = shelf;
    }

    /**
     * This method returns the board of the game
     *
     * @return the board of the game
     */
    public Tile[][] getBoard() {
        return board;
    }

    /**
     * This method returns the shelf of the game
     *
     * @return the shelf of the game
     */
    public Tile[][] getShelf() {
        return shelf;
    }
}
