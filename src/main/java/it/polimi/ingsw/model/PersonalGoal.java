package it.polimi.ingsw.model;

import com.google.gson.annotations.Expose;
import it.polimi.ingsw.App;
import it.polimi.ingsw.model.constants.AppConstants;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * This is the class containing all the information for the storage of a single personal shelf
 */
public class PersonalGoal implements Serializable {

    /**
     * This attribute stores a list of 6 objects of class SingleGoal
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
     * @param points array of poins assigned for the completion of the objective
     */
    public PersonalGoal(SingleGoal[] s, int[] points){
        this.personalGoal =new ArrayList<>(AppConstants.TOTAL_POINTS_FOR_PG);

        for(int i=0;i<AppConstants.TOTAL_POINTS_FOR_PG;i++){
            this.personalGoal.add(new SingleGoal(s[i].getPosition(),s[i].getColor()));
        }

        this.pointsForCompletion=new ArrayList<>(AppConstants.TOTAL_POINTS_FOR_PG);
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
     *
     * @param obj
     * @return
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
    public PersonalGoal getPersonalGoalCopy(){
        SingleGoal[] sgCopy=new SingleGoal[this.personalGoal.size()];
        for(int i=0;i<AppConstants.TOTAL_POINTS_FOR_PG;i++){
            sgCopy[i]=this.personalGoal.get(i);
        }
        int[] pointsCopy=new int[this.personalGoal.size()];
        for(int i=0;i<AppConstants.TOTAL_POINTS_FOR_PG;i++){
            pointsCopy[i]=this.pointsForCompletion.get(i);
        }
        return new PersonalGoal(sgCopy,pointsCopy);
    }
}























