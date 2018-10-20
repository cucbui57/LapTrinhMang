package Model;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

public class Message {
    private int conversation_id;
    private int message_id;
    private int sender_id;
    private Date date_created;
    private Time time_created;
    private boolean file;
    private String name;
    private byte[] content;

    public Message(int conversation_id, int message_id, int sender_id, Date date_created, Time time_created, Boolean file, String name, byte[] content) {
        this.conversation_id = conversation_id;
        this.message_id = message_id;
        this.sender_id = sender_id;
        this.date_created = date_created;
        this.time_created = time_created;
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

    public Date getDate_created() {
        return date_created;
    }

    public void setDate_created(Date date_created) {
        this.date_created = date_created;
    }

    public Time getTime_created() {
        return time_created;
    }

    public void setTime_created(Time time_created) {
        this.time_created = time_created;
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
