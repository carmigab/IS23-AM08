package it.polimi.ingsw.guitest;

import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.view.CLI;
import it.polimi.ingsw.view.View;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

public class HelloController {
    @FXML
    private TextField connectionType;
    @FXML
    private TextField serverIP;
    @FXML
    private TextField serverPort;
    @FXML
    private Label errorLabel;

    private Client client;

    private View gui;

    public HelloController(){
        this.gui = new CLI();
    }


    @FXML
    protected void onConnectButtonClick(){
        this.errorLabel.setText("");
        if(!this.connectionType.getText().trim().matches("rmi|tcp|RMI|TCP")) {this.errorLabel.setText("Set a valid connection type");return;}
        String zeroTo255 ="(\\d{1,2}|(0|1)\\d{2}|2[0-4]\\d|25[0-5])";
        String regexIP="|localhost|"+zeroTo255 + "\\." + zeroTo255 + "\\." + zeroTo255 + "\\." + zeroTo255;
        if(!this.serverIP.getText().trim().matches(regexIP)) {this.errorLabel.setText("Set a valid ip");return;}
        if(!this.serverPort.getText().trim().matches("|default|\\d+")) {this.errorLabel.setText("Set a valid port");return;}
    }
}