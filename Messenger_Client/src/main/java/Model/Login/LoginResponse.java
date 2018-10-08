package Model.Login;

import Model.*;

import java.util.HashMap;
import java.util.Vector;

public class LoginResponse {
    private boolean accepted;
    private User user;
    private Vector<Contact> contacts;
    private Vector<Conversation> conversations;
    private HashMap<Integer, Vector<Message>> messages;

    public LoginResponse() {
    }

    public LoginResponse(boolean accepted, User user, Vector<Contact> contacts, Vector<Conversation> conversations, HashMap<Integer, Vector<Message>> messages) {
        this.accepted = accepted;
        this.user = user;
        this.contacts = contacts;
        this.conversations = conversations;
        this.messages = messages;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Vector<Contact> getContacts() {
        return contacts;
    }

    public void setContacts(Vector<Contact> contacts) {
        this.contacts = contacts;
    }

    public Vector<Conversation> getConversations() {
        return conversations;
    }

    public void setConversations(Vector<Conversation> conversations) {
        this.conversations = conversations;
    }

    public HashMap<Integer, Vector<Message>> getMessages() {
        return messages;
    }

    public void setMessages(HashMap<Integer, Vector<Message>> messages) {
        this.messages = messages;
    }
}
