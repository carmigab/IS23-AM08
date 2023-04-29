package it.polimi.ingsw.network.client;

import it.polimi.ingsw.network.client.clientLocks.Lock;
import it.polimi.ingsw.network.client.exceptions.ConnectionError;
import it.polimi.ingsw.controller.exceptions.InvalidMoveException;
import it.polimi.ingsw.controller.exceptions.InvalidNicknameException;
import it.polimi.ingsw.gameInfo.GameInfo;
import it.polimi.ingsw.gameInfo.State;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.network.client.exceptions.GameEndedException;
import it.polimi.ingsw.network.messages.clientMessages.*;
import it.polimi.ingsw.network.messages.serverMessages.*;
import it.polimi.ingsw.network.server.exceptions.AlreadyInGameException;
import it.polimi.ingsw.network.server.exceptions.NonExistentNicknameException;
import it.polimi.ingsw.network.server.constants.ServerConstants;
import it.polimi.ingsw.network.server.exceptions.NoGamesAvailableException;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.view.View;

import java.io.*;
import java.rmi.RemoteException;
import java.util.List;

import java.net.*;

public class TcpClient implements Client{
    /**
     * This attribute is the nickname of the player
     */
    private String nickname;

    /**
     * This attribute is the socket on which the communication will occur
     */
    private Socket socket;

    /**
     * This attribute is the object output stream
     */
    private ObjectOutputStream objectOutputStream;

    /**
     * This attribute is the object input stream
     */
    private ObjectInputStream objectInputStream;

    /**
     * This attribute is the View
     */
    private View view;

    /**
     * This attribute is a lock on which all the actions will synchronize
     */
    private Lock actionLock = new Lock();

    /**
     * This attribute is a lock on which the ping will synchronize
     */
    private Lock pingLock = new Lock();

    /**
     * If this flag is true the client has to ping the server
     */
    private boolean toPing = true;
    /**
     * If this flag is true the client will listen for inbound messages
     */
    private boolean listeningForMessages = true;
    /**
     * If this flag is true the client is online
     */
    private boolean isClientOnline = true;

    /**
     * If this flag is true the client is mute
     */
    private boolean mute = false;

    /**
     * If this flag is true the client prints only essential messages
     */
    private boolean essential = true;


    /**
     * Constructor of TcpClient
     * @param nickname: nickname of the player
     * @param view: the view of the player
     * @param serverIp: the ip of the server
     * @param lobbyPort: the port of the lobby server
     * @throws InterruptedException
     * @throws ConnectionError
     */
    public TcpClient(String nickname, View view, String serverIp, Integer lobbyPort) throws InterruptedException, ConnectionError {
        super();
        this.view = view;
        this.nickname = nickname;

        this.connectToLobbyServer(serverIp, lobbyPort);
    }


    /**
     * This method connects the client to the lobby server, if it doesn't find a server it waits for 5 seconds
     * @param serverIp: the ip of the server
     * @param lobbyPort: the port of the server
     * @throws InterruptedException
     * @throws ConnectionError
     */
    private void connectToLobbyServer(String serverIp, Integer lobbyPort) throws InterruptedException, ConnectionError {
        while (true) {
            try {
                this.socket = new Socket(serverIp, lobbyPort);
                if (!mute) System.out.println("Tcp connection established");
                break;
            } catch (IOException e) {
                if (!mute) System.out.println("Server not found");
                Thread.sleep(5000);
            }
        }

        // Opening output streams
        try {
            if (!mute && !essential) System.out.println("Opening Output Streams");
            this.objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            if (!mute && !essential) System.out.println("Failed opening Output Streams");
            this.gracefulDisconnection();
            throw new ConnectionError();
        }

        // Here we set a timeout for the socket
        try {
            this.socket.setSoTimeout(ServerConstants.PING_TIME+ServerConstants.TCP_WAIT_TIME+1000);
        } catch (SocketException e) {
            if(!mute) System.out.println("SocketException from setSoTimeout");
            this.gracefulDisconnection();
        }

        // Thread to receive messages from server
        this.createInboundMessagesThread();

        // Thread to ping server
        this.createPingThread();
    }


    /**
     * This method creates a thread to receive inbound messages
     */
    private void createInboundMessagesThread(){
        if (!mute && !essential) System.out.println("New MessagesListener Thread starting");
        if (!mute && !essential) System.out.println("Opening Input Streams");
        Thread t = new Thread(() -> {
            try {
                this.objectInputStream  = new ObjectInputStream(socket.getInputStream());
            } catch (IOException e) {
                if (!mute && !essential) System.out.println("Failed opening Input Streams");
                this.gracefulDisconnection();
            }

            while(listeningForMessages){
                try {
                    Message message = (Message) objectInputStream.readObject();
                    this.manageInboundTcpMessages(message);

                } catch (SocketTimeoutException e ) {
                    if (listeningForMessages) {
                        if (!mute && !essential) System.out.println("SocketTimeout Exception InboundMessagesThread");
                        this.gracefulDisconnection();
                    }
                } catch (IOException e) {
                    if (listeningForMessages){
                        if (!mute && !essential) System.out.println("IOException from InboundMessagesThread");
                        // e.printStackTrace();
                        this.gracefulDisconnection();
                    }
                } catch (ClassNotFoundException e) {
                    if (listeningForMessages){
                        if (!mute && !essential) System.out.println("ClassNotFoundException from InboundMessagesThread");
                        this.gracefulDisconnection();
                    }
                }
            }

        });
        t.start();

    }


    /**
     * This method creates a thread to ping the server
     */
    private void createPingThread(){
        if (!mute && !essential) System.out.println("New Ping Thread starting");
        Thread t = new Thread(() -> {
            Lock pingThreadLock = new Lock();
            synchronized (pingThreadLock) {
                while (toPing) {
                    try {
                        this.manageTcpConversation(pingLock, new IsServerAliveMessage(this.nickname));
                        pingThreadLock.wait(ServerConstants.PING_TIME);
                    } catch (InterruptedException e) {
                        if (!mute && !essential) System.out.println("Interrupted exception from PingThread");
                        this.gracefulDisconnection();
                    } catch (ConnectionError e) {
                        if (!mute && !essential) System.out.println("Connection error from PingThread");
                        this.gracefulDisconnection();
                    }
                }

            }
        });
        t.start();
    }


    /**
     * This method manages the whole tcp conversation by creating thread to send a message and waiting for a response
     * This method waits only for a finite amount of time and then throws an exception if no response has arrived
     * @param lock
     * @param message
     * @return
     * @throws ConnectionError
     */
    private Message manageTcpConversation(Lock lock, Message message) throws ConnectionError {
        Lock manageTcpConversationLock = new Lock();

        // here we create a thread that manages the inbound message
        Thread t = new Thread(()->{
            try {
                synchronized (manageTcpConversationLock) {
                    synchronized (lock) {
                        this.sendTcpMessage(message);
                        // Version 1: infinite wait
                        //while (lock.toWait()) lock.wait();
                        // Version 2: finite wait
                        lock.wait(ServerConstants.TCP_WAIT_TIME);

                        manageTcpConversationLock.notifyAll();
                    }
                }
            } catch (InterruptedException e) {
                if (!mute && !essential) System.out.println("Interrupted Exception from manageTcpConversation");
                this.gracefulDisconnection();
            }


        });

        // Here we start the thread and release the lock
        try {
            synchronized (manageTcpConversationLock) {
                t.start();
                manageTcpConversationLock.wait();
                return this.retrieveMessageFromLock(lock);
            }
        }
         catch (ConnectionError e) {
            if (!mute && !essential) System.out.println("Connection error from "+ message.toString());
            this.gracefulDisconnection();
            throw new ConnectionError();
        } catch (InterruptedException e) {
            if (!mute && !essential) System.out.println("Interrupted Exception from "+ message.toString());
            this.gracefulDisconnection();
            throw new ConnectionError();
        }
    }


    /**
     * This method retrieves a message from a lock
     * @param lock: the lock
     * @return: the message
     * @throws ConnectionError
     */
    private Message retrieveMessageFromLock(Lock lock) throws ConnectionError {
        synchronized (lock) {
            Message newMessage = lock.getMessage();
            if (lock.isDisconnection()) throw new ConnectionError();
            // Version 2
            if (newMessage == null) throw new ConnectionError();
            lock.reset();
            return newMessage;
        }

    }

    /**
     * This method notifies the lock that a message has arrived and memorizes the message in the lock
     * @param lock: the lock
     * @param message: the message
     */
    private void notifyLockAndSetMessage(Lock lock, Message message){
        synchronized (lock){
            lock.setMessage(message);
            lock.setToWait(false);
            lock.notify();
        }
    }


    /**
     * This method sends a message over the socket
     * @param message: the message to send
     */
    private void sendTcpMessage(Message message){
        //if (!message.toString().equals("PingClientMessage"))
            if (!mute && !essential) System.out.println("Sending "+message.toString() +" to Server socket");
        try {
            this.objectOutputStream.writeObject(message);
            this.objectOutputStream.flush();
            //this.objectOutputStream.reset();
        } catch (IOException e) {
            if (!mute && !essential) System.out.println("An error occurred while trying to send a message to the server");
            this.gracefulDisconnection();
        }
    }


    /**
     * This method manages the reception of a message
     * @param message: the message
     */
    private void manageInboundTcpMessages(Message message){
        //if (!message.toString().equals("PingClientResponse"))
            if (!mute && !essential) System.out.println("Received a "+message.toString()+" from "+message.sender());
        // synchronous messages
        if (message instanceof ChatAllResponse)
            notifyLockAndSetMessage(actionLock, message);
        else if (message instanceof ChatSomeoneResponse)
            notifyLockAndSetMessage(actionLock, message);
        else if (message instanceof ChooseNicknameResponse)
            notifyLockAndSetMessage(actionLock, message);
        else if (message instanceof CreateGameResponse)
            notifyLockAndSetMessage(actionLock, message);
        else if (message instanceof JoinGameResponse)
            notifyLockAndSetMessage(actionLock, message);
        else if (message instanceof MakeMoveResponse)
            notifyLockAndSetMessage(actionLock, message);
        // The client keeps the heartbeat the server only responds
        else if (message instanceof IsServerAliveResponse)
            notifyLockAndSetMessage(pingLock, message);

        // asynchronous messages
        else if (message instanceof ChatReceiveMessage){
            ChatReceiveMessage m = (ChatReceiveMessage) message;
            this.receiveMessage(m.getChatMessage());
        }
        else if (message instanceof UpdateMessage) {
            UpdateMessage m = (UpdateMessage) message;
            this.update(m.getNewState(), m.getNewInfo());

        }
    }


    // synchronous methods

    /**
     * This method lets the player choose his nickname
     * @param nick: the nickname of the player
     * @return true if nickname is available
     * @throws ConnectionError
     */
    public synchronized boolean chooseNickname(String nick) throws ConnectionError {
        ChooseNicknameResponse response = (ChooseNicknameResponse) this.manageTcpConversation(actionLock,
                new ChooseNicknameMessage(this.nickname, nick));

        if (response.getResponse()) this.nickname = nick;
        return response.getResponse();
    }

    /**
     * This method lets the player make a move
     * @param pos : a List of positions
     * @param col : the column of the shelf
     * @throws InvalidNicknameException
     * @throws InvalidMoveException
     * @throws ConnectionError
     * @throws GameEndedException
     */
    public synchronized void makeMove(List<Position> pos, int col) throws InvalidMoveException, InvalidNicknameException, ConnectionError, GameEndedException {
        MakeMoveResponse response = (MakeMoveResponse) this.manageTcpConversation(actionLock,
                new MakeMoveMessage(this.nickname, pos, col));


        if (response.isGameEnded()) throw new GameEndedException();
        if (response.isInvalidMove()) throw new InvalidMoveException();
        if (response.isInvalidNickname()) throw new InvalidNicknameException();
    }

    /**
     * This method lets a player create a game and choose the available player slots
     * @param num : player slots
     * @throws NonExistentNicknameException
     * @throws AlreadyInGameException
     * @throws ConnectionError
     */
    public synchronized void createGame(int num) throws NonExistentNicknameException, AlreadyInGameException, ConnectionError {
        CreateGameResponse response = (CreateGameResponse) this.manageTcpConversation(actionLock,
                new CreateGameMessage(this.nickname, num));

        if (response.isNonExistentNickname()) throw new NonExistentNicknameException();
        if (response.isAlreadyInGame()) throw new AlreadyInGameException();
    }

    /**
     * This method lets a player join a game
     * @throws NoGamesAvailableException
     * @throws NonExistentNicknameException
     * @throws AlreadyInGameException
     * @throws ConnectionError
     */
    public synchronized void joinGame() throws NoGamesAvailableException, NonExistentNicknameException, AlreadyInGameException, ConnectionError {
        JoinGameResponse response = (JoinGameResponse) this.manageTcpConversation(actionLock,
                new JoinGameMessage(this.nickname));

        if (response.isAlreadyInGame()) throw new AlreadyInGameException();
        if (response.isNoGamesAvailable()) throw new NoGamesAvailableException();
        if (response.isNonExistentNickname()) throw new NonExistentNicknameException();


    }

    /**
     * This method lets the client send a message privately to someone
     * @param chatMessage: the message
     * @param receiver : the one that is supposed to receive the message
     * @throws ConnectionError
     */
    public synchronized void messageSomeone(String chatMessage, String receiver) throws ConnectionError {
        this.manageTcpConversation(actionLock,
                new ChatSomeoneMessage(this.nickname, chatMessage, receiver));
    }

    /**
     * This method lets the client send a message to every other client connected to the game
     * @param chatMessage: the message
     * @throws ConnectionError
     */
    public synchronized void messageAll(String chatMessage) throws ConnectionError {
        this.manageTcpConversation(actionLock,
                new ChatAllMessage(this.nickname, chatMessage));
    }


    // asynchronous methods

    /**
     * This method updates the view with new information
     * @param newState : the new state of the game
     * @param newInfo : the new info for the view
     */
    private void update(State newState, GameInfo newInfo){
        if (newState == State.GRACEFULDISCONNECTION) this.gracefulDisconnection();
        else this.view.update(newState, newInfo);
    }

    /**
     * This method notifies the view that a chat message has arrived
     * @param message: the message
     */
    private void receiveMessage(String message){
        this.view.displayChatMessage(message);
    }

    /**
     * This method manages the disconnection by setting the flags toPing, listeningForMessages, isClientOnline to false,
     * closing the socket and updating the view
     */
    private synchronized void gracefulDisconnection(){
        if (isClientOnline) {
            if (!mute && essential) System.out.println("Connection error");
            if (!mute) System.out.println("Initializing graceful disconnection");
            if (!mute && !essential) System.out.println("Terminating Ping thread");
            this.toPing = false;
            if (!mute && !essential) System.out.println("Terminating messageListener");
            this.listeningForMessages = false;

            try {
                if (!mute && !essential) System.out.println("Closing socket");
                this.socket.close();
            } catch (IOException e) {
                if (!mute && !essential) System.out.println("Error while closing socket");
            }

            //Notifying lock to stop
            synchronized (actionLock) {
                this.actionLock.setDisconnection(true);
                this.actionLock.notifyAll();
            }
            synchronized (pingLock) {
                this.pingLock.setDisconnection(true);
                this.pingLock.notifyAll();
            }

            view.update(State.GRACEFULDISCONNECTION, null);

            this.isClientOnline = false;
        }
    }
}




