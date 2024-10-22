package it.polimi.ingsw.constants;

/**
 * This class contains all the constants related to the server
 */
public class ServerConstants {

    public static final String SERVER_INITIAL_CONFIG_FILENAME="lobbyServerInitialConfig.json";
    public static final String SERVER_BAN_LIST_FILENAME="lobbyServerBanList.json";
    public static final Integer RMI_PORT = 42069;
    public static final Integer TCP_PORT = 42070;
    public static final Integer PING_TIME = 10000;
    public static final Integer TCP_WAIT_TIME = 2000;
    public static final Integer CLIENT_SLEEPING_TIME = 5000;
    public static final String LOBBY_SERVER = "LobbyServer";
    public static final String REGEX="_";
    public static final String JSON_EXTENSION="_.json";

}
