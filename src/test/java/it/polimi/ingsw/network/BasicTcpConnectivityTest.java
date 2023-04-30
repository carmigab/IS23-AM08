package it.polimi.ingsw.network;


import it.polimi.ingsw.dummies.FakeView;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.client.RmiClient;
import it.polimi.ingsw.network.client.TcpClient;
import it.polimi.ingsw.network.server.LobbyServer;
import it.polimi.ingsw.network.server.constants.ServerConstants;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class BasicTcpConnectivityTest {

    @Disabled
    @Test
    public void basicTcpClientServerConnectionTest(){
        boolean testSuccessful = true;
        try {
            LobbyServer lobbyS = new LobbyServer();
            lobbyS.start();

            String nickname = "TheOneWhoTestsTcp";
            FakeView fakeView = new FakeView();

            //System.out.println("Starting client");
            Client client = new TcpClient(nickname, fakeView, "localhost", ServerConstants.TCP_PORT);

            client.chooseNickname(nickname);

            client.createGame(2);
        } catch (Exception e) {
            testSuccessful = false;
            e.printStackTrace();
        }

        assertTrue(testSuccessful);
    }
}
