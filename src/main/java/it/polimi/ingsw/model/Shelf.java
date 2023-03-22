package it.polimi.ingsw.model;

import com.google.gson.annotations.Expose;
import it.polimi.ingsw.model.constants.AppConstants;
import it.polimi.ingsw.model.utilities.UtilityFunctions;

import java.util.ArrayDeque;
import java.util.ArrayList;

/**
 * This class represent the shelf of one of the player
 */
public class Shelf {
    /**
     * This attribute store all the tile in the player's shelf
     */
    @Expose
    private final Tile myShelf[][];


    /**
     * This method is the class constructor, it doesn't receive parameters and simply fill the shelf with empty
     * tiles (see Tile class for details on what empty tile means)
     */
    public Shelf() {
        myShelf = new Tile[AppConstants.ROWS_NUMBER][AppConstants.COLS_NUMBER];

        for (int i = 0; i < AppConstants.ROWS_NUMBER; i++) {
            for (int j = 0; j < AppConstants.COLS_NUMBER; j++) {
                myShelf[i][j] = new Tile(TileColor.EMPTY, 0);
            }
        }
    }


    /**
     * This method is the class constructor used to create a copy of an existing shelf
     */
    public Shelf(Shelf myShelf) {
        this.myShelf = myShelf.getCopy();
    }


    // Delete this method
    public Shelf(Tile[][] myShelf) {
        this.myShelf = myShelf;
    }

    /**
     * This method return a copy of the array of tile of the shelf
     *
     * @return a copy of myshelf
     */
    public Tile[][] getCopy() {
        Tile myShelfCopy[][] = new Tile[AppConstants.ROWS_NUMBER][AppConstants.COLS_NUMBER];

        for (int i = 0; i < AppConstants.ROWS_NUMBER; i++) {
            for (int j = 0; j < AppConstants.COLS_NUMBER; j++) {
                myShelfCopy[i][j] = new Tile(this.myShelf[i][j]);
            }
        }

        return myShelfCopy;
    }


    /**
     * This method receive a tile object and an int representing the column in which the tile
     * must be added
     * This method doesn't need to check if there's enough space in the given column to insert
     * the given tile because this control is delegated to the GameModel class, it will check if
     * the given player move is valid before calling this method
     *
     * @param tile = tile object to be inserted
     * @param column = column in which you must insert the tile
     */
    public void add(Tile tile, Integer column) {
        // rowToInsertInIdx set to the bottom row
        int rowToInsertInIdx = AppConstants.ROWS_NUMBER - 1;

        // decrement the row idx till it reach an empty cell
        while(!myShelf[rowToInsertInIdx][column].isEmpty()) rowToInsertInIdx--;

        // add the given tile in the empty cell
        // myShelf[rowToInsertInIdx][column] = tile;
        myShelf[rowToInsertInIdx][column] = new Tile(tile);
    }


    /**
     * This method check if there is at least one empty cell in the shelf, if so return false else true
     * @return boolean
     */
    public boolean isFull() {
        for (int i = 0; i < AppConstants.ROWS_NUMBER; i++) {
            for (int j = 0; j < AppConstants.COLS_NUMBER; j++) {
                if (myShelf[i][j].isEmpty()) return false;
            }
        }

        return true;
    }


    /**
     * This method return the tile in the given position in the shelf
     *
     * @param position : Position
     * @return : Tile
     */
    public Tile getTile(Position position) {
        return myShelf[position.y()][position.x()];
    }


    /**
     * This method is used by the GameModel class to check if in the column given by the player
     * there's enough space to insert the tile selected by the player
     *
     * @param col indicate the column to check
     * @return the number of free spaces in the column
     */
    public Integer getFreeSpaces(Integer col) {
        int result = 0;
        int rowIdx = 0;

        // starting from the top row, go down until finds a not empty cell
        while(rowIdx < AppConstants.ROWS_NUMBER && myShelf[rowIdx][col].isEmpty()) {
            rowIdx++;
            result++;
        }

        return result;
    }




    /**
     * This method finds the group of cells in the player shelf and return the sum of the points
     * given by each group
     * It requires that the shelf is always full (with empty or normal tiles)
     * @return total points for groups
     */
    public Integer evaluateGroupPoints() {

        int points = 0;
        ArrayList<Integer> comp = evaluateGroupComponents();

        for(int x: comp) {
            if (x<3) points +=0;
            else if (x==3) points += 2;
            else if (x==4) points += 3;
            else if (x==5) points += 5;
            else points += 8;

        }

        return points;
    }


    /**
     * This method calculates the number of components of all the groups present
     * @return an arrayList that contains the number of components for each group
     */
    private ArrayList<Integer> evaluateGroupComponents() {

        Shelf tempLib = new Shelf(this);
        ArrayDeque<Position> posToExplore = new ArrayDeque<>();
        ArrayList<Integer> components = new ArrayList<>();

        // Adds all the positions that don't have an empty tile to the list
        for (int i = 0; i < AppConstants.ROWS_NUMBER; i++) {
            for (int j = 0; j < AppConstants.COLS_NUMBER; j++) {
                posToExplore.addLast(new Position(j, i));
            }
        }

        // Main logic of the method
        // Create an array
        while(!posToExplore.isEmpty()){
            Position p = posToExplore.pop();
            components.add(UtilityFunctions.findGroupSize(tempLib, p));

        }

        return components;
    }
}