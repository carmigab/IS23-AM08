package it.polimi.ingsw.view;

import it.polimi.ingsw.network.client.RmiClient;
import it.polimi.ingsw.network.client.TcpClient;
import it.polimi.ingsw.network.client.exceptions.ConnectionError;
import it.polimi.ingsw.constants.ServerConstants;
import it.polimi.ingsw.network.server.exceptions.AlreadyInGameException;
import it.polimi.ingsw.network.server.exceptions.NoGamesAvailableException;
import it.polimi.ingsw.network.server.exceptions.NonExistentNicknameException;
import it.polimi.ingsw.view.View;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ResourceBundle;

public class HelloController implements Initializable {
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
    @FXML
    private Button joinButton;
    @FXML
    private Button createButton;
    @FXML
    private Label numPlayersLabel;
    @FXML
    private ChoiceBox<Integer> numPlayers;
    @FXML
    private Button goButton;

    private View guiView;
    private Stage stage;
    private Scene nextScene;

    public HelloController(){

    }

    public void setGuiView(View guiView){
        this.guiView=guiView;
    }
    public void setStage(Stage stage){
        this.stage=stage;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        connectionType.getItems().removeAll(connectionType.getItems());
        connectionType.getItems().addAll("rmi", "tcp");
        connectionType.getSelectionModel().select("rmi");

        numPlayers.getItems().removeAll(numPlayers.getItems());
        numPlayers.getItems().addAll(2,3,4);
        numPlayers.getSelectionModel().select(2);

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
                this.guiView.client = new RmiClient(this.guiView.myNickname, this.guiView, ip, intPort);
            } catch (RemoteException | NotBoundException | InterruptedException e) {
                this.errorLabel.setText("error while connecting to the server");
            }
        }
        else {
            try {
                if (port.equals("default") || port.equals("")) intPort = ServerConstants.TCP_PORT;
                else intPort = Integer.valueOf(port);
                this.guiView.client = new TcpClient(this.guiView.myNickname, this.guiView, ip, intPort);
            } catch (InterruptedException e) {
                this.errorLabel.setText("error while connecting to the server");
            } catch (ConnectionError e) {
            }
        }
        this.connectionLabel.setText("Connected succesfully");
        this.nicknameLabel.setVisible(true);
        this.nicknameTextField.setVisible(true);
        this.nicknameButton.setVisible(true);

        this.loadNextScene();
    }

    @FXML
    protected void onInsertNameButtonClick(){
        String nickname=this.nicknameTextField.getText().trim();
        this.errorLabel.setText("");
        if(nickname.matches("")) {this.errorLabel.setText("Insert something"); return;}
        try {
            if(!this.guiView.client.chooseNickname(nickname)) this.errorLabel.setText("Invalid name, try again");
            else {
                this.guiView.myNickname=nickname;
                this.nicknameLabel.setVisible(false);
                this.nicknameTextField.setVisible(false);
                this.nicknameButton.setVisible(false);
                this.joinButton.setVisible(true);
                this.createButton.setVisible(true);
            }
        } catch (ConnectionError e) {
            this.errorLabel.setText("Connection error");
        }
    }

    @FXML
    protected void onJoinButtonClick(){
        try {
            this.guiView.client.joinGame();
            this.changeScene();
        } catch (NoGamesAvailableException e) {
            this.errorLabel.setText("There are no games available");
        } catch (NonExistentNicknameException e) {
            this.errorLabel.setText("You did not put any nickname");
        } catch (AlreadyInGameException e) {
            this.errorLabel.setText("You are already in a game");
        } catch (ConnectionError e) {
            this.errorLabel.setText("Connection error");
        }
    }

    @FXML
    protected void onCreateButtonClick(){
        this.numPlayersLabel.setVisible(true);
        this.numPlayers.setVisible(true);
        this.goButton.setVisible(true);
    }

    @FXML
    protected void onGoButtonClick(){
        try {
            this.guiView.client.createGame(this.numPlayers.getValue());
            this.changeScene();
        } catch (NonExistentNicknameException e) {
            this.errorLabel.setText("You did not put any nickname");
        } catch (AlreadyInGameException e) {
            this.errorLabel.setText("You are already in a game");
        } catch (ConnectionError e) {
            this.errorLabel.setText("Connection error");
        }
    }

    private void changeScene(){
        this.stage.setScene(this.nextScene);
        this.stage.show();
    }

    private void loadNextScene(){
        FXMLLoader fxmlLoader=new FXMLLoader(getClass().getResource("gui/game-view.fxml"));

        try {
            this.nextScene=new Scene(fxmlLoader.load());
            ((GameViewController)fxmlLoader.getController()).setGuiView(this.guiView);
            this.guiView.setGameViewController(fxmlLoader.getController());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}