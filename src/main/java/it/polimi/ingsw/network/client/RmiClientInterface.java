package it.polimi.ingsw.network.client;

import it.polimi.ingsw.gameInfo.GameInfo;
import it.polimi.ingsw.gameInfo.State;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RmiClientInterface extends Remote {
    public void update(State newState, GameInfo newInfo) throws RemoteException;

    public boolean isAlive() throws RemoteException;

    public String name() throws RemoteException;

    public void receiveMessage(String message) throws RemoteException;

   }
