package View;

import Control.utils.IOUtils;
import Model.CloseSocket;

import javax.swing.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class Window extends JFrame {
    private JPanel jPanel;
    public Window(){
        this.setSize(600, 400);
        this.setVisible(true);
        this.setResizable(true);
        this.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {

            }

            @Override
            public void windowClosing(WindowEvent e) {
                IOUtils.closeSocket();
                System.exit(0);
            }

            @Override
            public void windowClosed(WindowEvent e) {
            }

            @Override
            public void windowIconified(WindowEvent e) {

            }

            @Override
            public void windowDeiconified(WindowEvent e) {

            }

            @Override
            public void windowActivated(WindowEvent e) {

            }

            @Override
            public void windowDeactivated(WindowEvent e) {

            }
        });
    }

    public JPanel getjPanel() {
        return jPanel;
    }

    public void setjPanel(JPanel jPanel) {
        this.jPanel = jPanel;
        this.add(jPanel);
        jPanel.setLocation(this.getWidth()/2 - jPanel.getWidth()/2, this.getHeight()/2 - jPanel.getHeight()/2);
    }
}
