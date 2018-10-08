package Model.CheckUsername;

public class CheckUsernameRequest {
    private String username;

    public CheckUsernameRequest(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
