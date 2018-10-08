package Model.CreateConversation;

import Model.Conversation;
import Model.Participant;

import java.util.Vector;

public class AcceptNewConversationClientSend2Server {
    private Participant participant;
    private Vector<Integer> participants_id;

    public AcceptNewConversationClientSend2Server(Participant participant, Vector<Integer> participants_id) {
        this.participant = participant;
        this.participants_id = participants_id;
    }

    public Vector<Integer> getParticipants_id() {

        return participants_id;
    }

    public void setParticipants_id(Vector<Integer> participants_id) {
        this.participants_id = participants_id;
    }

    public Participant getParticipant() {

        return participant;
    }

    public void setParticipant(Participant participant) {
        this.participant = participant;
    }
}
