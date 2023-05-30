package it.polimi.ingsw.network.server;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public record Lobby(String lobbyName, int playersNum, int playerInGame, List<String> players) implements Serializable {
    @Override
    public String toString() {
        return lobbyName + ": players " + playerInGame + "/" + playersNum + "\n" + "   players waiting: " + players;
    }
}
