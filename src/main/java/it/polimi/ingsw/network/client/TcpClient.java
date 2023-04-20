package it.polimi.ingsw.network.client;

import it.polimi.ingsw.network.client.clientLocks.Lock;
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

    private OutputStream outputStream;
    private ObjectOutputStream objectOutputStream;

    private View view;

    private InputStream inputStream;

    private ObjectInputStream objectInputStream;

    // Locks
    private Lock chooseNicknameLock = new Lock();
    private Lock makeMoveLock = new Lock();
    private Lock createGameLock = new Lock();
    private Lock joinGameLock = new Lock();
    private Lock chatSomeoneLock = new Lock();
    private Lock chatAllLock = new Lock();
    private Lock messagesListenerLock = new Lock();
    private Lock pingThreadLock = new Lock();

    private boolean toPing = true;
    private boolean listeningForMessages = true;



    public TcpClient(String nickname, View fV, String ipToConnect, Integer lobbyPort) throws InterruptedException {
        super();
        this.view = fV;
        this.nickname = nickname;

        this.connectToLobbyServer(ipToConnect, lobbyPort);
    }




    private void connectToLobbyServer(String ipToConnect, Integer lobbyPort) throws InterruptedException {
        while (true) {
            try {
                this.socket = new Socket(ipToConnect, lobbyPort);
                System.out.println("Tcp connection established");

                this.outputStream = socket.getOutputStream();
                this.objectOutputStream = new ObjectOutputStream(outputStream);

                this.inputStream = socket.getInputStream();
                this.objectInputStream  = new ObjectInputStream(inputStream);
                break;
            } catch (IOException e) {
                System.out.println("Server not found");
                Thread.sleep(5000);
            }
        }

        // Thread to receive messages from server
        this.createInboundMessagesThread();

        // Thread to ping server
        this.createPingThread();
    }


    private void createInboundMessagesThread(){
        Thread t = new Thread(() -> {
            synchronized (messagesListenerLock){
                System.out.println("New MessagesListener Thread starting");
                while(listeningForMessages){
                    try {
                        Message message = (Message) objectInputStream.readObject();
                        this.manageInboundTcpMessages(message);

                    } catch (IOException e) {
                        if (listeningForMessages){
                            System.out.println("IOException from InboundMessagesThread");
                            this.gracefulDisconnection();
                        }
                    } catch (ClassNotFoundException e) {
                        if (listeningForMessages){
                            System.out.println("ClassNotFoundException from InboundMessagesThread");
                            this.gracefulDisconnection();
                        }
                    }
                }

            }
        });
        t.start();

    }


    private void createPingThread(){
        Thread t = new Thread(() -> {
            synchronized (pingThreadLock) {
                System.out.println("New Ping Thread starting");
                while (toPing) {
                    try {
                    this.manageTcpConversation(pingThreadLock, new PingMessage(this.nickname));
                    pingThreadLock.wait(ServerConstants.PING_TIME);
                    } catch (InterruptedException e) {
                        System.out.println("Interrupted exception from PingThread");
                        this.gracefulDisconnection();
                    }
                }

            }
        });
        t.start();
    }


    // This method tries to retrieve the received message from the lock
    public Message manageTcpConversation(Lock lock, Message message) {
        try {
            synchronized (lock) {
                this.sendTcpMessage(message);
                lock.wait(ServerConstants.TCP_WAIT_TIME);
                // we try to retrieve the message from the lock
                Message newMessage = lock.getMessage();
                lock.reset();
                // if the new Message == null it means that we did not receive a response message from the server
                if (newMessage == null) throw new TimeOutException();
                return newMessage;
            }
        } catch (InterruptedException e) {
            System.out.println("Interrupted Exception");
            this.gracefulDisconnection();
        } catch (TimeOutException e) {
            System.out.println("Tcp server too slow to respond");
            this.gracefulDisconnection();
        }

        // the code should never arrive here
        this.gracefulDisconnection();
        System.out.println("Should never arrive here");
        return new Message("Error");
    }


    private void sendTcpMessage(Message message){
        System.out.println("Sending message to Server socket");
        try {
            this.objectOutputStream.writeObject(message);
        } catch (IOException e) {
            System.out.println("An error occurred while trying to send a message to the server");
            this.gracefulDisconnection();
        }
    }


    private void manageInboundTcpMessages(Message message){
        // synchronous messages
        if (message instanceof ChatAllResponse)
            notifyLockAndSetMessage(chatAllLock, message);
        else if (message instanceof ChatSomeoneResponse)
            notifyLockAndSetMessage(chatSomeoneLock, message);
        else if (message instanceof ChooseNicknameResponse)
            notifyLockAndSetMessage(chooseNicknameLock, message);
        else if (message instanceof CreateGameResponse)
            notifyLockAndSetMessage(createGameLock, message);
        else if (message instanceof JoinGameResponse)
            notifyLockAndSetMessage(joinGameLock, message);
        else if (message instanceof MakeMoveResponse)
            notifyLockAndSetMessage(makeMoveLock, message);
        else if (message instanceof PingResponse)
            notifyLockAndSetMessage(pingThreadLock, message);

        // asynchronous messages
        else if (message instanceof ChatReceiveMessage){
            this.sendTcpMessage(new ChatReceiveResponse(this.nickname));
            ChatReceiveMessage m = (ChatReceiveMessage) message;
            this.receiveMessage(m.getChatMessage());
        }
        else if (message instanceof IsClientAlive) {
            this.sendTcpMessage(new IsClientAliveResponse(this.nickname));
        }
        else if (message instanceof UpdateMessage) {
            this.sendTcpMessage(new UpdateResponse(this.nickname));
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

    public boolean chooseNickname(String nick){
        ChooseNicknameResponse response = (ChooseNicknameResponse) this.manageTcpConversation(chooseNicknameLock,
                new ChooseNicknameMessage(nick));
        return response.getResponse();
    }


    public void makeMove(List<Position> pos, int col) throws InvalidMoveException, InvalidNicknameException{
        MakeMoveResponse response = (MakeMoveResponse) this.manageTcpConversation(makeMoveLock,
                new MakeMoveMessage(this.nickname, pos, col));
        if (response.isInvalidMove()) throw new InvalidMoveException();
        if (response.isInvalidNickname()) throw new InvalidNicknameException();
    }


    public void createGame(int num) throws NonExistentNicknameException, AlreadyInGameException {
        CreateGameResponse response = (CreateGameResponse) this.manageTcpConversation(createGameLock,
                new CreateGameMessage(this.nickname, num));
        if (response.isNonExistentNickname()) throw new NonExistentNicknameException();
        if (response.isAlreadyInGame()) throw new AlreadyInGameException();
    }


    public void joinGame() throws NoGamesAvailableException, NonExistentNicknameException, AlreadyInGameException{
        JoinGameResponse response = (JoinGameResponse) this.manageTcpConversation(joinGameLock,
                new JoinGameMessage(this.nickname));
        if (response.isAlreadyInGame()) throw new AlreadyInGameException();
        if (response.isNoGamesAvailable()) throw new NoGamesAvailableException();
        if (response.isNonExistentNickname()) throw new NonExistentNicknameException();

    }


    public void messageSomeone(String chatMessage, String receiver){
        this.manageTcpConversation(chatSomeoneLock, new ChatSomeoneMessage(this.nickname, chatMessage, receiver));
    }


    public void messageAll(String chatMessage){
        this.manageTcpConversation(chatAllLock, new ChatAllMessage(this.nickname, chatMessage));
    }




    // asynchronous methods

    private void update(State newState, GameInfo newInfo){
        this.view.update(newState, newInfo);
    }

    private void receiveMessage(String message){
        this.view.displayChatMessage(message);
    }

    private void gracefulDisconnection(){
        System.out.println("Terminating Ping thread");
        this.toPing = false;
        System.out.println("Terminating messageListener");
        this.listeningForMessages = false;

        System.out.println("Initializing graceful disconnection");
        try {
            System.out.println("Closing socket");
            this.socket.close();
        } catch (IOException e) {
            System.out.println("Error while closing socket");
        }

        view.update(State.GRACEFULDISCONNECTION, null);
    }
}


// to do:
// add the remaining messages
// finish the manageInboundTcpMessages
// launch the listening thread