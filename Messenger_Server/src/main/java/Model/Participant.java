package Model;

public class Participant {
    private int conversation_id;
    private int user_id;
    private Boolean accepted;
    private String type;
    private Integer seen_message_id;
    private String nickname;

    public Participant() {
    }

    public Participant(int conversation_id, int user_id, Boolean accepted, String type, Integer seen_message_id, String nickname) {
        this.conversation_id = conversation_id;
        this.user_id = user_id;
        this.accepted = accepted;
        this.type = type;
        this.seen_message_id = seen_message_id;
        this.nickname = nickname;
    }

    public int getConversation_id() {
        return conversation_id;
    }

    public void setConversation_id(int conversation_id) {
        this.conversation_id = conversation_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public Boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(Boolean accepted) {
        this.accepted = accepted;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getSeen_message_id() {
        return seen_message_id;
    }

    public void setSeen_message_id(Integer seen_message_id) {
        this.seen_message_id = seen_message_id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
