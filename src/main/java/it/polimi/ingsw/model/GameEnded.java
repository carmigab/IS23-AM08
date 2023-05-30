package it.polimi.ingsw.model;

import java.io.Serializable;

public class GameEnded implements Serializable {
    /*
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

    public String getNickname() {
        return nickname;
    }

    public int getFinalPoints() {
        return finalPoints;
    }
}
