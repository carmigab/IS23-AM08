package it.polimi.ingsw.guitest;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class HelloApplication extends Application {
    public static void main(String[] args) {
        for(String arg: args){
            System.out.println(arg);
        }
        if(args.length>0){
            String param0 = args[0];
            if(param0.equals("--server sium")){
                runAsServer();
            }
        }
        else{
            launch();
        }
    }
    static void runAsServer(){
        System.out.println("Server started...adios!");
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader=new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene=new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }
}
