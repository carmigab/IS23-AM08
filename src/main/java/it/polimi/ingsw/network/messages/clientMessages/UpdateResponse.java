package it.polimi.ingsw.network.messages.clientMessages;

import it.polimi.ingsw.network.messages.Message;

public class UpdateResponse extends Message {
    public UpdateResponse(String sender) {
        super(sender);
    }
}
