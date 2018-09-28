package test;

import Control.DataConvertor;
import Control.Devices.Camera.CameraConnector;
import Control.Devices.Camera.MyFrame;
import Control.Devices.Microphone.MicrophoneConnector;
import View.CallingCameraViewer;
import javafx.util.Pair;

import javax.sound.sampled.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Test {
    static CameraConnector cameraConnector;
    static MicrophoneConnector microphoneConnector;
    static ByteArrayInputStream byteArrayInputStream;
    static AudioInputStream audioInputStream;

    static CallingCameraViewer callingCameraViewer;

    public static void main(String[] args) {
        try {

//            testByteArrayStream();
//            TestControl testControl1 = new TestControl();
//            testControl1.openUDPSocket();
//            TestControl testControl2 = new TestControl(1);

//            testCallVideo();
//            testAudio();
//            getDL();
            testSpeaker();

        } catch (Exception e) {

        }
    }

    static void testByteArrayStream() throws IOException, ClassNotFoundException {
        cameraConnector = new CameraConnector();
        cameraConnector.connect();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
//        oos.writeObject(cameraConnector.getCurrent_frame());
        cameraConnector.disconnect();
        System.out.println("baos size: " + baos.size());
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bais);
        MyFrame myFrame = (MyFrame) ois.readObject();
        BufferedImage bufferedImage = DataConvertor.MyFrame_to_BufferedImage(myFrame);
        CallingCameraViewer callingCameraViewer = new CallingCameraViewer();
        callingCameraViewer.setSize(640, 480);
        callingCameraViewer.setBufferedImage(bufferedImage);
//        callingCameraViewer.setBufferedImage(ImageIO.read(new File("src/resources/Black_1920x1080.jpg")));
        TestView testView = new TestView();
//        testView.addComponent(callingCameraViewer, 0, 0, 640, 480);
//        callingCameraViewer.setBounds(0, 0, 640, 480);
        testView.setContentPane(callingCameraViewer);
        System.out.println("hello mother fucker");
    }

    static void testSpeaker() throws InterruptedException, LineUnavailableException, IOException {
        microphoneConnector = new MicrophoneConnector();
        if (microphoneConnector.connect(1)) {
            System.out.println("Starting record:");
            microphoneConnector.listen();
            Thread.sleep(3000);
            microphoneConnector.disconnect();
            System.out.println("Stop record. Playing:");
            subTestSpeaker();
        } else {
            System.out.println("Cannot connect to Mic!");
        }
    }

    static void subTestSpeaker() throws LineUnavailableException, IOException {
        byteArrayInputStream = new ByteArrayInputStream(microphoneConnector.getByteArrayOutputStream().toByteArray());
        audioInputStream = new AudioInputStream(byteArrayInputStream, microphoneConnector.getMicrophone().getFormat(), microphoneConnector.getByteArrayOutputStream().size() / microphoneConnector.getMicrophone().getFormat().getFrameSize());
        AudioFormat format = new AudioFormat(8000.0f, 16, 1, true, true);
        System.out.println(AudioSystem.isConversionSupported(format, audioInputStream.getFormat()));
        AudioInputStream a = AudioSystem.getAudioInputStream(format, audioInputStream);
        DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, format);
        SourceDataLine sourceDataLine = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
        sourceDataLine.open();
        sourceDataLine.start();
        int cnt = 0;
        byte tempBuffer[] = new byte[10000];
        try {
            while ((cnt = a.read(tempBuffer, 0, tempBuffer.length)) != -1) {
                if (cnt > 0) {
                    // Write data to the internal buffer of
                    // the data line where it will be
                    // delivered to the speaker.
                    sourceDataLine.write(tempBuffer, 0, cnt);
                }// end if
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        microphoneConnector.resetData();
        audioInputStream.close();
        byteArrayInputStream.close();
        sourceDataLine.drain();
        sourceDataLine.close();
    }

    static class Player implements Runnable {

        @Override
        public void run() {
            try {
                byteArrayInputStream.read(microphoneConnector.getByteArrayOutputStream().toByteArray());
//                audioInputStream = new AudioInputStream(microphoneConnector.getMicrophone());
                audioInputStream = new AudioInputStream(byteArrayInputStream, microphoneConnector.getMicrophone().getFormat(), 10);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    static void getDL() {
        ArrayList<Pair<String, AudioFormat>> microphoneList = new ArrayList<>();
        ArrayList<Mixer.Info> mixerInfo = new ArrayList<Mixer.Info>(Arrays.asList(AudioSystem.getMixerInfo())); // Lấy danh sách thông tin Mixer(?) của máy
        Mixer mixer;
        ArrayList<Line.Info> targetLineInfo;
        ArrayList<Line> lines;
        for (int i = 0; i < mixerInfo.size(); ) { // Duyệt từng thông tin của Mixer
            mixer = AudioSystem.getMixer(mixerInfo.get(i)); // Mỗi thông tin của Mixer tương ứng với 1 Mixer
            targetLineInfo = new ArrayList<Line.Info>(Arrays.asList(mixer.getTargetLineInfo())); // Mỗi Mixer sẽ chứa danh sách thông tin của thiết bị trong Mixer đó
            lines = new ArrayList<>(); // Chứa các kết nối có thể sử dụng đến microphone
            for (int j = 0; j < targetLineInfo.size(); j++) {
                try {
                    lines.add(AudioSystem.getLine(targetLineInfo.get(j))); // Thêm các kết nối có thể sử dụng vào danh sách
                } catch (LineUnavailableException e) {
                    e.printStackTrace();
                }
            }
            for (Line line : lines) {
                if (line instanceof DataLine) { // Nếu các kết nối là DataLine thì lưu vào danh sách (Còn lại không phải DataLine thì có thể là port hoặc kiểu khác không dùng đến/không dùng được)
                    DataLine dataLine = (DataLine) line;
                    microphoneList.add(new Pair<>(mixerInfo.get(i).getName(), dataLine.getFormat()));
                }
            }
            i++;
            targetLineInfo.clear();
            lines.clear();
        }
        mixerInfo.clear();
        for (Pair p : microphoneList) {
            System.out.println(p.getKey() + ": " + ((AudioFormat) p.getValue()).toString());
        }
    }

    static void testAudio() {
        Mixer.Info[] mixerInfo = AudioSystem.getMixerInfo();
        for (Mixer.Info mixerInfo1 : mixerInfo) {
            System.out.println("Name: " + mixerInfo1.getName());
            System.out.println("Vendor: " + mixerInfo1.getVendor());
            System.out.println("Version: " + mixerInfo1.getVersion());
            System.out.println("Decription: " + mixerInfo1.getDescription());
            Mixer mixer = AudioSystem.getMixer(mixerInfo1);
            Line.Info[] target = mixer.getTargetLineInfo();
            System.out.println(target.length);
            Line[] all = new Line[target.length];
            try {
                for (int jj = 0; jj < all.length; jj++) { //*******************
                    all[jj] = AudioSystem.getLine(target[jj]);
                }
                for (Line line : all) {
                    if (line instanceof DataLine) {
                        DataLine dataLine = (DataLine) line;
                        AudioFormat audioFormat = dataLine.getFormat();
                        System.out.println(dataLine.getLineInfo());
                        System.out.println("Channels: " + audioFormat.getChannels());
                        System.out.println("Encoding: " + audioFormat.getEncoding());
                        System.out.println("Frame rate: " + audioFormat.getFrameRate());
                        System.out.println("Sample rate: " + audioFormat.getSampleRate());
                        System.out.println("Sample size in bits: " + audioFormat.getSampleSizeInBits());
                        System.out.println("Big Endian: " + audioFormat.isBigEndian());
                        System.out.println("Level: " + dataLine.getLevel());

                    }
                }
            } catch (LineUnavailableException e) {
                e.printStackTrace();
            } finally {
                System.out.println("-------------------------------------------");
            }
        }
    }

}
