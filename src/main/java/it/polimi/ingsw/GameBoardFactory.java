package it.polimi.ingsw;

/**
 *
 */
public class GameBoardFactory {
    /**
     *
     * @param numPlayers
     * @return
     */
    public static GameBoard createGameBoard(int numPlayers){
        if(numPlayers==2) return new GameBoard2Player();
        if(numPlayers==3) return new GameBoard3Player();
        return new GameBoard4Player();
    }
}
