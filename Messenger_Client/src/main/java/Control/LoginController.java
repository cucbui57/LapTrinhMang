package Control;

import Model.Login.LoginRequest;
import View.LoginView;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class LoginController {
    private LoginView loginView;
    private LoginRequest loginRequest;
    public LoginController(LoginView loginView){
        this.loginView = loginView;
    }
    public void sendRequest(){

    }
    class LoginListenner implements MouseListener{
        @Override
        public void mouseClicked(MouseEvent e){
            loginRequest = loginView.getLoginRequest();
            JOptionPane.showMessageDialog(loginView, loginRequest.getPassword());
        }

        @Override
        public void mousePressed(MouseEvent e) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }
    }
}
