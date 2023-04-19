module AM08 {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;
    requires java.rmi;


    opens it.polimi.ingsw.guitest to javafx.fxml;
    exports it.polimi.ingsw.guitest;
}