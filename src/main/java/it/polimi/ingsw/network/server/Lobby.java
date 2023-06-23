package it.polimi.ingsw.network.server;

import java.io.Serializable;
import java.util.List;

/**
 * This class represents a Lobby to be sent to the client as part of a list
 */
public class Lobby implements Serializable {
    /**
     * This attribute represents the name of the lobby
     */
    protected final String lobbyName;
    /**
     * This attribute represents the number of players
     */
    protected final int playersNum;
    /**
     * This attribute represents the number of players in game
     */
    protected final int playerInGame;
    /**
     * This attribute is a list of nicknames of the players
     */
    protected final List<String> players;
    /**
     * This flag is true if the game was recovered
     */
    protected final boolean recovered;
    /**
     * This flag is true if the lobby should be showed on the client
     */
    protected boolean toShow;

    /**
     * The constructor
     * @param lobbyName the name of the lobby
     * @param playersNum the number of players
     * @param playerInGame the number of player in game
     * @param players the players
     * @param recovered true if the lobby was recovered
     */
    public Lobby(String lobbyName, int playersNum, int playerInGame, List<String> players, boolean recovered) {
        this.lobbyName = lobbyName;
        this.playersNum = playersNum;
        this.playerInGame = playerInGame;
        this.players = players;
        this.recovered = recovered;


    }

    /**
     * Getter
     * @return the lobby name
     */
    public String getLobbyName() {
        return lobbyName;
    }

    /**
     * Getter
     * @return the number of players
     */
    public int getPlayersNum() {
        return playersNum;
    }

    /**
     * Getter
     * @return the number of players in game
     */
    public int getPlayerInGame() {
        return playerInGame;
    }

    /**
     * Getter
     * @return the list of nicknames of players in game
     */
    public List<String> getPlayers() {
        return players;
    }

    /**
     * This method returns true if the game was recovered
     * @return true if recovered
     */
    public boolean isRecovered() {
        return recovered;
    }

    /**
     * This method returns true if the game should be showed
     * @return true if the game should be showed
     */
    public boolean toShow(){
        return toShow;
    }

}
