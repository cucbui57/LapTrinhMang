package Control;

import Control.Devices.Camera.CameraConnector;
import Control.Devices.Microphone.MicrophoneConnector;
import Control.JDBC.SQLServer.SQLServerConnector;
import Control.UDPSocket.ProcessUDPSocketData;
import Control.UDPSocket.UDPSocketClient;
import View.CallingCameraViewer;
import View.LocalCameraPreviewer;
import View.MyView;

import java.sql.Connection;
import java.sql.SQLException;

public class MyControl {

    private static Connection connection;
    private MyView view;
    private CameraConnector cameraConnector;
    private MicrophoneConnector microphoneConnector;
    private LocalCameraPreviewer localCameraPreviewer;
    private CallingCameraViewer callingCameraViewer;
    private ProcessUDPSocketData processUDPSocketData;
    private UDPSocketClient udpSocketClient;

    public MyControl() {
//        init();

        view = new MyView(this);

        localCameraPreviewer = new LocalCameraPreviewer(connectToCamera());
        view.addComponent(localCameraPreviewer, 640, 0, 640, 480);

        callingCameraViewer = new CallingCameraViewer();
        view.addComponent(callingCameraViewer, 0, 0, 640, 480);


        processUDPSocketData = new ProcessUDPSocketData(1, cameraConnector, callingCameraViewer);
        openUDPSocketClient();
    }

    private void init() {
        try {
            connection = SQLServerConnector.getConnection("localhost", 1433, "MSSQLSERVER", "LTM", "sa", "1234");
            System.out.println("Connect to SQLServer successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        MyControl control = new MyControl();
    }

    public CameraConnector connectToCamera() { // Lấy kết nối đến camera
        cameraConnector = new CameraConnector();
        cameraConnector.connect();
        return cameraConnector;
    }

    public void disconnectCamera() { // Ngắt kết nối camera
        cameraConnector.disconnect();
    }

    public boolean openUDPSocketClient() {
        return processUDPSocketData.openUDPSocketClient();
    }
}
