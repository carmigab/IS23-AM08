package it.polimi.ingsw.server;

import java.io.Serializable;

/**
 * This class is used as an exchange of information between the client and the server in RMI
 */
public class ConnectionInformationRMI implements Serializable {

    /**
     * Registry name of the server to which the client has to connect
     */
    private final String registryName;

    /**
     * Registry port of the server containing the game
     */
    private final Integer registryPort;

    /**
     * Constructor of the class
     * @param registryName string containing the information of the registry name
     * @param registryPort integer containing the information of the registry port
     */
    public ConnectionInformationRMI(String registryName, Integer registryPort){
        this.registryName=registryName;
        this.registryPort=registryPort;
    }

    /**
     * Getter of the registry name
     * @return a string
     */
    public String getRegistryName() {
        return registryName;
    }

    /**
     * Getter of the registry port
     * @return an integer
     */
    public Integer getRegistryPort() {
        return registryPort;
    }
}
