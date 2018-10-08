package Model.CreateConversation;

import Model.*;

import java.util.Vector;

public class CreateConversationServerSend2Clients {
    private Vector<Participant> participants;
    private Conversation conversation;

    public CreateConversationServerSend2Clients() {
        participants = new Vector<Participant>();
    }

    public CreateConversationServerSend2Clients(Vector<Participant> participants, Conversation conversation) {
        this.participants = participants;
        this.conversation = conversation;
    }

    public Vector<Participant> getParticipants() {

        return participants;
    }

    public void setParticipants(Vector<Participant> participants) {
        this.participants = participants;
    }

    public Conversation getConversation() {
        return conversation;
    }

    public void setConversation(Conversation conversation) {
        this.conversation = conversation;
    }
}
