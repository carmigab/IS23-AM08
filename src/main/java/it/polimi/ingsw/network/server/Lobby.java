package it.polimi.ingsw.network.server;

import java.io.Serializable;
import java.util.List;

public record Lobby(int playersNum, int playerInGame, List<String> players) implements Serializable {
    public String toString(int index) {
        return "Lobby " + index + ": " + "players " + playerInGame + "/" + playersNum + "\n" + "   players waiting: " + players;
    }
}
