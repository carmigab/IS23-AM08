package it.polimi.ingsw;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 */
public abstract class GameBoard {
    /**
     *
     */
    private final Card[][] myGameBoard;
    /**
     *
     */
    protected final List<CommonObjective> commonObjectives;
    /**
     *
     */
    private final List<Card> allCards;
    /**
     *
     */
    private Integer remainingCards;
    /**
     *
     */
    private Random r;

    /**
     * TODO: decide which common objectives should be passed, maybe through the constructor
     *
     */
    public GameBoard(){
        myGameBoard=new Card[AppConstants.BOARD_DIMENSION][AppConstants.BOARD_DIMENSION];
        commonObjectives=new ArrayList<>(AppConstants.TOTAL_CO_PER_GAME);
        allCards=new ArrayList<>(AppConstants.TOTAL_CARDS);
        remainingCards=AppConstants.TOTAL_CARDS;
        r=new Random();
        fillAllCardsList();
        fillBoardInvalidPositions(createInvalidPositions());
        initialGameBoardFill(createValidPositions());
        fillPointStack();
    }

    /**
     *
     */
    private void fillAllCardsList(){
        for(int i=0; i<AppConstants.TOTAL_CARDS_PER_COLOR; i++){
            allCards.add(new Card(CardColor.BLUE,this.r.nextInt(2)+1));
        }
        for(int i=0; i<AppConstants.TOTAL_CARDS_PER_COLOR; i++){
            allCards.add(new Card(CardColor.YELLOW,this.r.nextInt(2)+1));
        }
        for(int i=0; i<AppConstants.TOTAL_CARDS_PER_COLOR; i++){
            allCards.add(new Card(CardColor.GREEN,this.r.nextInt(2)+1));
        }
        for(int i=0; i<AppConstants.TOTAL_CARDS_PER_COLOR; i++){
            allCards.add(new Card(CardColor.LIGHT_BLUE,this.r.nextInt(2)+1));
        }
        for(int i=0; i<AppConstants.TOTAL_CARDS_PER_COLOR; i++){
            allCards.add(new Card(CardColor.WHITE,this.r.nextInt(2)+1));
        }
        for(int i=0; i<AppConstants.TOTAL_CARDS_PER_COLOR; i++){
            allCards.add(new Card(CardColor.VIOLET,this.r.nextInt(2)+1));
        }
    }
    /**
     *
     */
    private void initialGameBoardFill(int[][] validPositions){
        for(int y=0; y<AppConstants.BOARD_DIMENSION; y++){
            for(int x=0; x<AppConstants.BOARD_DIMENSION; x++){
                if(validPositions[y][x]==1) myGameBoard[y][x]=new Card(allCards.remove(this.r.nextInt(this.remainingCards--)));
            }
        }
    }
    /**
     *
     */
    private void fillBoardInvalidPositions(int[][] invalidPositions){
        for(int y=0; y<AppConstants.BOARD_DIMENSION; y++){
            for(int x=0; x<AppConstants.BOARD_DIMENSION; x++){
                if(invalidPositions[y][x]==1) myGameBoard[y][x]=new Card(CardColor.INVALID,1);
            }
        }
    }
    /**
     *
     */
    protected abstract int[][] createValidPositions();
    /**
     *
     */
    protected abstract int[][] createInvalidPositions();

    /**
     * TODO
     * @return
     */
    public boolean fillBoard(){
        return true;
    }
    /**
     *
     */
    protected abstract void fillPointStack();
    /**
     *
     * @return
     */
    public boolean hasToBeFilled() {
        for(int y=0;y<AppConstants.BOARD_DIMENSION;y++){
            for(int x=0;x<AppConstants.BOARD_DIMENSION;x++){
                if (!myGameBoard[y][x].isInvalid() && !myGameBoard[y][x].isEmpty()) {
                    if (!this.everyAdjactentEmpty(y,x)) return false;
                }
            }
        }
        return true;
    }

    /**
     *
     * @param p
     * @return
     */
    public boolean positionOccupied(Position p){

        if(!myGameBoard[p.getY()][p.getX()].isInvalid() && !myGameBoard[p.getY()][p.getX()].isEmpty()) return true;
        return false;

    }

    /**
     *
     * @param p
     * @return
     */
    public boolean hasFreeAdjacent(Position p){

        int x=p.getX();
        int y=p.getY();
        Card c;
        if(x>0){
            c=myGameBoard[p.getY()][p.getX()-1];
            if(!c.isInvalid()) {
                if (c.isEmpty()) return true;
            }
        }
        if(x<AppConstants.BOARD_DIMENSION-1){
            c=myGameBoard[p.getY()][p.getX()+1];
            if(!c.isInvalid()) {
                if (c.isEmpty()) return true;
            }
        }
        if(y>0){
            c=myGameBoard[p.getY()-1][p.getX()];
            if(!c.isInvalid()) {
                if (c.isEmpty()) return true;
            }
        }
        if(y<table.getCOLS()-1){
            c=myGameBoard[p.getY()+1][p.getX()];
            if(!c.isInvalid()) {
                if (c.isEmpty()) return true;
            }
        }
        return true;
    }

    /**
     *
     * @param y
     * @param x
     * @return
     */
    private boolean everyAdjactentEmpty(int y, int x){
        Card c;
        if(x>0){
            c=myGameBoard[y][x-1];
            if(!c.isInvalid()) {
                if (!c.isEmpty()) return false;
            }
        }
        if(x<AppConstants.BOARD_DIMENSION-1){
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
        if(y<table.getCOLS()-1){
            c=myGameBoard[y][x];
            if(!c.isInvalid()) {
                if (!c.isEmpty()) return false;
            }
        }
        return true;
    }

    /**
     *
     * @param numObjective
     * @param l
     * @return
     */
    public Integer evaluateCommonObjective1(Integer numObjective, Library l){
        if(commonObjectives.get(numObjective).evaluate(l)){
            return commonObjectives.get(numObjective).pop();
        }
        return 0;
    }

    /**
     *
     * @param p
     * @return
     */
    public Card removeCard(Position p){
        Card copy=new Card(myGameBoard[p.getY()][p.getX()]);
        myGameBoard[p.getY()][p.getX()].setEmpty();
        return copy;
    }

}
