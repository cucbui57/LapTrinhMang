package View;

import javax.swing.*;
import java.awt.*;

public class RegisterView extends JFrame {
    private JLabel label_first_name;
    private JLabel label_last_name;
    private JLabel label_phone_number;
    private JLabel label_email;
    private JLabel label_username;
    private JLabel label_password;
    private JLabel label_confirm_password;
    private JLabel label_sex;
    private JLabel label_birthday;

    private JTextField text_first_name;
    private JTextField text_last_name;
    private JTextField text_phone_number;
    private JTextField text_email;
    private JTextField text_username;
    private JTextField text_confirm_password;
    private JTextField text_sex;
    private JTextField text_birthday;
    private JPasswordField password;

    RegisterView(){
        label_username = new JLabel("username");
        label_password = new JLabel("password");
        text_username = new JTextField();
        password = new JPasswordField();

        this.setSize(400, 600);
        this.setVisible(true);
        this.add(label_username);
        label_username.setLocation(20, 50);
        this.add(label_password);
        label_password.setLocation(20, 100);
        this.add(text_username);
        text_username.setLocation(100, 50);
        this.add(password);
        password.setLocation(100, 100);
    }
}
