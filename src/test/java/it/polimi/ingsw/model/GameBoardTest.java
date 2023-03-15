package it.polimi.ingsw.model;

import static org.junit.jupiter.api.Assertions.*;

import it.polimi.ingsw.model.constants.AppConstants;
import it.polimi.ingsw.model.constants.BoardConstants;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class GameBoardTest {

    @Test
    public void testCorrectGameBoard2Creation(){
        GameBoard gb= GameBoard.createGameBoard(2);
        assertTrue(gb.positionOccupied(new Position(4,5)));
        //check 4 extremes of the board, should be invalid
        assertFalse(gb.positionOccupied(new Position(0, 0)));
        assertFalse(gb.positionOccupied(new Position(0, BoardConstants.BOARD_DIMENSION)));
        assertFalse(gb.positionOccupied(new Position(BoardConstants.BOARD_DIMENSION, 0)));
        assertFalse(gb.positionOccupied(new Position(BoardConstants.BOARD_DIMENSION, BoardConstants.BOARD_DIMENSION)));
    }

    @Test
    public void testCorrectGameBoard3Creation(){
        GameBoard gb= GameBoard.createGameBoard(3);
        assertTrue(gb.positionOccupied(new Position(4,5)));
        assertTrue(gb.positionOccupied(new Position(5,BoardConstants.BOARD_DIMENSION)));
        assertTrue(gb.positionOccupied(new Position(3,0)));
        //check 4 extremes of the board, should be invalid
        assertFalse(gb.positionOccupied(new Position(0, 0)));
        assertFalse(gb.positionOccupied(new Position(0, BoardConstants.BOARD_DIMENSION)));
        assertFalse(gb.positionOccupied(new Position(BoardConstants.BOARD_DIMENSION, 0)));
        assertFalse(gb.positionOccupied(new Position(BoardConstants.BOARD_DIMENSION, BoardConstants.BOARD_DIMENSION)));
    }

    @Test
    public void testCorrectGameBoard4Creation(){
        GameBoard gb= GameBoard.createGameBoard(4);
        assertTrue(gb.positionOccupied(new Position(4,5)));
        assertTrue(gb.positionOccupied(new Position(5,BoardConstants.BOARD_DIMENSION)));
        assertTrue(gb.positionOccupied(new Position(3,0)));
        assertTrue(gb.positionOccupied(new Position(4,BoardConstants.BOARD_DIMENSION)));
        assertTrue(gb.positionOccupied(new Position(4,0)));
        //check 4 extremes of the board, should be invalid
        assertFalse(gb.positionOccupied(new Position(0, 0)));
        assertFalse(gb.positionOccupied(new Position(0, BoardConstants.BOARD_DIMENSION)));
        assertFalse(gb.positionOccupied(new Position(BoardConstants.BOARD_DIMENSION, 0)));
        assertFalse(gb.positionOccupied(new Position(BoardConstants.BOARD_DIMENSION, BoardConstants.BOARD_DIMENSION)));
    }

}
