package control;

import control.UDPSocket.ProcessUDPSocketData;
import control.UDPSocket.UDPSocketClient;
import control.devices.cameras.CameraConnector;
import control.devices.microphones.MicrophoneConnector;
import control.devices.speakers.SpeakerConnector;
import view.CallingCameraViewer;
import view.LocalCameraPreviewer;
import view.MyView;

import java.sql.Connection;

public class MyControl {

    private static Connection connection;

    private MyView view;

    private CameraConnector cameraConnector;
    private MicrophoneConnector microphoneConnector;
    private SpeakerConnector speakerConnector;

    private LocalCameraPreviewer localCameraPreviewer;
    private CallingCameraViewer callingCameraViewer;

    private String hostname;
    private int port;

    private UDPSocketClient udpSocketClient;
    private ProcessUDPSocketData processUDPSocketData;

    public MyControl() {
        hostname = "localhost";
        port = 6969;
        init();
    }

    private void init() {
        view = new MyView(this);

        speakerConnector = new SpeakerConnector();
        microphoneConnector = new MicrophoneConnector();
        cameraConnector = new CameraConnector();

        speakerConnector.connect();

        localCameraPreviewer = new LocalCameraPreviewer(cameraConnector);
        view.addComponent(localCameraPreviewer, 640, 0, 640, 480);

        callingCameraViewer = new CallingCameraViewer();
        view.addComponent(callingCameraViewer, 0, 0, 640, 480);

        processUDPSocketData = new ProcessUDPSocketData(1, cameraConnector, microphoneConnector, speakerConnector, callingCameraViewer);
    }

    public static void main(String[] args) {
        MyControl control = new MyControl();
        control.startVideoCall();
    }

    public void disconnectCamera() { // Ngắt kết nối camera
        cameraConnector.disconnect();
    }

    public void disconnectMicrophone() {
        microphoneConnector.disconnect();
    }

    public void disconnectSpeaker() {
        speakerConnector.disconnect();
    }

    public void closeUDPSocketClient() {
        processUDPSocketData.stop();
    }

    public boolean startVideoCall() {
        return processUDPSocketData.startVideoCall();
    }

    public void stopVideoCall() {
        processUDPSocketData.stopVideoCall();
    }
}
