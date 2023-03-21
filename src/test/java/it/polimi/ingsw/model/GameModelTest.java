package it.polimi.ingsw.model;

import static org.junit.jupiter.api.Assertions.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class GameModelTest {

//    @Test
//    public void testPersistency() throws IOException {
//        List<String> players=new ArrayList<>(4);
//        players.add("MatteCenz"); players.add("GabriCarr"); players.add("GabriCarm"); players.add("AleCappe");
//        GameModel gm=new GameModel(4, players);
//        Gson gs=new GsonBuilder().setPrettyPrinting().create();
//        String fileToSave="src/main/config/model/fileToSave.json";
//        FileWriter fr=new FileWriter(fileToSave);
//        String stringToSave=gs.toJson(gm);
//        System.out.println(stringToSave);
//        fr.write(stringToSave);
//        fr.close();
//        assertTrue(true);
//    }

}