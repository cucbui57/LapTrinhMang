package Model.AddParticipant;

import Model.Conversation;
import Model.Participant;

import java.util.Vector;

public class AddParticipantRequest {
    private Conversation conversation;
    private Vector<Participant> participants;

    public AddParticipantRequest() {
    }

    public AddParticipantRequest(Conversation conversation, Vector<Participant> participants) {

        this.conversation = conversation;
        this.participants = participants;
    }

    public Conversation getConversation() {

        return conversation;
    }

    public void setConversation(Conversation conversation) {
        this.conversation = conversation;
    }

    public Vector<Participant> getParticipants() {
        return participants;
    }

    public void setParticipants(Vector<Participant> participants) {
        this.participants = participants;
    }
}
