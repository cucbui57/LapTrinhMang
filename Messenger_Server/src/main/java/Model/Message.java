package Model;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

public class Message {
    private int conversation_id;
    private int message_id;
    private int sender_id;
    private Date date_create;
    private Time time_create;
    private boolean file;
    private String name;
    private byte[] content;

    public Message(int conversation_id, int message_id, int sender_id, Date date_create, Time time_create, Boolean file, String name, byte[] content) {
        this.conversation_id = conversation_id;
        this.message_id = message_id;
        this.sender_id = sender_id;
        this.date_create = date_create;
        this.time_create = time_create;
        this.file = file;
        this.name = name;
        this.content = content;
    }

    public int getConversation_id() {
        return conversation_id;
    }

    public void setConversation_id(int conversation_id) {
        this.conversation_id = conversation_id;
    }

    public int getMessage_id() {
        return message_id;
    }

    public void setMessage_id(int message_id) {
        this.message_id = message_id;
    }

    public int getSender_id() {
        return sender_id;
    }

    public void setSender_id(int sender_id) {
        this.sender_id = sender_id;
    }

    public Date getDate_create() {
        return date_create;
    }

    public void setDate_create(Date date_create) {
        this.date_create = date_create;
    }

    public Time getTime_create() {
        return time_create;
    }

    public void setTime_create(Time time_create) {
        this.time_create = time_create;
    }

    public void setFile(boolean file) {
        this.file = file;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isFile() {
        return file;
    }

    public void setType(Boolean file) {
        this.file = file;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }
}
