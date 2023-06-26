package it.polimi.ingsw.network.messages.serverMessages;

import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.server.Lobby;

import java.util.List;

/**
 * This class represents the response to a GetLobbies message
 */
public class GetLobbiesResponse extends Message {

    private final List<Lobby> lobbyList;

    private final boolean noGamesAvailableException;

    /**
     * Constructor
     *
     * @param sender                    : the one who sends the message
     * @param lobbyList                : the list of lobbies
     * @param noGamesAvailableException : true if there are no games available
     */
    public GetLobbiesResponse(String sender, List<Lobby> lobbyList, boolean noGamesAvailableException) {
        super(sender);
        this.lobbyList = lobbyList;
        this.noGamesAvailableException = noGamesAvailableException;
        setMessageType("GetLobbiesResponse");
    }

    /**
     * Method to get the list of lobbies
     * @return the list of lobbies
     */
    public List<Lobby> getLobbyList() {
        return lobbyList;
    }

    /**
     * Method to check if there are no games available
     * @return true if there are no games available
     */
    public boolean isNoGamesAvailableException() {
        return noGamesAvailableException;
    }
}
