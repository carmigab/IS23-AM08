package it.polimi.ingsw.network.server;

import it.polimi.ingsw.controller.exceptions.InvalidMoveException;
import it.polimi.ingsw.controller.exceptions.InvalidNicknameException;
import it.polimi.ingsw.gameInfo.GameInfo;
import it.polimi.ingsw.gameInfo.State;
import it.polimi.ingsw.network.client.clientLocks.Lock;
import it.polimi.ingsw.network.client.exceptions.ConnectionError;
import it.polimi.ingsw.network.client.exceptions.TimeOutException;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.clientMessages.*;
import it.polimi.ingsw.network.messages.clientMessages.ChatSomeoneMessage;
import it.polimi.ingsw.network.messages.serverMessages.*;
import it.polimi.ingsw.network.server.constants.ServerConstants;
import it.polimi.ingsw.network.server.exceptions.*;
import it.polimi.ingsw.view.View;

import java.io.*;
import java.net.Socket;
import java.rmi.RemoteException;

public class TcpClientHandler implements Runnable{
    Socket socket;
    LobbyServer lobbyServer;
    MatchServer matchServer;

    String nickname;

    boolean listeningForMessages = true;

    OutputStream outputStream;
    ObjectOutputStream objectOutputStream;


    InputStream inputStream;
    ObjectInputStream objectInputStream;

    Thread listeningThread;

    Lock uselessLock = new Lock();

    TcpClientHandler(Socket socket, LobbyServer lobbyServer) {
        this.socket = socket;
        this.lobbyServer = lobbyServer;
    }


    @Override
    public void run() {
        // Opening output streams
        try {
            System.out.println("Opening Output Streams");
            this.outputStream = socket.getOutputStream();
            this.objectOutputStream = new ObjectOutputStream(outputStream);
        } catch (IOException e) {
            System.out.println("Failed opening Output Streams");
            this.disconnection();
        }


        // Thread to receive messages from server
        this.createInboundMessagesThread();


    }

    private void createInboundMessagesThread(){
        System.out.println("New MessagesListener Thread starting");
        System.out.println("Opening Input Streams");
        Thread t = new Thread(() -> {
            try {
                this.inputStream = socket.getInputStream();
                this.objectInputStream  = new ObjectInputStream(inputStream);
            } catch (IOException e) {
                System.out.println("Failed opening Input Streams");
                this.disconnection();
            }


            while(listeningForMessages){
                try {
                    Message message = (Message) objectInputStream.readObject();
                    this.manageInboundTcpMessages(message);

                } catch (IOException e) {
                    if (listeningForMessages){
                        System.out.println("IOException from InboundMessagesThread");
                        e.printStackTrace();
                        this.disconnection();
                        break;
                    }
                } catch (ClassNotFoundException e) {
                    if (listeningForMessages){
                        System.out.println("ClassNotFoundException from InboundMessagesThread");
                        this.disconnection();
                        break;
                    }
                }
            }

        });
        t.start();

    }

    public Message manageTcpConversation(Lock lock, Message message) throws TimeOutException {
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
            this.disconnection();
        } catch (TimeOutException e) {
            System.out.println("Tcp server too slow to respond");
            this.disconnection();
        }

        throw new TimeOutException();
    }

    public void manageInboundTcpMessages(Message message){
        try {
            // asynchronous messages
            if (message instanceof ChatAllMessage) {
                ChatAllMessage m = (ChatAllMessage) message;
                this.matchServer.messageAll(m.getChatMessage(), m.sender());
                sendTcpMessage(new ChatAllResponse("Server"));
            }
            else if (message instanceof ChatSomeoneMessage) {
                ChatSomeoneMessage m = (ChatSomeoneMessage) message;
                this.matchServer.messageSomeone(m.getChatMessage(), m.sender(), m.getReceiver());
                sendTcpMessage(new ChatSomeoneResponse("Server"));
            }

            else if (message instanceof ChooseNicknameMessage) {
                ChooseNicknameMessage m = (ChooseNicknameMessage) message;
                boolean response;
                try {
                    response = this.lobbyServer.chooseNickname(m.getNick());
                } catch (ExistentNicknameExcepiton e) {
                    response = false;
                } catch (IllegalNicknameException e) {
                    response = false;
                }
                if (response) setNickname(m.getNick());
                sendTcpMessage(new ChooseNicknameResponse("Server", response));
            }

            else if (message instanceof CreateGameMessage) {
                CreateGameMessage m = (CreateGameMessage) message;
                boolean alreadyInGame = false;
                boolean nonExistentNickname = false;
                try {
                    this.lobbyServer.createGame(m.getNumberOfPlayers(), m.sender(), this);
                } catch (AlreadyInGameException e) {
                    alreadyInGame = true;
                } catch (NonExistentNicknameException e) {
                    nonExistentNickname = true;
                }
                sendTcpMessage(new CreateGameResponse("Server", nonExistentNickname, alreadyInGame));
            }

            else if (message instanceof JoinGameMessage) {
                JoinGameMessage m = (JoinGameMessage) message;
                boolean alreadyInGame = false;
                boolean nonExistentNickname = false;
                boolean noGamesAvailable = false;
                try {
                    this.lobbyServer.joinGame(m.sender(), this);
                } catch (NoGamesAvailableException e) {
                    noGamesAvailable = true;
                } catch (AlreadyInGameException e) {
                    alreadyInGame = true;
                } catch (NonExistentNicknameException e) {
                    nonExistentNickname = true;
                }
                sendTcpMessage(new JoinGameResponse("Server", noGamesAvailable, nonExistentNickname, alreadyInGame));
            }

            else if (message instanceof MakeMoveMessage) {
                MakeMoveMessage m = (MakeMoveMessage) message;
                boolean invalidNickname = false;
                boolean invalidMove = false;
                try {
                    this.matchServer.makeMove(m.getPositions(), m.getColumn(), m.sender());
                } catch (InvalidNicknameException e) {
                    invalidNickname = true;
                } catch (InvalidMoveException e) {
                    invalidMove = true;
                }
                sendTcpMessage(new MakeMoveResponse("Server", invalidMove, invalidNickname));
            }

            else if (message instanceof PingClientMessage)
                this.sendTcpMessage(new PingClientResponse("Server"));

            // synchronous messages


        } catch (RemoteException e) {
            //ignore
        }
    }

    private void notifyLockAndSetMessage(Lock lock, Message message){
        synchronized (lock){
            lock.setMessage(message);
            lock.notify();
        }
    }


    private void sendTcpMessage(Message message){
        if (!message.toString().equals("PingClientResponse"))
            System.out.println("Sending "+message.toString() +" to the client socket");
        try {
            this.objectOutputStream.writeObject(message);
            this.objectOutputStream.flush();
            //this.objectOutputStream.reset();
        } catch (IOException e) {
            System.out.println("An error occurred while trying to send a message");
            this.disconnection();
        }

    }


    public void update(State newState, GameInfo newInfo) throws TimeOutException {}

    public void isAlive() throws TimeOutException {}

    public String name() {return "Bill1";}

    public void receiveMessage(String message) throws TimeOutException{}

    public void setMatchServer(MatchServer matchServer){
        this.matchServer = matchServer;
    }

    public void setNickname(String nickname){
        this.nickname = nickname;
    }

    private void disconnection(){}
}
