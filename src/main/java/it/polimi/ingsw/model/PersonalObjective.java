package it.polimi.ingsw.model;

import it.polimi.ingsw.model.constants.AppConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * This is the class containing all of the information for the storage of a single personal objective
 */
public class PersonalObjective {

    /**
     * This attribute stores a list of 6 objects of class SingleObjective
     */
    private List<SingleObjective> personalObjective;

    /**
     * This attribute stores points assigned for the completion of the objective(in order from lowest to highest)
     */
    private List<Integer> pointsForCompletion;

    /**
     * This is the constructor, it should never be called since the information is created when the file is loaded
     * @param s array of single objectives used to construct the object
     */
    public PersonalObjective(SingleObjective[] s){
        this.personalObjective=new ArrayList<>(AppConstants.TOTAL_POINTS_FOR_PO);

        for(int i=0;i<6;i++){
            this.personalObjective.add(new SingleObjective(s[i].getPosition(),s[i].getColor()));
        }

        this.pointsForCompletion=new ArrayList<>(AppConstants.TOTAL_POINTS_FOR_PO);
    }

    /**
     * This method inserts manually the points assigned for the completion of the objectives
     * @param points array of integers
     */
    public void setPointsForCompletion(int[] points){
        for(int i=0;i<points.length;i++) this.pointsForCompletion.add(points[i]);
    }

    /**
     * This method calculates how many steps of the personal objective have been reached
     * @param lib library of the player
     * @return points assigned for the objective (0 if none)
     */
    public Integer evaluate(Library lib){
        int objCompleted=-1;

        for(SingleObjective s: this.personalObjective) if(lib.getCard(s.getPosition()).getColor().equals(s.getColor())) objCompleted++;

        if(objCompleted<0) return 0;
        return this.pointsForCompletion.get(objCompleted);
    }
}























