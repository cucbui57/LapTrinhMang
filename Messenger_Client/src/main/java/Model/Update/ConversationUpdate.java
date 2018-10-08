package Model.Update;

import Model.Conversation;
import Model.Message;
import Model.Participant;

import java.util.Vector;

public class ConversationUpdate {
    private Conversation conversation;
    private Vector<Participant> participants;
    private Vector<Message> messages;

    public ConversationUpdate() {
    }

    public ConversationUpdate(Conversation conversation, Vector<Participant> participants, Vector<Message> messages) {

        this.conversation = conversation;
        this.participants = participants;
        this.messages = messages;
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

    public Vector<Message> getMessages() {
        return messages;
    }

    public void setMessages(Vector<Message> messages) {
        this.messages = messages;
    }
}
