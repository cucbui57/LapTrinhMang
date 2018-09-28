package Model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;

public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final int max_friends = 5000;

    private String user_name;
    private String password;
    private String id_num;
    private String first_name;
    private String last_name;
    private Timestamp birth_day;
    private Sex sex;
    private String email_address;
    private String phone_number;
    private ArrayList<String> friends;

    public User() {
        user_name = null;
        password = null;
        id_num = null;
        first_name = null;
        last_name = null;
        birth_day = null;
        sex = null;
        email_address = null;
        phone_number = null;

    }

    public User(String user_name, String password, String id_num, String first_name, String last_name, Timestamp birth_day, Sex sex, String email_address, String phone_number) {
        this.user_name = user_name;
        this.password = password;
        this.id_num = id_num;
        this.first_name = first_name;
        this.last_name = last_name;
        this.birth_day = birth_day;
        this.sex = sex;
        this.email_address = email_address;
        this.phone_number = phone_number;
    }


}
