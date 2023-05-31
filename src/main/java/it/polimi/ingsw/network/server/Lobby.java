package it.polimi.ingsw.network.server;

import java.io.Serializable;
import java.util.List;

public class Lobby implements Serializable {

    protected final String lobbyName;

    protected final int playersNum;

    protected final int playerInGame;

    protected final List<String> players;

    protected final boolean recovered;

    protected boolean toShow;

    public Lobby(String lobbyName, int playersNum, int playerInGame, List<String> players, boolean recovered) {
        this.lobbyName = lobbyName;
        this.playersNum = playersNum;
        this.playerInGame = playerInGame;
        this.players = players;
        this.recovered = recovered;


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

    public boolean isRecovered() {
        return recovered;
    }

    public boolean toShow(){
        return toShow;
    }

}
