package Model;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

public class User implements Serializable {
    public static final long serialVersionUID = 1;
    private int user_id;
    private String username;
    private String password;
    private String email;
    private String phone_number;
    private String first_name;
    private String last_name;
    private boolean sex;
    private Date birthday;
    private byte[] avata;
    private int status;
    private boolean actived;+
    private boolean blocked;

    @Override
    public User clone() {
        return new User(user_id, username, null, email, phone_number, first_name, last_name, sex, birthday, avata, status, true, false);
    }

    public User() {
    }

    public User(int user_id, String username, String password, String email, String phone_number, String first_name, String last_name, boolean sex, Date birthday, byte[] avata, int status, boolean actived, boolean blocked) {

        this.user_id = user_id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.phone_number = phone_number;
        this.first_name = first_name;
        this.last_name = last_name;
        this.sex = sex;
        this.birthday = birthday;
        this.avata = avata;
        this.status = status;
        this.actived = actived;
        this.blocked = blocked;
    }

    public int getUser_id() {

        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public boolean isSex() {
        return sex;
    }

    public void setSex(boolean sex) {
        this.sex = sex;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public byte[] getAvata() {
        return avata;
    }

    public void setAvata(byte[] avata) {
        this.avata = avata;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public boolean isActived() {
        return actived;
    }

    public void setActived(boolean actived) {
        this.actived = actived;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }
}
