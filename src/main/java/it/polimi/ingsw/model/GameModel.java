package it.polimi.ingsw.model;
import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import it.polimi.ingsw.model.commonObjectives.*;
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
     * this attribute is the list of common objectives created in the specified game;
     */
    private final List<Integer> commonObjsCreated;
    /**
     * this attribute is the list of all possible personal objectives of the game;
     */
    private final List<PersonalObjective> personalObjs;

    /**
     * this attribute is used to indicate the player who must do the turn
     */
    private int currentPlayer;
    /**
     * this attribute is used to indicate that the current turn is the last turn of the match;
     */
    private Boolean isLastTurn;

    /**
     * TODO: initialize gameboard
     * @param numPlayers
     * @param nicknames
     */

    public GameModel(int numPlayers, List<String> nicknames){
        this.numPlayers = numPlayers;
        playerList = new ArrayList<>(this.numPlayers);
        commonObjsCreated = new ArrayList<>(AppConstants.TOTAL_CO_PER_GAME);
        gameBoard = GameBoard.createGameBoard(numPlayers, getRandomCommonObjectives());
        personalObjs = new ArrayList<>(AppConstants.TOTAL_OBJECTIVES);
        this.currentPlayer = 0;
        this.isLastTurn = false;
        initializePlayers(nicknames);
    }

    /**
     * This method reads all the single objectives from the file singleObjectives.json and gives a random one to every player
     * @param nicknames list of nicknames of all the players
     */
    private void initializePlayers(List<String> nicknames){

        Gson jsonLoader= JsonSingleton.getJsonSingleton();
        Reader fileReader= null;
        try {
            fileReader = new FileReader(AppConstants.FILE_CONFIG_PERSONALOBJECTIVES);
        }
        catch(FileNotFoundException e){
            System.out.println(e);
        }

        PersonalObjectivesConfiguration poc=jsonLoader.fromJson(fileReader, PersonalObjectivesConfiguration.class);
        for(int i=0;i< AppConstants.TOTAL_OBJECTIVES;i++){
            this.personalObjs.add(poc.getPersonalObjectiveAtIndex(i));
            this.personalObjs.get(this.personalObjs.size()-1).setPointsForCompletion(poc.getPointsForCompletion());
        }

        Random r= RandomSingleton.getRandomSingleton();
        for(String s: nicknames){
            playerList.add(new PlayerState(s, this.personalObjs.remove(r.nextInt(this.personalObjs.size()))));
        }
    }

    /**
     * This method selects two random numbers between 0 and 12 (total objectives, 12 excluded) and assigns a common objective to it.
     * It is important to note that it checks that which common objectives are created
     * @return the list of 2 random common objectives created
     */
    private List<CommonObjective> getRandomCommonObjectives(){
        Random r= RandomSingleton.getRandomSingleton();
        List<Integer> pool=new ArrayList<>(AppConstants.TOTAL_OBJECTIVES);
        List<CommonObjective> toReturn=new ArrayList<>(AppConstants.TOTAL_CO_PER_GAME);
        for(int i=0;i<AppConstants.TOTAL_OBJECTIVES;i++) pool.add(i);
        for(int i=0;i<AppConstants.TOTAL_CO_PER_GAME;){
            Integer candidate=r.nextInt(pool.size());
            if(!this.commonObjsCreated.contains(candidate)) {
                this.commonObjsCreated.add(candidate);
                i++;
            }
        }
        for(int i=0;i<AppConstants.TOTAL_CO_PER_GAME;i++){
            CommonObjective co;
            Integer selected=this.commonObjsCreated.get(i);
            co = switch (selected) {
                case 0 -> new CommonObjective1();
                case 1 -> new CommonObjective2();
                case 2 -> new CommonObjective3();
                case 3 -> new CommonObjective4();
                case 4 -> new CommonObjective5();
                case 5 -> new CommonObjective6();
                case 6 -> new CommonObjective7();
                case 7 -> new CommonObjective8();
                case 8 -> new CommonObjective9();
                case 9 -> new CommonObjective10();
                case 10 -> new CommonObjective11();
                default -> new CommonObjective12();
            };
            toReturn.add(co);
        }
        return toReturn;
    }

    /**
     * this method represents the move done by a player: the player removes from 1 to 3 card from the game board and
     * adds the cards in his library, in the specific column. We don't check if the positions in the list are correct
     * because we assumed the positions in the list are already check(by method checkValidMove). We assume the same for
     * the column;
     * @param pos: list of position chosen by the player
     * @param col : column chosen by the player
     */

    public void makeMove(List<Position> pos, int col){
        for(Position p : pos){
            this.playerList.get(currentPlayer).getLibrary().add(this.gameBoard.removeCard(p), col);
        }
    }

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

    //checkValidColumn postponed because maybe we will use an exception
    //all method evaluate postponed because i wait the push of algorithms calculating common and personal objective

    /**
     * this method is used to check if the game board has to be filled; it calls the method hasToBeFilled of the class
     * GameBoard
     * @return true if hasToBeFilled return true
     */
    private boolean boardToBeFilled(){
        return this.gameBoard.hasToBeFilled();
    }

    public String getWinner(){
        String tempString = new String();
        int tempInt = 0;
        for(PlayerState player : playerList){
            if(player.getPoints()> tempInt){
                tempInt = player.getPoints();
                tempString = player.getNickname();
            }
            //non corretto : manca il caso in cui ho lo stesso punteggio : in quel caso vince il
            //più "lontano"
        }
        return tempString;

    }


    /**
     * This method updates the score of the current player
     * and sets lastTurn to true if he filled the library
     */
    public void evaluatePoints(){
        PlayerState currP = playerList.get(currentPlayer);
        CommonObjective obj;

        currP.evaluatePOPoints();
        currP.evaluateGroupPoints();

        // Evaluate common objectives
        for(int i=0; i<2; i++) {
            if (!currP.isCODone(i)) {
                obj = gameBoard.getCommonObjective(i);
                if (obj.evaluate(currP.getLibrary())) {
                    currP.addCOPoints(obj.pop());
                    currP.setCODone(i);
                }
            }
        }

        // Evaluate First Point and sets last turn
        if (!isLastTurn){
            if (currP.getLibrary().isFull()) {
                currP.setFirstPoint();
                isLastTurn = true;
            }
        }
    }




    //GetWinner: prende tutti i punteggi dei giocatori e ritorna il nome di quello che ha più punti;
    //NextTurn : prima facciamo il controller
}
