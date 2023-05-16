package it.polimi.ingsw.computer.neuralNetwork;

import java.util.*;

import it.polimi.ingsw.model.Shelf;
import it.polimi.ingsw.model.TileColor;

public class TrainingModel {

    private final Integer INPUT_SIZE=312;
    private final Integer OUTPUT_SIZE=263;
    private final Double LEARNING_RATE=0.1;
    private final Integer POSSIBLE_MOVES_SIZE=258;
    private final Integer POSSIBLE_SINGLE_MOVES_SIZE=6;
    private final Integer POSSIBLE_DOUBLE_MOVES_SIZE=36;
    private final Integer POSSIBLE_TRIPLE_MOVES_SIZE=216;
    private final Integer COMMON_GOAL_SIZE=12;
    private final Integer COMMON_GOAL_SELECTED=2;
    private final Integer PERSONAL_GOAL_SIZE=12;
    private final Integer PERSONAL_GOAL_SELECTED=1;
    private final Integer SHELF_DIMENSION=30;
    private final Integer NUMBER_COLORS=6;
    private final Double SINGLE_TILES_SELECTED=0.995;
    private final Double DOUBLE_TILES_SELECTED=0.925;
    private final Double TRIPLE_TILES_SELECTED=0.35;
    private final SimplifiedNeuralNetwork snn;

    private final Shelf trainingShelf;
    private final List<Integer> possibleMoves;

    private final List<Integer> selectedCommonGoal;
    private final List<Integer> selectedPersonalGoal;
    private final Map<TileColor, Integer> colorsMapper;
    private final Map<Integer, Integer> movesMapper;
    private final TileColor[] allColors;

    //GREEN      0
    //WHITE      1
    //BLUE       2
    //YELLOW     3
    //LIGHT_BLUE 4
    //VIOLET     5

    public TrainingModel() {
        this.snn = new SimplifiedNeuralNetwork(INPUT_SIZE, OUTPUT_SIZE, LEARNING_RATE);
        this.trainingShelf = new Shelf();
        this.possibleMoves = new ArrayList<>(this.POSSIBLE_MOVES_SIZE);
        this.selectedCommonGoal = new ArrayList<>(this.COMMON_GOAL_SIZE);
        this.selectedPersonalGoal = new ArrayList<>(this.PERSONAL_GOAL_SIZE);
        this.colorsMapper=new HashMap<>();
        this.movesMapper=new HashMap<>();
        this.allColors=new TileColor[]{
                TileColor.BLUE,
                TileColor.GREEN,
                TileColor.WHITE,
                TileColor.CYAN,
                TileColor.YELLOW,
                TileColor.VIOLET
        };
        initializeMappers();
    }

    private void initializeMappers(){

        this.colorsMapper.put(TileColor.GREEN,1);
        this.colorsMapper.put(TileColor.WHITE,2);
        this.colorsMapper.put(TileColor.BLUE,3);
        this.colorsMapper.put(TileColor.YELLOW,4);
        this.colorsMapper.put(TileColor.CYAN,5);
        this.colorsMapper.put(TileColor.VIOLET,6);

        List<TileColor> currentMove=new ArrayList<>(3);

        Integer iterator=0;

        // to every possible move i associate a unique number

        //single moves
        for(int i=0;i<this.POSSIBLE_SINGLE_MOVES_SIZE;i++){
            currentMove.clear();
            currentMove.add(this.allColors[i]);
            this.movesMapper.put(this.getNumberFromListOfColors(currentMove),iterator++);
        }
        //double moves
        for(int i=0;i<this.POSSIBLE_SINGLE_MOVES_SIZE;i++){
            for(int j=0;j<this.POSSIBLE_SINGLE_MOVES_SIZE;j++) {
                currentMove.clear();
                currentMove.add(this.allColors[i]);
                currentMove.add(this.allColors[j]);
                this.movesMapper.put(this.getNumberFromListOfColors(currentMove), iterator++);
            }
        }
        //triple moves
        for(int i=0;i<this.POSSIBLE_SINGLE_MOVES_SIZE;i++){
            for(int j=0;j<this.POSSIBLE_SINGLE_MOVES_SIZE;j++) {
                for(int k=0;k<this.POSSIBLE_SINGLE_MOVES_SIZE;k++) {
                    currentMove.clear();
                    currentMove.add(this.allColors[i]);
                    currentMove.add(this.allColors[j]);
                    currentMove.add(this.allColors[k]);
                    this.movesMapper.put(this.getNumberFromListOfColors(currentMove), iterator++);
                }
            }
        }

    }

    private Integer getNumberFromListOfColors(List<TileColor> list){
        Integer sum=0;

        for(int i=list.size()-1;i>=0;i--){
            sum+=this.intPow(10, (list.size()-1-i))*(this.colorsMapper.get(list.get(i))+1);
        }

        return sum;
    }

    private List<TileColor> getColorFromNumber(Integer i){
        List<TileColor> toReturn=new ArrayList<>();

        for(;i<10;){
            toReturn.add(0,this.allColors[i%10]);
            i = (i-(i%10))/10;
        }
        toReturn.add(0,this.allColors[i%10]);

        return toReturn;
    }

    private Integer intPow(Integer a, Integer b){
        Integer partialProduct=1;
        for(int i=0;i<b;i++){
            partialProduct*=a;
        }
        return partialProduct;
    }

    private Set<Integer> getAllPermutationsOfMove(Integer i){
        Set<Integer> toReturn=new HashSet<>();
        List<TileColor> currMove=this.getColorFromNumber(i);
        if(currMove.size()==1){
            toReturn.add(i);
        }
        if(currMove.size()==2){
            toReturn.add(i);
            List<TileColor> temp = new ArrayList<>();
            temp.add(0,currMove.get(1));
            temp.add(1,currMove.get(0));
            toReturn.add(this.getNumberFromListOfColors(temp));
        }
        if(currMove.size()==3){
            toReturn.add(i);
            List<TileColor> temp = new ArrayList<>();
            temp.add(0,currMove.get(0));
            temp.add(1,currMove.get(2));
            temp.add(2,currMove.get(1));
            toReturn.add(this.getNumberFromListOfColors(temp));
            temp.clear();
            temp.add(0,currMove.get(1));
            temp.add(1,currMove.get(0));
            temp.add(2,currMove.get(2));
            toReturn.add(this.getNumberFromListOfColors(temp));
            temp.clear();
            temp.add(0,currMove.get(1));
            temp.add(1,currMove.get(2));
            temp.add(2,currMove.get(0));
            toReturn.add(this.getNumberFromListOfColors(temp));
            temp.clear();
            temp.add(0,currMove.get(2));
            temp.add(1,currMove.get(0));
            temp.add(2,currMove.get(1));
            toReturn.add(this.getNumberFromListOfColors(temp));
            temp.clear();
            temp.add(0,currMove.get(2));
            temp.add(1,currMove.get(1));
            temp.add(2,currMove.get(0));
            toReturn.add(this.getNumberFromListOfColors(temp));
            temp.clear();
        }
        return toReturn;
    }

}
