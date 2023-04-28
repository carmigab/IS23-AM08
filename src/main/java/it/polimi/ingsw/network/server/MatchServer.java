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
import it.polimi.ingsw.network.server.constants.ServerConstants;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MatchServer extends UnicastRemoteObject implements RmiServerInterface {

    // This list represents also the playing order
    private List<String> nicknamesList = new ArrayList<>();

    private List<ClientHandler> clientsList = new ArrayList<>();
    private int numPlayers;
    private State state;

    private GameController gameController;

    private LobbyServer lobby;

    private GameModel gameToLoad;
    private boolean toLoadGame;

    private boolean toPing = true;
    private boolean clientsDisconnected = false;

    private boolean mute = false;




    /**
     * Constructor of the RmiServer class
     * @param numPlayers
     * @throws RemoteException
     */
    public MatchServer(int numPlayers, LobbyServer lobby) throws RemoteException {
        super();
        this.lobby = lobby;
        this.numPlayers = numPlayers;
        this.state = State.WAITINGFORPLAYERS;
        this.toLoadGame = false;

        if(!mute) System.out.println("MS: New server for "+numPlayers+" players has been created");
    }

    /**
     * Constructor of the rmiServer class from a pre-existing gameModel
     * @param gameModel: the model to load
     * @throws RemoteException
     */
    public MatchServer(GameModel gameModel, LobbyServer lobby) throws RemoteException{
        super();
        // infers the numPlayers from playerList
        this.lobby = lobby;
        this.numPlayers = gameModel.getPlayerListCopy().size();
        this.toLoadGame = true;
        this.gameToLoad = gameModel;


        if(!mute) System.out.println("MS: New server for "+numPlayers+" players has been created from pre-existing model");
        this.state = State.WAITINGFORPLAYERS;
    }

    /**
     * This method let the current player make a move
     * @param pos
     * @param col
     * @param nickname
     * @throws RemoteException
     * @throws InvalidNicknameException
     * @throws InvalidMoveException
     */
    public void makeMove(List<Position> pos, int col, String nickname) throws RemoteException, InvalidNicknameException, InvalidMoveException, GameEndedException {
        if (this.state == State.ENDGAME) throw new GameEndedException();
        if(!isMyTurn(nickname)) {
            if(!mute) System.out.println("MS: Illegal move: InvalidNicknameException");
            throw new InvalidNicknameException();
        }
        try {
            gameController.makeMove(pos, col, nickname);
        } catch (InvalidMoveException e){
            if(!mute) System.out.println("MS: Illegal move: InvalidMoveException");
            throw new InvalidMoveException();
        }

        // Uncomment this line to test for endgame display in the cli
        // this.gameController.forceGameOver();
    }

    /**
     * This method checks if a player is the currentPlayer
     * @param nickname
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
     * This method lets the lobbyServer add a player and his client
     * and starts the game is no player slots are left
     * @param nickname
     * @param client
     */
    public void addPlayer(String nickname, ClientHandler client){
        nicknamesList.add(nickname);
        clientsList.add(client);
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
            this.gameController = new GameController(nicknamesList, numPlayers, this);
        }
        else {
            if(!mute) System.out.println("MS: Starting a pre-existing game");
            this.gameController = new GameController(gameToLoad, this);
        }

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
     * This method updates the server with the new information from the model
     * @param newState
     * @param newInfo
     */
    public void update(State newState, GameInfo newInfo){
        this.state = newState;
        this.updateClients(newState, newInfo);
    }

    /**
     * This method updates the clients
      * @param newState
     * @param newInfo
     */
    private synchronized void updateClients(State newState, GameInfo newInfo){
        // If the disconnection iter has begun we need to stop the clients from receiving updates
        if(!this.clientsDisconnected) {
            if(!mute) System.out.println("MS: Updating clients with newGameInfo and newState");

            // This updates the clients
            try {
                for (ClientHandler client : clientsList)
                    client.update(newState, newInfo);
            } catch (RemoteException | TimeOutException e) {
                // This flag is needed to stop the program to go on a loop
                if(!mute) System.out.println("MS: Remote exception from client.update");
                //e.printStackTrace();
                this.gracefulDisconnection();
            }



            if (this.state == State.ENDGAME){
                if(!mute) System.out.println("MS: The game has ended");
                // Here we tell the thread to stop
                if(!mute) System.out.println("MS: Terminating Ping Thread");
                this.toPing = false;
                // Here we notify to the lobby to free the player nicknames
                if(!mute) System.out.println("MS: Freeing used nicknames");
                this.lobby.removePlayersFromLobby(nicknamesList);
                // Here we empty the clients list
                this.clientsList.clear();
            }
        }
    }

    /**
     * This method signal the clients that someone has crashed
     */
    public synchronized void  gracefulDisconnection() {
        if (!clientsDisconnected) {
            if(!mute) System.out.println("MS: A client lost connection");
            if(!mute) System.out.println("MS: Disconnecting all clients...");
            // Beginning of disconnection iter
            this.clientsDisconnected = true;

            // Here we tell the thread to stop
            if(!mute) System.out.println("MS: Terminating Ping Thread");
            this.toPing = false;

            // This updates the clients
            try {
                for (ClientHandler client : clientsList)
                    client.update(State.GRACEFULDISCONNECTION, null);
            } catch (RemoteException | TimeOutException e) {
                //System.out.println("Remote Exception");
            }


            if(!mute) System.out.println("MS: Initialized graceful disconnection for all clients");

            // Here we end the current game
            if(!mute) System.out.println("MS: Forcing gameOver");
            if (this.gameController != null) this.gameController.forceGameOver();
            // Here we notify to the lobby to free those nicknames
            if(!mute) System.out.println("MS: Freeing used nicknames");
            this.lobby.removePlayersFromLobby(nicknamesList);
            // Here we empty the clients list
            this.clientsList.clear();
        }
    }

    /**
     * This method check if the clients are alive
     */
    private void pingClients() throws RemoteException, TimeOutException {
        //System.out.println("checking if RmiClient clients are alive...");

        // This updates the clients
        for (ClientHandler client : clientsList)
            client.isAlive();
    }


    /**
     * This method lets a client message to someone specific
     * @param message
     * @param speaker : the one who sends the message
     * @param receiver : the one that is supposed to receive the message
     * @throws RemoteException
     */
    public void messageSomeone(String message, String speaker, String receiver) throws RemoteException{
        String messageToSend = speaker + "[Privately]: " + message;
        if(!mute) System.out.println("MS: "+Thread.currentThread() + ": received '" + messageToSend + "'");
        if(!mute) System.out.println("MS: Sending message only to: '"+receiver+"'");


        if(!clientsDisconnected) {
            // This updates the clients
            try {
                for (ClientHandler client : clientsList) {
                    if (client.name().equals(receiver)) client.receiveMessage(messageToSend);
                    else if (client.name().equals(speaker)) client.receiveMessage(messageToSend);
                }
            } catch (RemoteException | TimeOutException e) {
                if(!mute) System.out.println("MS: Remote exception from client.receiveMessage in private chat");
                this.gracefulDisconnection();
            }
        }
    }

    /**
     * This method lets a client broadcast a message
     * @param message
     * @param speaker : the one who sends the message
     * @throws RemoteException
     */
    public void messageAll(String message, String speaker) throws RemoteException {
        String messageToSend = speaker + ": " + message;
        if(!mute) System.out.println("MS: "+Thread.currentThread() + ": received '" + messageToSend + "'");
        if(!mute) System.out.println("MS: Sending message to all clients");


        if(!clientsDisconnected) {
            // This updates the rmi clients
            try {
                for (ClientHandler client : clientsList)
                    client.receiveMessage(messageToSend);
            } catch (RemoteException | TimeOutException e) {
                if(!mute) System.out.println("MS: Remote exception from client.receiveMessage in public chat");
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
        return true;
    }


}
