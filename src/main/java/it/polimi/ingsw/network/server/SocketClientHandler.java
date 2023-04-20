package it.polimi.ingsw.network.server;

import it.polimi.ingsw.gameInfo.GameInfo;
import it.polimi.ingsw.gameInfo.State;
import it.polimi.ingsw.network.client.exceptions.TimeOutException;

import java.rmi.RemoteException;

public class SocketClientHandler {
    public void update(State newState, GameInfo newInfo) throws TimeOutException {}

    public void isAlive() throws TimeOutException {}

    public String name() {return "Bill1";}

    public void receiveMessage(String message) throws TimeOutException{}
}
