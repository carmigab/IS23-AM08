package it.polimi.ingsw.view;

import it.polimi.ingsw.constants.ModelConstants;
import it.polimi.ingsw.constants.ServerConstants;
import it.polimi.ingsw.network.client.RmiClient;
import it.polimi.ingsw.network.client.TcpClient;
import it.polimi.ingsw.network.client.exceptions.ConnectionError;
import it.polimi.ingsw.network.server.Lobby;
import it.polimi.ingsw.network.server.exceptions.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;

/**
 * This class is the controller for all the interactions of the user before entering a game
 */
public class HelloController implements Initializable {
    /**
     * Vbox containing all the elements of the connection to the server
     */
    @FXML
    public VBox connectionVbox;

    /**
     * Vbox containing all the elements to start a game
     */
    @FXML
    public VBox startGameVbox;

    /**
     * HBox containing all the elements of the menu
     */
    @FXML
    public HBox menuHBox;
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
     * Button used for the connection to the server
     */
    @FXML
    private Button connectButton;
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
     * Choice box containing all the lobbies retrieved from the server
     */
    @FXML
    private ChoiceBox<String> choiceLobbies;
    /**
     * Button used to refresh the lobbies retrieved from the server
     */
    @FXML
    private Button refreshLobbiesButton;
    /**
     * Button that the user can click to try and join a game selected in the choice box
     */
    @FXML
    private Button joinSelectedButton;
    /**
     * Button that the user can click to try and join a game
     */
    @FXML
    private Button joinRandomButton;
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
    private BorderPane container;

    /**
     * Image of the title of the game
     */
    @FXML
    private ImageView title;

    /**
     * Image of the publisher of the game
     */
    @FXML
    public ImageView publisher;

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
        this.connectionType.getItems().removeAll(this.connectionType.getItems());
        this.connectionType.getItems().addAll("rmi", "tcp");
        this.connectionType.getSelectionModel().select("rmi");

        this.choiceLobbies.getItems().removeAll(this.choiceLobbies.getItems());

        this.numPlayers.getItems().removeAll(this.numPlayers.getItems());

        List<Integer> numPlayers=new ArrayList<>(ModelConstants.MAX_PLAYERS-1);
        for(int i=2;i<=ModelConstants.MAX_PLAYERS;i++) numPlayers.add(i);

        this.numPlayers.getItems().addAll(numPlayers);
        this.numPlayers.getSelectionModel().select(2);

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
        this.errorLabel.setText("");
        String zeroTo255 ="(\\d{1,2}|(0|1)\\d{2}|2[0-4]\\d|25[0-5])";
        String regexIP="|localhost|"+zeroTo255 + "\\." + zeroTo255 + "\\." + zeroTo255 + "\\." + zeroTo255;
        if(!ip.matches(regexIP)) {this.errorLabel.setText("Set a valid ip");return;}

        this.connectionLabel.setText("Trying to connect");

        if (input.equals("rmi")) {
            new Thread(()->

            {
                try {
                    this.guiView.client = new RmiClient(this.guiView.myNickname, this.guiView, ip, ServerConstants.RMI_PORT);

                    Platform.runLater(()->
                            {
                                this.connectionLabel.setText("Connected successfully");
                                this.nicknameLabel.setVisible(true);
                                this.nicknameTextField.setVisible(true);
                                this.nicknameButton.setVisible(true);
                                this.connectButton.setVisible(false);

                                this.loadNextScene();
                            }

                            );

                } catch (NotBoundException | InterruptedException | RemoteException e) {
                    Platform.runLater(()-> this.errorLabel.setText("error while connecting to the server") );
                }
            }

                    ).start();

        }
        else {
            new Thread(()->

            {
                try {
                    this.guiView.client = new TcpClient(this.guiView.myNickname, this.guiView, ip, ServerConstants.TCP_PORT);

                    Platform.runLater(()->
                            {
                                this.connectionLabel.setText("Connected successfully");
                                this.nicknameLabel.setVisible(true);
                                this.nicknameTextField.setVisible(true);
                                this.nicknameButton.setVisible(true);
                                this.connectButton.setVisible(false);


                                this.loadNextScene();
                            }

                    );

                } catch ( InterruptedException  | ConnectionError e) {
                    Platform.runLater(()-> this.errorLabel.setText("error while connecting to the server") );
                }
            }

            ).start();

        }
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

        this.menuHBox.getChildren().remove(this.connectionVbox);
        this.menuHBox.getChildren().add(this.startGameVbox);

        try {
            if(!this.guiView.client.chooseNickname(nickname)) this.errorLabel.setText("Invalid name, try again");
            else {
                this.guiView.myNickname=nickname;
                this.nicknameLabel.setVisible(false);
                this.nicknameTextField.setVisible(false);
                this.nicknameButton.setVisible(false);
                this.choiceLobbies.setVisible(true);
                this.refreshLobbiesButton.setVisible(true);
                this.joinSelectedButton.setVisible(true);
                this.joinRandomButton.setVisible(true);
                this.recoverButton.setVisible(true);
                this.createButton.setVisible(true);
            }
        } catch (ConnectionError e) {
            this.errorLabel.setText("Connection error");
        }
    }

    /**
     * Method called when the join button is clicked
     * It tries to join a game and changes scene afterward if successful
     */
    @FXML
    protected void onJoinRandomButtonClick(){
        try {
            List<Lobby> activeLobbies = this.guiView.client.getLobbies();
            String lobby = activeLobbies.get(new Random().nextInt(activeLobbies.size())).getLobbyName();
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
     * Method called when the join button is clicked
     * It tries to join a game and changes scene afterward if successful
     */
    @FXML
    protected void onJoinSelectedButtonClick(){
        try {
            if(this.choiceLobbies.getItems().isEmpty()) {this.errorLabel.setText("Please click Refresh first"); return;}
            this.guiView.client.joinGame(this.choiceLobbies.getValue().split(" ")[0]);
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
     * It asks the server to create a new game and changes scene afterward
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
     * This method displays all the lobbies retrieved from the server in the choice box
     * @param lobbies list of all the lobbies
     */
    private void displayLobbies(List<Lobby> lobbies){

        this.choiceLobbies.getItems().removeAll(this.choiceLobbies.getItems());

        for(Lobby lobby: lobbies){
            if(!lobby.isRecovered()) this.choiceLobbies.getItems().add(lobby.getLobbyName()+" "+lobby.getPlayerInGame()+"/"+lobby.getPlayersNum());
        }

        this.choiceLobbies.getSelectionModel().select(0);

    }

    /**
     * This method is called when the corresponding button is clicked.
     * It asks the server which lobbies are available and displays them in the choice box
     */
    @FXML
    protected void onRefreshLobbiesButtonClick(){

        try {
            this.displayLobbies(this.guiView.client.getLobbies());
        } catch (NoGamesAvailableException e) {
            this.errorLabel.setText("There are no games available");
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