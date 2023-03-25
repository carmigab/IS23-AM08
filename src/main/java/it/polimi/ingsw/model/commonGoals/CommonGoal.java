package it.polimi.ingsw.model.commonGoals;

import it.polimi.ingsw.model.Shelf;

import java.util.Stack;

/**
 * This is the class that represents the common goal
 * It is abstract since every goal will be an extension of this class following the pattern Strategy, overriding the method for the evaluation of the goal
 */
public abstract class CommonGoal {

    /**
     * This Stack is the data structure of the points available for grabbing in the common goal
     */
    private Stack<Integer> pointStack;

    /**
     * The constructor just initializes the stack, since the addition of the numbers will be done in the GameBoard class
     */
    public CommonGoal(){
        pointStack=new Stack<>();
    }

    /**
     * This method is the push function for a usual stack
     * @param i integer to push in the stack
     */
    public void push(Integer i){
        if (this.pointStack == null) this.pointStack = new Stack<>();
        this.pointStack.push(i);
    }

    /**
     * This method is the push function for a usual stack
     * @return pop of the integer from the stack
     */
    public Integer pop(){
        return this.pointStack.pop();
    }

    /**
     * This method has to be overridden in its subclasses, with each implementation being a different algorithm to calculate the goal
     * @param shelf Shelf of the current player
     * @return true if the goal has been satisfied
     */
    public abstract boolean evaluate(Shelf shelf);

}
