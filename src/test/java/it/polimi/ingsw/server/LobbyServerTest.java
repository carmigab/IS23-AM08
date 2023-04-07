package it.polimi.ingsw.server;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LobbyServerTest {

    @Test
    public void testChooseNickNameAndConstructors(){
        LobbyServer ls1= new LobbyServer();

        //create a player with a correct nickname
        assertTrue(ls1.chooseNickname("Gabri"));
        //trying to insert it again will not work
        assertFalse(ls1.chooseNickname("Gabri"));
        //try a name from the ban list
        assertFalse(ls1.chooseNickname("Matteo"));

        LobbyServer ls2= new LobbyServer(1234, "Ciao",2000);

        assertTrue(ls2.chooseNickname("Gabri"));
        assertFalse(ls2.chooseNickname("Gabri"));
        assertFalse(ls2.chooseNickname("Matteo"));

        LobbyServer ls3= new LobbyServer(new LobbyServerConfig(1234, "Ciao",2000));

        assertTrue(ls3.chooseNickname("Gabri"));
        assertFalse(ls3.chooseNickname("Gabri"));
        assertFalse(ls3.chooseNickname("Matteo"));
    }

}