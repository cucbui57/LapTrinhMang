package View;

import Control.LoginController;
import Model.Login.LoginRequest;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class LoginView extends JPanel {
    private JLabel jLabel_username;
    private JLabel jLabel_password;
    private JTextField jTextField_username;
    private JPasswordField jPasswordField;
    private JButton jButton_submit;
    private JButton jButton_forgot_password;
    public LoginView(){
        this.setSize(400, 400);
        this.setVisible(true);

        jLabel_username = new JLabel("username");
        this.add(jLabel_username);
        jLabel_username.setSize(150, 20);
        jLabel_username.setVisible(true);
        jLabel_username.setFont(new Font("SFProText-Medium.ttf", 1, 15));
        jLabel_username.setLocation(this.getWidth()/5,this.getHeight()/4);

        jLabel_password = new JLabel("password");
        this.add(jLabel_password);
        jLabel_password.setSize(150, 20);
        jLabel_password.setVisible(true);
        jLabel_password.setFont(new Font("SFProText-Medium.ttf", 1, 15));
        jLabel_password.setLocation(this.getWidth()/5, this.getHeight()/4*2);

        jButton_submit = new JButton("login");
        this.add(jButton_submit);
        jButton_submit.setSize(150, 20);
        jButton_submit.setVisible(true);
        jButton_submit.setFont(new Font("SFProText-Medium.ttf", 1, 15));
        jButton_submit.setLocation(this.getWidth()/5*3, this.getHeight()/4*3);

        jButton_forgot_password = new JButton("forgot password");
        this.add(jButton_forgot_password);
        jButton_forgot_password.setSize(150, 20);
        jButton_forgot_password.setVisible(true);
        jButton_forgot_password.setFont(new Font("SFProText-Medium.ttf", 1, 15));
        jButton_forgot_password.setLocation(this.getWidth()/5, this.getHeight()/4*3);

        jTextField_username = new JTextField("");
        this.add(jTextField_username);
        jTextField_username.setSize(150, 20);
        jTextField_username.setVisible(true);
        jTextField_username.setFont(new Font("SFProText-Medium.ttf", 1, 15));
        jTextField_username.setLocation(this.getWidth()/5*2, this.getHeight()/4);

        jPasswordField = new JPasswordField("");
        this.add(jPasswordField);
        jPasswordField.setSize(150, 20);
        jPasswordField.setVisible(true);
        jPasswordField.setFont(new Font("SFProText-Medium.ttf", 1, 15));
        jPasswordField.setLocation(this.getWidth()/5*2, this.getHeight()/4*2);
    }
    public LoginRequest getLoginRequest(){
        StringBuffer s = new StringBuffer();
        char []password = jPasswordField.getPassword();
        for (char c: password ) {
            s.append(c);
        }
        return new LoginRequest(jTextField_username.getText(), s.toString());
    }

    public void reset(){
        jPasswordField.setText("");
        jTextField_username.setText("");
    }

    public void setLoginListenner(MouseListener mouseListener){
        jButton_submit.addMouseListener(mouseListener);
    }
}
