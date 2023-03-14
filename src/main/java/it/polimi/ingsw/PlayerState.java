package it.polimi.ingsw;
import it.polimi.ingsw.AppConstants.*;

public class PlayerState {
    /**
     * This attribute is the nickname of the player
     */
    private String nickname;

    /**
     * This attribute is the Library of the player
     */
    private Library myLibrary;

    /**
     * This attribute is the POPoints counter of the player
     */
    private int POPoints;

    /**
     * This attribute is the COPoints counter of the player
     */
    private int COPoints;

    /**
     * This attribute is the groupPoints counter of the player
     */
    private int groupPoints;

    /**
     * This attribute is the firstPoints counter of the player
     */
    private int firstPoint;

    /**
     * This attribute is the Personal Objective of the player
     */
    private PersonalObjective personalObj;

    /**
     * This attribute rappresents the state of the common
     *  Objectives for the player
     */
    private boolean comObjDone[];



    /**
     * This is the constructor
     * @param name: the nickname of the player
     * @param Obj: the personal objective of the player
     */
    public PlayerState(String name, PersonalObjective Obj){
        this.nickname = name;
        this.personalObj = Obj;

        // Initialization
        this.myLibrary = new Library();
        this.POPoints = 0;
        this.COPoints = 0;
        this.groupPoints = 0;
        this.firstPoint = 0;

        this.comObjDone = new boolean[2];      // Initialized to false
    }


    /**
     * This method returns the nickname
     * @return nickname
     */
    public String getNickname(){
        return this.nickname;
    }


    /**
     * This method returns the Library
     * @return myLibrary
     */
    public Library getLibrary(){
        return this.myLibrary;
    }


    /**
     * This method returns the total points of the player
     * @return total points
     */
    public int getPoints(){
        return POPoints + COPoints + groupPoints + firstPoint;
    }


    /**
     * This method returns if a commonObj is done or not
     * @param index: position in array comObjDone
     * @return array that tells if a commonObj is done or not
     */
    public boolean isCODone(int index){
        return comObjDone[index];
    }


    /**
     * This method adds points to COPoints counter
     * @param p : the points to add
     */
    public void addCOPoints(int p){
        this.COPoints += p;
    }

    /**
     * This method sets the POPoints counter
     * @param p : the points to set the PoPoints counter to
     */
    private void setPOPoints(int p){
        this.POPoints = p;
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
     * This method sets a position in comObjDone to true
     * @param index: position in array comObjDone
     */
    public void setCODone(int index){
        this.comObjDone[index] = true;
    }


    /**
     * This method evaluates and sets the groupPoints
     */
    public void evaluateGroupPoints(){
        setGroupPoints(myLibrary.evaluateGroupPoints());
    }


    /**
     * This method evaluates and sets the POPoints
     */
    public void evaluatePOPoints(){
        setPOPoints(personalObj.evaluate(this.myLibrary));
    }











}

























