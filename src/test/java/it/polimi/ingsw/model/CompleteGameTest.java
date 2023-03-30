package it.polimi.ingsw.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.exceptions.InvalidIdException;
import it.polimi.ingsw.controller.exceptions.InvalidMoveException;
import it.polimi.ingsw.model.commonGoals.NGroupsOfSizeM;
import it.polimi.ingsw.model.commonGoals.NGroupsOfSizeMConfiguration;
import it.polimi.ingsw.model.constants.AppConstants;
import it.polimi.ingsw.model.utilities.JsonWithExposeSingleton;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit test for a complete game
 */
class CompleteGameTest {
    
    /**
     * This method test a game with two players simulating all the actions to check if all flows correctly
     */     
    @Test
    void twoPlayerCompleteGame() throws FileNotFoundException {
        String configFile = "src/test/resources/CompleteGameTestConfig.json";
        String actionsFile = "src/test/resources/CompleteGameTestActions.json";

        Gson json = new GsonBuilder().setPrettyPrinting().create();
        GameController gameController = new GameController(json.fromJson(new FileReader(configFile), GameController.class));

        List<String> actions = json.fromJson(new FileReader(actionsFile), ArrayList.class);

        for (String action: actions) {
            int playerId = mapChars(action.charAt(0));
            int tilesNum = mapChars(action.charAt(1));

            List<Position> positions = new ArrayList<>();

            for (int i = 0; i < tilesNum; i++) {
                positions.add(new Position(mapChars(action.charAt(2 + i * 2)), mapChars(action.charAt(3 + i * 2))));
            }

            int col = mapChars(action.charAt(2 + tilesNum * 2));

            try {
                gameController.makeMove(positions, col, playerId);
            } catch (InvalidIdException e) {
                throw new RuntimeException(e);
            } catch (InvalidMoveException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private int mapChars(Character c){
        switch (c) {
            case '0' : return 0;
            case '1' : return 1;
            case '2' : return 2;
            case '3' : return 3;
            case '4' : return 4;
            case '5' : return 5;
            case '6' : return 6;
            case '7' : return 7;
            case '8' : return 8;
            default : return 9;
        }
    }
}