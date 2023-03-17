package it.polimi.ingsw.model;

import it.polimi.ingsw.model.constants.AppConstants;
import it.polimi.ingsw.model.utilities.UtilityFunctions;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * This class represent the library of one of the player
 */
public class Library{
    /**
     * This attribute store all the card in the player's library
     */
    private final Card myLibrary[][];


    /**
     * This method is the class constructor, it doesn't receive parameters and simply fill the library with empty
     * cards (see Card class for details on what empty card means)
     */
    public Library() {
        myLibrary = new Card[AppConstants.ROWS_NUMBER][AppConstants.COLS_NUMBER];

        for (int i = 0; i < AppConstants.ROWS_NUMBER; i++) {
            for (int j = 0; j < AppConstants.COLS_NUMBER; j++) {
                myLibrary[i][j] = new Card(CardColor.EMPTY, 0);
            }
        }
    }


    /**
     * This method is the class constructor used to create a copy of an existing library
     */
    public Library(Library myLibrary) {
        this.myLibrary = myLibrary.getCopy();
    }


    // Delete this method
    public Library(Card[][] myLibrary) {
        this.myLibrary = myLibrary;
    }

    /**
     * This method return a copy of the array of card of the library
     *
     * @return a copy of myLibrary
     */
    public Card[][] getCopy() {
        Card myLibraryCopy[][] = new Card[AppConstants.ROWS_NUMBER][AppConstants.COLS_NUMBER];

        for (int i = 0; i < AppConstants.ROWS_NUMBER; i++) {
            for (int j = 0; j < AppConstants.COLS_NUMBER; j++) {
                myLibraryCopy[i][j] = new Card(this.myLibrary[i][j]);
            }
        }

        return myLibraryCopy;
    }


    /**
     * This method receive a card object and an int representing the column in which the card
     * must be added
     * This method doesn't need to check if there's enough space in the given column to insert
     * the given card because this control is delegated to the GameModel class, it will check if
     * the given player move is valid before calling this method
     *
     * @param card = card object to be inserted
     * @param column = column in which you must insert the card
     */
    public void add(Card card, Integer column) {
        // rowToInsertInIdx set to the bottom row
        int rowToInsertInIdx = AppConstants.ROWS_NUMBER - 1;

        // decrement the row idx till it reach an empty cell
        while(!myLibrary[rowToInsertInIdx][column].isEmpty()) rowToInsertInIdx--;

        // add the given card in the empty cell
        // myLibrary[rowToInsertInIdx][column] = card;
        myLibrary[rowToInsertInIdx][column] = new Card(card);
    }


    /**
     * This method check if there is at least one empty cell in the library, if so return false else true
     * @return boolean
     */
    public boolean isFull() {
        for (int i = 0; i < AppConstants.ROWS_NUMBER; i++) {
            for (int j = 0; j < AppConstants.COLS_NUMBER; j++) {
                if (myLibrary[i][j].isEmpty()) return false;
            }
        }

        return true;
    }


    /**
     * This method return the card in the given position in the library
     *
     * @param position : Position
     * @return : Card
     */
    public Card getCard(Position position) {
        return myLibrary[position.y()][position.x()];
    }


    /**
     * This method is used by the GameModel class to check if in the column given by the player
     * there's enough space to insert the card selected by the player
     *
     * @param col indicate the column to check
     * @return the number of free spaces in the column
     */
    public Integer getFreeSpaces(Integer col) {
        int result = 0;
        int rowIdx = 0;

        // starting from the top row, go down until finds a not empty cell
        while(rowIdx < AppConstants.ROWS_NUMBER && myLibrary[rowIdx][col].isEmpty()) {
            rowIdx++;
            result++;
        }

        return result;
    }




    /**
     * This method finds the group of cells in the player library and return the sum of the points
     * given by each group
     * It requires that the library is always full (with empty or normal cards)
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

        Library tempLib = new Library(this);
        ArrayDeque<Position> posToExplore = new ArrayDeque<>();
        ArrayList<Integer> components = new ArrayList<>();


        // Adds all the positions that don't have an empty card to the list
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


