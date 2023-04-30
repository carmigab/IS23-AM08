package it.polimi.ingsw.network.messages.serverMessages;

import it.polimi.ingsw.network.messages.Message;

/**
 * This method represents the response to a ChooseNickname message
 */
public class ChooseNicknameResponse extends Message {
    /**
     * A flag that represents the response (true if successful)
     */
    private boolean response;

    /**
     * the constructor
     * @param sender: the one who sends the message
     * @param response: A flag that represents the response
     */
    public ChooseNicknameResponse(String sender, boolean response) {
        super(sender);
        this.response = response;
        setMessageType("ChooseNicknameResponse ");
    }

    /**
     * Method to get the response
     * @return the response
     */
    public boolean getResponse(){
        return this.response;
    }

}
