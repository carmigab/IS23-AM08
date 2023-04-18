package it.polimi.ingsw.dummies;

import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.constants.AppConstants;
import it.polimi.ingsw.model.utilities.JsonWithExposeSingleton;
import it.polimi.ingsw.network.server.LobbyServer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.Objects;

public class MainLobbyServer {
    public static void main(String[] args) {

        try {
            LobbyServer ls= new LobbyServer();
            ls.start();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }

    }
}
