package control.UDPSocket;

import control.DataConvertor;
import control.devices.cameras.CameraConnector;
import control.devices.microphones.MicrophoneConnector;
import control.devices.speakers.SpeakerConnector;
import model.MyAudioDataFrame;
import model.MyImageFrame;
import view.CallingCameraViewer;

import javax.sound.sampled.AudioFormat;

public class ProcessUDPSocketData {
    private String hostname;
    private int port;

    private UDPSocketClient udpSocketClient;

    private boolean isOpen;

    private int current_user_id; // ID của người dùng đang đăng nhập client hiện tại

    private CameraConnector cameraConnector;
    private int lastest_send_image_frame_index; // Chỉ số của frame cuối cùng được gửi đi
    private MicrophoneConnector microphoneConnector;
    private int lastest_send_audio_frame_index;

    private boolean is_calling;
    //    private int milisecond_period;
//    private int nanosecond_period;
    private SpeakerConnector speakerConnector;

    private int lastest_receive_image_frame_index; // Chỉ số của frame cuối cùng được nhận về
    private int lastest_receive_audio_frame_index;

    private CallingCameraViewer callingCameraViewer;

//    private int initialDelay;
//    private int period;
//    private ScheduledExecutorService scheduledSendFrame; // Kiểm soát luồng đọc từ camera

    public ProcessUDPSocketData(int current_user_id) {
        this.current_user_id = current_user_id;

        hostname = "localhost";
        port = 6969;

        this.cameraConnector = null;
        this.callingCameraViewer = null;
        this.microphoneConnector = null;
        this.speakerConnector = null;

        start();
    }

    public ProcessUDPSocketData(int current_user_id, CameraConnector cameraConnector, MicrophoneConnector microphoneConnector, SpeakerConnector speakerConnector, CallingCameraViewer callingCameraViewer) {
        this.current_user_id = current_user_id;

//        hostname = "laisoltmquaco.onthewifi.com";
        hostname = "localhost";
        port = 6969;

        this.current_user_id = current_user_id;
        this.cameraConnector = cameraConnector;
        this.microphoneConnector = microphoneConnector;
        this.speakerConnector = speakerConnector;
        this.callingCameraViewer = callingCameraViewer;

        start();
    }

    public boolean start() {
        udpSocketClient = new UDPSocketClient(hostname, port);

        isOpen = true;

        Thread processIncomingDataThread = new Thread(new ProcessIncomingData());
        processIncomingDataThread.start();

        return true;
    }

    public boolean startVideoCall() {
        lastest_send_image_frame_index = -1; // Chưa gửi khung hình nào
        lastest_send_audio_frame_index = -1;
        lastest_receive_image_frame_index = -1; // Chưa nhận khung hình nào
        lastest_receive_audio_frame_index = -1;

//        initialDelay = 0;
//        period = 1000000000 / 30; // cứ 1/30s thì gửi đi một khung hình

        isOpen = true;
        is_calling = true;
        microphoneConnector.setIs_listening(true);
        microphoneConnector.connect(microphoneConnector.getMicrophone_num() - 1);
        cameraConnector.connect();
//        milisecond_period = 1000 / cameraConnector.getFps();
//        nanosecond_period = 10000 / cameraConnector.getFps();

//        scheduledSendFrame = Executors.newScheduledThreadPool(1);  // Với mỗi thời điểm chỉ có 1 Thread chạy để gửi
        Thread sendImageFrameThread = new Thread(new SendImageFrame());
        Thread sendAudioFrameThread = new Thread(new SendAudioFrame());

        sendImageFrameThread.start();
        sendAudioFrameThread.start();
//        scheduledSendFrame.scheduleAtFixedRate(sendFrameThread, initialDelay, period, TimeUnit.NANOSECONDS);


        return true;
    }

    public void processUDPSocketIncomingData(Object receiveObject) { // Xử lý dữ liệu (Object) nhận về từ UDPSocket
        if (receiveObject instanceof MyImageFrame) { // Nếu là kiểu MyImageFrame
            MyImageFrame receiveImageFrame = (MyImageFrame) receiveObject;
            if (lastest_receive_image_frame_index < receiveImageFrame.getFrame_index()) { // Nếu chỉ số của khung hình hiện tại lớn hơn thì mới xử lý (Do UDP hướng không kết nối và packet có thể đến trước đến sau không theo thứ tự)
                lastest_receive_image_frame_index = receiveImageFrame.getFrame_index();
                callingCameraViewer.setBufferedImage(DataConvertor.MyFrame_to_BufferedImage(receiveImageFrame)); // Cập nhật BufferdImage của CallingCameraViewer là khung hình nhận dược
            }
        } else {
            if (receiveObject instanceof MyAudioDataFrame) {
                MyAudioDataFrame receiveAudioDataFrame = (MyAudioDataFrame) receiveObject;
                if (lastest_receive_audio_frame_index < receiveAudioDataFrame.getFrame_index()) {
                    System.out.println("Received Audio frame: " + receiveAudioDataFrame.getFrame_index());
                    lastest_receive_audio_frame_index = receiveAudioDataFrame.getFrame_index();
                    speakerConnector.play(DataConvertor.MyAudioDataFrame_to_AudioInputStream(receiveAudioDataFrame));
                }
            }
        }
    }

    public void stopVideoCall() {
        is_calling = false;
    }

    public void stop() { // Dừng UDPSocket
        is_calling = false;
        isOpen = false;
//        scheduledSendFrame.shutdown();
        udpSocketClient.close();
    }

    class SendImageFrame implements Runnable { // Luồng gửi 1 khung hình lấy từ Camera theo thời gian

        @Override
        public void run() {
            while (is_calling) {
                try {
                    udpSocketClient.offerSendObject(DataConvertor.create_MyImageFrame_from_BufferedImage(cameraConnector, current_user_id));
//                    Thread.sleep(milisecond_period, nanosecond_period);
                    Thread.sleep(34);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    class SendAudioFrame implements Runnable { // Luồng gửi âm thanh theo thời gian
        AudioFormat audioFormat;
        int CHUNK_SIZE;
        byte[] data;

        @Override
        public void run() {
            audioFormat = microphoneConnector.getCurrent_microphone().getFormat();
            CHUNK_SIZE = (32767 / microphoneConnector.getCurrent_microphone().getFormat().getFrameSize()) * microphoneConnector.getCurrent_microphone().getFormat().getFrameSize();
//            data = new byte[microphoneConnector.getCurrent_microphone().getBufferSize() / 5];
            data = new byte[10000];

            while (is_calling) {
                microphoneConnector.getCurrent_microphone().read(data, 0, data.length); // Đọc dữ liệu từ microphone vào mảng byte "data"

                try {
                    udpSocketClient.offerSendObject(new MyAudioDataFrame(current_user_id, lastest_send_audio_frame_index, audioFormat, data));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                lastest_send_audio_frame_index++;
            }
        }
    }

    class ProcessIncomingData implements Runnable {

        @Override
        public void run() {
            Object receiveObject;
            while (isOpen) {
                try {
                    receiveObject = udpSocketClient.getReceiveObject();

                    processUDPSocketIncomingData(receiveObject);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
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
