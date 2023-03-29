package it.polimi.ingsw.model;

import static org.junit.jupiter.api.Assertions.*;

import it.polimi.ingsw.model.constants.BoardConstants;
import it.polimi.ingsw.model.exceptions.NoMoreTilesAtStartFillBoardException;
import it.polimi.ingsw.model.exceptions.NoMoreTilesToFillBoardException;
import org.junit.jupiter.api.Test;
import java.util.List;

import java.util.ArrayList;

public class GameBoardTest {

    @Test
    void testCorrectGameBoard2Creation(){
        List<Integer> co=new ArrayList<>(2);
        co.add(10);
        co.add(11);
        GameBoard gb= GameBoard.createGameBoard(2, co);
        assertTrue(gb.positionOccupied(new Position(4,5)));
        //check 4 extremes of the board, should be invalid
        assertFalse(gb.positionOccupied(new Position(0, 0)));
        assertFalse(gb.positionOccupied(new Position(0, BoardConstants.BOARD_DIMENSION-1)));
        assertFalse(gb.positionOccupied(new Position(BoardConstants.BOARD_DIMENSION-1, 0)));
        assertFalse(gb.positionOccupied(new Position(BoardConstants.BOARD_DIMENSION-1, BoardConstants.BOARD_DIMENSION-1)));
    }

    @Test
    void testCorrectGameBoard3Creation(){
        List<Integer> co=new ArrayList<>(2);
        co.add(10);
        co.add(11);
        GameBoard gb= GameBoard.createGameBoard(3, co);
        assertTrue(gb.positionOccupied(new Position(4,5)));
        assertTrue(gb.positionOccupied(new Position(5,BoardConstants.BOARD_DIMENSION-1)));
        assertTrue(gb.positionOccupied(new Position(3,0)));
        //check 4 extremes of the board, should be invalid
        assertFalse(gb.positionOccupied(new Position(0, 0)));
        assertFalse(gb.positionOccupied(new Position(0, BoardConstants.BOARD_DIMENSION-1)));
        assertFalse(gb.positionOccupied(new Position(BoardConstants.BOARD_DIMENSION-1, 0)));
        assertFalse(gb.positionOccupied(new Position(BoardConstants.BOARD_DIMENSION-1, BoardConstants.BOARD_DIMENSION-1)));
    }

    @Test
    void testCorrectGameBoard4Creation(){
        List<Integer> co=new ArrayList<>(2);
        co.add(10);
        co.add(11);
        GameBoard gb= GameBoard.createGameBoard(4, co);
        assertTrue(gb.positionOccupied(new Position(4,5)));
        assertTrue(gb.positionOccupied(new Position(5,BoardConstants.BOARD_DIMENSION-1)));
        assertTrue(gb.positionOccupied(new Position(3,0)));
        assertTrue(gb.positionOccupied(new Position(4,BoardConstants.BOARD_DIMENSION-1)));
        assertTrue(gb.positionOccupied(new Position(4,0)));
        //check 4 extremes of the board, should be invalid
        assertFalse(gb.positionOccupied(new Position(0, 0)));
        assertFalse(gb.positionOccupied(new Position(0, BoardConstants.BOARD_DIMENSION-1)));
        assertFalse(gb.positionOccupied(new Position(BoardConstants.BOARD_DIMENSION-1, 0)));
        assertFalse(gb.positionOccupied(new Position(BoardConstants.BOARD_DIMENSION-1, BoardConstants.BOARD_DIMENSION-1)));
    }

    @Test
    void testHasFreeAdjacentAndRemoveTile(){
        List<Integer> co=new ArrayList<>(2);
        co.add(10);
        co.add(11);
        GameBoard gb= GameBoard.createGameBoard(4, co);
        //check position in the middle of the board, should not have free adjacent
        assertFalse(gb.hasFreeAdjacent(new Position(5,5)));
        //one at the edge which is limited by invalid cells should result free
        assertTrue(gb.hasFreeAdjacent(new Position(4,0)));
        //now if we remove the two tiles and check some adjacent ones should return true
        gb.removeTile(new Position(4,0));
        assertTrue(gb.hasFreeAdjacent(new Position(3,0)));
        assertTrue(gb.hasFreeAdjacent(new Position(4,1)));

        gb.removeTile(new Position(5,5));
        assertTrue(gb.hasFreeAdjacent(new Position(5,4)));
        assertTrue(gb.hasFreeAdjacent(new Position(5,6)));
        assertTrue(gb.hasFreeAdjacent(new Position(4,5)));
        assertTrue(gb.hasFreeAdjacent(new Position(6,5)));
        assertFalse(gb.hasFreeAdjacent(new Position(4,4)));

    }

    @Test
    void testHasToBeFilled(){
        List<Integer> co=new ArrayList<>(2);
        co.add(10);
        co.add(11);
        GameBoard gb= GameBoard.createGameBoard(4, co);
        //at the start should not be filled
        assertFalse(gb.hasToBeFilled());
        //remove one tile and check again
        gb.removeTile(new Position(0,4));
        assertFalse(gb.hasToBeFilled());
        //now remove half of the board and test it again
        gb.removeTile(new Position(0,5));
        gb.removeTile(new Position(0,5));

        gb.removeTile(new Position(1,3));
        gb.removeTile(new Position(1,4));
        gb.removeTile(new Position(1,5));

        gb.removeTile(new Position(2,2));
        gb.removeTile(new Position(2,3));
        gb.removeTile(new Position(2,4));
        gb.removeTile(new Position(2,5));
        gb.removeTile(new Position(2,6));

        gb.removeTile(new Position(3,0));
        gb.removeTile(new Position(3,1));
        gb.removeTile(new Position(3,2));
        gb.removeTile(new Position(3,3));
        gb.removeTile(new Position(3,4));
        gb.removeTile(new Position(3,5));
        gb.removeTile(new Position(3,6));
        gb.removeTile(new Position(3,7));

        assertFalse(gb.hasToBeFilled());

        // now remove other tiles and make them islands only
        gb.removeTile(new Position(4,0));
        gb.removeTile(new Position(4,1));
        gb.removeTile(new Position(4,2));
        gb.removeTile(new Position(4,3));
        gb.removeTile(new Position(4,4));
        gb.removeTile(new Position(4,5));
        gb.removeTile(new Position(4,6));
        gb.removeTile(new Position(4,7));
        gb.removeTile(new Position(4,8));

        gb.removeTile(new Position(5,1));
        gb.removeTile(new Position(5,2));
        gb.removeTile(new Position(5,3));
        gb.removeTile(new Position(5,4));
        gb.removeTile(new Position(5,5));
        gb.removeTile(new Position(5,6));
        gb.removeTile(new Position(5,7));
        gb.removeTile(new Position(5,8));

        gb.removeTile(new Position(6,3));
        gb.removeTile(new Position(6,4));
        gb.removeTile(new Position(6,5));

        gb.removeTile(new Position(7,3));
        gb.removeTile(new Position(7,4));

        //now it should be false since the eight column still has a pair
        assertFalse(gb.hasToBeFilled());

        //if i remove one or both should give true
        gb.removeTile(new Position(8,3));

        assertTrue(gb.hasToBeFilled());

    }

    @Test
    void testFillBoard() throws NoMoreTilesAtStartFillBoardException, NoMoreTilesToFillBoardException{
        List<Integer> co=new ArrayList<>(2);
        co.add(10);
        co.add(11);
        GameBoard gb= GameBoard.createGameBoard(4, co);

        prepareBoard4PlayersToBeFilled(gb);
        //the first time it should not throw the exception since there are 87=132-45 tiles remaining
        gb.fillBoard();

        //check some positions so it is indeed filled
        //duplicate of the test above
        assertTrue(gb.positionOccupied(new Position(4,5)));
        assertTrue(gb.positionOccupied(new Position(5,BoardConstants.BOARD_DIMENSION-1)));
        assertTrue(gb.positionOccupied(new Position(3,0)));
        assertTrue(gb.positionOccupied(new Position(4,BoardConstants.BOARD_DIMENSION-1)));
        assertTrue(gb.positionOccupied(new Position(4,0)));

        prepareBoard4PlayersToBeFilled(gb);
        // still no problems
        gb.fillBoard();

        //check some positions so it is indeed filled
        //duplicate of the test above
        assertTrue(gb.positionOccupied(new Position(4,5)));
        assertTrue(gb.positionOccupied(new Position(5,BoardConstants.BOARD_DIMENSION-1)));
        assertTrue(gb.positionOccupied(new Position(3,0)));
        assertTrue(gb.positionOccupied(new Position(4,BoardConstants.BOARD_DIMENSION-1)));
        assertTrue(gb.positionOccupied(new Position(4,0)));

        prepareBoard4PlayersToBeFilled(gb);

        //now it should throw the exception
        assertThrows(NoMoreTilesToFillBoardException.class, gb::fillBoard);

        //if i call it again now it should throw the other exception
        assertThrows(NoMoreTilesAtStartFillBoardException.class, gb::fillBoard);
    }

    static void prepareBoard4PlayersToBeFilled(GameBoard gb){
        gb.removeTile(new Position(0,4));
        gb.removeTile(new Position(0,5));
        gb.removeTile(new Position(0,5));

        gb.removeTile(new Position(1,3));
        gb.removeTile(new Position(1,4));
        gb.removeTile(new Position(1,5));

        gb.removeTile(new Position(2,2));
        gb.removeTile(new Position(2,3));
        gb.removeTile(new Position(2,4));
        gb.removeTile(new Position(2,5));
        gb.removeTile(new Position(2,6));

        gb.removeTile(new Position(3,0));
        gb.removeTile(new Position(3,1));
        gb.removeTile(new Position(3,2));
        gb.removeTile(new Position(3,3));
        gb.removeTile(new Position(3,4));
        gb.removeTile(new Position(3,5));
        gb.removeTile(new Position(3,6));
        gb.removeTile(new Position(3,7));

        gb.removeTile(new Position(4,0));
        gb.removeTile(new Position(4,1));
        gb.removeTile(new Position(4,2));
        gb.removeTile(new Position(4,3));
        gb.removeTile(new Position(4,4));
        gb.removeTile(new Position(4,5));
        gb.removeTile(new Position(4,6));
        gb.removeTile(new Position(4,7));
        gb.removeTile(new Position(4,8));

        gb.removeTile(new Position(5,1));
        gb.removeTile(new Position(5,2));
        gb.removeTile(new Position(5,3));
        gb.removeTile(new Position(5,4));
        gb.removeTile(new Position(5,5));
        gb.removeTile(new Position(5,6));
        gb.removeTile(new Position(5,7));
        gb.removeTile(new Position(5,8));

        gb.removeTile(new Position(6,3));
        gb.removeTile(new Position(6,4));
        gb.removeTile(new Position(6,5));

        gb.removeTile(new Position(7,3));
        gb.removeTile(new Position(7,4));
    }

}
