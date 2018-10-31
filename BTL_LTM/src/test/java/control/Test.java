package control;

import control.UDPSocket.UDPSocketClient;
import control.devices.cameras.CameraConnector;
import control.devices.microphones.MicrophoneConnector;
import control.devices.speakers.SpeakerConnector;
import javafx.util.Pair;
import model.MyAudioDataFrame;
import model.MyImageFrame;
import view.CallingCameraViewer;
import view.TestView;

import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class Test {
    static CameraConnector cameraConnector;
    static MicrophoneConnector microphoneConnector;
    static SpeakerConnector speakerConnector;

    static UDPSocketClient udpSocketClient;
    static boolean isOpen;

    static ByteArrayInputStream byteArrayInputStream;
    static AudioInputStream audioInputStream;

    static CallingCameraViewer callingCameraViewer;

    public static void main(String[] args) {
        try {
            cameraConnector = new CameraConnector();
            cameraConnector.connect();
            microphoneConnector = new MicrophoneConnector();
            microphoneConnector.connect(microphoneConnector.getMicrophone_num() - 1);
            speakerConnector = new SpeakerConnector();
            speakerConnector.connect();

//            udpSocketClient = new UDPSocketClient("localhost", 6969);

//            testByteArrayStream();
//            TestControl testControl1 = new TestControl();
//            testControl1.openUDPSocket();
//            TestControl testControl2 = new TestControl(1);

//            testUDPClient();

//            testAudio();
//            getDL();
            testSpeaker();


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cameraConnector.disconnect();
            microphoneConnector.disconnect();
            speakerConnector.disconnect();

            udpSocketClient.close();
        }
    }

    static void testUDPClient() throws InterruptedException {
        AudioFormat audioFormat;
        byte[] data;
        audioFormat = microphoneConnector.getCurrent_microphone().getFormat();
        data = new byte[microphoneConnector.getCurrent_microphone().getBufferSize() / 5];
        Thread processIncomingData = new Thread(new ProcessIncomingData());
        isOpen = true;
        processIncomingData.start();

        for (int i = 0; i < 10; i++) {
            if (udpSocketClient.offerSendObject(DataConvertor.create_MyImageFrame_from_BufferedImage(cameraConnector, 1)))
                System.out.println(i);

            microphoneConnector.getCurrent_microphone().read(data, 0, data.length); // Đọc dữ liệu từ microphone vào mảng byte "data"

//            udpSocketClient.offerSendObject(new MyAudioDataFrame(1, 1, audioFormat, data));
            Thread.sleep(1000);
        }
        isOpen = false;
        System.exit(0);
    }

    static class ProcessIncomingData implements Runnable {

        @Override
        public void run() {
            Object receiveObject;
            while (isOpen) {
                try {
                    receiveObject = udpSocketClient.getReceiveObject();

                    if (receiveObject instanceof MyImageFrame) { // Nếu là kiểu MyImageFrame
                        MyImageFrame receiveImageFrame = (MyImageFrame) receiveObject;
                        DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
                        Date date = new Date();
                        ImageIO.write(DataConvertor.MyFrame_to_BufferedImage(receiveImageFrame), "jpg", new File("testoutput/ReceivedImage/" + dateFormat.format(date) + "_" + receiveImageFrame.getFrame_index() + ".jpg"));
                    } else {
                        if (receiveObject instanceof MyAudioDataFrame) {
                            MyAudioDataFrame receiveAudioDataFrame = (MyAudioDataFrame) receiveObject;
                            speakerConnector.play(DataConvertor.MyAudioDataFrame_to_AudioInputStream(receiveAudioDataFrame));
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
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
        MyImageFrame myImageFrame = (MyImageFrame) ois.readObject();
        BufferedImage bufferedImage = DataConvertor.MyFrame_to_BufferedImage(myImageFrame);
        CallingCameraViewer callingCameraViewer = new CallingCameraViewer();
        callingCameraViewer.setSize(640, 480);
        callingCameraViewer.setBufferedImage(bufferedImage);
//        callingCameraViewer.setBufferedImage(ImageIO.read(new File("src/main.resources/Black_1920x1080.jpg")));
        TestView testView = new TestView();
//        testView.addComponent(callingCameraViewer, 0, 0, 640, 480);
//        callingCameraViewer.setBounds(0, 0, 640, 480);
        testView.setContentPane(callingCameraViewer);
        System.out.println("hello mother fucker");
    }

    static void testSpeaker() throws InterruptedException, IOException, UnsupportedAudioFileException {
        String filename = "Roller Coaster - Chung Ha [Lossless_FLAC].wav";
        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("E:\\Music\\" + filename).getAbsoluteFile());
        System.out.println("Playing audio file...");
//            speakerConnector.play(audioInputStream);

        System.out.println("Starting record:");
//            microphoneConnector.resetData();
        microphoneConnector.listen();
        Thread.sleep(3000);
        System.out.println("Stopped record. Playing:");
        microphoneConnector.getByteArrayOutputStream().flush();
        speakerConnector.play(microphoneConnector.getByteArrayOutputStream(), microphoneConnector.getMicrophoneList().get(microphoneConnector.getCurrent_microphone_index()).getValue());
        System.out.println("Play finished!");

        microphoneConnector.disconnect();
        Thread.sleep(6000);
        System.exit(0);
    }

    static void subTestSpeaker() throws LineUnavailableException, IOException {
        byteArrayInputStream = new ByteArrayInputStream(microphoneConnector.getByteArrayOutputStream().toByteArray());
        audioInputStream = new AudioInputStream(byteArrayInputStream, microphoneConnector.getCurrent_microphone().getFormat(), microphoneConnector.getByteArrayOutputStream().size() / microphoneConnector.getCurrent_microphone().getFormat().getFrameSize());
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
                audioInputStream = new AudioInputStream(byteArrayInputStream, microphoneConnector.getCurrent_microphone().getFormat(), 10);
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
            System.out.println(p.getKey() + ": " + p.getValue().toString());
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
