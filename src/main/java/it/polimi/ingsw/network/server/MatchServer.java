package it.polimi.ingsw.network.server;

import it.polimi.ingsw.network.client.RmiClientInterface;
import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.exceptions.InvalidNicknameException;
import it.polimi.ingsw.controller.exceptions.InvalidMoveException;
import it.polimi.ingsw.gameInfo.GameInfo;
import it.polimi.ingsw.gameInfo.State;
import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.Position;
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
    private List<RmiClientInterface> rmiClientsList = new ArrayList<>();
    private List<TcpClientHandler> tcpClientsList = new ArrayList<>();
    private int numPlayers;
    private State state;

    private GameController gameController;

    private boolean toLoadGame;
    private GameModel gameToLoad;

    private LobbyServer lobby;

    private boolean toPing = true;
    private boolean disconnectingClients = false;




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

        System.out.println("New server for "+numPlayers+" players has been created");
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


        System.out.println("New server for "+numPlayers+" players has been created from pre-existing model");
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
    public void makeMove(List<Position> pos, int col, String nickname) throws RemoteException, InvalidNicknameException, InvalidMoveException {
        if(!isMyTurn(nickname)) {
            System.out.println("Illegal move: InvalidNicknameException");
            throw new InvalidNicknameException();
        }
        try {
            gameController.makeMove(pos, col, nickname);
        } catch (InvalidMoveException e){
            System.out.println("Illegal move: InvalidMoveException");
            throw new InvalidMoveException();
        }

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
     * @param rmiClient
     */
    public void addPlayer(String nickname, RmiClientInterface rmiClient){
        nicknamesList.add(nickname);
        rmiClientsList.add(rmiClient);
        System.out.println("Added player: "+nickname);

        this.updateClients(State.WAITINGFORPLAYERS, null);
        if (this.getFreeSpaces() == 0) this.startGame();
    }


    public void addPlayer(String nickname, TcpClientHandler tcpClient){
        nicknamesList.add(nickname);
        tcpClientsList.add(tcpClient);
        System.out.println("Added player: "+nickname);

        this.updateClients(State.WAITINGFORPLAYERS, null);
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
            System.out.println("Starting new game");
            this.gameController = new GameController(nicknamesList, numPlayers, this);
        }
        else {
            System.out.println("Starting a pre-existing game");
            this.gameController = new GameController(gameToLoad, this);
        }

        Thread t = new Thread(() -> {
            synchronized (nicknamesList) {
                System.out.println("New Ping Thread starting");
                while (toPing) {
                    try {
                        //System.out.println("New Ping Iteration");
                        this.pingClients();
                        nicknamesList.wait(ServerConstants.PING_TIME);

                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    } catch (RemoteException e) {
                        System.out.println("Remote exception from pingClients caused by an rmiClient");
                        this.gracefulDisconnection();
                        break;
                    } catch (TimeOutException e) {
                        System.out.println("TimeOutException from pingClients caused by an tcpClient");
                        this.gracefulDisconnection();
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
    private void updateClients(State newState, GameInfo newInfo){
        // If the disconnection iter has begun we need to stop the clients from receiving updates
        if(!this.disconnectingClients) {
            System.out.println("Updating clients with newGameInfo and newState");

            // This updates the rmi clients
            Iterator<RmiClientInterface> rmiIter = rmiClientsList.iterator();
            while (rmiIter.hasNext()) {
                RmiClientInterface client = rmiIter.next();
                try {
                    client.update(newState, newInfo);
                } catch (RemoteException e) {
                    // This flag is needed to stop the program to go on a loop
                    System.out.println("Remote exception from client.update");
                    //e.printStackTrace();
                    this.gracefulDisconnection();
                    break;
                }
            }

            // This updates the tcp clients
            Iterator<TcpClientHandler> tcpIter = tcpClientsList.iterator();
            while (tcpIter.hasNext()) {
                TcpClientHandler client = tcpIter.next();
                try {
                    client.update(newState, newInfo);
                } catch (TimeOutException e) {
                    // This flag is needed to stop the program to go on a loop
                    System.out.println("TimeOutException from client.update");
                    //e.printStackTrace();
                    this.gracefulDisconnection();
                    break;
                }
            }

            if (this.state == State.ENDGAME){
                System.out.println("The game has ended");
                // Here we tell the thread to stop
                System.out.println("Terminating Ping Thread");
                this.toPing = false;
                // Here we notify to the lobby to free the player nicknames
                System.out.println("Freeing used nicknames");
                this.lobby.removePlayersFromGame(nicknamesList);
                // Here we empty the clients list
                this.rmiClientsList.clear();
            }
        }

    }

    /**
     * This method signal the clients that someone has crashed
     */
    public void gracefulDisconnection() {
        // Here we tell the thread to stop
        System.out.println("Terminating Ping Thread");
        this.toPing = false;
        System.out.println("A client lost connection");
        System.out.println("Disconnecting all clients...");

        // Beginning of disconnection iter
        this.disconnectingClients = true;

        // This updates the rmi clients
        Iterator<RmiClientInterface> rmiIter = rmiClientsList.iterator();
        while (rmiIter.hasNext()) {
            RmiClientInterface client = rmiIter.next();
            //System.out.println(client);
            try {
                client.update(State.GRACEFULDISCONNECTION, null);
            } catch (RemoteException e) {
                //System.out.println("Remote Exception");
                continue;
            }
        }

        // This updates the tcp clients
        Iterator<TcpClientHandler> tcpIter = tcpClientsList.iterator();
        while (tcpIter.hasNext()) {
            TcpClientHandler client = tcpIter.next();
            //System.out.println(client);
            try {
                client.update(State.GRACEFULDISCONNECTION, null);
            } catch (TimeOutException e) {
                //System.out.println("Remote Exception");
                continue;
            }
        }

        System.out.println("Initialized graceful disconnection for all clients");

        // Here we end the current game
        System.out.println("Forcing gameOver");
        this.gameController.forceGameOver();
        // Here we notify to the lobby to free those nicknames
        System.out.println("Freeing used nicknames");
        this.lobby.removePlayersFromGame(nicknamesList);
        // Here we empty the clients list
        this.rmiClientsList.clear();
    }

    /**
     * This method check if the clients are alive
     */
    private void pingClients() throws RemoteException, TimeOutException {
        //System.out.println("checking if RmiClient clients are alive...");

        // This updates the rmi clients
        Iterator<RmiClientInterface> rmiIter = rmiClientsList.iterator();
        while (rmiIter.hasNext()) {
            RmiClientInterface client = rmiIter.next();
            try {
                client.isAlive();
                //System.out.println("Ping");
            } catch (RemoteException e) {
                throw new RemoteException();
            }
        }

        // This pings the tcp clients
        Iterator<TcpClientHandler> tcpIter = tcpClientsList.iterator();
        while (tcpIter.hasNext()) {
            TcpClientHandler client = tcpIter.next();
            try {
                client.isAlive();
                //System.out.println("Ping");
            } catch (TimeOutException e) {
                throw new TimeOutException();
            }
        }
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
        System.out.println(Thread.currentThread() + ": received '" + messageToSend + "'");
        System.out.println("Sending message only to: '"+receiver+"'");

        // This updates the rmi clients
        Iterator<RmiClientInterface> rmiIter = rmiClientsList.iterator();
        while (rmiIter.hasNext()) {
            RmiClientInterface client = rmiIter.next();
            try {
                if (client.name().equals(receiver)) client.receiveMessage(messageToSend);
                else if(client.name().equals(speaker)) client.receiveMessage(messageToSend);

            } catch (RemoteException e) {
                System.out.println("Remote exception from client.receiveMessage in private chat");
                this.gracefulDisconnection();
            }
        }

        // This updates the tcp clients
        Iterator<TcpClientHandler> tcpIter = tcpClientsList.iterator();
        while (tcpIter.hasNext()) {
            TcpClientHandler client = tcpIter.next();
            try {
                if (client.name().equals(receiver)) client.receiveMessage(messageToSend);
                else if(client.name().equals(speaker)) client.receiveMessage(messageToSend);

            } catch (TimeOutException e) {
                System.out.println("TimeOutException from client.receiveMessage in private chat");
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
        System.out.println(Thread.currentThread() + ": received '" + messageToSend + "'");
        System.out.println("Sending message to all clients");

        // This updates the rmi clients
        Iterator<RmiClientInterface> rmiIter = rmiClientsList.iterator();
        while (rmiIter.hasNext()) {
            RmiClientInterface client = rmiIter.next();
            try {
                client.receiveMessage(messageToSend);
            } catch (RemoteException e) {
                System.out.println("Remote exception from client.receiveMessage in public chat");
                this.gracefulDisconnection();
            }
        }

        // This updates the tcp clients
        Iterator<TcpClientHandler> tcpIter = tcpClientsList.iterator();
        while (tcpIter.hasNext()) {
            TcpClientHandler client = tcpIter.next();
            try {
                client.receiveMessage(messageToSend);
            } catch (TimeOutException e) {
                System.out.println("TimeOutException from client.receiveMessage in public chat");
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
