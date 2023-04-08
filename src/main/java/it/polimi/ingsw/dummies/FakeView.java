package it.polimi.ingsw.dummies;

import it.polimi.ingsw.gameInfo.GameInfo;
import it.polimi.ingsw.gameInfo.State;

public class FakeView {
    GameInfo currentInfo;
    State currentState;

    public void update(State newState, GameInfo newInfo){
        System.out.println("Tutto funge!!");
    }
}
