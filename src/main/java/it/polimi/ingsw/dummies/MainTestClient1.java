package it.polimi.ingsw.dummies;

import it.polimi.ingsw.client.RmiClient;

import java.rmi.NotBoundException;
import java.io.*;

public class MainTestClient1 {
    public static void main(String[] args) throws Exception {
        try(
                BufferedReader input = new BufferedReader(new InputStreamReader(System.in))
        )
        {
            int port = 1888;
            String nickname = "Bill1";
            FakeView fakeView = new FakeView();

            System.out.println("Starting client");
            RmiClient rmiClient = new RmiClient(nickname, fakeView);

            rmiClient.chooseNickname(nickname);
            rmiClient.joinGame();

            System.out.println("You can now chat in the room");
            while(true){
                rmiClient.messageAll(input.readLine());
            }




        } catch (NotBoundException e) {
            throw new RuntimeException(e);
        }


    }
}
