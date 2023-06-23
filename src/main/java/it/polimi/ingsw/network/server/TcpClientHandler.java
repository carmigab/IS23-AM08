package it.polimi.ingsw.network.server;

import it.polimi.ingsw.constants.ServerConstants;
import it.polimi.ingsw.controller.exceptions.InvalidMoveException;
import it.polimi.ingsw.controller.exceptions.InvalidNicknameException;
import it.polimi.ingsw.gameInfo.GameInfo;
import it.polimi.ingsw.gameInfo.State;
import it.polimi.ingsw.network.client.exceptions.GameEndedException;
import it.polimi.ingsw.network.client.exceptions.TimeOutException;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.clientMessages.*;
import it.polimi.ingsw.network.messages.serverMessages.*;
import it.polimi.ingsw.network.server.exceptions.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.rmi.RemoteException;
import java.util.List;

/**
 * This class manages all the inbound and outgoing communication between the client and the server
 */
public class TcpClientHandler extends ClientHandler implements Runnable {
    /**
     * This attribute represents the socket
     */
    private Socket socket;
    /**
     * This attribute represents the lobby server
     */
    private LobbyServer lobbyServer;
    /**
     * This attribute represents the match server
     */
    private MatchServer matchServer;
    /**
     * This attribute represents the nickname of the player
     */
    private String nickname;
    /**
     * This attribute represents the objectOutputStream
     */
    private ObjectOutputStream objectOutputStream;
    /**
     * This attribute represents the objectInputStream
     */
    private ObjectInputStream objectInputStream;
    /**
     * This flag is true if the tcpClientHandler is listening for messages
     */
    private boolean listeningForMessages = true;
    /**
     * This flag is true if the tcpClientHandler is online
     */
    private boolean tcpClientHandlerOnline = true;
    /**
     * Set this flag to true to mute the tcpClientHandler
     */
    private boolean mute = true;

    /**
     * This is the constructor
     * @param socket: the socket
     * @param lobbyServer: the lobby server
     */
    TcpClientHandler(Socket socket, LobbyServer lobbyServer) {
        this.socket = socket;
        this.lobbyServer = lobbyServer;
    }

    /**
     * This is the run method that overrides run() from Runnable
     */
    @Override
    public void run() {
        // Setting a timeout, client must keep heartbeat
        try {
            this.socket.setSoTimeout(ServerConstants.PING_TIME+ServerConstants.TCP_WAIT_TIME+1000);
        } catch (SocketException e) {
            if(!mute) System.out.println("Tcp_CH["+nickname+"]: SocketException");
            this.disconnection();
        }

        // Opening output streams
        try {
            if(!mute) System.out.println("Tcp_CH: Opening Output Streams");
            this.objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            if(!mute) System.out.println("Tcp_CH["+nickname+"]: Failed opening Output Streams");
            this.disconnection();
        }

        // Creating a thread to receive messages from server
        this.createInboundMessagesThread();
    }

    /**
     * This method creates a thread that listens for inbound messages
     */
    private void createInboundMessagesThread(){
        if(!mute) System.out.println("Tcp_CH["+nickname+"]: New MessagesListener Thread starting");
        if(!mute) System.out.println("Tcp_CH["+nickname+"]: Opening Input Streams");
        Thread t = new Thread(() -> {
            // opening the input streams
            try {
                this.objectInputStream  = new ObjectInputStream(socket.getInputStream());
            } catch (IOException e) {
                if(!mute) System.out.println("Tcp_CH["+nickname+"]: Failed opening Input Streams");
                this.disconnection();
            }

            while(listeningForMessages){
                try {
                    Message message = (Message) objectInputStream.readObject();
                    this.manageInboundTcpMessages(message);
                } catch (SocketTimeoutException e) {
                    if(!mute) System.out.println("Tcp_CH["+nickname+"]: SocketTimeoutException from InboundMessagesThread");
                    // e.printStackTrace();
                    this.disconnection();
                    break;
                }
                catch (IOException e) {
                    if (listeningForMessages){
                        if(!mute) System.out.println("Tcp_CH["+nickname+"]: IOException from InboundMessagesThread");
                        //e.printStackTrace();
                        this.disconnection();
                        break;
                    }
                } catch (ClassNotFoundException e) {
                    if (listeningForMessages){
                        if(!mute) System.out.println("Tcp_CH["+nickname+"]: ClassNotFoundException from InboundMessagesThread");
                        this.disconnection();
                        break;
                    }
                }
            }
        });
        t.start();
    }

    /**
     * This method manages an inbound message
     * @param message: the inbound message
     */
    private void manageInboundTcpMessages(Message message){
        if(!mute) System.out.println("Tcp_CH["+nickname+"]: Received a "+message.toString()+" from "+message.sender());
        try {
            // asynchronous messages
            if (message instanceof ChatAllMessage) {
                ChatAllMessage m = (ChatAllMessage) message;
                this.matchServer.messageAll(m.getChatMessage(), m.sender());
            }

            else if (message instanceof ChatSomeoneMessage) {
                ChatSomeoneMessage m = (ChatSomeoneMessage) message;
                this.matchServer.messageSomeone(m.getChatMessage(), m.sender(), m.getReceiver());
            }

            else if (message instanceof ChooseNicknameMessage) {
                ChooseNicknameMessage m = (ChooseNicknameMessage) message;
                boolean response;
                try {
                    response = this.lobbyServer.chooseNickname(m.getNick());
                } catch (ExistentNicknameException e) {
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
                } catch (RemoteException e){
                    // ignore
                }
                catch (AlreadyInGameException e) {
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
                boolean noGameToRecover = false;
                boolean wrongLobbyIndex = false;
                boolean lobbyFull = false;
                try {
                    this.lobbyServer.joinGame(m.sender(), this, m.getLobbyName());
                } catch (NoGamesAvailableException e) {
                    noGamesAvailable = true;
                } catch (NoGameToRecoverException e){
                    noGameToRecover = true;
                }
                catch (AlreadyInGameException e) {
                    alreadyInGame = true;
                } catch (NonExistentNicknameException e) {
                    nonExistentNickname = true;
                } catch (WrongLobbyIndexException e) {
                    wrongLobbyIndex = true;
                } catch (LobbyFullException e) {
                    lobbyFull = true;
                }
                sendTcpMessage(new JoinGameResponse("Server", noGamesAvailable, nonExistentNickname, noGameToRecover, alreadyInGame, wrongLobbyIndex, lobbyFull));
            }

            else if (message instanceof RecoverGameMessage) {
                RecoverGameMessage m = (RecoverGameMessage) message;
                boolean noGamesAvailable = false;
                try {
                    this.lobbyServer.recoverGame(m.sender(), this);
                } catch (NoGameToRecoverException e) {
                    noGamesAvailable = true;
                }
                sendTcpMessage(new RecoverGameResponse("Server", noGamesAvailable));
            }

            else if (message instanceof MakeMoveMessage) {
                MakeMoveMessage m = (MakeMoveMessage) message;
                boolean invalidNickname = false;
                boolean invalidMove = false;
                boolean gameEnded = false;
                try {
                    this.matchServer.makeMove(m.getPositions(), m.getColumn(), m.sender());
                } catch (InvalidNicknameException e) {
                    invalidNickname = true;
                } catch (InvalidMoveException e) {
                    invalidMove = true;
                } catch (GameEndedException e) {
                    gameEnded = true;
                }
                sendTcpMessage(new MakeMoveResponse("Server", invalidMove, invalidNickname, gameEnded));
            }

            else if (message instanceof GetLobbiesMessage) {
                List<Lobby> lobbyList = null;
                boolean noGamesAvailableException = false;
                try {
                    lobbyList = this.lobbyServer.getLobbies(message.sender());
                } catch (NoGamesAvailableException e) {
                    noGamesAvailableException = true;
                }
                sendTcpMessage(new GetLobbiesResponse("Server", lobbyList, noGamesAvailableException));
            }

            // The client keeps the heartbeat, the server sends back the ping
            else if (message instanceof PingClientMessage)
                this.sendTcpMessage(new PingClientResponse("Server"));

        } catch (RemoteException e) {
            if(!mute) System.out.println("Tcp_CH["+nickname+"]: This remote exception shouldn't be here");
            //ignore
        }
    }

    /**
     * This method sends a message to the client
     * @param message: the message to be sent
     */
    private void sendTcpMessage(Message message){
        if (tcpClientHandlerOnline) {
            if(!mute) System.out.println("Tcp_CH["+nickname+"]: Sending " + message.toString() + " to the client socket");
            try {
                this.objectOutputStream.writeObject(message);
                this.objectOutputStream.flush();
                //this.objectOutputStream.reset();
            } catch (IOException e) {
                if(!mute) System.out.println("Tcp_CH["+nickname+"]: An error occurred while trying to send a message");
                this.disconnection();
            }
        }
    }

    /**
     * This method is called by the matchServer and sends a message to update the client with the
     * new state and new game info
     * @param newState: the new state
     * @param newInfo: the new game info
     * @throws TimeOutException
     */
    public void update(State newState, GameInfo newInfo) throws TimeOutException {
        this.sendTcpMessage(new UpdateMessage("Server", newState, newInfo));
    }

    /**
     * This method throws and exception if the client is not online
     * @throws TimeOutException
     */
    public void isAlive() throws TimeOutException {
        if (!tcpClientHandlerOnline) throw new TimeOutException();
    }

    /**
     * This method sends a chat message to the client
     * @param chatMessage: the chat message
     * @throws TimeOutException
     */
    public void receiveMessage(String chatMessage) throws TimeOutException{
        this.sendTcpMessage(new ChatReceiveMessage("Server", chatMessage));
    }

    /**
     * This method sets the match server
     * @param matchServer: the match server
     */
    public void setMatchServer(MatchServer matchServer){
        this.matchServer = matchServer;
    }

    /**
     * This method sets the nickname of the player
     * @param nickname: the nickname
     */
    private void setNickname(String nickname){
        this.nickname = nickname;
    }

    /**
     * This method is called by the matchServer and returns the nickname of the player
     * @return the nickname of the player
     */
    public String name() {
        return this.nickname;
    }

    /**
     * This method manages the disconnection of the client
     */
    private synchronized void disconnection(){
        if (tcpClientHandlerOnline){
            if(!mute) System.out.println("Tcp_CH["+nickname+"]: initializing disconnection");
            // ending the listening thread
            this.listeningForMessages = false;

            try {
                if(!mute) System.out.println("Tcp_CH["+nickname+"]: closing socket");
                this.socket.close();
            } catch (IOException e) {
                if(!mute) System.out.println("Tcp_CH["+nickname+"]: error while closing socket");
            }

            // client is now offline
            this.tcpClientHandlerOnline = false;
        }
    }
}
