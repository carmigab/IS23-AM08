package it.polimi.ingsw.model;

/**
 * This class is just a utility used for storing the loaded data from the config file
 */
public class PersonalObjectivesConfiguration {
    /**
     * This attribute stores all the personal objectives read from the config file
     */
    private PersonalObjective[] personalObjectives;
    /**
     * This attribute stores the array of points for each completed personal objective read from the config file
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
     * Getter of a single Personal objective
     * @param i index of the personal objective in the stored array
     * @return personal objective at index i
     */
    public PersonalObjective getPersonalObjectiveAtIndex(int i) {
        return personalObjectives[i];
    }
}
