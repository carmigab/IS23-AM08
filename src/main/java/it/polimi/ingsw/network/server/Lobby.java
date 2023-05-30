package it.polimi.ingsw.network.server;

import java.io.Serializable;
import java.util.List;

public class Lobby implements Serializable {

    protected final String lobbyName;

    protected final int playersNum;

    protected final int playerInGame;

    protected final List<String> players;

    public Lobby(String lobbyName, int playersNum, int playerInGame, List<String> players) {
        this.lobbyName = lobbyName;
        this.playersNum = playersNum;
        this.playerInGame = playerInGame;
        this.players = players;
    }

    public String getLobbyName() {
        return lobbyName;
    }

    public int getPlayersNum() {
        return playersNum;
    }

    public int getPlayerInGame() {
        return playerInGame;
    }

    public List<String> getPlayers() {
        return players;
    }

    @Override
    public String toString() {
        return lobbyName + ": players " + playerInGame + "/" + playersNum + "\n" + "   players waiting: " + players;
    }
}
