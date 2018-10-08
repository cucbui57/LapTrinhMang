package View;

import javax.swing.*;

public class Window extends JFrame {
    private JPanel jPanel;
    public Window(){
        this.setSize(600, 400);
        this.setVisible(true);
        this.setResizable(true);
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
