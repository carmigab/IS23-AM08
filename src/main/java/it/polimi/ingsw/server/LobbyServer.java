package it.polimi.ingsw.server;

import it.polimi.ingsw.client.RmiClientInterface;
import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.constants.AppConstants;
import it.polimi.ingsw.model.utilities.JsonWithExposeSingleton;
import it.polimi.ingsw.server.constants.ServerConstants;
import it.polimi.ingsw.server.exceptions.ExistentNicknameExcepiton;
import it.polimi.ingsw.server.exceptions.IllegalNicknameException;
import it.polimi.ingsw.server.exceptions.NoGamesAvailableException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

/**
 * This class sets up the main server which will make the player set his name and choose a game to join
 */
public class LobbyServer extends UnicastRemoteObject implements RMILobbyServerInterface {
    /**
     * Set that contains all the nicknames of every client connected
     */
    private final Set<String> nicknamesPool;
    /**
     * List of all the games currently active in the application
     */
    private final List<RmiServer> serverList;
    /**
     * List of all the game informations present in the application
     */
    private final List<ConnectionInformationRMI> serverInformation;
    /**
     * List of all the game registries present in the application
     */
    private final List<Registry> serverRegistries;
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
     * Object used as a lock for the insertion of the name in the list of chosen nicknames
     */
    private final Object lockChooseNickName;
    /**
     * Object used as a lock for the creation and joining of a game
     */
    private final Object lockCreateGame;
    /**
     * Registry containing the main part of LobbyServer
     */
    private Registry registry;


    /**
     * Constructor that loads the initial configuration of the server from file
     */
    public LobbyServer() throws RemoteException{
        super();
        this.config = loadInitialConfig();
        this.nicknamesPool = new HashSet<>();
        this.serverList = new ArrayList<>();
        this.serverInformation = new ArrayList<>();
        this.serverRegistries = new ArrayList<>();
        this.banList = new ArrayList<>();
        this.banList.addAll(loadBanList());
        lockChooseNickName=new Object();
        lockCreateGame=new Object();
    }


    /**
     * Constructor that loads the initial configuration from the object in input
     * @param config configuration of the server
     */
    public LobbyServer(LobbyServerConfig config) throws RemoteException{
        super();
        this.config = config;
        this.nicknamesPool = new HashSet<>();
        this.serverList = new ArrayList<>();
        this.serverInformation = new ArrayList<>();
        this.serverRegistries = new ArrayList<>();
        this.banList = new ArrayList<>();
        this.banList.addAll(loadBanList());
        lockChooseNickName=new Object();
        lockCreateGame=new Object();
    }


    /**
     * Constructor that receives in input the parameters and creates a configuration manually
     * @param serverPort integer containing the information of the server port
     * @param serverName string containing the information of the server name
     * @param startingPort integer containing the information of the starting port
     * @param startingName string containing the information of the starting name
     */
    public LobbyServer(int serverPort, String serverName, int startingPort, String startingName) throws RemoteException{
        this(new LobbyServerConfig(serverPort, serverName, startingPort, startingName));
    }


    /**
     * Method used for the loading of the initial configuration from the file saved in "config/server"
     * @return a LobbyServerConfig object
     */
    private LobbyServerConfig loadInitialConfig() {
        Reader r=null;
        try {
            r= new FileReader(ServerConstants.SERVER_INITIAL_CONFIG);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return JsonWithExposeSingleton.getJsonWithExposeSingleton().fromJson(r, LobbyServerConfig.class);
    }


    /**
     * This method puts the server online in the RMI registry, and it waits for someone to acquire it
     */
    public void start(){
        try {
            System.out.println("Initializing server...");
            System.out.println("Cleaning the directory "+ AppConstants.PATH_SAVED_MATCHES+" ...");
            this.cleanMatchDirectory();
            System.out.println("Cleaning done...");
            this.registry = LocateRegistry.createRegistry(this.config.getServerPort());

            System.out.println("Registry acquired...");
            this.registry.bind(this.config.getServerName(), this);

            System.out.println("RMI Server online...");
            System.out.println("Name: "+this.config.getServerName()+" Port: "+this.config.getServerPort());
        }catch (RemoteException e){
            System.out.println(e.getMessage());
        } catch (AlreadyBoundException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * This method starts the game by putting it online in the RMI registry
     * The port and the name of the game are chosen automatically and are incremental
     * @param rs reference to the RMI server that needs to be put online
     * @param info reference to the information (port and name) that is useful for the setup of the game
     */
    private void startGame(RmiServer rs, ConnectionInformationRMI info){
        try {
            System.out.println("Initializing game...");
            Registry r = LocateRegistry.createRegistry(info.getRegistryPort());
            this.serverRegistries.add(r);

            System.out.println("Registry acquired...");

            r.bind(info.getRegistryName(), rs);

            System.out.println("RMI Server online...");
            System.out.println("Name: "+info.getRegistryName()+" Port: "+info.getRegistryPort());
        }catch (RemoteException e){
            System.out.println(e.getMessage());
        } catch (AlreadyBoundException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * This method loads all games currently saved in the directory "savedMatches" and removes the ones which are ended already
     * TODO: understand why cannot delete files (used by other process)
     */
    private void cleanMatchDirectory(){
        /*
        Arrays.stream(Objects.requireNonNull(new File(AppConstants.PATH_SAVED_MATCHES).list()))
                .filter(match -> {
                    try {
                        return JsonWithExposeSingleton.getJsonWithExposeSingleton()
                                .fromJson(new FileReader(AppConstants.PATH_SAVED_MATCHES+match), GameModel.class)
                                .isGameOver();
                    } catch (FileNotFoundException e) {
                        System.out.println(e.getMessage());
                        return false;
                    }
                })
                .forEach((match) -> {
                    try {
                        Files.delete(Path.of(AppConstants.PATH_SAVED_MATCHES + match));
                    } catch (IOException e) {
                        System.out.println(e.getMessage());
                    }
                });
        */
        List<String> allMatches=Arrays.stream(Objects.requireNonNull(new File(AppConstants.PATH_SAVED_MATCHES).list())).toList();
        for(String match: allMatches){
            try {
                if(JsonWithExposeSingleton.getJsonWithExposeSingleton()
                        .fromJson(new FileReader(AppConstants.PATH_SAVED_MATCHES+match), GameModel.class)
                        .isGameOver()){
                    Files.delete(Path.of(AppConstants.PATH_SAVED_MATCHES + match));
                }
            } catch (FileNotFoundException e) {
                System.out.println(e.getMessage());
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
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
    public boolean chooseNickname(String nickname) throws RemoteException, ExistentNicknameExcepiton, IllegalNicknameException {
        synchronized (lockChooseNickName) {
            if (this.banList.stream().anyMatch(nickname::contains)) throw new IllegalNicknameException();
            if (!this.nicknamesPool.add(nickname)) throw new ExistentNicknameExcepiton();
            return true;
        }
    }


    /**
     * This method lets you create a game and it automatically puts it in the RMI registries
     * @param numPlayers number of players that the client has chosen
     * @param nickname nickname of the player that calls the method
     * @param client reference to the methods of the client that can be called by the server using RMI
     * @return the information useful for the connection to the game
     */
    @Override
    public ConnectionInformationRMI createGame(Integer numPlayers, String nickname, RmiClientInterface client) throws RemoteException {
        synchronized (lockCreateGame) {
            RmiServer rs = new RmiServer(numPlayers);
            rs.addPlayer(nickname, client);
            this.serverList.add(rs);
            ConnectionInformationRMI info = new ConnectionInformationRMI(this.config.getStartingName()+(this.serverList.size()), this.config.getStartingPort()+this.serverList.size());
            this.serverInformation.add(info);
            this.startGame(rs, info);
            return info;
        }
    }


    /**
     * This method lets you join the first game available in the list of all games active
     * If there is none or every game is full it throws a NoGamesAvailableException
     * @param nickname nickname of the player that calls the method
     * @param client reference to the methods of the client that can be called by the server using RMI
     * @return the information useful for the connection to the game
     */
    @Override
    public ConnectionInformationRMI joinGame(String nickname, RmiClientInterface client) throws RemoteException, NoGamesAvailableException {
        synchronized (lockCreateGame) {
            int gameFound = 0;
            ConnectionInformationRMI conn;

            while(gameFound < this.serverRegistries.size()){
                if(this.serverList.get(gameFound).getFreeSpaces()>0){
                    this.serverList.get(gameFound).addPlayer(nickname, client);
                    return this.serverInformation.get(gameFound);
                }
                gameFound++;
            }

            if(gameFound == this.serverRegistries.size())
                throw new NoGamesAvailableException();

            //Should never arrive here
            return null;
        }
    }

    /**
     * This method cheks if in all the saved matches there is a saved game (currently ongoing) with the player whose nickname is inserted in input
     * @param nickname nickname of the player to be checked
     * @return true if in the folder "savedMatches" there is a file that contains the nickname
     */
    @Override
    public boolean isGameExistent(String nickname) throws RemoteException{
        String jsonExtension=".json";
        String regex="_";
        //Should never be null
        return Arrays.stream(Objects.requireNonNull(new File(AppConstants.PATH_SAVED_MATCHES).list()))
                .filter(fileName -> fileName.endsWith(jsonExtension))
                .anyMatch(fileName -> fileName.contains(nickname+regex));
        /*
        return Arrays.stream(Objects.requireNonNull(new File(AppConstants.PATH_SAVED_MATCHES).list()))
                .filter(fileName -> fileName.endsWith(jsonExtension))
                .map( fileName -> fileName.substring(0, fileName.length()-jsonExtension.length()))
                .flatMap(fileName -> Arrays.stream(fileName.split(regex)))
                .anyMatch(savedNickname -> savedNickname.equals(nickname));
        */
    }

    /**
     * TODO
     * @param nickname
     * @param client
     * @return
     */
    @Override
    public ConnectionInformationRMI recoverGame(String nickname, RmiClientInterface client) throws RemoteException {
        return null;
    }

}
