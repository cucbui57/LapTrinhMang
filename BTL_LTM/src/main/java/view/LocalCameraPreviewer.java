package view;

import control.devices.cameras.CameraConnector;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;

public class LocalCameraPreviewer extends JPanel { // JPanel xem trước camera
    private CameraConnector cameraConnector;
    private Point position;
    private int width, height;
    private AffineTransform affineTransform;
    private AffineTransformOp affineTransformOp;

    public LocalCameraPreviewer(CameraConnector cameraConnector) {
        setLayout(null);
        this.cameraConnector = cameraConnector;
        position = new Point(0, 0);
        width = cameraConnector.getWidth();
        height = cameraConnector.getHeight();
        affineTransform = new AffineTransform();
        // Lật ngang hình
        affineTransform.concatenate(AffineTransform.getScaleInstance(-1, 1));
        affineTransform.translate(-width, 0);
        affineTransformOp = new AffineTransformOp(affineTransform, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
    }

    @Override
    public void paint(Graphics graphics) {
        super.paint(graphics);
//        graphics.drawImage(affineTransformOp.filter(cameraConnector.getCurrent_buffered_image(), null), 0, 0, this);
        graphics.drawImage(cameraConnector.getCurrent_buffered_image(), 0, 0, this);
    }

    public void setPosition(int x, int y) {
        position = new Point(x, y);
    }

    public Point getPosition() {
        return position;
    }

    public int get_width() {
        return width;
    }

    public int get_height() {
        return height;
    }
}
