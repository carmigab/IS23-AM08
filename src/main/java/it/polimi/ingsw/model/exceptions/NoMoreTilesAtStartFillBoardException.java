package it.polimi.ingsw.model.exceptions;

/**
 *  Exception thrown whenever there are no more tiles to fill the board and someone tries to fill it again
 */
public class NoMoreTilesAtStartFillBoardException extends Exception{
    /**
     * Constructor of the exception
     */
    public NoMoreTilesAtStartFillBoardException(){
        super();
    }
}
