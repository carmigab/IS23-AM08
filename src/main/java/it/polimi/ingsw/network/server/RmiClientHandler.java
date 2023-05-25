package it.polimi.ingsw.network.server;

import it.polimi.ingsw.gameInfo.GameInfo;
import it.polimi.ingsw.gameInfo.State;
import it.polimi.ingsw.network.client.RmiClientInterface;

import java.rmi.RemoteException;

public class RmiClientHandler extends ClientHandler {
    private RmiClientInterface rmiClient;


    RmiClientHandler(RmiClientInterface rmiClient){
        this.rmiClient = rmiClient;
    }


    public void update(State newState, GameInfo newInfo) throws RemoteException {
        rmiClient.update(newState, newInfo);
    }


    public void isAlive() throws RemoteException {
        rmiClient.isAlive();
    }


    public String name() throws RemoteException {
        return rmiClient.name();
    }


    public void receiveMessage(String message) throws RemoteException {
        rmiClient.receiveMessage(message);
    }

}
