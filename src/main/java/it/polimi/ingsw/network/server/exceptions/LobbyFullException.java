package it.polimi.ingsw.network.server.exceptions;

public class LobbyFullException extends Exception{
    public LobbyFullException(){
        super("Lobby is full");
    }
}
