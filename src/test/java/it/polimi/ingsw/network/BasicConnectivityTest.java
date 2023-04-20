package it.polimi.ingsw.network;

import it.polimi.ingsw.dummies.FakeView;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.client.RmiClient;
import it.polimi.ingsw.network.server.LobbyServer;
import it.polimi.ingsw.network.server.constants.ServerConstants;
import it.polimi.ingsw.network.server.exceptions.AlreadyInGameException;
import it.polimi.ingsw.network.server.exceptions.NoGamesAvailableException;
import it.polimi.ingsw.network.server.exceptions.NonExistentNicknameException;
import org.junit.jupiter.api.Test;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import static org.junit.jupiter.api.Assertions.*;

public class BasicConnectivityTest {

    @Test
    public void basicRmiClientServerConnectionTest(){
        boolean testSuccessful = true;
        try {
            LobbyServer lobbyS = new LobbyServer();
            lobbyS.start();

            String nickname = "TheOneWhoTests";
            FakeView fakeView = new FakeView();

            //System.out.println("Starting client");
            Client client = new RmiClient(nickname, fakeView, "localhost", ServerConstants.RMI_PORT);

            client.chooseNickname(nickname);

            client.createGame(2);
        } catch (Exception e) {
            testSuccessful = false;
            e.printStackTrace();
        }

        assertTrue(testSuccessful);
    }
}
