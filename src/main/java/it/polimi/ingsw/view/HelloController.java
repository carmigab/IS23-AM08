package it.polimi.ingsw.view;

import it.polimi.ingsw.network.client.RmiClient;
import it.polimi.ingsw.network.client.TcpClient;
import it.polimi.ingsw.network.client.exceptions.ConnectionError;
import it.polimi.ingsw.constants.ServerConstants;
import it.polimi.ingsw.network.server.Lobby;
import it.polimi.ingsw.network.server.exceptions.*;
import it.polimi.ingsw.view.View;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;

/**
 * This class is the controller for all the interactions of the user before entering a game
 */
public class HelloController implements Initializable {
    /**
     * Choice box containing the information of the connection to be used (either tcp or rmi)
     */
    @FXML
    private ChoiceBox<String> connectionType;
    /**
     * Text field where the user inputs the ip of the server to connect
     */
    @FXML
    private TextField serverIP;
    /**
     * Text field where the user inputs the port of the server
     */
    @FXML
    private TextField serverPort;
    /**
     * Label where any error (or exception) will be shown
     */
    @FXML
    private Label errorLabel;
    /**
     * Label that prompts the user if the connection to the server was successful
     */
    @FXML
    private Label connectionLabel;
    /**
     * Label that prompts the user to insert a nickname
     */
    @FXML
    private Label nicknameLabel;
    /**
     * Text field where the user inputs his nickname
     */
    @FXML
    private TextField nicknameTextField;
    /**
     * Button that the user can click to send the information of the chosen nickname to the server
     */
    @FXML
    private Button nicknameButton;
    /**
     * Button that the user can click to try and join a game
     */
    @FXML
    private Button joinButton;
    /**
     * Button that the user can click to try and recover a game
     */
    @FXML
    private Button recoverButton;
    /**
     * Button that the user can click to open the information relative to the creation of a game
     */
    @FXML
    private Button createButton;
    /**
     * Label that prompts the user to select how many players does the game contain
     */
    @FXML
    private Label numPlayersLabel;
    /**
     * Choice box containing the information of the connection to be used (either tcp or rmi)
     */
    @FXML
    private ChoiceBox<Integer> numPlayers;
    /**
     * Button that the user can click to create a game
     */
    @FXML
    private Button goButton;
    /**
     * Container of the whole scene
     */
    @FXML
    private VBox container;

    /**
     * View that connects to the server. It is passed between scenes
     */
    private View guiView;
    /**
     * Stage of the entire application
     */
    private Stage stage;
    /**
     * Game scene (it is preloaded for optimizing delays and update problems)
     */
    private Scene nextScene;

    /**
     * Empty constructor of the application
     */
    public HelloController(){

    }

    /**
     * Setter of the view
     * @param guiView view of the application
     */
    public void setGuiView(View guiView){
        this.guiView=guiView;
    }
    /**
     * Setter of the current stage
     * @param stage stage of the application
     */
    public void setStage(Stage stage){
        this.stage=stage;
    }

    /**
     * Method that is called when the fxml file is loaded.
     * It sets up all the choice boxes
     * @param url something
     * @param resourceBundle something else
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        connectionType.getItems().removeAll(connectionType.getItems());
        connectionType.getItems().addAll("rmi", "tcp");
        connectionType.getSelectionModel().select("rmi");

        numPlayers.getItems().removeAll(numPlayers.getItems());
        numPlayers.getItems().addAll(2,3,4);
        numPlayers.getSelectionModel().select(2);

        container.setPrefWidth(Screen.getPrimary().getBounds().getMaxX()/2);
        container.setPrefHeight(Screen.getPrimary().getBounds().getMaxY()/2);

    }

    /**
     * Method called when the connect button is clicked
     * It checks if the parameters inserted in the text fields are correct and then tries to connect to the server
     * At the end displays the new buttons used for accessing a game
     * When it connects it preloads the game scene
     */
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
                this.errorLabel.setText("Connection error");
            }
        }
        this.connectionLabel.setText("Connected succesfully");
        this.nicknameLabel.setVisible(true);
        this.nicknameTextField.setVisible(true);
        this.nicknameButton.setVisible(true);

        this.loadNextScene();
    }

    /**
     * Method called when the insert name button is clicked
     * It sends to the server the nickname and proceeds to display the buttons for the creation and the joining of a game
     */
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
                this.recoverButton.setVisible(true);
                this.createButton.setVisible(true);
            }
        } catch (ConnectionError e) {
            this.errorLabel.setText("Connection error");
        }
    }

    /**
     * Method called when the join button is clicked
     * It tries to join a game and changes scene afterwards if successful
     */
    @FXML
    protected void onJoinButtonClick(){
        try {
            List<Lobby> activeLobbies = this.guiView.client.getLobbies();
            String lobby = parseLobbyInput("r", activeLobbies);
            this.guiView.client.joinGame(lobby);
            this.changeScene();
        } catch (NoGamesAvailableException e) {
            this.errorLabel.setText("There are no games available");
        } catch (NonExistentNicknameException e) {
            this.errorLabel.setText("You did not put any nickname");
        } catch (NoGameToRecoverException e){
            this.errorLabel.setText("No games for recovery with your name");
        } catch (AlreadyInGameException e) {
            this.errorLabel.setText("You are already in a game");
        } catch (ConnectionError e) {
            this.errorLabel.setText("Connection error");
        } catch (WrongLobbyIndexException e) {
            this.errorLabel.setText("Wrong lobby index");
        } catch (LobbyFullException e) {
            this.errorLabel.setText("Lobby is full");
        }
    }

    /**
     * Method called when the recover button is clicked
     * It tries to recover a game (if there is one already with its name, it will join it)
     */
    @FXML
    protected void onRecoverButtonClick(){
        try{
            this.guiView.client.recoverGame();
            this.changeScene();
        } catch (NoGameToRecoverException e) {
            this.errorLabel.setText("No games for recovery with your name");
        } catch (ConnectionError e) {
            this.errorLabel.setText("Connection error");
        }
    }

    private String parseLobbyInput(String input, List<Lobby> activeLobbies) {
        if (input.equalsIgnoreCase("r")) {
            Random random = new Random();
            return activeLobbies.get(random.nextInt(activeLobbies.size())).getLobbyName();
        }
        return "nothing to see here";
//        else {
//            if (Integer.parseInt(input) >= activeLobbies.size()) throw new WrongLobbyIndexException();
//            return activeLobbies.get(Integer.parseInt(input)).lobbyName();
//        }
    }

    /**
     * Method called when the create game button is clicked
     * It displays all the information for the correct creation of the game
     */
    @FXML
    protected void onCreateButtonClick(){
        this.numPlayersLabel.setVisible(true);
        this.numPlayers.setVisible(true);
        this.goButton.setVisible(true);
    }

    /**
     * Method called when the go button is clicked
     * It asks the server to create a new game and changes scene afterwards
     */
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

    /**
     * Method used to change scene of the stage
     */
    private void changeScene(){
        this.stage.setScene(this.nextScene);
        this.stage.show();
    }

    /**
     * Method used to load the game scene
     */
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