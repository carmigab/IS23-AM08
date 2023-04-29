package it.polimi.ingsw.network.messages.clientMessages;

import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.network.messages.Message;

import java.util.List;

/**
 * This class represents a message to make a move
 */
public class MakeMoveMessage extends Message {
    /**
     * the list of tile positions
     */
    private List<Position> pos;
    /**
     * the column
     */
    private int col;

    /**
     * the constructor
     * @param sender: the onw who sends the message
     * @param pos: the list of tile positions
     * @param col: the column
     */
    public MakeMoveMessage(String sender, List<Position> pos, int col) {
        super(sender);
        this.pos = pos;
        this.col = col;
        setMessageType("MakeMoveMessage");
    }

    /**
     * method to get the chosen positions
     * @return the positions
     */
    public List<Position> getPositions(){
        return this.pos;
    }

    /**
     * method to get the chosen column
     * @return the column
     */
    public int getColumn(){
        return this.col;
    }
}
