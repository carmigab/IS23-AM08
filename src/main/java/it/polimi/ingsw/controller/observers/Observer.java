package it.polimi.ingsw.controller.observers;

import it.polimi.ingsw.model.GameModel;

/**
 * Interface for observer method
 */
public interface Observer {

    /**
     * this method is the classic method update of an observer
     * @param model the model it will update
     */
    public void update(GameModel model);
}
