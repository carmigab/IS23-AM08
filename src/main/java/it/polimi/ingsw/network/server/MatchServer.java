package it.polimi.ingsw.network.server;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.exceptions.InvalidNicknameException;
import it.polimi.ingsw.controller.exceptions.InvalidMoveException;
import it.polimi.ingsw.gameInfo.GameInfo;
import it.polimi.ingsw.gameInfo.State;
import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.network.client.exceptions.GameEndedException;
import it.polimi.ingsw.network.client.exceptions.TimeOutException;
import it.polimi.ingsw.constants.ServerConstants;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import com.google.gson.*;
/**
 * This class represents the server that manages a game
 */
public class MatchServer extends UnicastRemoteObject implements RmiServerInterface {
    /**
     * This attribute is a list of the players nicknames
     */
    private final List<String> nicknamesList = new ArrayList<>();
    /**
     * This attribute is a list of clientHandlers
     */
    private final List<ClientHandler> clientsList = new ArrayList<>();
    /**
     * This attribute represents the number of player slots
     */
    private int numPlayers;

    /**
     * This attribute represents the state of the game
     */
    private State state;
    /**
     * This attribute is the game controller
     */
    private GameController gameController;
    /**
     * This attribute is the lobby server
     */
    private final LobbyServer lobby;
    /**
     * This attribute represents the model to be loaded
     */
    private GameModel gameToLoad;
    /**
     * This attribute is true if there is a game to load
     */
    private final boolean toLoadGame;
    /**
     * This attribute is true if the server has to ping the clients
     */
    private boolean toPing = true;
    /**
     * This attribute is true when clients have to be disconnected
     */
    private boolean serverOffline = false;
    /**
     * Set this attribute to true to mute the server
     */
    private final boolean mute = false;


    /**
     * Constructor of the MatchServer class
     * @param numPlayers: number of player slots
     * @param lobby: lobby server
     * @throws RemoteException
     */
    public MatchServer(int numPlayers, LobbyServer lobby) throws RemoteException {
        super();
        this.lobby = lobby;
        this.numPlayers = numPlayers;
        this.state = State.WAITINGFORPLAYERS;
        this.toLoadGame = false;

        if(!mute) System.out.println("MS: New server for "+numPlayers+" players has been created");

        // Creating the ping thread
        this.createPingThread();
    }

    /**
     * Constructor of the MatchServer class from a pre-existing gameModel
     * @param gameModel: the model to load
     * @param lobby: lobby server
     * @throws RemoteException
     */
    public MatchServer(GameModel gameModel, LobbyServer lobby) throws RemoteException{
        super();
        this.lobby = lobby;
        this.gameToLoad = gameModel;
        // infers the numPlayers from playerList
        this.numPlayers = gameModel.getPlayerListCopy().size();
        this.toLoadGame = true;

        if(!mute) System.out.println("MS: New server for "+numPlayers+" players has been created from pre-existing model");
        this.state = State.WAITINGFORPLAYERS;

        // Creating the ping thread
        this.createPingThread();
    }

    /**
     * This method lets the current player make a move
     * @param pos: list of tile positions
     * @param col: column
     * @param nickname: nickname of the player making the move
     * @throws RemoteException
     * @throws InvalidNicknameException
     * @throws InvalidMoveException
     */
    public synchronized void makeMove(List<Position> pos, int col, String nickname) throws RemoteException, InvalidNicknameException, InvalidMoveException, GameEndedException {
        if (this.state == State.ENDGAME) throw new GameEndedException();

        if(!mute) System.out.println("MS: A client made a move");
        try {
            gameController.makeMove(pos, col, nickname);
        } catch (InvalidMoveException e){
            if(!mute) System.out.println("MS: Illegal move: InvalidMoveException");
            throw new InvalidMoveException();
        } catch (InvalidNicknameException e) {
            if(!mute) System.out.println("MS: Illegal move: InvalidNicknameException");
            throw new InvalidNicknameException();
        }

        // Uncomment this line to test for endgame display in the cli
        // this.gameController.forceGameOver();

    }

    /**
     * This method checks if a player is the currentPlayer
     * @param nickname: nickname of the player
     * @return true if the player with nickname 'nickname' is the current player
     */
    private boolean isMyTurn(String nickname){
        int currPlayer;
        switch (state){
            case TURN0 -> currPlayer = 0;
            case TURN1 -> currPlayer = 1;
            case TURN2 -> currPlayer = 2;
            case TURN3 -> currPlayer = 3;
            default -> {return false;}
        }
        return currPlayer == nicknamesList.indexOf(nickname);
    }

    /**
     * This method lets the lobbyServer add a player
     * and starts the game if no player slots are left
     * @param nickname: nickname of the player
     * @param client: a ClientHandler object
     */
    public void addPlayer(String nickname, ClientHandler client){
        nicknamesList.add(nickname);

        synchronized (clientsList) {clientsList.add(client);}
        if(!mute) System.out.println("MS: Added player: "+nickname);

        // we notify the clients to wait only if the players are not all here
        if (this.getFreeSpaces() != 0) this.updateClients(State.WAITINGFORPLAYERS, null);
        if (this.getFreeSpaces() == 0) this.startGame();
    }


    /**
     * This method returns the number of free spaces in the server
     * @return the number of free player slots in the server
     */
    public int getFreeSpaces(){
        return numPlayers - nicknamesList.size();
    }


    /**
     * This method creates a new controller and launches a new thread that pings the clients
     * oss: note that the first update to the server is called when the model is created
     */
    public void startGame(){
        if (!this.toLoadGame) {
            if(!mute) System.out.println("MS: Starting new game");
            // Shuffling the players order
            Collections.shuffle(nicknamesList);
            this.gameController = new GameController(nicknamesList, numPlayers, this);
        }
        else {
            if(!mute) System.out.println("MS: Starting a pre-existing game");
            this.gameController = new GameController(gameToLoad, this);
        }
    }

    /**
     * Method to create a ping thread
     */
    private void createPingThread(){
        Thread t = new Thread(() -> {
            synchronized (nicknamesList) {
                if(!mute) System.out.println("MS: New Ping Thread starting");
                while (toPing) {
                    try {
                        //System.out.println("New Ping Iteration");
                        this.pingClients();
                        nicknamesList.wait(ServerConstants.PING_TIME);

                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    } catch (RemoteException e) {
                        if(!mute) System.out.println("MS: Remote exception from pingClients caused by an rmiClient");
                        this.gracefulDisconnection();
                        break;
                    } catch (TimeOutException e) {
                        if(!mute) System.out.println("MS: TimeOutException from pingClients caused by an tcpClient");
                        this.gracefulDisconnection();
                        break;
                    }
                }
            }
        });
        t.start();
    }

    /**
     * This method updates the server with the new game information from the model
     * @param newState: the new state
     * @param newInfo: the new gameInfo
     */
    public void update(State newState, GameInfo newInfo){
        this.state = newState;
        this.updateClients(newState, newInfo);
    }

    /**
     * This method updates all the clients with the new information
     * @param newState: the new state
     * @param newInfo: the new gameInfo
     */
    private synchronized void updateClients(State newState, GameInfo newInfo){
        // If the disconnection iter has begun we need to stop the clients from receiving updates
        if(!this.serverOffline) {
            if(!mute) System.out.println("MS: Updating clients with newGameInfo and newState");

            // This updates the clients
            try {
                synchronized (clientsList) {
                    for (ClientHandler client : clientsList)
                        client.update(newState, newInfo);
                }
            } catch (RemoteException e) {
                if(!mute) System.out.println("MS: Rmi RemoteException from updateClients");
                //e.printStackTrace();
                this.gracefulDisconnection();
            } catch (TimeOutException e) {
                if(!mute) System.out.println("MS: TimeOutException from updateClients");
                //e.printStackTrace();
                this.gracefulDisconnection();
            }

            // If we reached the end of a game we enter here
            if (this.state == State.ENDGAME){
                if(!mute) System.out.println("MS: The game has ended");
                if(!mute) System.out.println("MS: Terminating Ping Thread");
                this.toPing = false;
                if(!mute) System.out.println("MS: Freeing used nicknames");
                // Here we notify to the lobby to free the player nicknames
                this.lobby.removePlayersAndMatchServerFromLobby(nicknamesList, this);
                // Here we empty the clients list
                synchronized (clientsList) {this.clientsList.clear();}
            }
        }
    }

    /**
     * This method handles the disconnection of one of the clients
     */
    public synchronized void  gracefulDisconnection() {
        if (!serverOffline) {
            if(!mute) System.out.println("MS: A client lost connection");
            if(!mute) System.out.println("MS: Disconnecting all clients...");
            // Beginning of disconnection iter
            this.serverOffline = true;

            if(!mute) System.out.println("MS: Terminating Ping Thread");
            // Here we tell the thread to stop
            this.toPing = false;

            // This updates the clients with the disconnection info

            synchronized (clientsList) {
                for (ClientHandler client : clientsList) {
                    try {
                        client.update(State.GRACEFULDISCONNECTION, null);
                    } catch (RemoteException | TimeOutException e) {
                        // Here we ignore exceptions
                    }
                }
            }


            if(!mute) System.out.println("MS: Initialized graceful disconnection for all clients");

            if(!mute) System.out.println("MS: Forcing gameOver");
            // Here we end the current game
            if (this.gameController != null) this.gameController.forceGameOver();
            // Here we manage the case when a player crashes when the server is not full
            if (this.getFreeSpaces() > 0) this.numPlayers = this.nicknamesList.size();
            if(!mute) System.out.println("MS: Freeing used nicknames");
            // Here we notify to the lobby to free those nicknames
            this.lobby.removePlayersAndMatchServerFromLobby(nicknamesList, this);
            // Here we empty the clients list
            synchronized (clientsList) {this.clientsList.clear();}
        }
    }

    /**
     * This method handles the disconnection of one of the clients
     */
    public synchronized void  killMatchServer() {
        if (!serverOffline) {
            if(!mute) System.out.println("MS: Match Server was killed");
            // Beginning of disconnection iter
            this.serverOffline = true;

            if(!mute) System.out.println("MS: Terminating Ping Thread");
            // Here we tell the thread to stop
            this.toPing = false;

            // This updates the clients with the disconnection info

            synchronized (clientsList) {
                for (ClientHandler client : clientsList) {
                    try {
                        client.update(State.GAMEABORTED, null);
                    } catch (RemoteException | TimeOutException e) {
                        // Here we ignore exceptions
                    }
                }
            }


            if(!mute) System.out.println("MS: Initialized graceful disconnection for all clients");

            if(!mute) System.out.println("MS: Forcing gameOver");
            // Here we end the current game
            if (this.gameController != null) this.gameController.forceGameOver();
            if(!mute) System.out.println("MS: Freeing used nicknames");
            // Here we notify to the lobby to free those nicknames
            this.lobby.removePlayersAndMatchServerFromLobby(nicknamesList, this);
            // Here we empty the clients list
            synchronized (clientsList) {this.clientsList.clear();}
        }
    }

    /**
     * This method check if the clients are alive
     */
    private void pingClients() throws RemoteException, TimeOutException {
        //System.out.println("checking if RmiClient clients are alive...");
        synchronized (clientsList) {
            for (ClientHandler client : clientsList)
                client.isAlive();
        }
    }


    /**
     * This method lets a client message someone specific
     * @param message: the message to send
     * @param speaker : the one who sends the message
     * @param receiver : the one that is supposed to receive the message
     * @throws RemoteException
     */
    public void messageSomeone(String message, String speaker, String receiver) throws RemoteException{
        String messageToSend = speaker + "[Privately]: " + message;
        if(!mute) System.out.println("MS: "+Thread.currentThread() + ": received '" + messageToSend + "'");
        if(!mute) System.out.println("MS: Sending a chat message only to: '"+receiver+"'");

        if(!serverOffline) {
            // This sends the message
            try {
                synchronized (clientsList) {
                    for (ClientHandler client : clientsList) {
                        if (client.name().equals(receiver)) client.receiveMessage(messageToSend);
                        else if (client.name().equals(speaker)) client.receiveMessage(messageToSend);
                    }
                }
            } catch (RemoteException | TimeOutException e) {
                if(!mute) System.out.println("MS: Exception from client.receiveMessage in private chat");
                this.gracefulDisconnection();
            }
        }
    }

    /**
     * This method lets a client broadcast a message
     * @param message: the message to send
     * @param speaker: the one who sends the message
     * @throws RemoteException
     */
    public void messageAll(String message, String speaker) throws RemoteException {
        String messageToSend = speaker + ": " + message;
        if(!mute) System.out.println("MS: "+Thread.currentThread() + ": received '" + messageToSend + "'");
        if(!mute) System.out.println("MS: Sending a chat message to all clients");

        if(!serverOffline) {
            // This sends the message
            try {
                synchronized (clientsList) {
                    for (ClientHandler client : clientsList)
                        client.receiveMessage(messageToSend);
                }
            } catch (RemoteException | TimeOutException e) {
                if(!mute) System.out.println("MS: Exception from client.receiveMessage in public chat");
                this.gracefulDisconnection();
            }
        }

    }

    /**
     * this method responds to the client's ping
     * @return true
     * @throws RemoteException
     */
    @Override
    public boolean isAlive() throws RemoteException {
        if (serverOffline) throw new RemoteException();
        return true;
    }

    /**
     * Getter for nicknamesList
     * @return the nicknamesList
     */
    public List<String> getNicknamesList() {
        return nicknamesList;
    }

    /**
     * Getter for numPlayers
     * @return the number of players
     */
    public int getNumPlayers() {
        return numPlayers;
    }
}
