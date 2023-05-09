package it.polimi.ingsw.view.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

import java.util.Objects;

public class RealGUILauncher extends Application {
    public static void main(String[] args) {
        launch();
    }
    static void runAsServer(){
        System.out.println("Server started...adios!");
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("images/Publisher_material/Icon_50x50px.png"))));
        stage.setTitle("MYSHELFIE");
        FXMLLoader fxmlLoader=new FXMLLoader(getClass().getResource("game-view.fxml"));
        Scene scene=new Scene(fxmlLoader.load());
        //scene.setOnKeyPressed((key)->{
        //    if(key.getCode().equals(KeyCode.ENTER)) ((HelloController)fxmlLoader.getController()).onConnectButtonClick();
        //});
        stage.setScene(scene);
        stage.show();
    }
}
