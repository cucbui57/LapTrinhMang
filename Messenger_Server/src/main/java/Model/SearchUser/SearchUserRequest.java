package Model.SearchUser;

public class SearchUserRequest {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SearchUserRequest(String name) {
        this.name = name;
    }
}
