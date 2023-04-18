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

    private int lobbyPort = ServerConstants.TCP_PORT;
    private String serverIp = "localhost";

    private Socket socket;

    private OutputStream outputStream;
    private ObjectOutputStream objectOutputStream;

    private View view;

    private InputStream inputStream;

    private ObjectInputStream objectInputStream;

    // Locks
    private int waitTime = 2000;
    private Lock chooseNicknameLock = new Lock();
    private Lock makeMoveLock = new Lock();
    private Lock createGameLock = new Lock();
    private Lock joinGameLock = new Lock();
    private Lock chatSomeoneLock = new Lock();
    private Lock chatAllLock = new Lock();



    public TcpClient(String nickname, View fV) throws InterruptedException {
        super();
        this.view = fV;
        this.nickname = nickname;

        this.connectToLobbyServer();
    }




    private void connectToLobbyServer() throws InterruptedException {
        while (true) {
            try {
                this.socket = new Socket(this.serverIp, this.lobbyPort);
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
    }



    // This method tries to retrieve the received message from the lock
    public Message manageTcpConversation(Lock lock, Message message) {
        try {
            synchronized (lock) {
                this.sendTcpMessage(message);
                lock.wait(this.waitTime);
                // we try to retrieve the message from the lock
                Message newMessage = lock.getMessage();
                lock.reset();
                // if new Message == null it means that we did not receive a response message from the server
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
        System.out.println("Should never arrve here");
        return new Message("None");
    }


    private void sendTcpMessage(Message message){

    }




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
        ChatSomeoneResponse response = (ChatSomeoneResponse) this.manageTcpConversation(chatSomeoneLock,
                new ChatSomeoneMessage(this.nickname, chatMessage, receiver));

    }


    public void messageAll(String chatMessage){
        ChatAllMessage response = (ChatAllMessage) this.manageTcpConversation(chatAllLock,
                new ChatAllMessage(this.nickname, chatMessage));

    }









    private void update(State newState, GameInfo newInfo){
        this.view.update(newState, newInfo);
    }

    private boolean isAlive(){
        return true;
    }

    private String name(){
        return this.nickname;
    }

    private void receiveMessage(String message){
        this.view.displayChatMessage(message);
    }

    private void gracefulDisconnection(){
        System.out.println("Connection Error");
        try {
            System.out.println("Closing socket");
            this.socket.close();
        } catch (IOException e) {
            System.out.println("Error while closing socket");
        }

        view.update(State.GRACEFULDISCONNECTION, null);
    }
}
