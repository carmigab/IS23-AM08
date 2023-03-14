package it.polimi.ingsw;
import java.util.List;

public class GameModel {
    private int numPlayers;
    private PlayerState playersList;
    private GameBoard gameBoard;
    private List<CommonObjective> commonObjs;
    private List<PersonalObjective> personalObjs;
    private int currentPlayer;
    private boolean isLastTurn;




    private void makeMove(int x, int y, int column){};
    public boolean checkValidMove(int pos[][], int num){}; // ?????????????? awful, disgusting


    public void evaluatePoints(){};
    private void evaluatePOPoints(){};
    private void evaluateGroupPoints(){};
    private void evaluateCOPoints(){};
    private void evaluateFirstPoints(){};


    private boolean boardToBeFilled(){};
    public boolean nextTurn(){};


    public String getWinner(){};














}















































