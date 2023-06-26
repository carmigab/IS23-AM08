package it.polimi.ingsw.gameInfo;
/**
 * This enum is used to store the state of the game
 */
public enum State {
    WAITINGFORPLAYERS,
    TURN0,
    TURN1,
    TURN2,
    TURN3,
    ENDGAME,
    GRACEFULDISCONNECTION,
    GAMEABORTED
}
