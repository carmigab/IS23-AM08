package it.polimi.ingsw.view;

import it.polimi.ingsw.view.GUIView;
import it.polimi.ingsw.view.HelloController;
import it.polimi.ingsw.view.View;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.util.Objects;

public class RealGUILauncher extends Application {
    public static void main(String[] args) {
        launch();
    }
    static void runAsServer(){
        System.out.println("Server started...adios!");
    }

    private View guiView;

    @Override
    public void start(Stage stage) throws Exception {
        stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("gui/images/Publisher_material/Icon_50x50px.png"))));
        stage.setTitle("MYSHELFIE");
        FXMLLoader fxmlLoader=new FXMLLoader(getClass().getResource("gui/hello-view.fxml"));
        Scene scene=new Scene(fxmlLoader.load());
        stage.setScene(scene);

        stage.setResizable(false);

        stage.addEventHandler(KeyEvent.KEY_PRESSED, keyEvent -> {
            if(KeyCode.F11.equals(keyEvent.getCode())) stage.setFullScreen(!stage.isFullScreen());
        });

        this.guiView=new GUIView();
        ((HelloController)fxmlLoader.getController()).setGuiView(this.guiView);
        ((HelloController)fxmlLoader.getController()).setStage(stage);

        stage.show();
    }
}
