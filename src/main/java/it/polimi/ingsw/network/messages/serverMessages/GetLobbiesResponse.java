package it.polimi.ingsw.network.messages.serverMessages;

import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.server.Lobby;

import java.util.List;

public class GetLobbiesResponse extends Message {

    private final List<Lobby> lobbyList;

    private final boolean noGamesAvailableException;

    /**
     * Constructor
     *
     * @param sender                    : the one who sends the message
     * @param lobbyList
     * @param noGamesAvailableException
     */
    public GetLobbiesResponse(String sender, List<Lobby> lobbyList, boolean noGamesAvailableException) {
        super(sender);
        this.lobbyList = lobbyList;
        this.noGamesAvailableException = noGamesAvailableException;
    }

    public List<Lobby> getLobbyList() {
        return lobbyList;
    }

    public boolean isNoGamesAvailableException() {
        return noGamesAvailableException;
    }
}
