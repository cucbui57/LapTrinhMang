package control;

import control.devices.cameras.CameraConnector;
import control.devices.microphones.MicrophoneConnector;
import model.MyAudioDataFrame;
import model.MyImageFrame;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class DataConvertor { // Chuyển đổi dữ liệu (tuỳ theo hàm)

    // Image

    public static MyImageFrame create_MyImageFrame_from_BufferedImage(BufferedImage bufferedImage, int frame_index, int from_user_id) { // Mã hoá ảnh "BufferedImage" ở định dạng JPG vào MyImageFrame
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        MyImageFrame myImageFrame = null;
        try {
            ImageIO.write(bufferedImage, "jpg", byteArrayOutputStream);
            myImageFrame = new MyImageFrame(from_user_id, frame_index, bufferedImage.getWidth(), bufferedImage.getHeight(), BufferedImage.TYPE_3BYTE_BGR, byteArrayOutputStream.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                byteArrayOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return myImageFrame;
    }

    public static MyImageFrame create_MyImageFrame_from_BufferedImage(CameraConnector cameraConnector, int from_user_id) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        MyImageFrame myImageFrame = null;
        try {
            ImageIO.write(cameraConnector.getCurrent_buffered_image(), "jpg", byteArrayOutputStream);
            myImageFrame = new MyImageFrame(from_user_id, cameraConnector.getCurrent_frame_index(), cameraConnector.getWidth(), cameraConnector.getHeight(), BufferedImage.TYPE_3BYTE_BGR, byteArrayOutputStream.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                byteArrayOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return myImageFrame;
    }

    public static BufferedImage MyFrame_to_BufferedImage(MyImageFrame myImageFrame) { // Giải mã hình ảnh được mã hoá JPG thành một ảnh "BufferedImage"
//        byte[] data = myImageFrame.getData().clone();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(myImageFrame.getData());
//        System.out.println("MyImageFrame: " + myImageFrame.getWidth() + "x" + myImageFrame.getHeight() + ": " + myImageFrame.getType());
        BufferedImage bufferedImage = new BufferedImage(myImageFrame.getWidth(), myImageFrame.getHeight(), myImageFrame.getType());
        try {
            bufferedImage = ImageIO.read(byteArrayInputStream);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                byteArrayInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bufferedImage;
    }

    public static BufferedImage CopyBufferedImage(BufferedImage bufferedImage) { // Sao chép hình ảnh ra thêm một đối tượng mới (trong khi sử dụng hay dùng biến (con trỏ) trỏ chung vào cùng 1 vùng data nên cần sao chép)
        ColorModel colorModel = bufferedImage.getColorModel();
        boolean isAlphaPremultiplied = colorModel.isAlphaPremultiplied();
        WritableRaster writableRaster = bufferedImage.copyData(null);
        return new BufferedImage(colorModel, writableRaster, isAlphaPremultiplied, null);
    }

    // Audio

    public static MyAudioDataFrame create_MyAudioDataFrame_from_MicrophoneConnector_current_data(int current_user_id, int frame_index, MicrophoneConnector microphoneConnector) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        MyAudioDataFrame myAudioDataFrame = null;
        try {
            microphoneConnector.getByteArrayOutputStream().writeTo(byteArrayOutputStream);

//            System.out.println(byteArrayOutputStream.size());
            myAudioDataFrame = new MyAudioDataFrame(current_user_id, frame_index,
                    microphoneConnector.getMicrophoneList().get(microphoneConnector.getCurrent_microphone_index()).getValue().getSampleRate(),
                    microphoneConnector.getMicrophoneList().get(microphoneConnector.getCurrent_microphone_index()).getValue().getSampleSizeInBits(),
                    microphoneConnector.getMicrophoneList().get(microphoneConnector.getCurrent_microphone_index()).getValue().getChannels(),
                    microphoneConnector.getMicrophoneList().get(microphoneConnector.getCurrent_microphone_index()).getValue().getEncoding().equals(AudioFormat.Encoding.PCM_SIGNED),
                    microphoneConnector.getMicrophoneList().get(microphoneConnector.getCurrent_microphone_index()).getValue().isBigEndian(),
                    byteArrayOutputStream.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                byteArrayOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return myAudioDataFrame;
    }

    public static MyAudioDataFrame create_MyAudioDataFrame_from_MicrophoneConnector_current_data(int current_user_id, int frame_index, AudioFormat audioFormat, byte[] data) {

        MyAudioDataFrame myAudioDataFrame = new MyAudioDataFrame(current_user_id, frame_index, audioFormat, data);

        return myAudioDataFrame;
    }

    public static AudioInputStream MyAudioDataFrame_to_AudioInputStream(MyAudioDataFrame myAudioDataFrame) {
        AudioFormat audioFormat = new AudioFormat(myAudioDataFrame.getSampleRate(), myAudioDataFrame.getSampleSizeInBits(), myAudioDataFrame.getChannels(), myAudioDataFrame.isSigned(), myAudioDataFrame.isBigEndian());

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(myAudioDataFrame.getData());
        AudioInputStream audioInputStream = new AudioInputStream(byteArrayInputStream, audioFormat, myAudioDataFrame.getData().length / audioFormat.getFrameSize());
        return audioInputStream;
    }
}
