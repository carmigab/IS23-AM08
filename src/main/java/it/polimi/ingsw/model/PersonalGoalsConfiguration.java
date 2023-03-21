package it.polimi.ingsw.model;

/**
 * This class is just a utility used for storing the loaded data from the config file
 */
public class PersonalGoalsConfiguration {
    /**
     * This attribute stores all the personal shelfs read from the config file
     */
    private PersonalGoal[] personalGoals;
    /**
     * This attribute stores the array of points for each completed personal shelf read from the config file
     */
    private int[] pointsForCompletion;

    /**
     * Getter of the points
     * @return array of the points
     */
    public int[] getPointsForCompletion() {
        return pointsForCompletion;
    }

    /**
     * Getter of a single Personal shelf
     * @param i index of the personal shelf in the stored array
     * @return personal shelf at index i
     */
    public PersonalGoal getPersonalGoalAtIndex(int i) {
        return personalGoals[i];
    }
}
