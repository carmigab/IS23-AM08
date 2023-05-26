package it.polimi.ingsw.network;

import it.polimi.ingsw.constants.ModelConstants;
import it.polimi.ingsw.constants.ServerConstants;
import it.polimi.ingsw.controller.exceptions.InvalidNicknameException;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.client.TcpClient;
import it.polimi.ingsw.network.server.LobbyServer;
import it.polimi.ingsw.network.server.LobbyServerConfig;
import javafx.geometry.Pos;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class BasicRmiTcpConnectivityTest {

    @Test
    public void basicTcpClientServerConnectionTest(){
        boolean testSuccessful = true;
        try {
            LobbyServer lobbyS = new LobbyServer(new LobbyServerConfig(ServerConstants.RMI_PORT, ServerConstants.TCP_PORT, ServerConstants.LOBBY_SERVER,"gamedummy"));
            lobbyS.start();

            String nickname1 = "Bill1";
            String nickname2 = "Bill2";
            FakeView fakeView = new FakeView();

            // First Client
            Client client1 = new TcpClient(nickname1, fakeView, "localhost", ServerConstants.TCP_PORT);
            client1.chooseNickname(nickname1);
            client1.createGame(2);

            // Second Client
            Client client2 = new TcpClient(nickname2, fakeView, "localhost", ServerConstants.TCP_PORT);
            client2.chooseNickname(nickname2);
            client2.joinGame();


            ArrayList<Position> positions = new ArrayList<>();
            positions.add(new Position(3,1));

            try {
                client1.makeMove(positions, 1);
            } catch (InvalidNicknameException e){
                client2.makeMove(positions, 1);
            }


            // With this we delete the match that we have created

            Arrays.stream(Objects.requireNonNull(new File(ModelConstants.PATH_SAVED_MATCHES).list()))
                    .forEach((match) -> new File(ModelConstants.PATH_SAVED_MATCHES + match).delete());





        } catch (Exception e) {
            testSuccessful = false;
            e.printStackTrace();
        }

        assertTrue(testSuccessful);
    }
}
