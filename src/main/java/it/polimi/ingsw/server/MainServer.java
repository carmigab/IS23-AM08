package it.polimi.ingsw.Server;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class MainServer {
    public static void main(String[] args) throws RemoteException {

        int port = 1888;

        RmiServer rmiServer = new RmiServer(4);

        Registry registry = LocateRegistry.createRegistry(port);
        registry.rebind("rmiServer", rmiServer);

        System.out.println("rmiServer bound in registry");


    }
}
