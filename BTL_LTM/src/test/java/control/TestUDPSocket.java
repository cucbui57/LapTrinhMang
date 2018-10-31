package control;

import control.devices.cameras.CameraConnector;
import model.MyImageFrame;
import view.CallingCameraViewer;

import java.awt.image.BufferedImage;
import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TestUDPSocket {
    private DatagramPacket sendPacket;
    private DatagramPacket receivePacket;
    private DatagramSocket datagramSocket;

    private InetAddress address;
    private int port;

    private boolean isOpen;

    private byte[] receiveData;
    private byte[] sendData;

    private CameraConnector cameraConnector;
    private int lastest_send_frame_index;

    private Object receiveObject;

    private int lastest_receive_frame_index;
    private MyImageFrame receiveFrame;
    private CallingCameraViewer callingCameraViewer;

    private ObjectOutputStream objectOutputStream;
    private ByteArrayOutputStream byteArrayOutputStream;
    private ObjectInputStream objectInputStream;
    private ByteArrayInputStream byteArrayInputStream;

    private int initialDelay;
    private int period;
    private ScheduledExecutorService scheduledSendFrame; // Kiểm soát luồng đọc từ camera

    public TestUDPSocket(CameraConnector cameraConnector, CallingCameraViewer callingCameraViewer) {
        this.cameraConnector = cameraConnector;
        this.callingCameraViewer = callingCameraViewer;
        try {
            address = InetAddress.getByName("laisoltmquaco.onthewifi.com");
            System.out.println(address.getHostAddress());
            port = 6969;
            datagramSocket = new DatagramSocket(port);
            receiveData = new byte[32000];
            sendData = new byte[32000];
            isOpen = true;
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void send(Object sendObject) {
        try {
            byteArrayOutputStream = new ByteArrayOutputStream();
            objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
//                System.out.println("Send frame: " + cameraConnector.getCurrent_frame().getFrame_index());
            objectOutputStream.writeObject(sendObject);
//                byteArrayOutputStream.flush();
            sendData = byteArrayOutputStream.toByteArray();
            sendPacket = new DatagramPacket(sendData, sendData.length, address, port);
            datagramSocket.send(sendPacket);
            objectOutputStream.close();
            byteArrayOutputStream.close();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void receive() {
        while (isOpen) {
            try {
                receivePacket = new DatagramPacket(receiveData, receiveData.length);
                datagramSocket.receive(receivePacket);
                receiveData = receivePacket.getData();
                byteArrayInputStream = new ByteArrayInputStream(receiveData);
                objectInputStream = new ObjectInputStream(byteArrayInputStream);
//                receiveFrame = (MyImageFrame) objectInputStream.readObject();
                receiveObject = objectInputStream.readObject();
                if (receiveObject instanceof MyImageFrame) {
                    receiveFrame = (MyImageFrame) receiveObject;
                    if (lastest_receive_frame_index < receiveFrame.getFrame_index()) {
                        lastest_receive_frame_index = receiveFrame.getFrame_index();
                        callingCameraViewer.setBufferedImage(DataConvertor.MyFrame_to_BufferedImage(receiveFrame));
                        BufferedImage bufferedImage = DataConvertor.MyFrame_to_BufferedImage(receiveFrame);
//                    System.out.println("Receive frame: " + receiveFrame.getFrame_index());
//                    ImageIO.write(bufferedImage, "jpg", new File("testoutput/" + receiveFrame.getFrame_index() + ".jpg"));
                        callingCameraViewer.setBufferedImage(bufferedImage);
                    }
                }
                objectInputStream.close();
                byteArrayInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public void start() {
        lastest_send_frame_index = -1;
        lastest_receive_frame_index = -1;

        initialDelay = 0;
        period = 1000000000 / 30;
        scheduledSendFrame = Executors.newScheduledThreadPool(1);
        Thread sendFrameThread = new Thread(new SendFrame());
        scheduledSendFrame.scheduleAtFixedRate(sendFrameThread, initialDelay, period, TimeUnit.NANOSECONDS);

        receive();
    }

    public void stop() {
        isOpen = false;
        scheduledSendFrame.shutdown();
    }

    class SendFrame implements Runnable {

        @Override
        public void run() {
            if (lastest_send_frame_index < cameraConnector.getCurrent_frame_index()) {
                lastest_send_frame_index = cameraConnector.getCurrent_frame_index();
//                sendObject(cameraConnector.getCurrent_frame());
            }
        }
    }
}
