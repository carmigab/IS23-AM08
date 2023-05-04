package it.polimi.ingsw.network;

import it.polimi.ingsw.network.client.RmiClient;
import it.polimi.ingsw.network.client.RmiClientInterface;
import it.polimi.ingsw.network.server.LobbyServer;
import it.polimi.ingsw.network.server.LobbyServerConfig;
import it.polimi.ingsw.constants.ServerConstants;
import it.polimi.ingsw.network.server.exceptions.AlreadyInGameException;
import it.polimi.ingsw.network.server.exceptions.ExistentNicknameException;
import it.polimi.ingsw.network.server.exceptions.IllegalNicknameException;
import it.polimi.ingsw.network.server.exceptions.NonExistentNicknameException;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import static org.junit.jupiter.api.Assertions.*;

class LobbyServerTest {

    @Test
    public void testChooseNickNameAndConstructors() throws RemoteException, ExistentNicknameException, IllegalNicknameException, FileNotFoundException {
        LobbyServer ls1= new LobbyServer(new LobbyServerConfig(ServerConstants.RMI_PORT, ServerConstants.TCP_PORT, "dummy","gamedummy"));

        //create a player with a correct nickname
        assertTrue(ls1.chooseNickname("Gabri"));
        //trying to insert it again will not work
        assertThrows(ExistentNicknameException.class, ()-> ls1.chooseNickname("Gabri"));
        //try a name from the ban list
        String bannedName="Matteo";
        assertThrows(IllegalNicknameException.class, ()-> ls1.chooseNickname(bannedName));
    }

    @Test
    public void testCorrectRegex() throws RemoteException, ExistentNicknameException, IllegalNicknameException {
        LobbyServer ls=new LobbyServer(new LobbyServerConfig(ServerConstants.RMI_PORT, ServerConstants.TCP_PORT, "dummy","gamedummy"));

        // add a non banned name
        assertTrue(ls.chooseNickname("Gabriele"));
        // add a name with a banned regex (there is a space
        assertThrows(IllegalNicknameException.class, ()-> ls.chooseNickname("Gabriele_"));
        // try another one in different positions also
        assertThrows(IllegalNicknameException.class, ()-> ls.chooseNickname("Gab riele "));
        assertThrows(IllegalNicknameException.class, ()-> ls.chooseNickname(" Gabriele"));
        // now try with another banned regex
        assertThrows(IllegalNicknameException.class, ()-> ls.chooseNickname("Matteo"));
        assertThrows(IllegalNicknameException.class, ()-> ls.chooseNickname("aaMatteo"));
        assertThrows(IllegalNicknameException.class, ()-> ls.chooseNickname("Matteoa"));
        // now try the keyword "all" alone, should throw the exception
        assertThrows(IllegalNicknameException.class, ()-> ls.chooseNickname("all"));
        // try some variants to show it does not throw anything
        assertTrue(ls.chooseNickname("call"));
        assertTrue(ls.chooseNickname("allc"));
        assertTrue(ls.chooseNickname("All"));
    }

    @Disabled
    @Test
    public void testCreateGame() throws RemoteException, ExistentNicknameException, IllegalNicknameException, NonExistentNicknameException, AlreadyInGameException, NotBoundException, InterruptedException {
        LobbyServer ls=new LobbyServer(new LobbyServerConfig(ServerConstants.RMI_PORT, ServerConstants.TCP_PORT, "dummy","gamedummy"));
        RmiClientInterface rmiClient = new RmiClient("The one who tests", null, "localhost", ServerConstants.RMI_PORT);
        //Let's use a banned word first
        String nickname="all";
        assertThrows(IllegalNicknameException.class, ()-> ls.chooseNickname("The one who tests"));
        // it should not let you create the game (for the moment we do not care about the clients, so let them null
        assertThrows(NonExistentNicknameException.class, ()-> ls.createGame(3, nickname, rmiClient));
        // now use a normal nickname but still should throw same exception
        String nickname2="Gabriele";
        assertThrows(NonExistentNicknameException.class, ()-> ls.createGame(3, nickname2, rmiClient));
        // now add it
        assertTrue(ls.chooseNickname(nickname2));
        //and ask same thing but do not throw anything
        assertNotNull(ls.createGame(3,nickname2,rmiClient));
        //if we try to add it again with the same nickname it should not be possible
        assertThrows(AlreadyInGameException.class, ()-> ls.createGame(3, nickname2, rmiClient));
    }

}