package it.polimi.ingsw.model;

import com.google.gson.annotations.Expose;
import it.polimi.ingsw.constants.ModelConstants;
import it.polimi.ingsw.gameInfo.PlayerInfo;

/**
 * this class represents a generic player of the match
 */

public class PlayerState {
    /**
     * This attribute is the nickname of the player
     */
    @Expose
    private final String nickname;

    /**
     * This attribute is the Shelf of the player
     */
    @Expose
    private Shelf myShelf;

    /**
     * This attribute is the PGPoints counter of the player
     */
    @Expose
    private int PGPoints;

    /**
     * This attribute is the CGPoints counter of the player
     */
    @Expose
    private int[] CGPoints;

    /**
     * This attribute is the groupPoints counter of the player
     */
    @Expose
    private int groupPoints;

    /**
     * This attribute is the firstPoints counter of the player
     */
    @Expose
    private int firstPoint;

    /**
     * This attribute is the Personal goal of the player
     */
    @Expose
    private PersonalGoal personalGoal;

    /**
     * This attribute is the Personal goal number of the player
     */
    @Expose
    private Integer personalGoalNumber;

    /**
     * This attribute represents the state of the common
     *  goal for the player
     */
    @Expose
    private boolean[] comGoalDone;



    /**
     * This is the constructor
     * @param name: the nickname of the player
     * @param personalGoal: the personal shelf of the player
     * @param personalGoalNumber: the personal goal number of the player
     */
    public PlayerState(String name, PersonalGoal personalGoal, Integer personalGoalNumber){
        this.nickname = name;
        this.personalGoal = personalGoal;
        this.personalGoalNumber = personalGoalNumber;
        this.CGPoints = new int[ModelConstants.TOTAL_CG_PER_GAME];

        // Initialization
        this.myShelf = new Shelf();
        this.PGPoints = 0;
        this.CGPoints[0] = 0;
        this.CGPoints[1] = 0;
        this.groupPoints = 0;
        this.firstPoint = 0;

        this.comGoalDone = new boolean[ModelConstants.TOTAL_CG_PER_GAME];      // Initialized to false
    }

    /**
     * This is a second constructor : it creates a copy of an existing playerState
     * @param playerState: the player
     */
    public PlayerState(PlayerState playerState) {
        this.nickname = playerState.nickname;
        this.myShelf = playerState.myShelf;
        this.PGPoints = playerState.PGPoints;
        this.CGPoints = playerState.CGPoints;
        this.groupPoints = playerState.groupPoints;
        this.firstPoint = playerState.firstPoint;
        this.personalGoal = playerState.personalGoal;
        this.personalGoalNumber = playerState.personalGoalNumber;
        this.comGoalDone = playerState.comGoalDone;
    }

    /**
     * This method returns the nickname of the player
     * @return nickname
     */
    public String getNickname(){
        return this.nickname;
    }


    /**
     * This method returns the Shelf of the player
     * @return myShelf
     */
    public Shelf getShelf(){
        return this.myShelf;
    }


    /**
     * This method returns the total points of the player
     * @return total points
     */
    public int getPoints(){
        return PGPoints + CGPoints[0] + CGPoints[1] + groupPoints + firstPoint;
    }


    /**
     * This method returns if a commonGoal is done or not
     * @param index: position in array comGoalDone
     * @return true if common goal at index is already been done by the player, false elsewhere
     */
    public boolean isCGDone(int index){
        return comGoalDone[index];
    }


    /**
     * This method adds points to CGPoints counter
     * @param p : the points to add
     */
    public void addCGPoints(int p, int index){
        this.CGPoints[index] += p;
    }

    /**
     * This method sets the PGPoints counter
     * @param p : the points to set the PGPoints counter to
     */
    private void setPGPoints(int p){
        this.PGPoints = p;
    }


    /**
     * This method sets the groupPoints counter
     * @param p : the points to set the groupPoints counter to
     */
    private void setGroupPoints(int p){
        this.groupPoints = p;
    }


    /**
     * This method sets firstPoint to 1
     */
    public void setFirstPoint(){
        this.firstPoint = 1;
    }


    /**
     * This method sets a position in comGoalDone to true
     * @param index: position in array comGoalDone
     */
    public void setCGDone(int index){
        this.comGoalDone[index] = true;
    }


    /**
     * This method evaluates and sets the groupPoints
     */
    public void evaluateGroupPoints(){
        setGroupPoints(myShelf.evaluateGroupPoints());
    }


    /**
     * This method evaluates and sets the PGPoints
     */
    public void evaluatePGPoints(){
        setPGPoints(personalGoal.evaluate(this.myShelf));
    }


    /**
     * This method overrides the method equals. it assumes that two players are equals if they have the same
     * attributes (the same Shelf, the same firstPoint ...); if the object passed by parameter isn't a
     * PlayerState, return false
     * @param obj the object to check
     * @return true if the player are equals
     */
    @Override
    public boolean equals(Object obj){
        if(!(obj instanceof PlayerState playerState)) return false;

        for (int i = 0; i < comGoalDone.length; i++) {
            if (this.comGoalDone[i] != playerState.comGoalDone[i]) return false;
            if (!(this.CGPoints[i] == playerState.CGPoints[i])) return false;
        }

        if (!this.myShelf.equals(playerState.myShelf)) return false;

        if (!(this.firstPoint == playerState.firstPoint)) return false;

        if (!(this.groupPoints == playerState.groupPoints)) return false;

        if (!this.nickname.equals(playerState.nickname)) return false;

        if (!this.personalGoal.equals(playerState.personalGoal)) return false;

        return this.PGPoints == playerState.PGPoints;

//        return this.myShelf.equals(playerState.myShelf) &&
//                this.CGPoints == playerState.CGPoints &&
//                this.firstPoint == playerState.firstPoint &&
//                this.groupPoints == playerState.groupPoints &&
//                this.nickname.equals(playerState.nickname) &&
//                this.personalGoal.equals(playerState.personalGoal) &&
//                this.PGPoints == playerState.PGPoints;
    }

    /**
     * This method returns a full copy of the player's state
     * @return a copied object of the player state
     */
    public PlayerInfo getPlayerInfo(){

        return new PlayerInfo(this.nickname,
                this.PGPoints,
                this.CGPoints,
                this.firstPoint,
                this.groupPoints,
                this.myShelf.getCopy(),
                this.personalGoal.getCopy(),
                this.personalGoalNumber);
    }

}

























