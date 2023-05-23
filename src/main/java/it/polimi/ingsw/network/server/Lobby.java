package it.polimi.ingsw.network.server;

import java.util.List;

public record Lobby(int playersNum, int playerInGame, List<String> players) {
    public String toString(int index) {
        return "Lobby " + index + ": " + "players " + playerInGame + "/" + playersNum + "\n" + "   players waiting: " + players;
    }
}
