package it.polimi.ingsw.dummies;

import it.polimi.ingsw.client.RmiClient;
import it.polimi.ingsw.server.ConnectionInformationRMI;
import it.polimi.ingsw.server.RmiServerInterface;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class MainClient2 {
    public static void main(String[] args) throws Exception {
        try(
                BufferedReader input = new BufferedReader(new InputStreamReader(System.in))
        )
        {
            int port = 1888;
            String nickname = "Bill2";
            FakeView fakeView = new FakeView();

            System.out.println("Starting client");
            RmiClient rmiClient = new RmiClient(nickname, fakeView);

            System.out.println("Looking up the registry");
            Registry registry = LocateRegistry.getRegistry(port);
            RmiServerInterface rmiServer = (RmiServerInterface) registry.lookup("rmiServer");

            rmiClient.connectToMatchServer(new ConnectionInformationRMI("rmiServer", port));

            System.out.println("registering player: " + nickname);
            rmiServer.registerPlayer(nickname, rmiClient);



            System.out.println("You can now chat in the room");
            while(true){
                rmiClient.messageSomeone(input.readLine(), "Bill1");
            }




        } catch (NotBoundException e) {
            throw new RuntimeException(e);
        }


    }
}
