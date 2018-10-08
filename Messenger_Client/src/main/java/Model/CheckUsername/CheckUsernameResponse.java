package Model.CheckUsername;

public class CheckUsernameResponse {
    private boolean exist;

    public CheckUsernameResponse(boolean exist) {
        this.exist = exist;
    }

    public boolean isExist() {
        return exist;
    }
}
