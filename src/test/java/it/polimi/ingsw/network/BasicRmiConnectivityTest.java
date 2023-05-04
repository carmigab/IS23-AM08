package it.polimi.ingsw.network;

import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.client.RmiClient;
import it.polimi.ingsw.network.server.LobbyServer;
import it.polimi.ingsw.constants.ServerConstants;
import it.polimi.ingsw.network.server.LobbyServerConfig;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BasicRmiConnectivityTest {

    @Test
    public void basicRmiClientServerConnectionTest(){
        boolean testSuccessful = true;
        try {
            LobbyServer lobbyS = new LobbyServer(new LobbyServerConfig(ServerConstants.RMI_PORT, ServerConstants.TCP_PORT, ServerConstants.LOBBY_SERVER,"gamedummy"));
            lobbyS.start();

            String nickname = "TheOneWhoTestsRmi";
            FakeView fakeView = new FakeView();

            //System.out.println("Starting client");
            Client client = new RmiClient(nickname, fakeView, "localhost", ServerConstants.RMI_PORT);

            client.chooseNickname(nickname);

            client.createGame(2);
        } catch (Exception e) {
            testSuccessful = false;
            // e.printStackTrace();
        }

        assertTrue(testSuccessful);
    }



}
