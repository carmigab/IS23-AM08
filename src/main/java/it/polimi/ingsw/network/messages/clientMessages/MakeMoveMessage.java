package it.polimi.ingsw.network.messages.clientMessages;

import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.network.messages.Message;

import java.util.List;

public class MakeMoveMessage extends Message {
    private List<Position> pos;
    private int col;

    public MakeMoveMessage(String text, List<Position> pos, int col) {
        super(text);
        this.pos = pos;
        this.col = col;
    }

    public List<Position> getPositions(){
        return this.pos;
    }

    public int getColumn(){
        return this.col;
    }
}
