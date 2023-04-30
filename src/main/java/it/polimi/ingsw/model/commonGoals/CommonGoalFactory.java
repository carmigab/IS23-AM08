package it.polimi.ingsw.model.commonGoals;

import com.google.gson.Gson;
import it.polimi.ingsw.model.constants.AppConstants;
import it.polimi.ingsw.utilities.JsonWithExposeSingleton;
import it.polimi.ingsw.utilities.UtilityFunctions;
import it.polimi.ingsw.utilities.UtilityFunctionsModel;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;

public class CommonGoalFactory {

    /**
     * This method receive and integer and initialize and return the corresponding common goal
     * @param goalIndex index of the goal to be instantiated
     * @return the goal corresponding to index
     */
    public static CommonGoal createCommonGoal(int goalIndex) {
        Gson jsonLoader = JsonWithExposeSingleton.getJsonWithExposeSingleton();

        // creating configuration from json file for common goal 1 and 2
        NGroupsOfSizeMConfiguration nGroupsOfSizeMConfiguration = jsonLoader.fromJson(UtilityFunctions.getReaderFromFileNameRelativePath(AppConstants.FILE_CONFIG_NGROUPOFSIZEM, CommonGoalFactory.class), NGroupsOfSizeMConfiguration.class);

        // creating configuration from json file for common goal 5, 8, 9 and 10
        NLinesOfAtMostMDifferentColorsConfiguration nLinesOfAtMostMDifferentColorsConfiguration = jsonLoader.fromJson(UtilityFunctions.getReaderFromFileNameRelativePath(AppConstants.FILE_CONFIG_NLINESOFATMOSTMDIFFERENTCOLORS, CommonGoalFactory.class), NLinesOfAtMostMDifferentColorsConfiguration.class);

        // creating configuration from json file for common goal 3, 7 and 11
        SingleOccurrenceOfGivenShapeConfiguration singleOccurrenceOfGivenShapeConfiguration = jsonLoader.fromJson(UtilityFunctions.getReaderFromFileNameRelativePath(AppConstants.FILE_CONFIG_SINGLEOCCURRENCEOFGIVENSHAPE, CommonGoalFactory.class), SingleOccurrenceOfGivenShapeConfiguration.class);

        CommonGoal commonGoal;

        commonGoal = switch (goalIndex) {
            case 0 -> nGroupsOfSizeMConfiguration.getGoalAt(0);
            case 1 -> nGroupsOfSizeMConfiguration.getGoalAt(1);
            case 2 -> singleOccurrenceOfGivenShapeConfiguration.getGoalAt(0);
            case 3 -> new TwoSquares();
            case 4 -> nLinesOfAtMostMDifferentColorsConfiguration.getGoalAt(0);
            case 5 -> new EightTilesOfTheSameColor();
            case 6 -> singleOccurrenceOfGivenShapeConfiguration.getGoalAt(1);
            case 7 -> nLinesOfAtMostMDifferentColorsConfiguration.getGoalAt(1);
            case 8 -> nLinesOfAtMostMDifferentColorsConfiguration.getGoalAt(2);
            case 9 -> nLinesOfAtMostMDifferentColorsConfiguration.getGoalAt(3);
            case 10 -> singleOccurrenceOfGivenShapeConfiguration.getGoalAt(2);
            default -> new Ladder();
        };

        return commonGoal;
    }

}
