package it.polimi.ingsw.view;

import it.polimi.ingsw.gameInfo.GameInfo;
import it.polimi.ingsw.gameInfo.State;
import it.polimi.ingsw.model.Tile;
import it.polimi.ingsw.model.constants.AppConstants;
import it.polimi.ingsw.model.utilities.JsonWithExposeSingleton;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;

/**
 * This class is the test of the CLI
 */
class CLITest {

    /**
     * This method tests the display method
     */
    @Test
    void display() {
        CLI cli = new CLI();

        Tile[][] myGameBoard = new Tile[AppConstants.BOARD_DIMENSION][AppConstants.BOARD_DIMENSION];

        String configFile = "src/test/resources/view_tests_configs/boardDisplayTest.json";

        try {
            myGameBoard = JsonWithExposeSingleton.getJsonWithExposeSingleton().fromJson(new FileReader(configFile), Tile[][].class);
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        }

        GameInfo gameInfo = new GameInfo(myGameBoard, null, null, null);

        cli.update(State.TURN0, gameInfo);
    }

    /**
     * This method tests the user input flow
     */
    @Test
    void getUserInput() {
        CLI cli = new CLI();

        //cli.getUserInput();
    }
}