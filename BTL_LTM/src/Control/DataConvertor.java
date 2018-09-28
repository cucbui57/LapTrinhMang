package Control;

import Control.Devices.Camera.MyFrame;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class DataConvertor { // Chuyển đổi dữ liệu (tuỳ theo hàm)

    public static byte[] BufferedImage_to_ByteArray(BufferedImage bufferedImage) { // Mã hoá (nén) hình ảnh sang định dạng JPG vào mảng byte
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] data = null;
        try {
            ImageIO.write(bufferedImage, "jpg", byteArrayOutputStream);
            data = byteArrayOutputStream.toByteArray();

            byteArrayOutputStream.reset();
            byteArrayOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        System.out.println("DataConvertor.data.length: " + data.length);
        return data;
    }

    public static MyFrame BufferedImage_to_MyFrame(BufferedImage bufferedImage, int frame_index, int from_user_id) { // Mã hoá ảnh "BufferedImage" ở định dạng JPG vào MyFrame
        MyFrame myFrame = new MyFrame(from_user_id, frame_index, bufferedImage.getWidth(), bufferedImage.getHeight(), BufferedImage.TYPE_3BYTE_BGR, DataConvertor.BufferedImage_to_ByteArray(bufferedImage));
        return myFrame;
    }

    public static BufferedImage MyFrame_to_BufferedImage(MyFrame myFrame) { // Giải mã hình ảnh được mã hoá JPG thành một ảnh "BufferedImage"
//        byte[] data = myFrame.getData().clone();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(myFrame.getData());
//        System.out.println("MyFrame: " + myFrame.getWidth() + "x" + myFrame.getHeight() + ": " + myFrame.getType());
        BufferedImage bufferedImage = new BufferedImage(myFrame.getWidth(), myFrame.getHeight(), myFrame.getType());
        try {
            bufferedImage = ImageIO.read(byteArrayInputStream);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return bufferedImage;
    }

    public static BufferedImage CopyBufferedImage(BufferedImage bufferedImage) { // Sao chép hình ảnh ra thêm một đối tượng mới (trong khi sử dụng hay dùng biến (con trỏ) trỏ chung vào cùng 1 vùng data nên cần sao chép)
        ColorModel colorModel = bufferedImage.getColorModel();
        boolean isAlphaPremultiplied = colorModel.isAlphaPremultiplied();
        WritableRaster writableRaster = bufferedImage.copyData(null);
        return new BufferedImage(colorModel, writableRaster, isAlphaPremultiplied, null);
    }
}
