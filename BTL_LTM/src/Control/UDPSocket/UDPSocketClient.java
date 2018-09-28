package Control.UDPSocket;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class UDPSocketClient implements Runnable { // Luồng kiểm soát vào ra UDPSocket
    private DatagramPacket sendPacket; // Gửi
    private DatagramPacket receivePacket; // Nhận
    private DatagramSocket datagramSocket; // UDPSocket

    private String hostname; // Địa chỉ target
    private InetAddress address;
    private int port; // Cổng (Cho cả target và client)

    private boolean isOpen; // Có đang mở socket không?

    private byte[] sendData; // Dữ liệu gửi
    private byte[] receiveData; // Dữ liệu nhận

    private Object receive_object; // Object nhận được

    private ObjectOutputStream objectOutputStream;
    private ByteArrayOutputStream byteArrayOutputStream;
    private ObjectInputStream objectInputStream;
    private ByteArrayInputStream byteArrayInputStream;

    private ProcessUDPSocketData processUDPSocketData;

    public UDPSocketClient(ProcessUDPSocketData processUDPSocketData, String hostname, int port) {
        this.processUDPSocketData = processUDPSocketData;
        this.hostname = hostname;
        this.port = port;
        try {
            address = InetAddress.getByName(hostname);
            System.out.println("UDP Socket starting at: " + address.getHostAddress() + ":" + port);
            datagramSocket = new DatagramSocket(port);
            receiveData = new byte[32000]; // Dữ liệu nhận về lớn nhất là 32KB
            sendData = new byte[32000]; // Dữ liệu gửi đi lớn nhất là 32KB
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void send(Object sendObject) {
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
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                objectOutputStream.close();
                byteArrayOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void listen() {
        while (isOpen) {
            try {
                receivePacket = new DatagramPacket(receiveData, receiveData.length); // Tạo DatagramPacket ghi dữ liệu vào "receiveData" có độ dài "receiveData.length"
                datagramSocket.receive(receivePacket); // Nhận dữ liệu
                receiveData = receivePacket.getData();
                // Ghi dữ liệu nhận được vào ByteArrayInputStream, đẩy sang ObjectInputStream để đọc dạng Object
                byteArrayInputStream = new ByteArrayInputStream(receiveData);
                objectInputStream = new ObjectInputStream(byteArrayInputStream);
                receive_object = objectInputStream.readObject();

                processUDPSocketData.processUDPSocketIncomingData(receive_object); // Xử lý Object nhận được
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
        }
    }

    @Override
    public void run() {
        isOpen = true; // Đang mở
        listen();
    }

    public void close() {
        isOpen = false;
        try {
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

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

}
