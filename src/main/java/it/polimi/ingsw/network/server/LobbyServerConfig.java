package it.polimi.ingsw.network.server;

import com.google.gson.annotations.Expose;

import java.util.Optional;

/**
 * This class contains all the information for the correct setup of the server
 * It can also be loaded from file
 */
public class LobbyServerConfig {

    /**
     * Integer containing the port that the lobby server will be open on RMI
     */
    @Expose
    private Integer serverPortRMI;
    /**
     * Integer containing the port that the lobby server will be open on TCP
     */
    @Expose
    private Integer serverPortTCP;
    /**
     * String containing the server name that the client will refer to
     */
    @Expose
    private String serverName;
    /**
     * String containing the prefix used for the creaton of the game (in the form of startingName+numberOfGames)
     */
    @Expose
    private String startingName;

    /**
     * Empty constructor of the class
     */
    public LobbyServerConfig(){
    }


    /**
     * Constructor that takes all the parameters of the class
     * @param serverPortTCP integer containing the information of the server port
     * @param serverPortRMI integer containing the information of the server port
     * @param serverName string containing the information of the server name
     * @param startingName string containing the information of the starting name
     */
    public LobbyServerConfig( Integer serverPortRMI, Integer serverPortTCP, String serverName, String startingName){
        this.serverName=serverName;
        this.serverPortRMI=serverPortRMI;
        this.serverPortTCP=serverPortTCP;
        this.startingName=startingName;
    }

    /**
     * Getter of the server port in RMI
     * @return an integer
     */
    public Integer getServerPortRMI() {
        return this.serverPortRMI;
    }
    /**
     * Getter of the server port in TCP
     * @return an integer
     */
    public Integer getServerPortTCP() {
        return this.serverPortTCP;
    }
    /**
     * Getter of the server name
     * @return a string
     */
    public String getServerName() {
        return this.serverName;
    }

    /**
     * Getter fo the starting name of the games
     * @return a string
     */
    public String getStartingName(){ return this.startingName; }

    /**
     * Setter of the RMI server port
     * @param serverPortRMI an integer
     */
    public void setServerPortRMI(Integer serverPortRMI){
        this.serverPortRMI=serverPortRMI;
    }
    /**
     * Setter of the TCP server port
     * @param serverPortTCP an integer
     */
    public void setServerPortTCP(Integer serverPortTCP) {
        this.serverPortTCP = serverPortTCP;
    }
    /**
     * Setter of the server name
     * @param serverName a string
     */
    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    /**
     * Setter of the game name
     * @param startingName a string
     */
    public void setStartingName(String startingName) {
        this.startingName = startingName;
    }
}
