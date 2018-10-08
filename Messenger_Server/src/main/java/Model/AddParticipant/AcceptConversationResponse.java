package Model.AddParticipant;

import Model.Conversation;
import Model.Participant;

public class AcceptConversationResponse {
    private Participant participant;
    private Conversation conversation;

    public AcceptConversationResponse() {
    }

    public AcceptConversationResponse(Participant participant, Conversation conversation) {

        this.participant = participant;
        this.conversation = conversation;
    }

    public Participant getParticipant() {

        return participant;
    }

    public void setParticipant(Participant participant) {
        this.participant = participant;
    }

    public Conversation getConversation() {
        return conversation;
    }

    public void setConversation(Conversation conversation) {
        this.conversation = conversation;
    }
}
