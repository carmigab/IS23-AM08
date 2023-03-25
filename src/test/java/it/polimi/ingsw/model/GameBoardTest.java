package it.polimi.ingsw.model;

import static org.junit.jupiter.api.Assertions.*;

import it.polimi.ingsw.model.constants.BoardConstants;
import org.junit.jupiter.api.Test;
import java.util.List;

import java.util.ArrayList;

public class GameBoardTest {

    @Test
    public void testCorrectGameBoard2Creation(){
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
    public void testCorrectGameBoard3Creation(){
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
    public void testCorrectGameBoard4Creation(){
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

}
