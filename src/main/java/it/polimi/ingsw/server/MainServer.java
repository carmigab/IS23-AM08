package it.polimi.ingsw.server;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.RemoteException;

public class MainServer {
    public static void main(String[] args) throws RemoteException {

        int port = 1888;

        RmiServer rmiServer = new RmiServer(4);

        Registry registry = LocateRegistry.createRegistry(port);
        registry.rebind("rmiServer", rmiServer);

        System.out.println("rmiServer bound in registry");


    }
}
