package it.polimi.ingsw.model;

import java.io.Serializable;

/**
 * this class represents the "data structure" useful to create the final leaderboard of the game (which is a list
 * gameEnded objects)
 */
public class GameEnded implements Serializable {
    /**
     * this attribute represents the nickname of a player
     */
    private final  String nickname;

    /**
     * this attribute represents the final points of a player
     */
    public final int finalPoints;

    /**
     * this is the constructor of the class gameEnded, it creates a new gameEnded object
     * @param nickname the name of the player
     * @param finalPoints the final points of the player
     */
    public GameEnded(String nickname, int finalPoints) {
        this.nickname = nickname;
        this.finalPoints = finalPoints;
    }

    /**
     * this method return the nickname of the player
     * @return a String, the specific nickname of the player
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * this method return the final points of a specific player
     * @return an int, the final points of a specific player
     */
    public int getFinalPoints() {
        return finalPoints;
    }
}
