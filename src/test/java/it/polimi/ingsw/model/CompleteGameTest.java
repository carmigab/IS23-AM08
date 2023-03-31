package it.polimi.ingsw.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.exceptions.InvalidIdException;
import it.polimi.ingsw.controller.exceptions.InvalidMoveException;
import it.polimi.ingsw.model.constants.AppConstants;
import it.polimi.ingsw.model.utilities.JsonWithExposeSingleton;
import it.polimi.ingsw.model.utilities.UtilityFunctions;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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
        String modelsFileStart = "src/test/resources/Model";
        String modelsFileCount = "1";
        String modelsFileExtension = ".json";

        Gson json = new GsonBuilder().setPrettyPrinting().create();
        GameController gameController = new GameController(json.fromJson(new FileReader(configFile), GameController.class));

        Gson jsonLoader = JsonWithExposeSingleton.getJsonWithExposeSingleton();

        List<String> players=new ArrayList<>(2);
        players.add("MatteCenz"); players.add("GabriCarr");
        String file = AppConstants.PATH_SAVED_FILES+ UtilityFunctions.getJSONFileName(players);
        GameModel savedModel;

        List<String> actions = json.fromJson(new FileReader(actionsFile), ArrayList.class);

        int index = 1;
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
                savedModel = new GameModel(json.fromJson(new FileReader(file), GameModel.class));
                if (index < 14) {
                    modelsFileCount = new String(intToString(index));
                    GameModel expectedGameModel = new GameModel(jsonLoader.fromJson(new FileReader(modelsFileStart + modelsFileCount + modelsFileExtension), GameModel.class));
                    assertTrue(expectedGameModel.equals(savedModel));
                }
            } catch (InvalidIdException e) {
                throw new RuntimeException(e);
            } catch (InvalidMoveException e) {
                throw new RuntimeException(e);
            }
            index++;
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

    private String intToString(int i){
        switch (i) {
            case 1 : return "1";
            case 2 : return "2";
            case 3 : return "3";
            case 4 : return "4";
            case 5 : return "5";
            case 6 : return "6";
            case 7 : return "7";
            case 8 : return "8";
            case 9 : return "9";
            case 10 : return "10";
            case 11 : return "11";
            case 12 : return "12";
            case 13: return "13";
            default: return "14";
        }
    }

    private static class expectedModels {
        List<GameModel> models;
    }
}