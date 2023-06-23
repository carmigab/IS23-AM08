package it.polimi.ingsw.network.server;

import java.util.List;

/**
 * This class represents a Standard lobby
 */
public class LobbyStandard extends Lobby{
    /**
     * The constructor
     * @param lobbyName the name of the lobby
     * @param playersNum the number of players
     * @param playerInGame the number of player in game
     * @param players the players
     * @param recovered true if the lobby was recovered
     */
    public LobbyStandard(String lobbyName, int playersNum, int playerInGame, List<String> players, boolean recovered) {
        super(lobbyName, playersNum, playerInGame, players, recovered);
        // We show a standard lobby only if it was not recovered
        this.toShow = !recovered;
    }

    /**
     * Override of toString
     * @return string
     */
    @Override
    public String toString() {
        if (!recovered)
            return lobbyName + ": players " + playerInGame + "/" + playersNum + "\n" + "   players waiting: " + players;
        else
            return lobbyName + ": Someone else recovered game " + "\n" + "   players waiting: " + players;

    }

}
