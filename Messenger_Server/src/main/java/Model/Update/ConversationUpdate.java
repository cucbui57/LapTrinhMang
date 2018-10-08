package Model.Update;

import Model.Conversation;
import Model.Message;
import Model.Participant;

import java.util.Vector;

public class ConversationUpdate {
    private Conversation conversation;
    private Vector<Participant> participants;
    private Vector<Message> messages;
    private Vector<AttachFile> attachFiles;

    public ConversationUpdate() {
    }

    public ConversationUpdate(Conversation conversation, Vector<Participant> participants, Vector<Message> messages, Vector<AttachFile> attachFiles) {

        this.conversation = conversation;
        this.participants = participants;
        this.messages = messages;
        this.attachFiles = attachFiles;
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

    public Vector<AttachFile> getAttachFiles() {
        return attachFiles;
    }

    public void setAttachFiles(Vector<AttachFile> attachFiles) {
        this.attachFiles = attachFiles;
    }
}
