

public class GameModel {
    private int numPlayers;
    private PlayerState playersList;
    private GameBoard gameBoard;
    private CommonObjective commonObjs[];
    private PersonalObjective personalObjs[];
    private int currentPlayer;
    private boolean isLastTurn;




    private void makeMove(int x, int y, int column){};
    public boolean checkValidMove(int pos[][], int num){};


    public void evaluatePoints(){};
    private void evaluatePOPoints(){};
    private void evaluateGroupPoints(){};
    private void evaluateCOPoints(){};
    private void evaluateFirstPoints(){};


    private boolean boardToBeFilled(){};
    public boolean nextTurn(){};


    public String getWinner(){};














}















































