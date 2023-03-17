package it.polimi.ingsw.model;

import it.polimi.ingsw.model.commonObjectives.CommonObjective;
import it.polimi.ingsw.model.constants.BoardConstants;
import com.google.gson.Gson;
import it.polimi.ingsw.model.exceptions.NoMoreCardsAtStartFillBoardException;
import it.polimi.ingsw.model.exceptions.NoMoreCardsToFillBoardException;
import it.polimi.ingsw.model.utilities.JsonLoader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * This is the class used for the simulation of the physical game board.
 * It is indeed very articulated, but it can do all the features required for the correct advancement of the game
 */
public final class GameBoard {
    /**
     * Matrix used for the storage of the physical gameboard
     */
    private final Card[][] myGameBoard;
    /**
     * List of the two common objectives chosen for the current game
     */
    private final List<CommonObjective> commonObjectives;
    /**
     * List of all the 132 cards that can be found in the game "bucket"
     */
    private final List<Card> allCards;
    /**
     * Attribute from the java.util.package used for utility, such as choosing a random card from the bucket when filling the board
     */
    private final Random r;

    /**
     * This method is the utility used by the GameModel to get the gameBoard based on the number of players
     * @param numPlayers number of players of the current game (between 2 and 4)
     * @return game board configurated and filled
     */
    public static GameBoard createGameBoard(int numPlayers){
        if(numPlayers==2) return new GameBoard(BoardConstants.FILE_CONFIG_GAMEBOARD2);
        if(numPlayers==3) return new GameBoard(BoardConstants.FILE_CONFIG_GAMEBOARD3);
        return new GameBoard(BoardConstants.FILE_CONFIG_GAMEBOARD4);
    }
    /**
     * TODO: decide which common objectives should be passed, maybe through the constructor
     * The constructor creates all the data structures and the utility attributes.
     * Note that the exception should never be thrown since the file will obviously be always present in the directory (unless someone makes a typo)
     * Note also that it creates a temporary object GameBoardConfiguration used to store all the loaded data
     * @param typeOfBoard string chosen in the GameBoardFactory containing the path of the config file
     */
    private GameBoard(String typeOfBoard){
        myGameBoard=new Card[BoardConstants.BOARD_DIMENSION][BoardConstants.BOARD_DIMENSION];
        commonObjectives=new ArrayList<>(BoardConstants.TOTAL_CO_PER_GAME);
        allCards=new ArrayList<>(BoardConstants.TOTAL_CARDS);
        r=new Random();
        Gson jsonParser= JsonLoader.getJsonLoader();
        Reader fileReader = null;
        try {
            fileReader = new FileReader(BoardConstants.FILE_CONFIG_GAMEBOARD2);
        }
        catch(FileNotFoundException e){
            System.out.println(e);
        }
        GameBoardConfiguration g=jsonParser.fromJson(fileReader, GameBoardConfiguration.class);

        fillAllCardsList();
        initialGameBoardFill(g.getValidPositions(),g.getInvalidPositions());
        fillPointStack(g.getPointStack());
    }

    /**
     * This method fills the bucket of possible cards with TOTAL_COLORS x TOTAL_CARDS_PER_COLOR =132 cards
     * and a random sprite from 1 to 3
     */
    private void fillAllCardsList(){
        CardColor[] allColors=new CardColor[]{
                CardColor.BLUE,
                CardColor.GREEN,
                CardColor.WHITE,
                CardColor.LIGHT_BLUE,
                CardColor.YELLOW,
                CardColor.VIOLET
        };
        for(int j=0; j<BoardConstants.TOTAL_COLORS;j++){
            for(int i=0; i<BoardConstants.TOTAL_CARDS_PER_COLOR; i++){
                allCards.add(new Card(allColors[j],this.r.nextInt(2)+1));
            }
        }
    }

    /**
     * This method is called only in the constructor and its only purpose is to fill the board with the valid and invalid positions
     * @param validPositions integer matrix loaded from the json config file containing the information of a valid position
     * @param invalidPositions integer matrix loaded from the json config file containing the information of an invalid position
     */
    private void initialGameBoardFill(Integer[][] validPositions, Integer[][] invalidPositions){
        for(int y=0; y<BoardConstants.BOARD_DIMENSION; y++){
            for(int x=0; x<BoardConstants.BOARD_DIMENSION; x++){
                if(validPositions[y][x]==1) myGameBoard[y][x]=new Card(allCards.remove(this.r.nextInt(allCards.size())));
                if(invalidPositions[y][x]==1) myGameBoard[y][x]=new Card(CardColor.INVALID,1);
            }
        }
    }

    /**
     * The method fills the board up until it is possible.
     * It fills just the positions which are empty, so it avoids to overwrite the remaining cards on the board.
     * If at the start of the method the bag is empty, then it throws an exception that needs to be checked
     * If at some moment after it started filling it finds out that the bag is empty, then also needs to throw an exception (different)
     * that can be checked maybe to avoid calling the function again
     * @throws NoMoreCardsAtStartFillBoardException self-explainatory
     * @throws NoMoreCardsToFillBoardException self-explainatory
     */
    public void fillBoard() throws NoMoreCardsAtStartFillBoardException, NoMoreCardsToFillBoardException {
        if(allCards.size()==0) throw new NoMoreCardsAtStartFillBoardException();

        for(int y=0;y<BoardConstants.BOARD_DIMENSION;y++){
            for(int x=0;x<BoardConstants.BOARD_DIMENSION;x++){
                if(!myGameBoard[y][x].isInvalid() && myGameBoard[y][x].isEmpty()){
                    myGameBoard[y][x]=new Card(allCards.remove(this.r.nextInt(allCards.size())));
                    if(allCards.size()==0) throw new NoMoreCardsToFillBoardException();
                }
            }
        }

    }

    /**
     * This method is only called in the constructor and it is used for the creation of the stack for each common objective
     * @param pointStack integer array loaded from the json config file containing the stack of points(from lowest to highest)
     */
    private void fillPointStack(Integer[] pointStack){
        for(int i=0;i<BoardConstants.TOTAL_CO_PER_GAME;i++){
            for(int j=0;j<pointStack.length;j++) {
                this.commonObjectives.get(i).push(pointStack[i]);
            }
        }
    }
    /**
     * This method checks if the board has to be filled.
     * In detail for each card in the board(not invaild and not empty) you look if it has any neighbors not empty.
     * If no tile satisfies the condition then it means there are only lonely "islands" on the board and it should be filled again by calling fillBoard()
     * @return true if the board has to be filled
     */
    public boolean hasToBeFilled() {
        for(int y=0;y<BoardConstants.BOARD_DIMENSION;y++){
            for(int x=0;x<BoardConstants.BOARD_DIMENSION;x++){
                if (!myGameBoard[y][x].isInvalid() && !myGameBoard[y][x].isEmpty()) {
                    if (!this.everyAdjactentEmpty(y,x)) return false;
                }
            }
        }
        return true;
    }

    /**
     * This method is useful to the GameModel to check if the user did input a correct move (not empty or invaild)
     * @param p coordinates of the card
     * @return true if the position is effectively occupied by some valid card
     */
    public boolean positionOccupied(Position p){

        if(!myGameBoard[p.y()][p.x()].isInvalid() && !myGameBoard[p.y()][p.x()].isEmpty()) return true;
        return false;

    }

    /**
     * This method checks if a card has at least one free adjacent space, useful to the GameModel to check also that it is a valid move
     * @param p coordinates of the card
     * @return true if the card in position p has at least one empty space in one of the cardinal directions
     */
    public boolean hasFreeAdjacent(Position p){

        int x=p.x();
        int y=p.y();
        Card c;
        if(x>0){
            c=myGameBoard[p.y()][p.x()-1];
            if(!c.isInvalid()) {
                if (c.isEmpty()) return true;
            }
        }
        if(x<BoardConstants.BOARD_DIMENSION-1){
            c=myGameBoard[p.y()][p.x()+1];
            if(!c.isInvalid()) {
                if (c.isEmpty()) return true;
            }
        }
        if(y>0){
            c=myGameBoard[p.y()-1][p.x()];
            if(!c.isInvalid()) {
                if (c.isEmpty()) return true;
            }
        }
        if(y<BoardConstants.BOARD_DIMENSION-1){
            c=myGameBoard[p.y()+1][p.x()];
            if(!c.isInvalid()) {
                if (c.isEmpty()) return true;
            }
        }
        return true;
    }

    /**
     * This method is similar to hasFreeAdjacent, but it checks that every adjacent is empty.
     * Only called in hasToBeFilled()
     * @param y y coordinate of the card
     * @param x x coordinate of the card
     * @return true if every adjacent position to the card is free
     */
    private boolean everyAdjactentEmpty(int y, int x){
        Card c;
        if(x>0){
            c=myGameBoard[y][x-1];
            if(!c.isInvalid()) {
                if (!c.isEmpty()) return false;
            }
        }
        if(x<BoardConstants.BOARD_DIMENSION-1){
            c=myGameBoard[y][x];
            if(!c.isInvalid()) {
                if (!c.isEmpty()) return false;
            }
        }
        if(y>0){
            c=myGameBoard[y][x];
            if(!c.isInvalid()) {
                if (!c.isEmpty()) return false;
            }
        }
        if(y<BoardConstants.BOARD_DIMENSION-1){
            c=myGameBoard[y][x];
            if(!c.isInvalid()) {
                if (!c.isEmpty()) return false;
            }
        }
        return true;
    }


    public CommonObjective getCommonObjective(int idx){
        return commonObjectives.get(idx);
    }


    /**
     * This method is useful to the GameModel when the player makes a move, it removes it from the current board (sets it to empty) and returns it
     * @param p position of the move that needs to be done (assumed correct, since all the controls are done before)
     * @return the card contained in position p
     */
    public Card removeCard(Position p){
        Card copy=new Card(myGameBoard[p.y()][p.x()]);
        myGameBoard[p.y()][p.x()].setEmpty();
        return copy;
    }

}
