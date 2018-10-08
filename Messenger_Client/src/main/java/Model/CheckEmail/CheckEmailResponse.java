package Model.CheckEmail;

public class CheckEmailResponse {
    private boolean exist;

    public CheckEmailResponse(boolean exist) {
        this.exist = exist;
    }

    public boolean isExist() {

        return exist;
    }
}
