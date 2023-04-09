package it.polimi.ingsw.dummies;

import it.polimi.ingsw.client.RmiClient;
import it.polimi.ingsw.server.RmiServer;
import it.polimi.ingsw.server.RmiServerInterface;

import java.rmi.NotBoundException;
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

//            System.out.println("Looking up the registry");
//            Registry registry = LocateRegistry.getRegistry(port);
//            RmiServerInterface rmiServer = (RmiServerInterface) registry.lookup("rmiServer");

            System.out.println("registering player: " + nickname);
            //rmiServer.registerPlayer(nickname, rmiClient);
            //rmiClient.setMatchServer(rmiServer);
            //rmiClient.chooseNickname(nickname);





        } catch (NotBoundException e) {
            throw new RuntimeException(e);
        }


    }
}
