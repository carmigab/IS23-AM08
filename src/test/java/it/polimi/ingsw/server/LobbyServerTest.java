package it.polimi.ingsw.server;

import com.google.gson.Gson;
import it.polimi.ingsw.model.exceptions.NoMoreTilesAtStartFillBoardException;
import it.polimi.ingsw.server.constants.ServerConstants;
import it.polimi.ingsw.server.exceptions.AlreadyInGameException;
import it.polimi.ingsw.server.exceptions.ExistentNicknameExcepiton;
import it.polimi.ingsw.server.exceptions.IllegalNicknameException;
import it.polimi.ingsw.server.exceptions.NonExistentNicknameException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class LobbyServerTest {

    @Test
    public void testChooseNickNameAndConstructors() throws RemoteException, ExistentNicknameExcepiton, IllegalNicknameException, FileNotFoundException {
        LobbyServer ls1= new LobbyServer();

        //create a player with a correct nickname
        assertTrue(ls1.chooseNickname("Gabri"));
        //trying to insert it again will not work
        assertThrows(ExistentNicknameExcepiton.class, ()-> ls1.chooseNickname("Gabri"));
        //try a name from the ban list
        String bannedName="Matteo";
        assertThrows(IllegalNicknameException.class, ()-> ls1.chooseNickname(bannedName));

        LobbyServer ls2= new LobbyServer(1234, "Ciao",2000,"Game");

        assertTrue(ls2.chooseNickname("Gabri"));
        assertThrows(ExistentNicknameExcepiton.class, ()-> ls2.chooseNickname("Gabri"));
        assertThrows(IllegalNicknameException.class, ()-> ls2.chooseNickname(bannedName));

        LobbyServer ls3= new LobbyServer(new LobbyServerConfig(1234, 1235, "Ciao",2000,"Game"));

        assertTrue(ls3.chooseNickname("Gabri"));
        assertThrows(ExistentNicknameExcepiton.class, ()-> ls3.chooseNickname("Gabri"));
        assertThrows(IllegalNicknameException.class, ()-> ls3.chooseNickname(bannedName));
    }

    @Test
    public void testCorrectRegex() throws RemoteException, ExistentNicknameExcepiton, IllegalNicknameException {
        LobbyServer ls=new LobbyServer();

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

    @Test
    public void testCreateGame() throws RemoteException, ExistentNicknameExcepiton, IllegalNicknameException, NonExistentNicknameException, AlreadyInGameException {
        LobbyServer ls=new LobbyServer();
        //Let's use a banned word first
        String nickname="all";
        assertThrows(IllegalNicknameException.class, ()-> ls.chooseNickname("Matteoa"));
        // it should not let you create the game (for the moment we do not care about the clients, so let them null
        assertThrows(NonExistentNicknameException.class, ()-> ls.createGame(3, nickname, null));
        // now use a normal nickname but still should throw same exception
        String nickname2="Gabriele";
        assertThrows(NonExistentNicknameException.class, ()-> ls.createGame(3, nickname2, null));
        // now add it
        assertTrue(ls.chooseNickname(nickname2));
        //and ask same thing but do not throw anything
        assertNotNull(ls.createGame(3,nickname2,null));
        //if we try to add it again with the same nickname it should not be possible
        assertThrows(AlreadyInGameException.class, ()-> ls.createGame(3, nickname2, null));
    }

}