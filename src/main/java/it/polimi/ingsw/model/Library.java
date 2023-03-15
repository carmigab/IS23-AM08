package it.polimi.ingsw.model;

import it.polimi.ingsw.model.constants.AppConstants;

import java.util.ArrayList;

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
     * This method receive a card object and an int representing the column in which the card
     * must be added
     * This method doesn't need to check if there's enough space in the given column to insert
     * the given card because this control is delegated to the GameModel class, it will check if
     * the given player move is valid before calling this method
     *
     * @param c = card object to be inserted
     * @param col = column in which you must insert the card
     */
    public void add(Card c, int col) {
        // rowToInsertInIdx set to the bottom row
        int rowToInsertInIdx = AppConstants.ROWS_NUMBER - 1;

        // decrement the row idx till it reach an empty cell
        while(!myLibrary[rowToInsertInIdx][col].getColor().equals(CardColor.EMPTY)) rowToInsertInIdx--;

        // add the given card in the empty cell
        myLibrary[rowToInsertInIdx][col] = c;
    }

    /**
     * This method check if there is at least one empty cell in the library, if so return false else true
     * @return boolean
     */
    public boolean isFull() {
        for (int i = 0; i < AppConstants.ROWS_NUMBER; i++) {
            for (int j = 0; j < AppConstants.COLS_NUMBER; j++) {
                if (myLibrary[i][j].getColor().equals(CardColor.EMPTY)) return false;
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
        return myLibrary[position.x()][position.y()];
    }

    /**
     * This method is used by the GameModel class to check if in the column given by the player
     * there's enough space to insert the card selected by the player
     *
     * @param col : int, indicate the column to check
     * @return : int, the number of free spaces in the column
     */
    public int getFreeSpaces(int col) {
        int result = 0;
        int rowIdx = 0;

        // starting from the top row, go down until finds a not empty cell
        while(rowIdx < AppConstants.ROWS_NUMBER && myLibrary[rowIdx][col].getColor().equals(CardColor.EMPTY)) {
            rowIdx++;
            result++;
        }

        return result;
    }



    /**
     * This method finds the group of cells in the player library and return the sum of the points
     * given by each group
     *
     * It requires that the library is always full (with empty or normal cards)
     * @return : int, total points for groups
     */
    public int evaluateGroupPoints() {return 1;}



    /**
     * This method calculate the number of components of all the groups present
     * @return an arrayList that contans the number of components for each group
     */
    public ArrayList<Integer> evaluateGroupComponents() {

        Card tempLib[][] = new Card[AppConstants.ROWS_NUMBER][AppConstants.COLS_NUMBER];
        ArrayList<Position> posToExplore = new ArrayList<>();
        ArrayList<Integer> components = new ArrayList<>();


        // Builds a copy of the current library
        for (int i = 0; i < AppConstants.ROWS_NUMBER; i++) {
            for (int j = 0; j < AppConstants.COLS_NUMBER; j++) {
                tempLib[i][j] = this.myLibrary[i][j];
            }
        }

        // Adds all the positions that don't have an empty card to the list
        for (int i = 0; i < AppConstants.ROWS_NUMBER; i++) {
            for (int j = 0; j < AppConstants.COLS_NUMBER; j++) {
                if(!tempLib[i][j].getColor().equals(CardColor.EMPTY))
                    posToExplore.add(new Position(i, j));
            }
        }

        // Main logic of the method
        // Create an array
        while(!posToExplore.isEmpty()){
            Position p = posToExplore.get(0);
            components.add(searchGroup(p, posToExplore));
        }

        // TODO
        // oss: mi serve un oggetto che si comporta come una queue ma da cui posso pure rimuovere
        //      senza rispettare la queue (fare dopo)

        return components;
    }


    private int searchGroup(Position pos, ArrayList<Position> toExplore ){
        return 1;
    }















}


