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

/**
 * This class launches the application in gui mode
 */
public class RealGUILauncher extends Application {
    /**
     * Main of the application
     * @param args parameters received via command line
     */
    public static void main(String[] args) {
        launch();
    }

    /**
     * View containing the information used to connect to the server
     */
    private View guiView;

    /**
     * Method that launches the gui
     * It loads the starting scene, sets an icon to the application and adds the possibility to go full screen
     * @param stage stage of the application
     * @throws Exception generic exception
     */
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
