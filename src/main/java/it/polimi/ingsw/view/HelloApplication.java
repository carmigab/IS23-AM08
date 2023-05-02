package it.polimi.ingsw.view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

import java.util.Objects;

public class HelloApplication extends Application {
    public static void main(String[] args) {
        launch();
    }
    static void runAsServer(){
        System.out.println("Server started...adios!");
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader=new FXMLLoader(getClass().getResource("hello-view.fxml"));
        Scene scene=new Scene(fxmlLoader.load());
        scene.setOnKeyPressed((key)->{
            if(key.getCode().equals(KeyCode.ENTER)) ((HelloController)fxmlLoader.getController()).onConnectButtonClick();
        });
        stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("icon_50x50px.png"))));
        stage.setTitle("MYSHELFIE");
        stage.setScene(scene);
        stage.show();
    }
}
