package control.Devices.Camera;

import com.github.sarxos.webcam.Webcam;
import control.Devices.Connector;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CameraConnector implements Connector {

    static {
        try {
            Class.forName("com.github.sarxos.webcam.WebcamDriver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private int webcam_num; // Số lượng camera có thể sử dụng
    private List<Webcam> webcams; // Danh sách các camera có thể sử dụng
    private Webcam current_webcam; // Webcam đang được sử dụng
    private ArrayList<Dimension> current_webcam_dimension; // Danh sách độ phân giải có thể sử dụng của camera đang được sử dụng
    private int current_webcam_index; // Chỉ số của webcam đang được sử dụng
    private int current_webcam_dimension_index; // Chỉ số của độ phân giải hiện tại của webcam đang được sử dụng
    private int fps; // Frame rate
    private int width, height; // Độ phân giải
    private boolean is_capturing;
    private int milisecond_period;
    private int nanosecond_period;
    //    private int initialDelay; // Thời gian trễ trước khi bắt đầu khởi chạy "scheduledCaptureFrame"
//    private int period; // Chu kỳ khởi động luồng đọc camera
    private int current_frame_index; // Chỉ số của khung hình được đọc hiện tại
    private BufferedImage current_buffered_image; // Khung hình được đọc hiện tại

//    private ScheduledExecutorService scheduledCaptureFrame; // Kiểm soát luồng đọc từ camera

    public CameraConnector() {
        webcams = Webcam.getWebcams(); // Lấy toàn bộ danh sách các camera của máy
        webcam_num = webcams.size();
        current_webcam = null;
        current_webcam_dimension = null;
        current_webcam_index = -1;
        current_webcam_dimension_index = -1;
        fps = 24;
        width = 0;
        height = 0;
        is_capturing = false;
        milisecond_period = 1000 / fps;
        nanosecond_period = 10000 / fps;
//        initialDelay = 0;
//        period = 1000000000;
        current_buffered_image = null;
        current_frame_index = 0;
    }

    @Override
    public boolean connect() { // Kết nối đến camera có thể sử dụng đầu tiên trong danh sách
        if (webcam_num <= 0) return false;
        if (current_webcam != null && current_webcam.isOpen()) disconnect();
        current_webcam_index = 0;
        fps = 30;
        while (current_webcam_index < webcam_num) {
            init_connection();
            break;
//            current_webcam_index++;
        }
        return true;
    }

    @Override
    public boolean connect(int device_index) { // Kết nối đến camera có vị trí "device_index" trong danh sách
        if (device_index < 0 || device_index > webcam_num) return false;
        if (current_webcam != null && current_webcam.isOpen()) disconnect();
        fps = 30;
        current_webcam_index = device_index;
        init_connection();
        return true;
    }

    private void init_connection() {
        current_webcam = webcams.get(current_webcam_index); // Camera thứ "current_webcam_index" trong danh sách

        current_webcam_dimension = new ArrayList<Dimension>(Arrays.asList(current_webcam.getViewSizes())); // Lấy tất cả độ phân giải mà camera hỗ trợ (thường sẽ từ nhỏ đến lớn)
//        current_webcam_dimension_index = 0; // Chọn một độ phân giải để sử dụng (nhỏ nhất)
        current_webcam_dimension_index = current_webcam_dimension.size() - 1; // Chọn một độ phân giải để sử dụng (lớn nhất)

//        System.out.println("Camera resolution: " + current_webcam_dimension.get(current_webcam_dimension_index).getWidth() + "x" + current_webcam_dimension.get(current_webcam_dimension_index).getHeight());

        if (current_webcam.isOpen()) {
            System.out.println("Camera opened!");
            current_webcam.close();
        }

        current_webcam.setViewSize(current_webcam_dimension.get(current_webcam_dimension_index)); // Đặt độ phân giải camera là độ phân giải đã chọn

        width = (int) current_webcam_dimension.get(current_webcam_dimension_index).getWidth();
        height = (int) current_webcam_dimension.get(current_webcam_dimension_index).getHeight();

        current_webcam.open(); // Mở camera

        is_capturing = true;

        current_buffered_image = current_webcam.getImage(); // Đọc thử một khung hình từ camera

        current_frame_index = 0; // Bộ đếm khung hình bắt đầu từ 0
        // Khởi tạo khung hình được mã hoá chuẩn nén JPG, 3 kênh màu BGR, với độ phân giải width x height

//        initialDelay = 0;
//        period = 1000000000 / fps; // Chu kỳ là 1000000000 nano giây (1 giây) chia cho số khung hình trên giây

        milisecond_period = 1000 / fps;
        nanosecond_period = 10000 / fps;

//        scheduledCaptureFrame = Executors.newScheduledThreadPool(1); // Một thread được chạy để đọc camera trong một thời điểm
        Thread captureFrameThread = new Thread(new CaptureFrame());
        captureFrameThread.start();
//        scheduledCaptureFrame.scheduleAtFixedRate(captureFrameThread, initialDelay, period, TimeUnit.NANOSECONDS);
    }

    @Override
    public void disconnect() {
        is_capturing = false;
//        scheduledCaptureFrame.shutdown(); // Ngừng đọc camera theo thời gian
        current_webcam.close(); // Đóng camera
    }

    class CaptureFrame implements Runnable { // Luồng đọc dữ liệu từ camera và ghi vào "current_buffered_image"

        @Override
        public void run() {
            while (is_capturing) {
                current_buffered_image = current_webcam.getImage(); // Lấy 1 khung hình từ camera
                current_frame_index++; // Mỗi lần đọc tăng chỉ số của khung hình lên
                try {
//                    Thread.sleep(milisecond_period, nanosecond_period);
                    Thread.sleep(34);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public int getWebcam_num() {
        return webcam_num;
    }

    public void setWebcam_num(int webcam_num) {
        this.webcam_num = webcam_num;
    }

    public List<Webcam> getWebcams() {
        return webcams;
    }

    public Webcam getCurrent_webcam() {
        return current_webcam;
    }

    public void setCurrent_webcam(Webcam current_webcam) {
        this.current_webcam = current_webcam;
    }

    public ArrayList<Dimension> getCurrent_webcam_dimension() {
        return current_webcam_dimension;
    }

    public void setCurrent_webcam_dimension(ArrayList<Dimension> current_webcam_dimension) {
        this.current_webcam_dimension = current_webcam_dimension;
    }

    public int getCurrent_webcam_index() {
        return current_webcam_index;
    }

    public void setCurrent_webcam_index(int current_webcam_index) {
        this.current_webcam_index = current_webcam_index;
    }

    public int getCurrent_webcam_dimension_index() {
        return current_webcam_dimension_index;
    }

    public void setCurrent_webcam_dimension_index(int current_webcam_dimension_index) {
        this.current_webcam_dimension_index = current_webcam_dimension_index;
    }

    public int getFps() {
        return fps;
    }

    public void setFps(int fps) {
        this.fps = fps;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setWebcams(List<Webcam> webcams) {
        this.webcams = webcams;
    }

    public boolean isIs_capturing() {
        return is_capturing;
    }

    public void setIs_capturing(boolean is_capturing) {
        this.is_capturing = is_capturing;
    }

    public int getMilisecond_period() {
        return milisecond_period;
    }

    public void setMilisecond_period(int milisecond_period) {
        this.milisecond_period = milisecond_period;
    }

    public int getNanosecond_period() {
        return nanosecond_period;
    }

    public void setNanosecond_period(int nanosecond_period) {
        this.nanosecond_period = nanosecond_period;
    }

    public BufferedImage getCurrent_buffered_image() {
        return current_buffered_image;
    }

    public void setCurrent_buffered_image(BufferedImage current_buffered_image) {
        this.current_buffered_image = current_buffered_image;
    }

    public int getCurrent_frame_index() {
        return current_frame_index;
    }

    public void setCurrent_frame_index(int current_frame_index) {
        this.current_frame_index = current_frame_index;
    }

//    public MyFrame getCurrent_frame() {
//        return current_frame;
//    }
//
//    public void setCurrent_frame(MyFrame current_frame) {
//        this.current_frame = current_frame;
//    }
}
