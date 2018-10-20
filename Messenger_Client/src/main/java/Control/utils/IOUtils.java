package Control.utils;

import Model.CloseSocket;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class IOUtils {
    private static final String host = "localhost";
    private static final int port = 10000;
    private static Socket client;
    private static ObjectInputStream objectInputStream;
    private static ObjectOutputStream objectOutputStream;

    public static void createIOSocket(){
        if(client == null || client.isClosed()){
            try {
                client = new Socket(host, port);
                objectOutputStream = new ObjectOutputStream(client.getOutputStream());
                objectInputStream = new ObjectInputStream(client.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void writeObject(Object object){
        createIOSocket();
        try {
            objectOutputStream.writeObject(object);
            System.out.println("write oke");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Object readObject(){
        Object object = null;
        createIOSocket();
        try {
            try{
                object = objectInputStream.readObject();
            } catch(EOFException e){

            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return object;
    }

    public static void closeSocket(){
        try {
            writeObject(new CloseSocket());
            Thread.sleep(1000);
            client.close();
            objectInputStream = null;
            objectOutputStream = null;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
