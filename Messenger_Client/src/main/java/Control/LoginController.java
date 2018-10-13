package Control;

import Control.utils.IOUtils;
import Model.Login.LoginRequest;
import Model.Login.LoginResponse;
import View.LoginView;

import javax.sound.midi.Receiver;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.ObjectOutputStream;

public class LoginController {
    private LoginView loginView;
    private LoginRequest loginRequest;
    private ObjectOutputStream objectOutputStream;
    public LoginController(LoginView loginView){
        this.loginView = loginView;
        loginView.setLoginListenner(new LoginListenner());
    }
    class LoginListenner implements MouseListener{
        @Override
        public void mouseClicked(MouseEvent e) {
            IOUtils.createIOSocket();
            loginRequest = loginView.getLoginRequest();
            IOUtils.writeObject(loginRequest);
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

    public static void ReceiveResponse(Object object){
        LoginResponse loginResponse = (LoginResponse)object;
        if(loginResponse.isAccepted()){
            System.out.println("success");
        }else {
            System.out.println("fail");
        }
    }
}
