package it.polimi.ingsw.model;

import com.google.gson.annotations.Expose;

/**
 * This class is just a utility used for storing the loaded data from the config file
 */
public class PersonalGoalsConfiguration {
    /**
     * This attribute stores all the personal goals read from the config file
     */
    @Expose
    private PersonalGoal[] personalGoals;

    /**
     * Getter of a single Personal shelf
     * @param i index of the personal shelf in the stored array
     * @return personal shelf at index i
     */
    public PersonalGoal getPersonalGoalAtIndex(int i) {
        return personalGoals[i];
    }
}
