package it.polimi.ingsw;
import it.polimi.ingsw.AppConstants.*;

public class PlayerState {
    private String nickname;
    private Library myLibrary;
    private int POPoints;
    private int COPoints;
    private int groupPoints;
    private int firstPoint;

    private PersonalObjective personalObj;
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
     * This method returns the array that tells if a commonObj is done or not
     * @return array that tells if a commonObj is done or not
     */
    public boolean[] getCODone(){
        return comObjDone;
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
     * @param pos: position in array comObjDone
     */
    public void setCODone(int pos){
        this.comObjDone[pos] = true;
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

























