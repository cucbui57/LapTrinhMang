package Model.Register;

import Model.User;

public class RegisterRequest {
     private User user;

    public User getUser() {
        return user;
    }

    public RegisterRequest(User user) {

        this.user = user;
    }
}
