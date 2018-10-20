package Model;

import java.sql.Date;
import java.util.Vector;

public class Conversation {
    private int conversation_id;
    private String title;
    private Vector<Participant> participants;
    private Vector<User> users;
    private Date date_created;
    private Date date_deleted;

    public Conversation() {
    }

    public Conversation(int conversation_id, String title, Vector<Participant> participants, Vector<User> users, Date date_created, Date date_deleted) {
        this.conversation_id = conversation_id;
        this.title = title;
        this.participants = participants;
        this.users = users;
        this.date_created = date_created;
        this.date_deleted = date_deleted;
    }

    public Conversation(int conversation_id, String title, Date date_created, Date date_deleted) {
        this.conversation_id = conversation_id;
        this.title = title;
        this.date_created = date_created;
        this.date_deleted = date_deleted;
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

    public Vector<User> getUsers() {
        return users;
    }

    public void setUsers(Vector<User> users) {
        this.users = users;
    }

    public Date getDate_created() {
        return date_created;
    }

    public void setDate_created(Date date_created) {
        this.date_created = date_created;
    }

    public Date getDate_deleted() {
        return date_deleted;
    }

    public void setDate_deleted(Date date_deleted) {
        this.date_deleted = date_deleted;
    }

}
