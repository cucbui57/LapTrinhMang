import View.LoginView;
import View.Window;

public class MessengerClient {
    public static void main(String[] args) {
        Window window = new Window();
        LoginView loginView = new LoginView();
        window.setjPanel(loginView);
    }
}
