package Model.CreateConversation;

import Model.Conversation;
import Model.Participant;
import Model.User;

import java.io.Serializable;
import java.util.Vector;

public class CreateConversationRequest implements Serializable {
    private Conversation conversation;
    private Vector<User> users;

    public CreateConversationRequest() {
    }

    public CreateConversationRequest(Conversation conversation, Vector<User> users) {

        this.conversation = conversation;
        this.users = users;
    }

    public Conversation getConversation() {
        return conversation;
    }

    public void setConversation(Conversation conversation) {
        this.conversation = conversation;
    }

    public Vector<User> getUsers() {
        return users;
    }

    public void setUsers(Vector<User> users) {
        this.users = users;
    }
}
