package Model.AddParticipant;

import Model.Conversation;
import Model.Participant;

import java.util.Vector;

public class AcceptConversationRequest {
    private Participant participant;
    private Conversation conversation;
    private Vector<Integer> participants_id;

    public AcceptConversationRequest() {
    }

    public AcceptConversationRequest(Participant participant, Conversation conversation, Vector<Integer> participants_id) {
        this.participant = participant;
        this.conversation = conversation;
        this.participants_id = participants_id;
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

    public Vector<Integer> getParticipants_id() {
        return participants_id;
    }

    public void setParticipants_id(Vector<Integer> participants_id) {
        this.participants_id = participants_id;
    }
}
