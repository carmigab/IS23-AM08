package it.polimi.ingsw.model;

import static org.junit.jupiter.api.Assertions.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.UtilityTestFunctions;
import it.polimi.ingsw.model.constants.AppConstants;
import it.polimi.ingsw.utilities.UtilityFunctionsModel;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

class GameModelTest {

    @Test
    @Disabled
    public void testPersistence(){
        List<String> players=new ArrayList<>(4);
        players.add("MatteCenz"); players.add("GabriCarr"); players.add("GabriCarm"); players.add("AleCappe");
        GameModel gm=new GameModel(4, players);
        assertTrue(true);
    }

    @Test
    public void testLoadFromFile() throws FileNotFoundException {
        List<String> players=new ArrayList<>(4);
        players.add("MatteCenz"); players.add("GabriCarr"); players.add("GabriCarm"); players.add("AleCappe");
        String file= AppConstants.PATH_SAVED_MATCHES + UtilityFunctionsModel.getJSONFileName(players);
        Gson json=new GsonBuilder().setPrettyPrinting().create();
        GameModel gm=new GameModel(json.fromJson(new FileReader(file), GameModel.class));
        //System.out.println(json.toJson(gm));
        assertTrue(true);
    }

    /**
     * this method tests the method makeMove of the class GameModel
     * @throws FileNotFoundException if the file didn't find
     */
    @Test
    public void makeMove() throws FileNotFoundException {
        List<String> players=new ArrayList<>(4);
        players.add("MatteCenz"); players.add("GabriCarr"); players.add("GabriCarm"); players.add("AleCappe");
        String file= "src/main/resources/testMatches/MatchTestMakeMove.json";
        Gson json=new GsonBuilder().setPrettyPrinting().create();
        GameModel gm=new GameModel(json.fromJson(UtilityTestFunctions.getReaderFromFileNameRelativePath("MatchTestMakeMove.json", this.getClass()), GameModel.class));

        Position p = new Position(0, 4);
        List<Position> pos = new ArrayList<>();
        pos.add(p);
        gm.makeMove(pos, 0);
        assertFalse(gm.getGameBoard().positionOccupied(p));

        Position p1 = new Position(0, AppConstants.ROWS_NUMBER - 1);
        assertEquals(TileColor.VIOLET, gm.getPlayer().getShelf().getTile(p1).getColor());
        assertEquals(2, gm.getPlayer().getShelf().getTile(p1).getSprite());

        // I test the method with 2 cards taken by the board
        gm=new GameModel(json.fromJson(UtilityTestFunctions.getReaderFromFileNameRelativePath("MatchTestMakeMove.json", this.getClass()), GameModel.class));
        Position p2 = new Position(0, 4);
        Position p3 = new Position(1, 4);
        Position p4 = new Position(2, 4);
        List<Position> pos1 = new ArrayList<>();
        pos1.add(p2);
        pos1.add(p3);
        pos1.add(p4);
        gm.makeMove(pos1, 0);
        assertFalse(gm.getGameBoard().positionOccupied(p2));
        assertFalse(gm.getGameBoard().positionOccupied(p3));
        assertFalse(gm.getGameBoard().positionOccupied(p4));
        Position p5 = new Position(0, AppConstants.ROWS_NUMBER - 1);
        assertEquals(TileColor.VIOLET, gm.getPlayer().getShelf().getTile(p5).getColor());
        assertEquals(2, gm.getPlayer().getShelf().getTile(p5).getSprite());
        Position p6 = new Position(0, AppConstants.ROWS_NUMBER - 2);
        assertEquals(TileColor.CYAN, gm.getPlayer().getShelf().getTile(p6).getColor());
        assertEquals(1, gm.getPlayer().getShelf().getTile(p6).getSprite());
        Position p7 = new Position(0, AppConstants.ROWS_NUMBER - 3);
        assertEquals(TileColor.CYAN, gm.getPlayer().getShelf().getTile(p7).getColor());
        assertEquals(1, gm.getPlayer().getShelf().getTile(p7).getSprite());
    }

    /**
     * this method tests the method checkValidMove of the class GameModel
     */
    @Test
    public void checkValidMove(){
        List<String> players=new ArrayList<>(4);
        players.add("MatteCenz"); players.add("GabriCarr"); players.add("GabriCarm"); players.add("AleCappe");
        Gson json=new GsonBuilder().setPrettyPrinting().create();
        GameModel gm=new GameModel(json.fromJson(UtilityTestFunctions.getReaderFromFileNameRelativePath("TestCheckValidMoveAndCheckValidColumn.json", this.getClass()), GameModel.class));

        List<Position> pos = new ArrayList<>();
        assertFalse(gm.checkValidMove(pos));
        pos.add(new Position(3,0));
        pos.add(new Position(3,0));
        assertFalse(gm.checkValidMove(pos));
        pos.remove(1);
        pos.add(new Position(4,0));
        assertTrue(gm.checkValidMove(pos));
        pos.add(new Position(3,1));
        assertFalse(gm.checkValidMove(pos));
        pos.add(new Position(3,2));
        assertFalse(gm.checkValidMove(pos));
        List<Position> pos1 = new ArrayList<>();
        pos1.add(new Position(2,2));
        pos1.add(new Position(2,3));
        pos1.add(new Position(2,4));
        assertFalse(gm.checkValidMove(pos1));
        List<Position> pos2 = new ArrayList<>();
        pos2.add(new Position(4,0));
        pos2.add(new Position(3,0));
        pos2.add(new Position ( 3,1));
        assertFalse(gm.checkValidMove(pos2));
        List<Position> pos3 = new ArrayList<>();
        //problems on this test (maybe on the hasFreeAdjacent)
        pos3.add(new Position(3,1));
        pos3.add(new Position(5, 1));
        assertFalse(gm.checkValidMove(pos3));

    }

    /**
     * this method tests the method checkValidColumn of the class GameModel
     */
    @Test
    public void checkValidColumn() throws FileNotFoundException {
        List<String> players=new ArrayList<>(4);
        players.add("MatteCenz"); players.add("GabriCarr"); players.add("GabriCarm"); players.add("AleCappe");
        Gson json=new GsonBuilder().setPrettyPrinting().create();
        GameModel gm=new GameModel(json.fromJson(UtilityTestFunctions.getReaderFromFileNameRelativePath("TestCheckValidMoveAndCheckValidColumn.json", this.getClass()), GameModel.class));
        assertFalse(gm.checkValidColumn(6,1));
        assertTrue(gm.checkValidColumn(0,3));
        assertFalse(gm.checkValidColumn(1, 1));
        assertFalse(gm.checkValidColumn(2,3));
        assertTrue(gm.checkValidColumn(2,2));
        assertFalse(gm.checkValidColumn(3,2));
        assertTrue(gm.checkValidColumn(3,1));
    }

    /**
     * this method tests the method getCurrentPlayer of the class GameModel
     * @throws FileNotFoundException if the file which simulate the match didn't find
     */
    @Test
    public void getCurrentPlayer() throws FileNotFoundException {
        List<String> players=new ArrayList<>(4);
        players.add("MatteCenz"); players.add("GabriCarr"); players.add("GabriCarm"); players.add("AleCappe");
        String file= AppConstants.PATH_SAVED_MATCHES + UtilityFunctionsModel.getJSONFileName(players);
        Gson json=new GsonBuilder().setPrettyPrinting().create();
        GameModel gm=new GameModel(json.fromJson(new FileReader(file), GameModel.class));

        assertEquals(0, gm.getCurrentPlayer());
    }

    /**
     * this method tests the method getPlayer of the class GameModel
     * @throws FileNotFoundException if the file which simulate the match didn't find
     */
    @Test
    public void getPlayer() throws FileNotFoundException {
        List<String> players=new ArrayList<>(4);
        players.add("MatteCenz"); players.add("GabriCarr"); players.add("GabriCarm"); players.add("AleCappe");
        String file= AppConstants.PATH_SAVED_MATCHES + UtilityFunctionsModel.getJSONFileName(players);
        Gson json=new GsonBuilder().setPrettyPrinting().create();
        GameModel gm=new GameModel(json.fromJson(new FileReader(file), GameModel.class));
        PlayerState p = gm.getPlayer();
        assertEquals("MatteCenz", p.getNickname());
        assertEquals(0,p.getPoints());
        assertFalse(p.isCGDone(0));
        assertFalse(p.isCGDone(1));
    }


}