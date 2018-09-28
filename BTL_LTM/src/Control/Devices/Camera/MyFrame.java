package Control.Devices.Camera;

import java.awt.image.BufferedImage;
import java.io.Serializable;

public class MyFrame implements Serializable { // Khung hình được mã hoá (nén chuẩn JPG) để có thể đọc ghi theo luồng
    private static final long serialVersionUID = 69L;

    private int from_user_id;

    private int frame_num; // Chỉ số của khung hình
    //    private BufferedImage bufferedImage;
    private int width, height, type; // Độ phân giải, kiểu (ảnh xám, ảnh 3 kênh màu, ảnh có kênh alpha...)
    private byte[] data; // Dữ liệu đã được mã hoá (nén)

    public MyFrame() {
        this.frame_num = 0;
        this.width = 640;
        this.height = 480;
        type = BufferedImage.TYPE_3BYTE_BGR;
        this.data = null;
    }

    public MyFrame(int from_user_id, int frame_num, int width, int height, int type, byte[] data) {
        this.frame_num = frame_num;
        this.width = width;
        this.height = height;
        this.type = type;
        this.data = data;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public int getFrom_user_id() {
        return from_user_id;
    }

    public void setFrom_user_id(int from_user_id) {
        this.from_user_id = from_user_id;
    }

    public int getFrame_num() {
        return frame_num;
    }

    public void setFrame_num(int frame_num) {
        this.frame_num = frame_num;
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}
