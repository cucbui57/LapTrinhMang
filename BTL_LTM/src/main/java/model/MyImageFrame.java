package model;

import java.awt.image.BufferedImage;
import java.io.Serializable;

public class MyImageFrame implements Serializable { // Khung hình được mã hoá (nén chuẩn JPG) để có thể đọc ghi theo luồng
    private static final long serialVersionUID = 69L;

    private int from_user_id;

    private int frame_index; // Chỉ số của khung hình
    //    private BufferedImage bufferedImage;
    private int width, height, type; // Độ phân giải, kiểu (ảnh xám, ảnh 3 kênh màu, ảnh có kênh alpha...)
    private byte[] data; // Dữ liệu đã được mã hoá (nén)

    public MyImageFrame() {
        this.frame_index = 0;
        this.width = 640;
        this.height = 480;
        type = BufferedImage.TYPE_3BYTE_BGR;
        this.data = null;
    }

    public MyImageFrame(int from_user_id, int frame_index, int width, int height, int type, byte[] data) {
        this.frame_index = frame_index;
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

    public int getFrame_index() {
        return frame_index;
    }

    public void setFrame_index(int frame_index) {
        this.frame_index = frame_index;
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
