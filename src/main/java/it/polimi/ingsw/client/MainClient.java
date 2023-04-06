package it.polimi.ingsw.Client;

import it.polimi.ingsw.Server.RmiServer;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class MainClient {
    public static void main(String[] args) throws Exception {
        try {
            int port = 1888;
            String nickname = "Bill1";
            FakeView fakeView = new FakeView();

            System.out.println("Starting client");
            RmiClient rmiClient = new RmiClient("nickname", fakeView);

            System.out.println("Bounding registry");
            Registry registry = LocateRegistry.getRegistry(port);
            RmiServer rmiServer = (RmiServer) registry.lookup("rmiServer");

            System.out.println("registering player: " + nickname);
            rmiServer.registerPlayer(nickname, rmiClient);






        } catch (NotBoundException e) {
            throw new RuntimeException(e);
        }


    }
}
