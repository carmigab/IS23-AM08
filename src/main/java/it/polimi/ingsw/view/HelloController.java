package it.polimi.ingsw.view;

import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.client.RmiClient;
import it.polimi.ingsw.network.client.TcpClient;
import it.polimi.ingsw.network.client.exceptions.ConnectionError;
import it.polimi.ingsw.network.server.constants.ServerConstants;
import it.polimi.ingsw.view.CLI;
import it.polimi.ingsw.view.View;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class HelloController extends View{
    @FXML
    private ChoiceBox<String> connectionType;
    @FXML
    private TextField serverIP;
    @FXML
    private TextField serverPort;
    @FXML
    private Label errorLabel;
    @FXML
    private Label connectionLabel;
    @FXML
    private Label nicknameLabel;
    @FXML
    private TextField nicknameTextField;
    @FXML
    private Button nicknameButton;

    public HelloController(){

    }

    @FXML
    public void initialize() {
        connectionType.getItems().removeAll(connectionType.getItems());
        connectionType.getItems().addAll("rmi", "tcp");
        connectionType.getSelectionModel().select("rmi");
    }

    @FXML
    protected void onConnectButtonClick(){
        String input=connectionType.getValue().trim();
        String ip=serverIP.getText().trim();
        String port=serverPort.getText().trim();
        this.errorLabel.setText("");
        String zeroTo255 ="(\\d{1,2}|(0|1)\\d{2}|2[0-4]\\d|25[0-5])";
        String regexIP="|localhost|"+zeroTo255 + "\\." + zeroTo255 + "\\." + zeroTo255 + "\\." + zeroTo255;
        if(!ip.matches(regexIP)) {this.errorLabel.setText("Set a valid ip");return;}
        if(!port.matches("|default|\\d+")) {this.errorLabel.setText("Set a valid port");return;}

        this.connectionLabel.setText("Trying to connect");
        int intPort=ServerConstants.RMI_PORT;

        if (input.equals("rmi")) {
            try {
                if(port.equals("default") || port.equals("")) intPort= ServerConstants.RMI_PORT;
                else intPort=Integer.valueOf(port);
                client = new RmiClient(myNickname, this, ip, intPort);
            } catch (RemoteException | NotBoundException | InterruptedException e) {
                this.errorLabel.setText("error while connecting to the server");
            }
        }
        else {
            try {
                if (port.equals("default") || port.equals("")) intPort = ServerConstants.TCP_PORT;
                else intPort = Integer.valueOf(port);
                client = new TcpClient(myNickname, this, ip, intPort);
            } catch (InterruptedException e) {
                this.errorLabel.setText("error while connecting to the server");
            } catch (ConnectionError e) {
            }
        }
        this.connectionLabel.setText("Connected succesfully");
        this.nicknameLabel.setVisible(true);
        this.nicknameTextField.setVisible(true);
        this.nicknameButton.setVisible(true);
    }

    @FXML
    protected void onInsertNameButtonClick(){
        String nickname=this.nicknameTextField.getText().trim();
        this.errorLabel.setText("");
        if(nickname.matches("")) {this.errorLabel.setText("Insert something"); return;}
        try {
            if(!this.client.chooseNickname(nickname)) this.errorLabel.setText("Invalid name, try again");
            else {
                this.myNickname=nickname;
                this.nicknameLabel.setVisible(false);
                this.nicknameTextField.setVisible(false);
                this.nicknameButton.setVisible(false);
            }
        } catch (ConnectionError e) {
            this.errorLabel.setText("Connection error");
        }
    }

    @Override
    protected void display() {

    }

    @Override
    protected void waitForGameStart() {

    }

    @Override
    protected String waitCommand() {
        return null;
    }

    @Override
    protected void parseCommand(String command) {

    }

    @Override
    protected void chooseConnectionType() {

    }

    @Override
    protected void askNickname() {

    }

    @Override
    protected void createOrJoinGame() {

    }

    @Override
    protected boolean askIfWantToPlayAgain() {
        return false;
    }

    @Override
    protected void notifyClose(String message) {

    }

    @Override
    public void displayChatMessage(String message) {

    }
}