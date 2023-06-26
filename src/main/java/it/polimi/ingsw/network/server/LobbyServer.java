package it.polimi.ingsw.network.server;

import it.polimi.ingsw.constants.ModelConstants;
import it.polimi.ingsw.network.client.RmiClientInterface;
import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.utilities.JsonWithExposeSingleton;
import it.polimi.ingsw.constants.ServerConstants;
import it.polimi.ingsw.network.server.exceptions.*;
import it.polimi.ingsw.utilities.UtilityFunctions;
import it.polimi.ingsw.constants.ViewConstants;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.google.gson.*;
/**
 * This class sets up the main server which will make the player set his name and choose a game to join
 */
public class LobbyServer extends UnicastRemoteObject implements RMILobbyServerInterface {
    /**
     * Set that contains all the nicknames of every client connected
     */
    private final Set<String> nicknamesPool;
    /**
     * Set that contains all the nicknames of every client that is currently playing a game
     */
    private final Set<String> nicknamesInGame;
    /**
     * Map from the nicknames to al optional
     */
    private final Map<String, Optional<String>> potentialPlayers;
    /**
     * List of all the games currently active in the application
     */
    private final List<MatchServer> serverList;
    /**
     * List of all the game information present in the application
     */
    private final List<String> serverInformation;
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
     * Flag to se to true to mute the lobby server
     */
    private final boolean mute = false;


    /**
     * Constructor that loads the initial configuration from the object in input
     * @param config configuration of the server
     * @throws RemoteException if there are problems with the remote connection
     */
    public LobbyServer(LobbyServerConfig config) throws RemoteException{
        this.config = config;
        this.nicknamesPool = new HashSet<>();
        this.nicknamesInGame = new HashSet<>();
        this.potentialPlayers = new HashMap<>();
        this.serverList = new ArrayList<>();
        this.serverInformation = new ArrayList<>();
        this.banList = new ArrayList<>();
        this.banList.addAll(loadBanList());
        lockChooseNickName=new Object();
        lockCreateGame=new Object();

        // with this command we set a timeout for a rmi method invocation
        int timeout = ServerConstants.PING_TIME;
        System.getProperties().setProperty("sun.rmi.transport.tcp.responseTimeout", String.valueOf(timeout));

    }

    /**
     * Method used for the loading of the ban list from the file contained in "config/server"
     * @return list of banned words
     */
    private List<String> loadBanList(){
        return JsonWithExposeSingleton.getJsonWithExposeSingleton().fromJson(UtilityFunctions.getReaderFromFileNameRelativePath(ServerConstants.SERVER_BAN_LIST_FILENAME, this.getClass()),ArrayList.class);
    }


    /**
     * This method puts the server online in the RMI registry, it waits for someone to acquire it
     * and stats the tcp server
     */
    public void start(){
        try {

            if(!mute) System.out.println("LS: Initializing server...");
            if(!mute) System.out.println("LS: Cleaning the directory "+ ModelConstants.PATH_SAVED_MATCHES+" ...");
            this.cleanMatchDirectory();
            if(!mute) System.out.println("LS: Cleaning done...");
            this.loadPreviousGames();
            if(!mute) System.out.println("LS: Loaded previous games...");
            // Swap 'localhost' with the server ip if clients are not local
            //System.setProperty("java.rmi.server.hostname", "192.168.1.6");
            this.registry = LocateRegistry.createRegistry(this.config.getServerPortRMI());

            if(!mute) System.out.println("LS: Registry acquired...");
            this.registry.bind(this.config.getServerName(), this);

            if(!mute) System.out.println("LS: RMI Server online...");
        }catch (RemoteException | AlreadyBoundException e){
            System.out.println(e.getMessage());
        }

        // Here we start the Tcp Server
        this.startTcpServer(this.config.getServerPortTCP());

        // Info about the server
        if(!mute) System.out.println("LS: Name: "+this.config.getServerName());
        if(!mute) System.out.println("LS: Rmi Port: "+this.config.getServerPortRMI());
        if(!mute) System.out.println("LS: Tcp Port: "+this.config.getServerPortTCP());
    }

    /**
     * This method starts the game by putting it online in the RMI registry
     * The port and the name of the game are chosen automatically and are incremental
     * @param rs reference to the RMI server that needs to be put online
     * @param name reference to the information (name) that is useful for the setup of the game
     */
    private void startGame(MatchServer rs, String name){
        try {
            if(!mute) System.out.println("LS: Initializing game...");

            //System.setProperty("java.rmi.server.hostname", "192.168.43.4");
            //Registry r = LocateRegistry.createRegistry(info.getRegistryPort());
            //this.serverRegistries.add(r);

            if(!mute) System.out.println("LS: Registry acquired...");

            //r.bind(info.getRegistryName(), rs);
            this.registry.bind(name, rs);

            if(!mute) System.out.println("LS: RMI Server online...");
            if(!mute) System.out.println("LS: Name: "+name);
        }catch (RemoteException | AlreadyBoundException e){
            System.out.println(e.getMessage());
        }
    }

    /**
     * This method loads all games currently saved in the directory "savedMatches" and removes the ones which are ended already
     */
    private void cleanMatchDirectory(){

        try {
            Files.createDirectory(Paths.get(ModelConstants.PATH_SAVED_MATCHES));
        } catch (IOException e) {
            System.out.println("LS: Directory "+ModelConstants.PATH_SAVED_MATCHES+" already created...");
        }

        Arrays.stream(Objects.requireNonNull(new File(ModelConstants.PATH_SAVED_MATCHES).list()))
                .filter(match -> {
                    try (FileReader fr = new FileReader(ModelConstants.PATH_SAVED_MATCHES+match)){
                        return JsonWithExposeSingleton.getJsonWithExposeSingleton()
                                .fromJson(fr, GameModel.class)
                                .isGameOver();
                    } catch (IOException e) {
                        System.out.println(e.getMessage());
                        return false;
                    }
                })
                .forEach((match) -> new File(ModelConstants.PATH_SAVED_MATCHES+match).delete());
    }

    /**
     * This method loads pre-existing games
     */
    private void loadPreviousGames(){
        Arrays.stream(Objects.requireNonNull(new File(ModelConstants.PATH_SAVED_MATCHES).list()))
                .map(fileName-> fileName.substring(0,fileName.length()-ServerConstants.JSON_EXTENSION.length()))
                .flatMap(fileName -> Arrays.stream(fileName.split(ServerConstants.REGEX)))
                .forEach(playerName -> this.potentialPlayers.put(playerName, Optional.empty()));
    }

    /**
     * Method that the client can call to get a nickname assigned on the server
     * @param nickname string containing the nickname of the client
     * @return false if the nickname is either banned or already present
     * @throws RemoteException if the connection is lost
     * @throws ExistentNicknameException if the nickname is already present in the list
     * @throws IllegalNicknameException if the nickname is one of the banned words (or contains one)
     */
    @Override
    public boolean chooseNickname(String nickname) throws RemoteException, ExistentNicknameException, IllegalNicknameException {
        synchronized (lockChooseNickName) {
            if(!mute) System.out.println("LS: Someone is choosing the nickname "+nickname+"...");
            if (this.banList.stream().anyMatch(nickname::matches) || nickname.length()>= ViewConstants.MAX_NICKNAME_LENGTH) throw new IllegalNicknameException();
            if (!this.nicknamesPool.add(nickname)) throw new ExistentNicknameException();
            return true;
        }
    }

    /**
     * This method checks if a nickname has been asked to the server (is registered)
     * and is not currently in game
     * @param nickname nickname of the player to be checked
     * @throws AlreadyInGameException if the player is already in a different game
     * @throws NonExistentNicknameException if the player's nickname is not in the server's list
     */
    private void checkCredentialsIntegrity(String nickname) throws AlreadyInGameException, NonExistentNicknameException {
        if(!this.nicknamesPool.contains(nickname)) throw new NonExistentNicknameException();
        if(this.nicknamesInGame.contains(nickname)) throw new AlreadyInGameException();
    }

    /**
     * This method lets you create a game, and it automatically puts it in the RMI registries
     * @param numPlayers number of players that the client has chosen
     * @param nickname   nickname of the player that calls the method
     * @param client     reference to the methods of the client that can be called by the server using RMI
     * @return the information useful for the connection to the game
     * @throws RemoteException           if the connection is lost
     * @throws AlreadyInGameException       if the player is already in a different game
     * @throws NonExistentNicknameException if the player's nickname is not in the server's list
     */
    private String createGameTcpRmi(Integer numPlayers, String nickname, ClientHandler client) throws RemoteException, AlreadyInGameException, NonExistentNicknameException {
        synchronized (lockCreateGame) {
            // This code kills pre-existing games
            if (this.potentialPlayers.containsKey(nickname)){
                if(!mute) System.out.println("LS: Killing a MatchServer...");
                String toReturn=this.potentialPlayers.get(nickname).orElseGet(()->this.recoverGame(nickname));
                // here we manage the client
                this.serverList.get(this.serverInformation.indexOf(toReturn)).killMatchServer();
            }

            if(!mute) System.out.println("LS: Creating new game...");
            this.checkCredentialsIntegrity(nickname);
            this.nicknamesInGame.add(nickname);
            MatchServer rs = new MatchServer(numPlayers, this);
            // here we manage the client
            rs.addPlayer(nickname, client);
            client.setMatchServer(rs);

            this.serverList.add(rs);
            String gameName = this.config.getStartingName()+(this.serverList.size());
            this.serverInformation.add(gameName);

            this.startGame(rs, gameName);
            return gameName;
        }
    }

    /**
     * This method lets you recover a game from persistence, and it automatically puts it in the RMI registries
     * @param nickname   nickname of the player that calls the method
     * @param client     reference to the methods of the client that can be called by the server using RMI
     * @return the information useful for the connection to the game
     * @throws NoGameToRecoverException if there are no games that can be recovered from persistence
     */
    private String recoverGameTcpRmi(String nickname, ClientHandler client) throws NoGameToRecoverException{
        synchronized (lockCreateGame) {
            if (this.potentialPlayers.containsKey(nickname)) {
                if (!mute) System.out.println("LS: Joining game recovered from persistence...");
                String toReturn = this.potentialPlayers.get(nickname).orElseGet(() -> this.recoverGame(nickname));
                // here we manage the client
                this.serverList.get(this.serverInformation.indexOf(toReturn)).addPlayer(nickname, client);
                client.setMatchServer(this.serverList.get(this.serverInformation.indexOf(toReturn)));

                this.potentialPlayers.remove(nickname);
                return toReturn;
            }
            throw new NoGameToRecoverException();
        }
    }

    /**
     * This method lets you join one of the games listed in the current active games
     * @param nickname nickname of the player that calls the method
     * @param client reference to the methods of the client that can be called by the server
     * @param lobbyName the name of the lobby
     * @return the information useful for the connection to the game
     * @throws AlreadyInGameException       if the player is already in a different game
     * @throws NonExistentNicknameException if the player's nickname is not in the server's list
     * @throws NoGameToRecoverException     if there are no games available for recovery with your name
     */
    private String joinGameTcpRmi(String nickname, ClientHandler client, String lobbyName) throws AlreadyInGameException, NonExistentNicknameException, NoGameToRecoverException, WrongLobbyIndexException, LobbyFullException {
        synchronized (lockCreateGame) {

            this.checkCredentialsIntegrity(nickname);

            if (!serverInformation.contains(lobbyName)) {
                throw new WrongLobbyIndexException();
            }

            if (this.serverList.get(serverInformation.indexOf(lobbyName)).getFreeSpaces() == 0) {
                throw new LobbyFullException();
            }

            // check if the game is a recovered pre-existing game
            if (this.serverList.get(serverInformation.indexOf(lobbyName)).isRecovered()){
                throw new NoGameToRecoverException();
            }

            MatchServer matchServer;

            if(!mute) System.out.println("LS: Joining game at index...");
            matchServer = this.serverList.get(serverInformation.indexOf(lobbyName));

            matchServer.addPlayer(nickname, client);
            client.setMatchServer(matchServer);
            this.nicknamesInGame.add(nickname);

            return lobbyName;
            //Should never arrive here
//            return null;
        }
    }


    /**
     * This method lets you recover a game from where it has been stopped
     * It takes the information from the file inferring it by your name
     * Also adds the players to the potential players list (inferred from the file name)
     * @param nickname nickname of the player who asks to recover a game
     * @return the information useful for the connection to the game
     */
    private String recoverGame(String nickname) {
        synchronized (lockCreateGame) {
            if(!mute) System.out.println("LS: Recovering game...");
            this.nicknamesInGame.add(nickname);

            //load filename
            String fileName = Arrays.stream(Objects.requireNonNull(new File(ModelConstants.PATH_SAVED_MATCHES).list()))
                    .filter(file -> file.contains(nickname+ServerConstants.REGEX))
                    .findFirst()
                    .orElse("LS: shouldneverenterhere");

            try {
                //create a game with the GameModel as parameter
                GameModel gm = new GameModel(JsonWithExposeSingleton.getJsonWithExposeSingleton().fromJson(new FileReader(ModelConstants.PATH_SAVED_MATCHES + fileName), GameModel.class));
                MatchServer rs = new MatchServer(gm, this);
                this.serverList.add(rs);
                String gameName = this.config.getStartingName() + (this.serverList.size());
                this.serverInformation.add(gameName);
                //add the potential players to the list
                this.addPotentialPlayers(fileName, gameName, nickname);
                this.startGame(rs, gameName);
                return gameName;
            } catch (FileNotFoundException | RemoteException e) {
                System.out.println(e.getMessage());
            }
            //Should never arrive here
            return null;
        }
    }

    /**
     * This method is used to add potential players to a game
     * @param fileName: name of the file
     * @param gameName: name of the game
     * @param firstPlayer: first player of the game
     */
    private void addPotentialPlayers(String fileName, String gameName, String firstPlayer){
        Arrays.stream(fileName
                .substring(0,fileName.length()-ServerConstants.JSON_EXTENSION.length())
                .split(ServerConstants.REGEX))
                .filter(name -> !name.equals(firstPlayer))
                .forEach(match -> this.potentialPlayers.put(match,Optional.of(gameName)));
    }

    /**
     * This method takes in input the list of nicknames, and it removes all the players
     * from the current pool of players in game
     * @param playersList file name where the game was stored
     * @param match the match server
     */
    public void removePlayersAndMatchServerFromLobby(List<String> playersList, MatchServer match){
        playersList.forEach(this.nicknamesInGame::remove);
        playersList.forEach(this.nicknamesPool::remove);
        playersList.forEach(this.potentialPlayers::remove);

//        if(!mute) System.out.println("LS: Freeing a MatchServer...");
//        this.serverList.remove(match);

    }

    /**
     * This method calls the recoverGameTcpRmi method
     * It is the method called by a rmi remote call
     * @param nickname nickname of the player that calls the method
     * @param rmiClient reference to the methods of the client that can be called by the server using RMI
     * @return the information useful for the connection to the game
     * @throws RemoteException           if the connection fails
     * @throws NoGameToRecoverException    if every game is full or there is no game currently ongoing in the server
     */
    @Override
    public String recoverGame(String nickname, RmiClientInterface rmiClient) throws RemoteException, NoGameToRecoverException {
        return this.recoverGameTcpRmi(nickname, new RmiClientHandler(rmiClient));
    }

    /**
     * This method calls the joinGameTcpRmi method
     * It is the method called by a rmi remote call
     * @param nickname nickname of the player that calls the method
     * @param rmiClient reference to the methods of the client that can be called by the server using RMI
     * @param gameIndex index of the game
     * @return the information useful for the connection to the game
     * @throws RemoteException           if the connection fails
     * @throws NoGamesAvailableException    if every game is full or there is no game currently ongoing in the server
     * @throws AlreadyInGameException       if the player is already in a different game
     * @throws NonExistentNicknameException if the player's nickname is not in the server's list
     */
    @Override
    public String joinGame(String nickname, RmiClientInterface rmiClient, String gameIndex) throws RemoteException, NoGamesAvailableException, AlreadyInGameException, NonExistentNicknameException, NoGameToRecoverException, WrongLobbyIndexException, LobbyFullException {
        return this.joinGameTcpRmi(nickname, new RmiClientHandler(rmiClient), gameIndex);
    }

    /**
     * This method returns a list of the active lobbies
     * @param nickname the nickname
     * @return the list of active lobbies
     * @throws NoGamesAvailableException
     */
    @Override
    public List<Lobby> getLobbies(String nickname) throws NoGamesAvailableException {
        List<Lobby> activeLobbies = new ArrayList<>();

        for (MatchServer matchServer : serverList) {
            if (matchServer.getFreeSpaces() > 0) {
                if (matchServer.isRecovered())
                    // We add a 'standard lobby' with the recovered flag set to true
                    activeLobbies.add(new LobbyStandard(serverInformation.get(serverList.indexOf(matchServer)),
                        0, 0,
                        List.copyOf(matchServer.getNicknamesList()), true));
                else
                    // We add a standard lobby with the recovered flag set to false
                    activeLobbies.add(new LobbyStandard(serverInformation.get(serverList.indexOf(matchServer)),
                        matchServer.getNumPlayers(), matchServer.getNicknamesList().size(),
                        List.copyOf(matchServer.getNicknamesList()), false));
            }
        }

        // If the player is a potential one we add a 'recovered lobby'
        if(this.potentialPlayers.containsKey(nickname)){
            activeLobbies.add(new LobbyRecovered("Recovered", 0, 0, List.of()));
        }

        if(activeLobbies.isEmpty())
            throw new NoGamesAvailableException();

        return activeLobbies;
    }

    /**
     * This method calls the createGameTcpRmi method
     * It is the method called by a rmi remote call
     * @param numPlayers number of players that the client has chosen
     * @param nickname   nickname of the player that calls the method
     * @param rmiClient     reference to the methods of the client that can be called by the server using RMI
     * @return the information useful for the connection to the game
     * @throws RemoteException          if the connection fails
     * @throws AlreadyInGameException       if the player is already in a different game
     * @throws NonExistentNicknameException if the player's nickname is not in the server's list
     */
    @Override
    public String createGame(Integer numPlayers, String nickname, RmiClientInterface rmiClient) throws RemoteException, AlreadyInGameException, NonExistentNicknameException{
        return this.createGameTcpRmi(numPlayers, nickname, new RmiClientHandler(rmiClient));
    }


    ///////////////////////////////////  Tcp methods  ////////////////////////////////////

    /**
     * This method calls the joinGameTcpRmi method
     * It is the method called by the tcp client handler
     * @param nickname nickname of the player that calls the method
     * @param tcpClient reference to the methods of the client that can be called by the server using RMI
     * @return the information useful for the connection to the game
     * @throws NoGameToRecoverException if no games are available to recover
     */
    public String recoverGame(String nickname, TcpClientHandler tcpClient) throws NoGameToRecoverException{
        return this.recoverGameTcpRmi(nickname, tcpClient);
    }

    /**
     * This method calls the joinGameTcpRmi method
     * It is the method called by the tcp client handler
     * @param nickname nickname of the player that calls the method
     * @param tcpClient reference to the methods of the client that can be called by the server using RMI
     * @param lobbyName the name of the lobby
     * @return the information useful for the connection to the game
     * @throws NoGamesAvailableException if no games are available
     * @throws AlreadyInGameException if the player is already in a different game
     * @throws NonExistentNicknameException if the player's nickname is not in the server's list
     */
    public String joinGame(String nickname, TcpClientHandler tcpClient, String lobbyName) throws NoGamesAvailableException, AlreadyInGameException, NonExistentNicknameException, NoGameToRecoverException, WrongLobbyIndexException, LobbyFullException {
        return this.joinGameTcpRmi(nickname, tcpClient, lobbyName);
    }

    /**
     * This method calls the createGameTcpRmi method
     * It is the method called by the tcp client handler
     * @param numPlayers the number of player slots
     * @param nickname nickname of the player that calls the method
     * @param tcpClient reference to the methods of the client that can be called by the server using RMI
     * @return the information useful for the connection to the game
     * @throws RemoteException if the connection fails
     * @throws AlreadyInGameException if the player is already in a different game
     * @throws NonExistentNicknameException if the player's nickname is not in the server's list
     */
    public String createGame(Integer numPlayers, String nickname, TcpClientHandler tcpClient) throws RemoteException, AlreadyInGameException, NonExistentNicknameException{
        return this.createGameTcpRmi(numPlayers, nickname, tcpClient);
    }

    /**
     * Method to start the tcp server and waits for connections
     * @param port: the tcp server port
     */
    private void startTcpServer(int port){
        if(!mute) System.out.println("LS: Starting Tcp Server...");

        Thread t = new Thread(() -> {
            ExecutorService executor = Executors.newCachedThreadPool();
            ServerSocket serverSocket;

            try {
                serverSocket = new ServerSocket(port);
                if(!mute) System.out.println("LS: Server ip: " + InetAddress.getLocalHost().getHostAddress());
            } catch (IOException e) {
                if(!mute) System.out.println("LS: Error while opening the tcp Server port");
                System.err.println(e.getMessage());
                return;
            }

            while (true) {
                try {
                    Socket socket = serverSocket.accept();
                    if(!mute) System.out.println("LS: Accepted new tcp connection");
                    executor.submit(new TcpClientHandler(socket, this));
                    if(!mute) System.out.println("LS: Connection submitted to executor");
                } catch(IOException e) {
                    if(!mute) System.out.println("LS: Error while accepting tcp connection");
                    break;
                }
            }

            executor.shutdown();
            if(!mute) System.out.println("LS: Shutting down Tcp Server");
        });
        t.start();

        if(!mute) System.out.println("LS: Tcp Server online...");
    }
}
