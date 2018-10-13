package Model.Login;

import Model.User;

import java.io.Serializable;

public class LoginRequest implements Serializable {
    public static final long serialVersionUID = 5;
    private String username;
    private String password;

    public LoginRequest() {

    }

    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
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
}
