package it.polimi.ingsw.model.observers;

import it.polimi.ingsw.model.GameModel;

/**
 * This is an observer of the game model
 */
public class GameStateObserver implements Observer{
//    /**
//     * This attribute is the current State of the game
//     */
//    private State currentState;
//    /**
//     * This attribute is the current player
//     */
//    private int currentPlayer;
//    /**
//     * This attribute is the game info for the client
//     */
//    private GameInfo gameInfo;
//
//
//    /**
//     * This method updates the observer
//     * @param model: the gameModel
//     */
//    @Override
//    public void update(GameModel model) {
//        currentPlayer = model.getCurrentPlayer();
//
//        if(model.isGameOver()) currentState = State.EndGame;
//        else if (currentPlayer == 0) currentState = State.Turn0;
//        else if (currentPlayer == 1) currentState = State.Turn1;
//        else if (currentPlayer == 2) currentState = State.Turn2;
//        else if (currentPlayer == 3) currentState = State.Turn3;
//
//        this.updateGameInfo(model);
//    }
//
//
//    /**
//     * This method create a new gameInfo
//     * @param model: the gameModel
//     */
//    private void updateGameInfo(GameModel model){
//        this.gameInfo = new GameInfo(model);
//    }
//
//
//    /**
//     * This method returns the current state of the game
//     * @return currentState
//     */
//    public State getCurrentState() {
//        return currentState;
//    }
//
//    /**
//     * This method return the game info for the client
//     * @return gameInfo
//     */
//    public GameInfo getGameinfo() {
//        return gameInfo;
//    }
//
//    /**
//     * This method returns the current player
//     * @return currentPlayer
//     */
//    public int getCurrentPlayer() {
//        return currentPlayer;
//    }
//



    // to delete
    public void update(GameModel model){};


}
