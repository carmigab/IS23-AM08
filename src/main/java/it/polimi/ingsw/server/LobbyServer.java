package it.polimi.ingsw.server;

import it.polimi.ingsw.client.RmiClient;
import it.polimi.ingsw.model.utilities.JsonWithExposeSingleton;
import it.polimi.ingsw.server.constants.ServerConstants;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This class sets up the main server which will make the player set his name and choose a game to join
 */
public class LobbyServer implements RMILobbyServerInterface{

    /**
     * Set that contains all the nicknames of every client connected
     */
    private final Set<String> nicknamesPool;
    /**
     * List of all the games currently active in the application
     */
    private final List<RmiServer> serverList;
    /**
     * Setup information of the server
     * Can be loaded from file
     */
    private final LobbyServerConfig config;
    /**
     * List loaded from file that contains all the words that cannot be used as nicknames for the players
     * Useful for avoiding ambiguites when calling some commands (especially from cli)
     */
    private final List<String> banList;

    /**
     * Constructor that loads the initial configuration of the server from file
     */
    public LobbyServer(){
        this.config=loadInitialConfig();
        this.nicknamesPool=new HashSet<>();
        this.serverList=new ArrayList<>();
        this.banList=new ArrayList<>();
        this.banList.addAll(loadBanList());
    }
    /**
     * Constructor that loads the initial configuration from the object in input
     * @param config configuration of the server
     */
    public LobbyServer(LobbyServerConfig config){
        this.config=config;
        this.nicknamesPool=new HashSet<>();
        this.serverList=new ArrayList<>();
        this.banList=new ArrayList<>();
        this.banList.addAll(loadBanList());
    }
    /**
     * Constructor that receives in input the parameters and creates a configuration manually
     * @param serverPort integer containing the information of the server port
     * @param serverName string containing the information of the server name
     * @param startingPort integer containing the information of the starting port
     */
    public LobbyServer(int serverPort, String serverName, int startingPort){
        this(new LobbyServerConfig(serverPort, serverName, startingPort));
    }

    /**
     * Method used for the loading of the initial configuration from the file saved in "config/server"
     * @return a LobbyServerConfig object
     */
    private LobbyServerConfig loadInitialConfig(){
        Reader r=null;
        try {
            r= new FileReader(ServerConstants.SERVER_INITIAL_CONFIG);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return JsonWithExposeSingleton.getJsonWithExposeSingleton().fromJson(r, LobbyServerConfig.class);
    }

    /**
     * Method used for the loading of the ban list from the file contained in "config/server"
     */
    private List<String> loadBanList(){
        Reader r=null;
        try {
            r= new FileReader(ServerConstants.SERVER_BAN_LIST);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return JsonWithExposeSingleton.getJsonWithExposeSingleton().fromJson(r, ArrayList.class);
    }

    /**
     * Method that the client can call to get a nickname assigned on the server
     * @param nickname string containing the nickname of the client
     * @return false if the nickname is either banned or already present
     */
    @Override
    public boolean chooseNickname(String nickname){
        if(this.banList.contains(nickname)) return false;
        return this.nicknamesPool.add(nickname);
    }

    /**
     * TODO
     * @param nickname
     * @param client
     * @return
     */
    @Override
    public ConnectionInformationRMI createGame(Integer numPlayers, String nickname, RmiClient client) {
        RmiServer rs=new RmiServer(numPlayers);
        this.serverList.add(rs);
        return new ConnectionInformationRMI("Prova", 2345);
    }

    /**
     * TODO
     * @param nickname
     * @param client
     * @return
     */
    @Override
    public ConnectionInformationRMI joinGame(String nickname, RmiClient client) {
        return null;
    }
}
