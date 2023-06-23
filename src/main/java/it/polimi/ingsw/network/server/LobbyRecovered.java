package it.polimi.ingsw.network.server;

import java.util.List;

/**
 * This class represents a Recovered Lobby
 */
public class LobbyRecovered extends Lobby {
    /**
     * The constructor
     * @param lobbyName the name of the lobby
     * @param playersNum the number of players
     * @param playerInGame the number of player in game
     * @param players the players
     */
    public LobbyRecovered(String lobbyName, int playersNum, int playerInGame, List<String> players) {
        super(lobbyName, playersNum, playerInGame, players, true);
        this.toShow = true;
    }

    /**
     * Override of toString
     * @return string
     */
    @Override
    public String toString(){
        return "Recovered lobby";
    }
}
