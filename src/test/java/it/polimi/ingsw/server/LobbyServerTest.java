package it.polimi.ingsw.server;

import com.google.gson.Gson;
import it.polimi.ingsw.model.exceptions.NoMoreTilesAtStartFillBoardException;
import it.polimi.ingsw.server.constants.ServerConstants;
import it.polimi.ingsw.server.exceptions.ExistentNicknameExcepiton;
import it.polimi.ingsw.server.exceptions.IllegalNicknameException;
import org.junit.jupiter.api.Test;

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
        List<String> bannedNames=new ArrayList<>();
        bannedNames=new Gson().fromJson(new FileReader(ServerConstants.SERVER_BAN_LIST),ArrayList.class);
        String bannedName=bannedNames.get(new Random().nextInt(bannedNames.size()));
        assertThrows(IllegalNicknameException.class, ()-> ls1.chooseNickname(bannedName));

        LobbyServer ls2= new LobbyServer(1234, "Ciao",2000,"Game");

        assertTrue(ls2.chooseNickname("Gabri"));
        assertThrows(ExistentNicknameExcepiton.class, ()-> ls2.chooseNickname("Gabri"));
        assertThrows(IllegalNicknameException.class, ()-> ls2.chooseNickname(bannedName));

        LobbyServer ls3= new LobbyServer(new LobbyServerConfig(1234, "Ciao",2000,"Game"));

        assertTrue(ls3.chooseNickname("Gabri"));
        assertThrows(ExistentNicknameExcepiton.class, ()-> ls3.chooseNickname("Gabri"));
        assertThrows(IllegalNicknameException.class, ()-> ls3.chooseNickname(bannedName));
    }

}