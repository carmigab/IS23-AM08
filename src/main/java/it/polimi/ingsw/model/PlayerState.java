package it.polimi.ingsw.model;

public class PlayerState {
    /**
     * This attribute is the nickname of the player
     */
    private String nickname;

    /**
     * This attribute is the Shelf of the player
     */
    private Shelf myShelf;

    /**
     * This attribute is the PGPoints counter of the player
     */
    private int PGPoints;

    /**
     * This attribute is the CGPoints counter of the player
     */
    private int CGPoints;

    /**
     * This attribute is the groupPoints counter of the player
     */
    private int groupPoints;

    /**
     * This attribute is the firstPoints counter of the player
     */
    private int firstPoint;

    /**
     * This attribute is the Personal shelf of the player
     */
    private PersonalGoal personalGoal;

    /**
     * This attribute rappresents the state of the common
     *  shelfs for the player
     */
    private boolean comGoalDone[];



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
     * @return array that tells if a commonGoal is done or not
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

























