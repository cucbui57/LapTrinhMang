package view;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class CallingCameraViewer extends JPanel { // Hiển thị hình ảnh nhận được trong quá trình video call

    private Point position;
    private int width, height;
    private BufferedImage bufferedImage;

    public CallingCameraViewer() {
        setLayout(null);
        position = new Point(0, 0);
        width = 640;
        height = 480;
    }

    @Override
    public void paint(Graphics graphics) {
        super.paint(graphics);
        graphics.drawImage(bufferedImage, 0, 0, this);
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setBufferedImage(BufferedImage bufferedImage) {
        this.bufferedImage = bufferedImage;
    }
}
