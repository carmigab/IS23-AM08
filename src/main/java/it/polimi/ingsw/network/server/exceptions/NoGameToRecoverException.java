package it.polimi.ingsw.network.server.exceptions;

/**
 * Exception that represents that there are no games available for recovery / joining a recovered game with your name
 */
public class NoGameToRecoverException extends Exception{
    /**
     * Constructor
     */
    public NoGameToRecoverException(){ super();}
}
