package it.polimi.ingsw.network.client;

import it.polimi.ingsw.network.client.clientLocks.Lock;
import it.polimi.ingsw.network.client.exceptions.ConnectionError;
import it.polimi.ingsw.network.client.exceptions.TimeOutException;
import it.polimi.ingsw.controller.exceptions.InvalidMoveException;
import it.polimi.ingsw.controller.exceptions.InvalidNicknameException;
import it.polimi.ingsw.gameInfo.GameInfo;
import it.polimi.ingsw.gameInfo.State;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.network.messages.clientMessages.*;
import it.polimi.ingsw.network.messages.serverMessages.*;
import it.polimi.ingsw.network.server.exceptions.AlreadyInGameException;
import it.polimi.ingsw.network.server.exceptions.NonExistentNicknameException;
import it.polimi.ingsw.network.server.constants.ServerConstants;
import it.polimi.ingsw.network.server.exceptions.NoGamesAvailableException;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.view.View;

import java.io.*;
import java.util.List;

import java.net.*;

public class TcpClient implements Client{

    private String nickname;

    private Socket socket;

    private ObjectOutputStream objectOutputStream;

    private View view;

    private ObjectInputStream objectInputStream;

    // Locks

    private Lock actionLock = new Lock();

    private Lock pingThreadConversationLock = new Lock();
    private Lock pingThreadLock = new Lock();

    private Lock manageTcpConversationLock = new Lock();

    private boolean toPing = true;
    private boolean listeningForMessages = true;
    private boolean isClientOnline = true;

    private boolean mute = false;
    // when this flag is true the clients prints only essential messages
    private boolean essential = false;



    public TcpClient(String nickname, View fV, String ipToConnect, Integer lobbyPort) throws InterruptedException, ConnectionError {
        super();
        this.view = fV;
        this.nickname = nickname;

        this.connectToLobbyServer(ipToConnect, lobbyPort);
    }




    private void connectToLobbyServer(String ipToConnect, Integer lobbyPort) throws InterruptedException, ConnectionError {
        while (true) {
            try {
                this.socket = new Socket(ipToConnect, lobbyPort);
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


        // Thread to receive messages from server
        this.createInboundMessagesThread();

        // to fix
        // Thread to ping server
        this.createPingThread();
    }


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

                } catch (IOException e) {
                    if (listeningForMessages){
                        if (!mute && !essential) System.out.println("IOException from InboundMessagesThread");
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


    private void createPingThread(){
        if (!mute && !essential) System.out.println("New Ping Thread starting");
        Thread t = new Thread(() -> {
            synchronized (pingThreadLock) {
                while (toPing) {
                    try {
                    this.manageTcpConversation(pingThreadConversationLock, new IsServerAliveMessage(this.nickname));
                    pingThreadLock.wait(ServerConstants.PING_TIME);
                    } catch (InterruptedException e) {
                        if (!mute && !essential) System.out.println("Interrupted exception from PingThread");
                        this.gracefulDisconnection();
                    } catch (ConnectionError e) {
                        //ignore
                    }
                }

            }
        });
        t.start();
    }


    // This method tries to retrieve the received message from the lock
    public Message manageTcpConversation(Lock lock, Message message) throws ConnectionError {
        // here we create a thread that manages the inbound message
        Thread t = new Thread(()->{
            try {
                synchronized (manageTcpConversationLock) {
                    synchronized (lock) {
                        this.sendTcpMessage(message);
                        lock.wait(ServerConstants.TCP_WAIT_TIME);

                        // With this we notify all the synchronous methods that are waiting
                        this.manageTcpConversationLock.notifyAll();
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
            }
        }
        catch (InterruptedException e) {
            if (!mute && !essential) System.out.println("Interrupted Exception from "+ message.toString());
            this.gracefulDisconnection();
            throw new ConnectionError();
        }

        // here we return the message and check for exceptions
        try {
            return this.retrieveMessageFromLock(lock);
        } catch (TimeOutException e) {
            if (!mute && !essential) System.out.println("Servet too slow to respond to "+ message.toString());
            throw new ConnectionError();
        }
    }


    public Message retrieveMessageFromLock(Lock lock) throws TimeOutException {
        Message newMessage = lock.getMessage();
        lock.reset();
        // if the new Message == null it means that we did not receive a response message from the server
        if (newMessage == null) throw new TimeOutException();
        return newMessage;

    }


    private void sendTcpMessage(Message message){
        if (!message.toString().equals("PingClientMessage"))
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


    private void manageInboundTcpMessages(Message message){
        if (!message.toString().equals("PingClientResponse"))
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
        else if (message instanceof IsServerAliveResponse)
            notifyLockAndSetMessage(pingThreadConversationLock, message);

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



    private void notifyLockAndSetMessage(Lock lock, Message message){
        synchronized (lock){
            lock.setMessage(message);
            lock.notify();
        }
    }



    // synchronous methods

    public synchronized boolean chooseNickname(String nick) throws ConnectionError {
        ChooseNicknameResponse response = (ChooseNicknameResponse) this.manageTcpConversation(actionLock,
                new ChooseNicknameMessage(this.nickname, nick));

        if (response.getResponse()) this.nickname = nick;
        return response.getResponse();
    }


    public synchronized void makeMove(List<Position> pos, int col) throws InvalidMoveException, InvalidNicknameException, ConnectionError {
        MakeMoveResponse response = (MakeMoveResponse) this.manageTcpConversation(actionLock,
                new MakeMoveMessage(this.nickname, pos, col));


        if (response.isInvalidMove()) throw new InvalidMoveException();
        if (response.isInvalidNickname()) throw new InvalidNicknameException();
    }


    public synchronized void createGame(int num) throws NonExistentNicknameException, AlreadyInGameException, ConnectionError {
        CreateGameResponse response = (CreateGameResponse) this.manageTcpConversation(actionLock,
                new CreateGameMessage(this.nickname, num));

        if (response.isNonExistentNickname()) throw new NonExistentNicknameException();
        if (response.isAlreadyInGame()) throw new AlreadyInGameException();
    }


    public synchronized void joinGame() throws NoGamesAvailableException, NonExistentNicknameException, AlreadyInGameException, ConnectionError {
        JoinGameResponse response = (JoinGameResponse) this.manageTcpConversation(actionLock,
                new JoinGameMessage(this.nickname));

        if (response.isAlreadyInGame()) throw new AlreadyInGameException();
        if (response.isNoGamesAvailable()) throw new NoGamesAvailableException();
        if (response.isNonExistentNickname()) throw new NonExistentNicknameException();


    }


    public synchronized void messageSomeone(String chatMessage, String receiver) throws ConnectionError {
        this.manageTcpConversation(actionLock,
                new ChatSomeoneMessage(this.nickname, chatMessage, receiver));
    }


    public synchronized void messageAll(String chatMessage) throws ConnectionError {
        this.manageTcpConversation(actionLock,
                new ChatAllMessage(this.nickname, chatMessage));
    }




    // asynchronous methods

    private void update(State newState, GameInfo newInfo){
        if (newState == State.GRACEFULDISCONNECTION) this.gracefulDisconnection();
        else this.view.update(newState, newInfo);
    }

    private void receiveMessage(String message){
        this.view.displayChatMessage(message);
    }

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

            view.update(State.GRACEFULDISCONNECTION, null);

            this.isClientOnline = false;
        }
    }
}

// TODO
//possibilità: spostare il ping sul server e implementare la disconnessione con i timeout

