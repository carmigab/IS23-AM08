package it.polimi.ingsw.network.server;

import it.polimi.ingsw.controller.exceptions.InvalidMoveException;
import it.polimi.ingsw.controller.exceptions.InvalidNicknameException;
import it.polimi.ingsw.gameInfo.GameInfo;
import it.polimi.ingsw.gameInfo.State;
import it.polimi.ingsw.network.client.exceptions.GameEndedException;
import it.polimi.ingsw.network.client.exceptions.TimeOutException;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.clientMessages.*;
import it.polimi.ingsw.network.messages.clientMessages.ChatSomeoneMessage;
import it.polimi.ingsw.network.messages.serverMessages.*;
import it.polimi.ingsw.network.server.constants.ServerConstants;
import it.polimi.ingsw.network.server.exceptions.*;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.rmi.RemoteException;

public class TcpClientHandler implements Runnable {
    Socket socket;
    LobbyServer lobbyServer;
    MatchServer matchServer;

    String nickname;

    ObjectOutputStream objectOutputStream;
    ObjectInputStream objectInputStream;

    boolean listeningForMessages = true;
    boolean tcpClientHandlerOnline = true;

    // flag to mute the clientHandler
    private boolean mute = true;


    TcpClientHandler(Socket socket, LobbyServer lobbyServer) {
        this.socket = socket;
        this.lobbyServer = lobbyServer;
    }


    @Override
    public void run() {
        // Setting a timeout, client must keep heartbeat
        try {
            this.socket.setSoTimeout(ServerConstants.PING_TIME+ServerConstants.TCP_WAIT_TIME+1000);
        } catch (SocketException e) {
            if(!mute) System.out.println("CH["+nickname+"]: SocketException");
            this.disconnection();
        }

        // Opening output streams
        try {
            if(!mute) System.out.println("CH: Opening Output Streams");
            this.objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            if(!mute) System.out.println("CH["+nickname+"]: Failed opening Output Streams");
            this.disconnection();
        }


        // Thread to receive messages from server
        this.createInboundMessagesThread();


    }

    private void createInboundMessagesThread(){
        if(!mute) System.out.println("CH["+nickname+"]: New MessagesListener Thread starting");
        if(!mute) System.out.println("CH["+nickname+"]: Opening Input Streams");
        Thread t = new Thread(() -> {
            try {
                this.objectInputStream  = new ObjectInputStream(socket.getInputStream());
            } catch (IOException e) {
                if(!mute) System.out.println("CH["+nickname+"]: Failed opening Input Streams");
                this.disconnection();
            }


            while(listeningForMessages){
                try {
                    Message message = (Message) objectInputStream.readObject();
                    this.manageInboundTcpMessages(message);

                } catch (SocketTimeoutException e) {
                    if(!mute) System.out.println("CH["+nickname+"]: SocketTimeoutException from InboundMessagesThread");
                    // e.printStackTrace();
                    this.disconnection();
                    break;
                }
                catch (IOException e) {
                    if (listeningForMessages){
                        if(!mute) System.out.println("CH["+nickname+"]: IOException from InboundMessagesThread");
                        // e.printStackTrace();
                        this.disconnection();
                        break;
                    }
                } catch (ClassNotFoundException e) {
                    if (listeningForMessages){
                        if(!mute) System.out.println("CH["+nickname+"]: ClassNotFoundException from InboundMessagesThread");
                        this.disconnection();
                        break;
                    }
                }
            }

        });
        t.start();

    }

    private void manageInboundTcpMessages(Message message){
        if (!message.toString().equals("PingClientMessage"))
            if(!mute) System.out.println("CH["+nickname+"]: Received a "+message.toString()+" from "+message.sender());
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
            else if (message instanceof IsServerAliveMessage)
                this.sendTcpMessage(new IsServerAliveResponse("Server"));

        } catch (RemoteException e) {
            if(!mute) System.out.println("CH["+nickname+"]: This remote exception shouldn't be here");
            //ignore
        }
    }


    private void sendTcpMessage(Message message){
        if (tcpClientHandlerOnline) {
            if (!message.toString().equals("PingClientResponse"))
                if(!mute) System.out.println("CH["+nickname+"]: Sending " + message.toString() + " to the client socket");
            try {
                this.objectOutputStream.writeObject(message);
                this.objectOutputStream.flush();
                //this.objectOutputStream.reset();
            } catch (IOException e) {
                if(!mute) System.out.println("CH["+nickname+"]: An error occurred while trying to send a message");
                this.disconnection();
            }
        }

    }


    public void update(State newState, GameInfo newInfo) throws TimeOutException {
        this.sendTcpMessage(new UpdateMessage("Server", newState, newInfo));
    }

    // the client must keep the hearthbeat!!! not the server
    public void isAlive() throws TimeOutException {
        if (!tcpClientHandlerOnline) throw new TimeOutException();
    }


    public void receiveMessage(String message) throws TimeOutException{
        this.sendTcpMessage(new ChatReceiveMessage("Server", message));
    }



    public void setMatchServer(MatchServer matchServer){
        this.matchServer = matchServer;
    }

    private void setNickname(String nickname){
        this.nickname = nickname;
    }

    public String name() {
        return this.nickname;
    }

    private synchronized void disconnection(){
        if (tcpClientHandlerOnline){
            if(!mute) System.out.println("CH["+nickname+"]: initializing disconnection");

            // ending the listening thread
            this.listeningForMessages = false;

            try {
                if(!mute) System.out.println("CH["+nickname+"]: closing socket");
                this.socket.close();
            } catch (IOException e) {
                if(!mute) System.out.println("CH["+nickname+"]: error while closing socket");
            }

            // changing the flag to the correct value
            this.tcpClientHandlerOnline = false;
        }
    }
}
