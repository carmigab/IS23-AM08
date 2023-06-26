package it.polimi.ingsw.controller.observers;

import it.polimi.ingsw.model.GameModel;

/**
 * Interface for observer method
 */
public interface Observer {
    /**
     * this method is the classic method update of the observer
     * @param model the model we want to update
     */
    public void update(GameModel model);
}
