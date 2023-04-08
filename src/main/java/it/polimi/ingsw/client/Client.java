package it.polimi.ingsw.client;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public abstract class Client extends UnicastRemoteObject {
    protected Client() throws RemoteException {
    }
}
