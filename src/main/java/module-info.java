module AM08 {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;
    requires java.rmi;

    opens it.polimi.ingsw.model.commonGoals to com.google.gson;
    opens it.polimi.ingsw.model to com.google.gson;
    opens it.polimi.ingsw.network.server to java.rmi, com.google.gson;
    opens it.polimi.ingsw.guitest to javafx.fxml;
    exports it.polimi.ingsw.guitest;
}