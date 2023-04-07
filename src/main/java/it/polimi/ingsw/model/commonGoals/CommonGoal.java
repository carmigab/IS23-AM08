package it.polimi.ingsw.model.commonGoals;

import com.google.gson.annotations.Expose;
import it.polimi.ingsw.model.Shelf;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

/**
 * This is the class that represents the common goal
 * It is abstract since every goal will be an extension of this class following the pattern Strategy, overriding the method for the evaluation of the goal
 */
public abstract class CommonGoal {

    /**
     * This method has to be overridden in its subclasses, with each implementation being a different algorithm to calculate the goal
     * @param shelf Shelf of the current player
     * @return true if the goal has been satisfied
     */
    public abstract boolean evaluate(Shelf shelf);

}
