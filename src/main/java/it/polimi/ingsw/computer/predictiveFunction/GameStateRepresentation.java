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
    private Tile[][] board;

    @Expose
    private Tile[][] shelf;

    @Expose
    private final List<SingleGoal> personalGoal;


    /**
     * This constructor creates a state of the game
     *
     * @param board         the board of the game
     * @param shelf         the shelf of the game
     * @param personalGoal
     */
    public GameStateRepresentation(Tile[][] board, Tile[][] shelf, List<SingleGoal> personalGoal) {
        this.board = board;
        this.shelf = shelf;
        this.personalGoal = personalGoal;
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

    /**
     * This method returns the personal goals of the player
     *
     * @return the personal goals of the player
     */
    public List<SingleGoal> getPersonalGoal() {
        return personalGoal;
    }

    public void setBoard(Tile[][] board) {
        this.board = board;
    }

    public void setShelf(Tile[][] shelf) {
        this.shelf = shelf;
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
