package it.polimi.ingsw.model;
import it.polimi.ingsw.model.constants.AppConstants;
import it.polimi.ingsw.model.constants.BoardConstants;

import java.util.ArrayList;
import java.util.List;
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
     * this attribute is the list of all possible common objectives of the game;
     */
    private final List<CommonObjective>  commonObjs;
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

    public GameModel(int numPlayers){
        this.numPlayers = numPlayers;
        playerList = new ArrayList<>(this.numPlayers);
        gameBoard = GameBoard.createGameBoard(this.numPlayers);
        commonObjs = new ArrayList<>(AppConstants.TOTAL_OBJECTIVES);
        personalObjs = new ArrayList<>(AppConstants.TOTAL_OBJECTIVES);
        this.currentPlayer = 0;
        this.isLastTurn = false;
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
    }




    //GetWinner: prende tutti i punteggi dei giocatori e ritorna il nome di quello che ha più punti;
    //NextTurn : prima facciamo il controller
}
