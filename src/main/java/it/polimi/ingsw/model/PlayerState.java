package it.polimi.ingsw.model;

import com.google.gson.annotations.Expose;

public class PlayerState {
    /**
     * This attribute is the nickname of the player
     */
    @Expose
    private String nickname;

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
    private int CGPoints;

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
     * This attribute is the Personal shelf of the player
     */
    @Expose
    private PersonalGoal personalGoal;

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
     */
    public PlayerState(String name, PersonalGoal personalGoal){
        this.nickname = name;
        this.personalGoal = personalGoal;

        // Initialization
        this.myShelf = new Shelf();
        this.PGPoints = 0;
        this.CGPoints = 0;
        this.groupPoints = 0;
        this.firstPoint = 0;

        this.comGoalDone = new boolean[2];      // Initialized to false
    }

    /**
     * This is a second constructor
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
        this.comGoalDone = playerState.comGoalDone;
    }

    /**
     * This method returns the nickname
     * @return nickname
     */
    public String getNickname(){
        return this.nickname;
    }


    /**
     * This method returns the Shelf
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
        return PGPoints + CGPoints + groupPoints + firstPoint;
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
    public void addCGPoints(int p){
        this.CGPoints += p;
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











}

























