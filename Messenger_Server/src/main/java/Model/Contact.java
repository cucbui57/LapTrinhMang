package Model;

import java.sql.Date;

public class Contact {
    private int user_id;
    private int contact_id;
    private int status;

    public Contact() {
    }

    public Contact(int user_id, int contact_id, int status) {

        this.user_id = user_id;
        this.contact_id = contact_id;
        this.status = status;
    }

    public int getUser_id() {

        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getContact_id() {
        return contact_id;
    }

    public void setContact_id(int contact_id) {
        this.contact_id = contact_id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int accepted) {
        this.status = accepted;
    }
}
