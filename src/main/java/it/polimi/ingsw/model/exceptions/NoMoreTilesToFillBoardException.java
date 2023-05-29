package it.polimi.ingsw.model.exceptions;

/**
 *Exception thrown whenever there are no more tiles to fill the board while filling the board
 */
public class NoMoreTilesToFillBoardException extends Exception{
    /**
     * Constructor of the exception
     */
    public NoMoreTilesToFillBoardException(){
        super();
    }
}
