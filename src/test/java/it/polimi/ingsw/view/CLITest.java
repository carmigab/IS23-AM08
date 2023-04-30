package it.polimi.ingsw.view;

import it.polimi.ingsw.gameInfo.GameInfo;
import it.polimi.ingsw.gameInfo.State;
import it.polimi.ingsw.utilities.JsonWithExposeSingleton;
import org.junit.jupiter.api.Disabled;
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
    @Disabled
    void display() {
        CLI cli = new CLI();

        GameInfo gameInfo = null;
        String configFile = "src/test/resources/view_tests_configs/boardDisplayTest.json";

        try {
            gameInfo = JsonWithExposeSingleton.getJsonWithExposeSingleton().fromJson(new FileReader(configFile), GameInfo.class);
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        }

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