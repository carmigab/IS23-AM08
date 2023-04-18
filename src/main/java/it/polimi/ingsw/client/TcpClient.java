package it.polimi.ingsw.client;

import it.polimi.ingsw.client.clientLocks.Lock;
import it.polimi.ingsw.client.exceptions.TimeOutException;
import it.polimi.ingsw.controller.exceptions.InvalidMoveException;
import it.polimi.ingsw.controller.exceptions.InvalidNicknameException;
import it.polimi.ingsw.gameInfo.GameInfo;
import it.polimi.ingsw.gameInfo.State;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.server.constants.ServerConstants;
import it.polimi.ingsw.server.exceptions.AlreadyInGameException;
import it.polimi.ingsw.server.exceptions.NoGamesAvailableException;
import it.polimi.ingsw.server.exceptions.NonExistentNicknameException;
import it.polimi.ingsw.server.messages.Message;
import it.polimi.ingsw.server.messages.clientMessages.ChooseNicknameMessage;
import it.polimi.ingsw.server.messages.serverMessages.ChooseNicknameResponse;
import it.polimi.ingsw.view.View;

import java.io.*;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
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




    public boolean chooseNickname(String nick){
        ChooseNicknameResponse response = (ChooseNicknameResponse) this.manageConversation(chooseNicknameLock, new ChooseNicknameMessage(nick));
        return response.getResponse();
    }


    // This method tries to retrieve the received message from the lock
    public Message manageConversation(Lock lock, Message message) {
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
        System.out.println("Qualcosa è andato orribilmente male");
        return new Message("None");
    }


    public void makeMove(List<Position> pos, int col) throws InvalidNicknameException, InvalidMoveException, InvalidNicknameException{}

    public void createGame(int num) throws NonExistentNicknameException, AlreadyInGameException{}

    public void joinGame() throws NoGamesAvailableException, NonExistentNicknameException, AlreadyInGameException{}

    public void messageSomeone(String message, String receiver){}

    public void messageAll(String message){}



    private void sendTcpMessage(Message message){

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
