package it.polimi.ingsw.network.server;

import it.polimi.ingsw.gameInfo.GameInfo;
import it.polimi.ingsw.gameInfo.State;
import it.polimi.ingsw.network.client.exceptions.TimeOutException;

import java.rmi.RemoteException;

public abstract class ClientHandler {

    public void update(State newState, GameInfo newInfo) throws RemoteException, TimeOutException {}

    public void isAlive() throws RemoteException, TimeOutException {}

    public String name() throws RemoteException {
        return null;
    }


    public void receiveMessage(String message) throws RemoteException, TimeOutException {}

    public void setMatchServer(MatchServer matchServer){}
}
