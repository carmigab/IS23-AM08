package it.polimi.ingsw.view.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneController {
    private Stage stage;
    private Scene scene;

    public void switchToFirstScene(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader=new FXMLLoader(getClass().getResource("hello-view.fxml"));
        this.stage= (Stage)((Node)event.getSource()).getScene().getWindow();
        this.scene=new Scene(fxmlLoader.load());
        this.scene.setOnKeyPressed((key)->{
            if(key.getCode().equals(KeyCode.ENTER)) ((HelloController)fxmlLoader.getController()).onConnectButtonClick();
        });
        this.stage.setScene(this.scene);
        this.stage.show();
    }

}
