package it.polimi.ingsw.model;
import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import it.polimi.ingsw.constants.ModelConstants;
import it.polimi.ingsw.gameInfo.PlayerInfo;
import it.polimi.ingsw.model.commonGoals.*;
import it.polimi.ingsw.constants.BoardConstants;
import it.polimi.ingsw.model.exceptions.NoMoreTilesAtStartFillBoardException;
import it.polimi.ingsw.model.exceptions.NoMoreTilesToFillBoardException;
import it.polimi.ingsw.utilities.JsonWithExposeSingleton;
import it.polimi.ingsw.utilities.RandomSingleton;
import it.polimi.ingsw.utilities.UtilityFunctions;
import it.polimi.ingsw.utilities.UtilityFunctionsModel;
import it.polimi.ingsw.controller.observers.Observer;

import java.io.*;
import java.util.*;

/**
 * this class is the main class of the model : it contains the methods called by the Controller and in this
 * class there are all the methods that implement the logic of the game (check of a move, the logic of the
 * turns, the final leaderboard ...)
 */
public class GameModel {
    /**
     * this attribute is a list of observers
     */
    private List<Observer> observers = new ArrayList<>();

    /**
     * this attribute is the number of the player of a specific match
     */
    @Expose
    private final int numPlayers;
    /**
     * this attribute is a list of the players of the match
     */
    @Expose
    private final List<PlayerState> playerList;

    /**
     * this attribute represents the game board of the match; it will update after every turn
     */
    @Expose
    private final GameBoard gameBoard;
    /**
     * this attribute is the list of common goals created in the specific match;
     */
    @Expose
    private final List<Integer> commonGoalsCreated;

    /**
     * this attribute is used to indicate the player who must do the turn (the current player)
     */
    @Expose
    private int currentPlayer;
    /**
     * this attribute is used to indicate that the current turn is the last turn of the match;
     */
    @Expose
    private boolean isLastTurn;


    /**
     * this attribute represents the final leaderboard of the game; it will be shown at the end of the game
     */
    @Expose
    private List<GameEndedScore> leaderBoard;
    /**
     * This attribute stores the information of the file name in the class, so that it does not have to
     * be constructed each time
     */
    @Expose
    private String fileName;

    /**
     *this attribute is used to indicate that the game is finished
     */
    @Expose
    private boolean gameOver;

    /**
     * this method is the class constructor : it creates a new GameMode object
     * @param numPlayers number of players for the game
     * @param nicknames players' nicknames
     */
    public GameModel(int numPlayers, List<String> nicknames){
        this.numPlayers = numPlayers;
        this.playerList = new ArrayList<>(this.numPlayers);
        this.commonGoalsCreated = new ArrayList<>(ModelConstants.TOTAL_CG_PER_GAME);
        this.gameBoard = GameBoard.createGameBoard(numPlayers, getRandomCommonGoals());
        this.currentPlayer = 0;
        this.isLastTurn = false;
        this.gameOver = false;
        initializePlayers(nicknames);
        initializePersistenceFile(nicknames);

    }

    /**
     * This constructor copies the gameModel when loaded from file.
     * Note that it copies the reference, but it is fine because when the object is loaded from file
     * GSON creates a new instance of the object, so even if we copy by reference it is fine
     * We could also avoid implementing it
     * @param gameModel copy of the gameModel we want
     */
    public GameModel(GameModel gameModel){
        this.numPlayers = gameModel.numPlayers;
        this.playerList = gameModel.playerList;
        this.commonGoalsCreated = gameModel.commonGoalsCreated;
        this.gameBoard = new GameBoard(gameModel.gameBoard, this.commonGoalsCreated);
        this.currentPlayer = gameModel.currentPlayer;
        this.isLastTurn = gameModel.isLastTurn;
        this.leaderBoard = gameModel.leaderBoard;
        this.gameOver = gameModel.gameOver;
        this.fileName = gameModel.fileName;


        // oss: the observers are added from outside
    }

    /**
     * This method reads all the single goals from the file singleGoals.json and gives a random one to every player
     * @param nicknames list of nicknames of all the players
     */
    private void initializePlayers(List<String> nicknames){

        Gson jsonLoader= JsonWithExposeSingleton.getJsonWithExposeSingleton();
        PersonalGoalsConfiguration poc = jsonLoader.fromJson(UtilityFunctions.getReaderFromFileNameRelativePath(ModelConstants.FILE_CONFIG_PERSONALGOAL, this.getClass()), PersonalGoalsConfiguration.class);

        Set<Integer> extractedPersonalGoals = new HashSet<>();
        Random r = RandomSingleton.getRandomSingleton();
        int random = r.nextInt(ModelConstants.TOTAL_GOALS);
        for(String s: nicknames){
            while (extractedPersonalGoals.contains(random)) {
                random = r.nextInt(ModelConstants.TOTAL_GOALS);
            }
            playerList.add(new PlayerState(s, poc.getPersonalGoalAtIndex(random), random));
            extractedPersonalGoals.add(random);
        }

    }

    /**
     * This method is called only by the constructor and what it does is simply create the file where the current
     * state of the game is saved. The object also keeps track of the file name once it is created, so that it can
     * be easily accessed (not by constructing it every time)
     * @param nicks list of the names of the players
     */
    private void initializePersistenceFile(List<String> nicks){
        this.fileName= ModelConstants.PATH_SAVED_MATCHES + UtilityFunctionsModel.getJSONFileName(nicks);
        //Commented to save time
        //saveCurrentState();
    }

    /**
     * This method is called at the end of each turn, and it overwrites the file with the new state of the game
     */
    private void saveCurrentState(){
        Writer fileWriter;
        try {
            fileWriter=new FileWriter(this.fileName);
            fileWriter.write(JsonWithExposeSingleton.getJsonWithExposeSingleton().toJson(this));
            fileWriter.close();
        } catch (IOException e) {
            System.out.println("Error in opening to the file "+this.fileName+" plz restart application");
            e.printStackTrace();
        }

    }

    /**
     * This method selects two random numbers between 0 and 12 (total goals, 12 excluded) and assigns a
     * common shelf to it. It is important to note that it checks that which common goals are created
     * @return the list of 2 random common goals created
     */
    private List<Integer> getRandomCommonGoals(){
        Random r = RandomSingleton.getRandomSingleton();
        List<Integer> pool = new ArrayList<>(ModelConstants.TOTAL_GOALS);
        for(int i = 0; i< ModelConstants.TOTAL_GOALS; i++) pool.add(i);
        for(int i=0; i< ModelConstants.TOTAL_CG_PER_GAME;){
            Integer candidate = r.nextInt(pool.size());
            if(!this.commonGoalsCreated.contains(candidate)) {
                this.commonGoalsCreated.add(candidate);
                i++;
            }
        }
        return this.commonGoalsCreated;
    }

    /**
     * this method represents the move done by a player: the player removes from 1 to 3 tile from the game board and
     * adds the tiles in his shelf, in the specific column. We don't check if the positions in the list are correct
     * because we assumed the positions in the list are already check(by method checkValidMove).We assume the same
     * for the column;
     * @param pos: list of position chosen by the player (tiles chosen by the player)
     * @param col : column chosen by the player
     */

    public void makeMove(List<Position> pos, int col){
        for(Position p : pos){
            this.playerList.get(currentPlayer).getShelf().add(this.gameBoard.removeTile(p), col);
        }
    }

    /**
     * this method is used to check if the move done by the current player is correct, according to the rules of
     * the game
     * @param pos list of the position of the tiles taken by the player from the gameBoard
     * @return true if the move is valid, false if the move isn't valid
     */
    public boolean checkValidMove(List<Position> pos){
        if(pos.size() > ModelConstants.MAX_NUM_OF_MOVES || pos.isEmpty()) return false;
        for(Position p : pos){
            if(!this.gameBoard.positionOccupied(p)) return false;
            if(p.x()>= BoardConstants.BOARD_DIMENSION || p.y() >= BoardConstants.BOARD_DIMENSION) return false;
            if(!this.gameBoard.hasFreeAdjacent(p)) return false;
        }

        if(pos.stream().distinct().count()!=pos.size()) return false;

        //we have to check that they are in the same line,
        //so we take a copy of the list, we sort it in the x and y direction (if in a line it is obvious that one of them will not be sorted)

        List<Position> copyPos=new ArrayList<>();
        copyPos.addAll(pos);

        copyPos.sort((p1, p2) -> p1.x() < p2.x() ? 1 : -1);
        copyPos.sort((p1, p2) -> p1.y() < p2.y() ? 1 : -1);

        //and we have to check the distance between the current and the next. if it is 1 it is ok, if not then it means they are not adjacent
        for(int i=0;i<pos.size()-1;i++){
            if(UtilityFunctionsModel.distanceSquared(copyPos.get(i), copyPos.get(i+1))!=1) return false;
        }
        //also at the end we have to check that the first and the last are in a line (have a distance of exactly 4)
        //now 4 is the constant which depends on the length of the total array, for now I will leave it hard coded

        if(pos.size()> ModelConstants.MAX_NUM_OF_MOVES-1) if(UtilityFunctionsModel.distanceSquared(copyPos.get(0),copyPos.get(copyPos.size()-1)) != 4) return false;

        return true;
    }

    /**
     * this method is used to verify if the column col has free spaces to put the tiles taken from the game board
     * @param col column where the player wants to put the tiles
     * @param numTiles number of tiles taken by the player from the game board
     * @return true if there is space in the column, false if there isn't space in the column
     */
    public boolean checkValidColumn(int col, int numTiles){
        if(col >= ModelConstants.COLS_NUMBER) return false;
        return this.playerList.get(this.currentPlayer).getShelf().getFreeSpaces(col) >= numTiles;
    }

    /**
     * this method is used to check if the game board has to be filled; it calls the method hasToBeFilled of the class
     * GameBoard
     * @return true if hasToBeFilled return true, false otherwise
     */
    private boolean boardToBeFilled(){
        return this.gameBoard.hasToBeFilled();
    }

    /**
     * This method updates the score of the current player
     * and sets lastTurn to true if his personal shelf is filled
     */
    private void evaluatePoints(){
        PlayerState currP = playerList.get(currentPlayer);
        CommonGoal commonGoal;

        currP.evaluatePGPoints();
        currP.evaluateGroupPoints();

        // Evaluate common goals
        for(int i = 0; i< ModelConstants.TOTAL_CG_PER_GAME; i++) {
            if (!currP.isCGDone(i)) {
                commonGoal = gameBoard.getCommonGoal(i);
                if (commonGoal.evaluate(currP.getShelf())) {
                    currP.addCGPoints(gameBoard.pop(i), i);
                    currP.setCGDone(i);
                }
            }
        }

        // Evaluate First Point and sets last turn
        if (!isLastTurn){
            if (currP.getShelf().isFull()) {
                currP.setFirstPoint();
                isLastTurn = true;
            }
        }
    }

    /**
     * this method creates the final leaderBoard of the match. if two players have the same points, the player
     * who is farther from the first player will be higher in the leaderboard; the leaderBoard will be shown at
     * the end of the game
     */
    private void createLeaderBoard(){
        List<PlayerState> temp = new ArrayList<>(this.playerList);
        leaderBoard = new ArrayList<>();
        temp.sort(Comparator.comparingInt(PlayerState::getPoints));
        for(int i=temp.size()-1; i>=0;i--){
            leaderBoard.add(new GameEndedScore(temp.get(i).getNickname(), temp.get(i).getPoints()));
        }
    }

    /**
     * This method represents the logic of the turns: is used to update the currentPlayer and
     * evaluate the points of the currentPlayer. It also checks at the end of the turn if the board has to be
     * filled
     */
    public void nextTurn(){
        this.evaluatePoints();
        if(!this.isLastTurn){
            this.currentPlayer = (this.currentPlayer + 1) % this.numPlayers;
            if(this.boardToBeFilled()){
                try{
                    this.gameBoard.fillBoard();
                }
                catch (NoMoreTilesAtStartFillBoardException e){
                    forceEndGame(false);
                }
                catch (NoMoreTilesToFillBoardException e) {
                    //do nothing
                }
            }
        }
        else{
            if(this.currentPlayer == this.numPlayers - 1){
                forceEndGame(false);
            }
            else{
                this.currentPlayer++;
                if(this.boardToBeFilled()){
                    try{
                        this.gameBoard.fillBoard();
                    } catch (NoMoreTilesAtStartFillBoardException | NoMoreTilesToFillBoardException e) {
                        forceEndGame(false);
                    }

                }

            }
        }
        // save the state of the game to be reloaded in case of server crash
        //Commented to save time
        //saveCurrentState();

        // Notifies all observers at hte end of the turn
        this.notifyObservers();
    }

    /**
     * this method return the current player
     * @return an int, the current player
     */
    public int getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * this method return the number of players of a specific match
     * @return an int, the number of players
     */
    public String getCurrentPlayerNickName() {
        return playerList.get(currentPlayer).getNickname();
    }

    /**
     * this method adds observers
     * @param o the observer added
     */
    public void addObserver(Observer o){
        observers.add(o);
        this.notifyObservers();
    }


    /**
     * this method update listening observers
     */
    public void notifyObservers(){
        for (Observer obs: observers)
            obs.update(this);
    }


    /**
     * this method removes all listening observers
     */
    public void removeObservers(){
        observers.clear();
    }

    /**
     * this method returns the current player in a specific game
     * @return a PlayerState in the position "currentPlayer" of the List playerList
     */
    public PlayerState getCurrentPlayerState() {
        return this.playerList.get(currentPlayer);
    }

    /**
     * this method returns the gameBoard of a specific game
     * @return a gameBoard, the gameBoard of a specific game
     */
    public GameBoard getGameBoard() {
        return this.gameBoard;
    }

    /**
     * this method returns the final leaderboard of the game
     * @return the final leaderboard of the game
     */
    public List<GameEndedScore> getLeaderBoard() {
        return leaderBoard;
    }

    /**
     * this method returns true if the game is over, false otherwise
     * @return a boolean which is the value of the attribute gameOver
     */
    public boolean isGameOver(){
        return this.gameOver;
    }


    /**
     * This method forces and end to the game
     * @param forced true if forced from outside
     */
    public void forceEndGame(boolean forced){
        this.gameOver = true;
            createLeaderBoard();
        // if we force the end of the game (from server) we must save the current state
        if (forced) this.saveCurrentState();
        this.notifyObservers();
    }


    /**
     * This method overrides the method equals. it assumes that two gameModel are equals if they have the same
     * attributes; if the parameter passed by parameter isn't a gameModel, the method returns false
     * @param obj the object to check
     * @return true if the gameModel are equals
     */
    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof GameModel gameModel)) return false;

        if (this.isLastTurn != gameModel.isLastTurn) return false;

        if (this.currentPlayer != gameModel.currentPlayer) return false;

        if (this.numPlayers != gameModel.numPlayers) return false;

        if (!this.commonGoalsCreated.equals(gameModel.commonGoalsCreated)) return false;

        if (!this.gameBoard.equals(gameModel.gameBoard)) return false;

        if (!this.playerList.equals(gameModel.playerList)) return false;

        return true;

    }

    /**
     * This method calls the method GameBoard.getGameBoardCopy() to return a copy of the current game board
     * @return a copy of the current game board
     */
    public Tile[][] getGameBoardCopy(){
        return this.gameBoard.getGameBoardCopy();
    }

    /**
     * This method returns a full copy of the list of common goals created
     * @return a copy of the common goals created
     */
    public List<Integer> getCommonGoalsCreatedCopy(){
        List<Integer> toReturn = new ArrayList<>(ModelConstants.TOTAL_CG_PER_GAME);
        for(Integer cg: this.commonGoalsCreated){
            toReturn.add(Integer.valueOf(cg));
        }
        return toReturn;
    }

    /**
     * This method returns a full copy of all the stacks of the common goals created
     * @return a list of the copied stacks from the game model
     */
    public List<Integer> getCommonGoalsStackCopy(){
        return this.gameBoard.getPointStacksCopy();
    }

    /**
     * This method creates a list of copies of all the players' states
     * @return a list of copied player states
     */
    public List<PlayerInfo> getPlayerListCopy(){
        List<PlayerInfo> toReturn = new ArrayList<>(this.numPlayers);
        for(PlayerState player: this.playerList){
            toReturn.add(player.getPlayerInfo());
        }
        return toReturn;
    }


}


