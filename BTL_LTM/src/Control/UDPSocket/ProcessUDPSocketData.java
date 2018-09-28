package Control.UDPSocket;

import Control.DataConvertor;
import Control.Devices.Camera.CameraConnector;
import Control.Devices.Camera.MyFrame;
import View.CallingCameraViewer;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ProcessUDPSocketData {
    private String hostname;
    private int port;

    private UDPSocketClient udpSocketClient;
    private Thread udpSocketClientThread; // UDPSocketClient sẽ chạy theo luồng để khi gọi hàm listen có vòng While(true) không block chương trình lại

    private int current_user_id; // ID của người dùng đang đăng nhập client hiện tại

    private CameraConnector cameraConnector;
    private int lastest_send_frame_index; // Chỉ số của frame cuối cùng được gửi đi

    private int lastest_receive_frame_index; // Chỉ số của frame cuối cùng được nhận về
    private MyFrame receiveFrame; //  Frame nhận được
    private CallingCameraViewer callingCameraViewer;

    private int initialDelay;
    private int period;
    private ScheduledExecutorService scheduledSendFrame; // Kiểm soát luồng đọc từ camera

    public ProcessUDPSocketData(int current_user_id) {
        this.current_user_id = current_user_id;
        this.cameraConnector = null;
        this.callingCameraViewer = null;

        hostname = "localhost";
        port = 6969;
    }

    public ProcessUDPSocketData(int current_user_id, CameraConnector cameraConnector, CallingCameraViewer callingCameraViewer) {
        this.current_user_id = current_user_id;
        this.cameraConnector = cameraConnector;
        this.callingCameraViewer = callingCameraViewer;

//        hostname = "laisoltmquaco.onthewifi.com";
        hostname = "localhost";
        port = 6969;
    }

    public void processUDPSocketIncomingData(Object receive_object) { // Xử lý dữ liệu (Object) nhận về từ UDPSocket
        if (receive_object instanceof MyFrame) { // Nếu là kiểu MyFrame
            receiveFrame = (MyFrame) receive_object;
            if (lastest_receive_frame_index < receiveFrame.getFrame_num()) { // Nếu chỉ số của khung hình hiện tại lớn hơn thì mới xử lý (Do UDP hướng không kết nối và packet có thể đến trước đến sau không theo thứ tự)
                lastest_receive_frame_index = receiveFrame.getFrame_num();
                callingCameraViewer.setBufferedImage(DataConvertor.MyFrame_to_BufferedImage(receiveFrame)); // Cập nhật BufferdImage của CallingCameraViewer là khung hình nhận dược
            }
        }
    }

    public void send_current_camera_frame() { // Gửi khung hình hiện tại thu được từ Camera
        if (lastest_send_frame_index < cameraConnector.getCurrent_frame_index()) { // Nếu khung hình lần cuối gửi đi có chỉ số nhỏ hơn khung hình hiện tại thì mới gửi để tránh gửi nhiều lần một khung hình
            lastest_send_frame_index = cameraConnector.getCurrent_frame_index();
            udpSocketClient.send(DataConvertor.BufferedImage_to_MyFrame(cameraConnector.getCurrent_buffered_image(), cameraConnector.getCurrent_frame_index(), current_user_id)); // Chuyển đổi BufferdImage lấy từ Camera và chuyển sang MyFrame để có thể gửi được đi
        }
    }

    public boolean openUDPSocketClient() { // Mở UDPSocket để gửi và nhận dữ liệu
        udpSocketClient = new UDPSocketClient(this, hostname, port);
        udpSocketClientThread = new Thread(udpSocketClient);
        start();
        return true;
    }

    public void start() {
        lastest_send_frame_index = -1; // Chưa gửi khung hình nào
        lastest_receive_frame_index = -1; // Chưa nhận khung hình nào

        initialDelay = 0;
        period = 1000000000 / 30; // cứ 1/30s thì gửi đi một khung hình
        scheduledSendFrame = Executors.newScheduledThreadPool(1);  // Với mỗi thời điểm chỉ có 1 Thread chạy để gửi
        Thread sendFrameThread = new Thread(new SendFrame());
        scheduledSendFrame.scheduleAtFixedRate(sendFrameThread, initialDelay, period, TimeUnit.NANOSECONDS);

        udpSocketClientThread.start();
    }

    public void stop() { // Dừng UDPSocket
        scheduledSendFrame.shutdown();
        udpSocketClient.close();
    }

    class SendFrame implements Runnable { // Luồng gửi 1 khung hình lấy từ Camera theo thời gian

        @Override
        public void run() {
            send_current_camera_frame();
        }
    }

    public int getCurrent_user_id() {
        return current_user_id;
    }

    public void setCurrent_user_id(int current_user_id) {
        this.current_user_id = current_user_id;
    }

    public CameraConnector getCameraConnector() {
        return cameraConnector;
    }

    public void setCameraConnector(CameraConnector cameraConnector) {
        this.cameraConnector = cameraConnector;
    }

    public CallingCameraViewer getCallingCameraViewer() {
        return callingCameraViewer;
    }

    public void setCallingCameraViewer(CallingCameraViewer callingCameraViewer) {
        this.callingCameraViewer = callingCameraViewer;
    }
}
