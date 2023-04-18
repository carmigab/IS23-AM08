package it.polimi.ingsw.dummies;

import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.client.RmiClient;
import it.polimi.ingsw.network.server.constants.ServerConstants;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.rmi.NotBoundException;

public class MainTestClient2 {
    public static void main(String[] args) throws Exception {
        try(
                BufferedReader input = new BufferedReader(new InputStreamReader(System.in))
        )
        {
            int port = ServerConstants.RMI_PORT;
            String nickname = "Bill2";
            FakeView fakeView = new FakeView();

            System.out.println("Starting client");
            Client client = new RmiClient(nickname, fakeView);

            client.chooseNickname(nickname);
            try {
                client.joinGame();
            } catch (Exception e) {
                client.createGame(2);
            }

            System.out.println("You can now chat in the room");
            while(true){
                client.messageSomeone(input.readLine(), "Bill1");
            }




        } catch (NotBoundException e) {
            throw new RuntimeException(e);
        }

    }
}
