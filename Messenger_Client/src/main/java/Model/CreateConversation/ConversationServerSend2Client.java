package Model.CreateConversation;

import Model.Conversation;
import Model.Message;
import Model.Participant;

import java.util.Vector;

public class ConversationServerSend2Client {
    private Conversation conversation;
    private Vector<Message> messages;
    private Vector<Participant> participants;

    public ConversationServerSend2Client() {
    }

    public ConversationServerSend2Client(Conversation conversation, Vector<Message> messages, Vector<Participant> participants) {

        this.conversation = conversation;
        this.messages = messages;
        this.participants = participants;
    }

    public Conversation getConversation() {

        return conversation;
    }

    public void setConversation(Conversation conversation) {
        this.conversation = conversation;
    }

    public Vector<Message> getMessages() {
        return messages;
    }

    public void setMessages(Vector<Message> messages) {
        this.messages = messages;
    }

    public Vector<Participant> getParticipants() {
        return participants;
    }

    public void setParticipants(Vector<Participant> participants) {
        this.participants = participants;
    }
}
