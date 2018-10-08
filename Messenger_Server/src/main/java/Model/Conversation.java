package Model;

import java.sql.Date;
import java.util.Vector;

public class Conversation {
    private int conversation_id;
    private String title;
    private Vector<Participant> participants;

    public Conversation() {
    }

    public Conversation(int conversation_id, String title) {
        this.conversation_id = conversation_id;
        this.title = title;
    }

    public Conversation(int conversation_id, String title, Vector<Participant> participants) {
        this.conversation_id = conversation_id;
        this.title = title;
        this.participants = participants;
    }

    public int getConversation_id() {
        return conversation_id;
    }

    public void setConversation_id(int conversation_id) {
        this.conversation_id = conversation_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Vector<Participant> getParticipants() {
        return participants;
    }

    public void setParticipants(Vector<Participant> participants) {
        this.participants = participants;
    }
}
