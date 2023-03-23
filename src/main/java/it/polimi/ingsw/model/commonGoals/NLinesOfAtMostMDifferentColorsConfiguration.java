package it.polimi.ingsw.model.commonGoals;

import java.util.List;

/**
 * This class is just a utility used for storing the loaded data from the config file
 */
public class NLinesOfAtMostMDifferentColorsConfiguration {
    /**
     * This attribute stores all the NLinesOfAtMostMDifferentColors read from the config file
     */
    private List<NLinesOfAtMostMDifferentColors> goalList;

    /**
     * This method return the goal at the given index
     * @param index index of the goal you want to retrieve
     * @return the requested goal
     */
    public NLinesOfAtMostMDifferentColors getGoalAt(int index) {
        return goalList.get(index);
    }
}
