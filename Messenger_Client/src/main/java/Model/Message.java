package Model;

import java.sql.Date;
import java.sql.Timestamp;

public class Message {
    private int conversation_id;
    private int message_id;
    private int sender_id;
    private Timestamp create_at;
    private Boolean file;
    private String name;
    private byte[] content;

    public Message(int conversation_id, int message_id, int sender_id, Timestamp create_at, Boolean file, String name, byte[] content) {
        this.conversation_id = conversation_id;
        this.message_id = message_id;
        this.sender_id = sender_id;
        this.create_at = create_at;
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

    public Timestamp getCreate_at() {
        return create_at;
    }

    public void setCreate_at(Timestamp create_at) {
        this.create_at = create_at;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean isFile() {
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
