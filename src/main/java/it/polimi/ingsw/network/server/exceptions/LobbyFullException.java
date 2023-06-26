package it.polimi.ingsw.network.server.exceptions;

/**
 * This exception is thrown when the lobby is full
 */
public class LobbyFullException extends Exception{
    /**
     * Constructor
     */
    public LobbyFullException(){
        super("Lobby is full");
    }
}
