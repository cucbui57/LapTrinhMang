package control.UDPSocket;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class UDPSocketClient { // UDPSocket
    private DatagramPacket sendPacket; // Gửi
    private DatagramPacket receivePacket; // Nhận
    private DatagramSocket datagramSocket; // UDPSocket

    private String hostname; // Địa chỉ target
    private InetAddress address;
    private int port; // Cổng (Cho cả target và client)

    private boolean isOpen; // Có đang mở socket không?

    private ArrayBlockingQueue<Serializable> sendObjectQueue;
    private LinkedBlockingQueue<Object> receiveObjectQueue;

    private byte[] sendData; // Dữ liệu gửi
    private byte[] receiveData; // Dữ liệu nhận

    private ObjectOutputStream objectOutputStream;
    private ByteArrayOutputStream byteArrayOutputStream;
    private ObjectInputStream objectInputStream;
    private ByteArrayInputStream byteArrayInputStream;

    public UDPSocketClient(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
        try {
            address = InetAddress.getByName(hostname);
            System.out.println("UDP Socket starting at: " + address.getHostAddress() + ":" + port);
            datagramSocket = new DatagramSocket(port);

            receiveData = new byte[32000]; // Dữ liệu nhận về lớn nhất là 32KB
            sendData = new byte[32000]; // Dữ liệu gửi đi lớn nhất là 32KB

            sendObjectQueue = new ArrayBlockingQueue<>(10000);
            receiveObjectQueue = new LinkedBlockingQueue<>();

            start();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        isOpen = true;

        Thread sendObjectThread = new Thread(new SendObject());
        Thread receiveObjectThread = new Thread(new ReceiveObject());

        receiveObjectThread.start();
        sendObjectThread.start();
    }

    public boolean sendObject(Serializable sendObject) { // Gửi đi 1 Object
        try {
            // (Đọc 3 dòng dưới từ dưới lên) Ghi obbject muốn gửi đi vào ObjectOutputStream, đẩy vào ByteArrayOutputStream
            byteArrayOutputStream = new ByteArrayOutputStream();
            objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(sendObject);
//                byteArrayOutputStream.flush();
            sendData = byteArrayOutputStream.toByteArray(); // Chuyển dữ liệu thành mảng byte để có thể được gửi đi
            sendPacket = new DatagramPacket(sendData, sendData.length, address, port); // Đóng gói dữ liệu
            datagramSocket.send(sendPacket); // Gửi dữ liệu đi
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                objectOutputStream.close();
                byteArrayOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    public Object receiveObject() { // Đọc về 1 Object
        Object receivedObject = null;
        try {
            receivePacket = new DatagramPacket(receiveData, receiveData.length); // Tạo DatagramPacket ghi dữ liệu vào "receiveData" có độ dài "receiveData.length"
            datagramSocket.receive(receivePacket); // Nhận dữ liệu
            receiveData = receivePacket.getData();
            // Ghi dữ liệu nhận được vào ByteArrayInputStream, đẩy sang ObjectInputStream để đọc dạng Object
            byteArrayInputStream = new ByteArrayInputStream(receiveData);
            objectInputStream = new ObjectInputStream(byteArrayInputStream);
            receivedObject = objectInputStream.readObject();
        } catch (EOFException e) {
            receivedObject = "";
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                objectInputStream.close();
                byteArrayInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return receivedObject;
    }

    public void close() {
        isOpen = false;
        try {
            sendObjectQueue.clear();
            receiveObjectQueue.clear();
            byteArrayInputStream.close();
            objectInputStream.close();
            byteArrayOutputStream.close();
            objectOutputStream.close();
            datagramSocket.disconnect();
            datagramSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean offerSendObject(Serializable serializableObject) throws InterruptedException {
        return sendObjectQueue.offer(serializableObject, 1, TimeUnit.SECONDS);
    }

    public Object getReceiveObject() throws InterruptedException {
        return receiveObjectQueue.take();
    }

    class SendObject implements Runnable {

        @Override
        public void run() {
            while (isOpen) {
                try {
                    sendObject(sendObjectQueue.take());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    class ReceiveObject implements Runnable {

        @Override
        public void run() {
            while (isOpen) {
                try {
                    receiveObjectQueue.put(receiveObject());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
