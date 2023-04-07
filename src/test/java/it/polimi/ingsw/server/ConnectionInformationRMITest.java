package it.polimi.ingsw.server;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConnectionInformationRMITest {

    @Test
    public void testConnectionInformationRMI(){
        ConnectionInformationRMI ci=new ConnectionInformationRMI("Prova", 1234);
        assertEquals("Prova", ci.getRegistryName());
        assertEquals(1234, ci.getRegistryPort());
    }

}