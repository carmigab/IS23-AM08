package it.polimi.ingsw.controller.exceptions;

/**
 * this exception is thrown by the class gameController if the nickname of the currentPlayer is wrong
 */
public class InvalidNicknameException extends Exception{
    public InvalidNicknameException(){
        super();
    }
}
