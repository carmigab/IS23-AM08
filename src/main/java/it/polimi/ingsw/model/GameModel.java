package it.polimi.ingsw.model;
import com.google.gson.Gson;
import it.polimi.ingsw.model.commonGoals.*;
import it.polimi.ingsw.model.constants.AppConstants;
import it.polimi.ingsw.model.constants.BoardConstants;
import it.polimi.ingsw.model.utilities.JsonSingleton;
import it.polimi.ingsw.model.utilities.RandomSingleton;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.*;

public class GameModel {
    /**
     * this attribute is the number of the player of a specific match
     */
    private final int numPlayers;
    /**
     * this attribute is a list of the states of the players; one state for each player;
     */
    private final List<PlayerState> playerList;

    /**
     * this attribute represents the game board of the match; it will update after every turn
     */
    private final GameBoard gameBoard;
    /**
     * this attribute is the list of common shelfs created in the specified game;
     */
    private final List<Integer> commonObjsCreated;
    /**
     * this attribute is the list of all possible personal shelfs of the game;
     */
    private final List<PersonalGoal> personalObjs;

    /**
     * this attribute is used to indicate the player who must do the turn
     */
    private int currentPlayer;
    /**
     * this attribute is used to indicate that the current turn is the last turn of the match;
     */
    private Boolean isLastTurn;
    /**
     * this attribute is used to indicate the end of the game;
     */
    private boolean endGame;

    /**
     * this attribute represents the final leaderboard of the game
     */
    private List<PlayerState> leaderBoard;

    /**
     * TODO: initialize gameboard
     * @param numPlayers
     * @param nicknames
     */

    public GameModel(int numPlayers, List<String> nicknames){
        this.numPlayers = numPlayers;
        playerList = new ArrayList<>(this.numPlayers);
        commonObjsCreated = new ArrayList<>(AppConstants.TOTAL_CG_PER_GAME);
        gameBoard = GameBoard.createGameBoard(numPlayers, getRandomCommonGoals());
        personalObjs = new ArrayList<>(AppConstants.TOTAL_GOALS);
        this.currentPlayer = 0;
        this.isLastTurn = false;
        this.endGame = false;
        initializePlayers(nicknames);
    }

    /**
     * This method reads all the single shelfs from the file singleshelfs.json and gives a random one to every player
     * @param nicknames list of nicknames of all the players
     */
    private void initializePlayers(List<String> nicknames){

        Gson jsonLoader= JsonSingleton.getJsonSingleton();
        Reader fileReader= null;
        try {
            fileReader = new FileReader(AppConstants.FILE_CONFIG_PERSONALGOAL);
        }
        catch(FileNotFoundException e){
            System.out.println(e);
        }

        PersonalGoalsConfiguration poc=jsonLoader.fromJson(fileReader, PersonalGoalsConfiguration.class);
        for(int i = 0; i< AppConstants.TOTAL_GOALS; i++){
            this.personalObjs.add(poc.getPersonalGoalAtIndex(i));
            this.personalObjs.get(this.personalObjs.size()-1).setPointsForCompletion(poc.getPointsForCompletion());
        }

        Random r= RandomSingleton.getRandomSingleton();
        for(String s: nicknames){
            playerList.add(new PlayerState(s, this.personalObjs.remove(r.nextInt(this.personalObjs.size()))));
        }
    }

    /**
     * This method selects two random numbers between 0 and 12 (total shelfs, 12 excluded) and assigns a common shelf to it.
     * It is important to note that it checks that which common shelfs are created
     * @return the list of 2 random common shelfs created
     */
    private List<CommonGoal> getRandomCommonGoals(){
        Random r= RandomSingleton.getRandomSingleton();
        List<Integer> pool=new ArrayList<>(AppConstants.TOTAL_GOALS);
        List<CommonGoal> toReturn=new ArrayList<>(AppConstants.TOTAL_CG_PER_GAME);
        for(int i = 0; i<AppConstants.TOTAL_GOALS; i++) pool.add(i);
        for(int i=0;i<AppConstants.TOTAL_CG_PER_GAME;){
            Integer candidate=r.nextInt(pool.size());
            if(!this.commonObjsCreated.contains(candidate)) {
                this.commonObjsCreated.add(candidate);
                i++;
            }
        }

        Gson jsonLoader= JsonSingleton.getJsonSingleton();
        Reader fileReader= null;
        try {
            fileReader = new FileReader(AppConstants.FILE_CONFIG_NGROUPOFSIZEM);
        }
        catch(FileNotFoundException e){
            System.out.println(e);
        }
        NGroupsOfSizeMConfiguration nGroupsOfSizeMConfiguration = jsonLoader.fromJson(fileReader, NGroupsOfSizeMConfiguration.class);
        try {
            fileReader = new FileReader(AppConstants.FILE_CONFIG_NLINESOFATMOSTMDIFFERENTCOLORS);
        }
        catch(FileNotFoundException e){
            System.out.println(e);
        }
        NLinesOfAtMostMDifferentColorsConfiguration nLinesOfAtMostMDifferentColorsConfiguration = jsonLoader.fromJson(fileReader, NLinesOfAtMostMDifferentColorsConfiguration.class);
        try {
            fileReader = new FileReader(AppConstants.FILE_CONFIG_SINGLEOCCURRENCEOFGIVENSHAPE);
        }
        catch(FileNotFoundException e){
            System.out.println(e);
        }
        SingleOccurrenceOfGivenShapeConfiguration singleOccurrenceOfGivenShapeConfiguration = jsonLoader.fromJson(fileReader, SingleOccurrenceOfGivenShapeConfiguration.class);


        for(int i = 0; i<AppConstants.TOTAL_CG_PER_GAME; i++){
            CommonGoal co;
            Integer selected=this.commonObjsCreated.get(i);
            co = switch (selected) {
                case 0 -> nGroupsOfSizeMConfiguration.getGoalAt(0);
                case 1 -> nGroupsOfSizeMConfiguration.getGoalAt(1);
                case 2 -> singleOccurrenceOfGivenShapeConfiguration.getGoalAt(0);
                case 3 -> new TwoSquares();
                case 4 -> nLinesOfAtMostMDifferentColorsConfiguration.getGoalAt(0);
                case 5 -> new EightTilesOfTheSameColor();
                case 6 -> singleOccurrenceOfGivenShapeConfiguration.getGoalAt(1);
                case 7 -> nLinesOfAtMostMDifferentColorsConfiguration.getGoalAt(1);
                case 8 -> nLinesOfAtMostMDifferentColorsConfiguration.getGoalAt(2);
                case 9 -> nLinesOfAtMostMDifferentColorsConfiguration.getGoalAt(3);
                case 10 -> singleOccurrenceOfGivenShapeConfiguration.getGoalAt(2);
                default -> new Ladder();
            };
            toReturn.add(co);
        }
        return toReturn;
    }

    /**
     * this method represents the move done by a player: the player removes from 1 to 3 tile from the game board and
     * adds the tiles in his shelf, in the specific column. We don't check if the positions in the list are correct
     * because we assumed the positions in the list are already check(by method checkValidMove). We assume the same for
     * the column;
     * @param pos: list of position chosen by the player
     * @param col : column chosen by the player
     */

    public void makeMove(List<Position> pos, int col){
        for(Position p : pos){
            this.playerList.get(currentPlayer).getShelf().add(this.gameBoard.removeTile(p), col);
        }
    }

    /**
     * this method is used to verify if the move done by the current player is correct
     * @param pos list of the position of the tiles taken by the player from the game board
     * @return true if the move is valid, false if the move isn't valid
     */
    public boolean checkValidMove(List<Position> pos){
        for(int i=0; i<pos.size()-1; i++){
            if(pos.get(i).x() != pos.get(i+1).x()) return false;
        }
        for(int i=0; i<pos.size()-1; i++){
            if(pos.get(i).y() != pos.get(i+1).y()) return false;
        }
        for (Position p : pos){
            if(p.x()>= BoardConstants.BOARD_DIMENSION || p.y() >= BoardConstants.BOARD_DIMENSION) return false;
            if(!this.gameBoard.hasFreeAdjacent(p)) return false;
        }
        return true;
    }

    /**
     * this method is used to verify if the column col has free spaces to put the tiles taken from the game board
     * @param col column where the player wants to put the tiles
     * @param numTiles number of tiles taken by the player from the game board
     * @return true if there is space in the column, false if there isn't space in the column
     */
    public boolean checkValidColumn(int col, int numTiles){
        return this.playerList.get(this.currentPlayer).getShelf().getFreeSpaces(col) >= numTiles;
    }

    /**
     * this method is used to check if the game board has to be filled; it calls the method hasToBeFilled of the class
     * GameBoard
     * @return true if hasToBeFilled return true
     */
    private boolean boardToBeFilled(){
        return this.gameBoard.hasToBeFilled();
    }

    /**
     * This method updates the score of the current player
     * and sets lastTurn to true if he filled the shelf
     */
    public void evaluatePoints(){
        PlayerState currP = playerList.get(currentPlayer);
        CommonGoal obj;

        currP.evaluatePGPoints();
        currP.evaluateGroupPoints();

        // Evaluate common shelfs
        for(int i=0; i<2; i++) {
            if (!currP.isCGDone(i)) {
                obj = gameBoard.getCommonGoal(i);
                if (obj.evaluate(currP.getShelf())) {
                    currP.addCGPoints(obj.pop());
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
     * this method creates the final leaderBoard of the match. if two players have the same points, the player who is
     * farther from the first player will be higher in the leaderboard
     */
    private void createLeaderBoard(){
        List<PlayerState> temp = new ArrayList<>(playerList);
        leaderBoard = new ArrayList<>(numPlayers);
        Collections.sort(temp, (p1, p2) -> {                        // sort() preserves the order
            return Integer.compare(p1.getPoints(), p2.getPoints());
        });
        for(int i=temp.size()-1; i>=0;i--){
            leaderBoard.add(temp.get(i));
        }
    }

    /**
     * ths method return the final leaderboard of the match
     * @return the final leaderboard of the match;
     */
    public List<PlayerState> getLeaderBoard() {
        return leaderBoard;
    }

    /**
     * this method is used to update the currentPlayer and evaluate the points of the currentPlayer; the currentPlayer
     * is finished his turn;
     */
    public void nextTurn(){
        this.evaluatePoints();
        if(!this.isLastTurn){
            this.currentPlayer = (this.currentPlayer + 1) % this.numPlayers;
            if(this.boardToBeFilled()){
                //this.gameBoard.fillBoard();    bisogna gestire le eccezioni
            }
        }
        else{
            if(this.currentPlayer == this.numPlayers - 1){
                //endgame
            }
            else{
                this.currentPlayer++;
                if(this.boardToBeFilled()){
                    //this.gameBoard.fillBoard();    bisogna gestire le eccezioni
                }

            }
        }
    }

}