package it.polimi.ingsw.gameInfo;

import com.google.gson.annotations.Expose;
import it.polimi.ingsw.constants.ModelConstants;
import it.polimi.ingsw.model.SingleGoal;
import it.polimi.ingsw.model.Tile;

import java.io.Serializable;
import java.util.List;

/**
 * This immutable class is used for transferring the data to be updated from the server to the client
 */
public class PlayerInfo implements Serializable {
    /**
     * This attribute stores the nickname of the player
     */
    @Expose
    private final String nickname;

    /**
     * This attribute stores the total points of the player
     */
    @Expose
    private final int pgPoints;

    /**
     * This attribute stores the points of the player for each common goal
     */
    @Expose
    private final List<Integer> comGoalPoints;

    /**
     * This attribute stores the points of the player for the first player
     */
    @Expose
    private final int firstPoint;

    /**
     * This attribute stores the points of the player for the groups
     */
    @Expose
    private final int groupPoints;

    /**
     * This attribute stores the shelf of the player
     */
    @Expose
    private final Tile[][] shelf;

    /**
     * This attribute stores the personal goals of the player
     */
    @Expose
    private final List<SingleGoal> personalGoal;

    /**
     * This attribute stores the personal goals number of the player
     */
    @Expose
    private final Integer personalGoalNumber;

    /**
     * Constructor
     *
     * @param nickname      of the player
     * @param pgPoints      of the player
     * @param comGoalPoints of the player
     * @param firstPoint    of the player
     * @param groupPoints   of the player
     * @param shelf         of the player
     * @param personalGoal  of the player
     * @param personalGoalNumber of the player
     */
    public PlayerInfo(String nickname, int pgPoints, List<Integer> comGoalPoints, int firstPoint, int groupPoints, Tile[][] shelf, List<SingleGoal> personalGoal, int personalGoalNumber) {
        this.nickname = nickname;
        this.pgPoints = pgPoints;
        this.comGoalPoints = comGoalPoints;
        this.firstPoint = firstPoint;
        this.groupPoints = groupPoints;
        this.shelf = shelf;
        this.personalGoal = personalGoal;
        this.personalGoalNumber = personalGoalNumber;
    }

    /**
     * Getter
     *
     * @return the nickname of the player
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * Getter
     *
     * @return the total points of the player
     */
    public int getPgPoints() {
        return pgPoints;
    }

    /**
     * Getter
     *
     * @return the points of the player for each common goal
     */
    public List<Integer> getComGoalPoints() {
        return comGoalPoints;
    }

    /**
     * Getter
     *
     * @return the shelf of the player
     */
    public Tile[][] getShelf() {
        Tile[][] shelfCopy = new Tile[ModelConstants.ROWS_NUMBER][ModelConstants.COLS_NUMBER];

        for (int i = 0; i < ModelConstants.ROWS_NUMBER; i++) {
            for (int j = 0; j < ModelConstants.COLS_NUMBER; j++) {
                shelfCopy[i][j] = new Tile(shelf[i][j]);
            }
        }

        return shelfCopy;
    }
    ;
    /**
     * Getter
     *
     * @return the personal goals of the player
     */
    public List<SingleGoal> getPersonalGoal() {
        return personalGoal;
    }

    /**
     * Getter
     *
     * @return the personal goals of the player
     */
    public Integer getPersonalGoalNumber() {return personalGoalNumber; }

    /**
     * Getter
     * @return the points of the player for the first player
     */
    public int getFirstPoint() {
        return firstPoint;
    }

    /**
     * Getter
     * @return the points of the player for the groups
     */
    public int getGroupPoints() {
        return groupPoints;
    }

    /**
     * This method returns the total points of the player
     * @return the total points of the player
     */
    public int getScore(){
        int scores = pgPoints + firstPoint + groupPoints;
        for (int i = 0; i < comGoalPoints.size(); i++) {
            scores += comGoalPoints.get(i);
        }
        return scores;
    }
}
