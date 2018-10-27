package control;

import control.Devices.Camera.CameraConnector;
import view.CallingCameraViewer;
import view.LocalCameraPreviewer;
import view.TestView;

import java.sql.Connection;

public class TestControl {
    private static Connection connection;
    private TestView view;
    private TestUDPSocket testUDPSocket;

    private CameraConnector cameraConnector;

    CallingCameraViewer callingCameraViewer;
    LocalCameraPreviewer localCameraPreviewer;

    public TestControl() {
        view = new TestView(this);

        localCameraPreviewer = new LocalCameraPreviewer(connectToCamera());
        view.addComponent(localCameraPreviewer, 640, 0, 640, 480);

        callingCameraViewer = new CallingCameraViewer();
        view.addComponent(callingCameraViewer, 0, 0, 640, 480);

    }

    public CameraConnector connectToCamera() { // Lấy kết nối đến camera
        cameraConnector = new CameraConnector();
        cameraConnector.connect();
        return cameraConnector;
    }

    public void disconnectCamera() { // Ngắt kết nối camera
        cameraConnector.disconnect();
    }

    public void openUDPSocket() {
        testUDPSocket = new TestUDPSocket(cameraConnector, callingCameraViewer);
        testUDPSocket.start();
    }

    public void closeUDPSocket() {
        testUDPSocket.stop();
    }
}
