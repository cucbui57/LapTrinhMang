package Model.CheckEmail;

public class CheckEmailRequest {
    private String Email;

    public CheckEmailRequest(String email) {
        Email = email;
    }

    public String getEmail() {

        return Email;
    }
}
