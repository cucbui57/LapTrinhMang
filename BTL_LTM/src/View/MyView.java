package View;

import Control.MyControl;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MyView extends JFrame {

    private int screen_width, screen_height;
    private int window_width, windown_height;
    int frame_rate;
    private JPanel contentPane;

    private static MyControl control;

    private ScheduledExecutorService scheduledRepaint; // Kiểm soát luồng vẽ cửa sổ

    public MyView(MyControl control) {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setName("BTL_LTM_Group_8");

        this.control = control;
        screen_width = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode().getWidth();
        screen_height = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode().getHeight();
        window_width = 1280;
        windown_height = 480;
        frame_rate = 30;

        setSize(window_width, windown_height);
        setLocation((screen_width - window_width) / 2, (screen_height - windown_height) / 2);

        contentPane = new JPanel();
        setContentPane(contentPane);
        contentPane.setLayout(new FlowLayout());

//        LocalCameraPreviewer localCameraPreviewer = new LocalCameraPreviewer(control.connectToCamera());
//        addComponent(localCameraPreviewer);

        int initialDelay = 0;
        int period = 1000000000 / frame_rate;

        scheduledRepaint = Executors.newScheduledThreadPool(1); // Sẽ có 1 luồng dùng để cập nhật cửa sổ theo thời gian
        Thread myRepaint = new Thread(new MyRepaint());
        scheduledRepaint.scheduleAtFixedRate(myRepaint, initialDelay, period, TimeUnit.NANOSECONDS); // Cửa sổ được vẽ lại mỗi "period" nano giây

        setVisible(true);
    }

    class MyRepaint implements Runnable {

        @Override
        public void run() {
            repaint();
        }
    }

    public void addComponent(Component component) {
        contentPane.add(component);
        component.setBounds(0, 0, 640, 480);
    }

    public void addComponent(Component component, int x, int y, int width, int height) {
        contentPane.add(component);
        component.setBounds(x, y, width, height);
    }
}
