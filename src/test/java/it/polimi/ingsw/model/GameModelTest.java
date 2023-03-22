package it.polimi.ingsw.model;

import static org.junit.jupiter.api.Assertions.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.model.constants.AppConstants;
import it.polimi.ingsw.model.utilities.UtilityFunctions;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

class GameModelTest {

    @Test
    public void testPersistency(){
        List<String> players=new ArrayList<>(4);
        players.add("MatteCenz"); players.add("GabriCarr"); players.add("GabriCarm"); players.add("AleCappe");
        GameModel gm=new GameModel(4, players);
        assertTrue(true);
    }

    @Test
    public void testLoadFromFile() throws FileNotFoundException {
        List<String> players=new ArrayList<>(4);
        players.add("MatteCenz"); players.add("GabriCarr"); players.add("GabriCarm"); players.add("AleCappe");
        String file= AppConstants.PATH_SAVED_FILES+ UtilityFunctions.getJSONFileName(players);
        Gson json=new GsonBuilder().setPrettyPrinting().create();
        GameModel gm=new GameModel(json.fromJson(new FileReader(file), GameModel.class));
        System.out.println(json.toJson(gm));
    }

}