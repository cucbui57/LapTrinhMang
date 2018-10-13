package Control;

import View.LoginView;

public class WindowPoolManager {
    private static LoginView loginView;

    public static LoginView getLoginViewSingleton(){
        if(loginView == null){
            loginView = new LoginView();
        }
        else {
            loginView.reset();
        }
        return loginView;
    }
}
