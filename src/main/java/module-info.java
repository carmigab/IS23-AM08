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
    opens it.polimi.ingsw.constants to javafx.fxml;
    opens it.polimi.ingsw.launchers to com.google.gson, java.rmi, javafx.fxml;
    opens it.polimi.ingsw.view.gui to javafx.fxml;


    exports it.polimi.ingsw.constants;
    exports it.polimi.ingsw.controller;
    exports it.polimi.ingsw.gameInfo;
    exports it.polimi.ingsw.launchers;
    exports it.polimi.ingsw.model;
    exports it.polimi.ingsw.network.client;
    exports it.polimi.ingsw.network.server;
    exports it.polimi.ingsw.network.messages;
    exports it.polimi.ingsw.utilities;
    exports it.polimi.ingsw.view;
    exports it.polimi.ingsw.view.gui;



}