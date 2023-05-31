package it.polimi.ingsw.network.server;

import java.util.List;

public class LobbyStandard extends Lobby{
    public LobbyStandard(String lobbyName, int playersNum, int playerInGame, List<String> players, boolean recovered) {
        super(lobbyName, playersNum, playerInGame, players, recovered);
        // We show a standard lobby only if it was not recovered
        this.toShow = !recovered;
    }

    @Override
    public String toString() {
        if (!recovered)
            return lobbyName + ": players " + playerInGame + "/" + playersNum + "\n" + "   players waiting: " + players;
        else
            return lobbyName + ": Someone else recovered game " + "\n" + "   players waiting: " + players;

    }

}
