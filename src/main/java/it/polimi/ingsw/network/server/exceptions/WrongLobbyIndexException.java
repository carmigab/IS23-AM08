package it.polimi.ingsw.network.server.exceptions;

/**
 * Exception that arises if the client tries to join a lobby that doesn't exist
 */
public class WrongLobbyIndexException extends Exception{
    public WrongLobbyIndexException() {
        super();
    }
}
