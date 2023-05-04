module AM08 {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;
    requires java.rmi;

    opens it.polimi.ingsw.model.commonGoals to com.google.gson;
    opens it.polimi.ingsw.model to com.google.gson;
    opens it.polimi.ingsw.controller to com.google.gson;
    opens it.polimi.ingsw.network.server to java.rmi, com.google.gson;
    opens it.polimi.ingsw.network.client to java.rmi;
    opens it.polimi.ingsw.view to javafx.fxml;
    exports it.polimi.ingsw.view;
    exports it.polimi.ingsw.constants;
    opens it.polimi.ingsw.constants to javafx.fxml;
    exports it.polimi.ingsw.launchers;
    opens it.polimi.ingsw.launchers to com.google.gson, java.rmi, javafx.fxml;
}