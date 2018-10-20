package Model.SearchUser;

import Model.User;

import java.util.Vector;

public class SearchUserResponse {
    private Vector<User> users;

    public SearchUserResponse() {
    }

    public SearchUserResponse(Vector<User> users) {
        this.users = users;
    }

    public Vector<User> getUsers() {
        return users;
    }

    public void setUsers(Vector<User> users) {
        this.users = users;
    }
}
