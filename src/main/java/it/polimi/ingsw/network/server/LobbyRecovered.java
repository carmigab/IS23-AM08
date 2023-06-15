package it.polimi.ingsw.network.server;

import java.util.List;

public class LobbyRecovered extends Lobby {
    public LobbyRecovered(String lobbyName, int playersNum, int playerInGame, List<String> players) {
        super(lobbyName, playersNum, playerInGame, players, true);
        this.toShow = true;
    }

    @Override
    public String toString(){
        return "Recovered lobby";
    }
}
