package it.polimi.ingsw.model;

import com.google.gson.annotations.Expose;
import it.polimi.ingsw.constants.ModelConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * This is the class containing all the information for the storage of a single personal goal
 */
public class PersonalGoal {

    /**
     * This attribute stores a list of 6 objects of the class SingleGoal
     */
    @Expose
    private List<SingleGoal> personalGoal;

    /**
     * This attribute stores points assigned for the completion of the shelf(in order from lowest to highest)
     */
    @Expose
    private List<Integer> pointsForCompletion;

    /**
     * This is the constructor, it should never be called since the information is created when the file is loaded
     * @param s array of single goals used to construct the object
     * @param points array of points assigned for the completion of the objective
     */
    public PersonalGoal(SingleGoal[] s, int[] points){
        this.personalGoal =new ArrayList<>(ModelConstants.SINGLE_GOALS_NUMBER);

        for(int i = 0; i< ModelConstants.SINGLE_GOALS_NUMBER; i++){
            this.personalGoal.add(new SingleGoal(s[i].getPosition(),s[i].getColor()));
        }

        this.pointsForCompletion=new ArrayList<>(ModelConstants.SINGLE_GOALS_NUMBER);
        for (int point : points) this.pointsForCompletion.add(point);
    }

    /**
     * This method calculates how many steps of the personal shelf have been reached
     * @param shelf shelf of the player
     * @return points assigned for the shelf (0 if none)
     */
    public Integer evaluate(Shelf shelf){
        int objCompleted=-1;

        for(SingleGoal s: this.personalGoal) if(shelf.getTile(s.getPosition()).getColor().equals(s.getColor())) objCompleted++;

        if(objCompleted<0) return 0;
        return this.pointsForCompletion.get(objCompleted);
    }

    /**
     * Override of equality operator
     * @param obj: the object to check
     * @return true if objects are equals
     */
    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof PersonalGoal personalGoal)) return false;
        return this.pointsForCompletion.equals(personalGoal.pointsForCompletion) &&
                this.personalGoal.equals(personalGoal.personalGoal);
    }

    /**
     * This method returns a full copy of the personal goal, with copies also of the single goals
     * @return a copy of the personal goal
     */
    public List<SingleGoal> getCopy(){
        List<SingleGoal> sgCopy = new ArrayList<>(this.personalGoal.size());
        for(int i = 0; i< ModelConstants.SINGLE_GOALS_NUMBER; i++){
            sgCopy.add(this.personalGoal.get(i));
        }
        return sgCopy;
    }
}























