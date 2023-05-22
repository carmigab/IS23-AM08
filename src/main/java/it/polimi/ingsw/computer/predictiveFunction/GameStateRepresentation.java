package it.polimi.ingsw.computer.predictiveFunction;

import com.google.gson.annotations.Expose;
import it.polimi.ingsw.model.SingleGoal;
import it.polimi.ingsw.model.Tile;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * This class represents a state of the game, that is the board and the shelf.
 */
public class GameStateRepresentation {

    @Expose
    public Tile[][] board;

    @Expose
    public Tile[][] shelf;


    /**
     * This constructor creates a state of the game
     *
     * @param board        the board of the game
     * @param shelf        the shelf of the game
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
        return copyTileMatrix(board);
    }

    /**
     * This method returns the shelf of the game
     *
     * @return the shelf of the game
     */
    public Tile[][] getShelf() {
        return copyTileMatrix(shelf);
    }

    @NotNull
    private Tile[][] copyTileMatrix(Tile[][] matrix) {
        Tile[][] matrixCopy = new Tile[matrix.length][matrix[0].length];

        for(int i = 0; i < matrix.length; i++){
            for(int j = 0; j < matrix[0].length; j++) {
                matrixCopy[i][j] = new Tile(matrix[i][j]);
            }
        }

        return matrixCopy;
    }
}
