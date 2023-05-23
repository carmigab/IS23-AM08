package it.polimi.ingsw.network.server;

import java.io.Serializable;
import java.util.List;

public record Lobby(int playersNum, int playerInGame, List<String> players) implements Serializable {
    @Override
    public String toString() {
        return "players " + playerInGame + "/" + playersNum + "\n" + "   players waiting: " + players;
    }
}
