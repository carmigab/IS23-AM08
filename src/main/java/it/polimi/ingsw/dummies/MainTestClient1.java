package it.polimi.ingsw.dummies;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.RmiClient;
import it.polimi.ingsw.server.constants.ServerConstants;

import java.rmi.NotBoundException;
import java.io.*;

public class MainTestClient1 {
    public static void main(String[] args) throws Exception {
        try(
                BufferedReader input = new BufferedReader(new InputStreamReader(System.in))
        )
        {
            int port = ServerConstants.VERY_NICE;
            String nickname = "Bill1";
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
                client.messageAll(input.readLine());
            }




        } catch (NotBoundException e) {
            throw new RuntimeException(e);
        }


    }
}
