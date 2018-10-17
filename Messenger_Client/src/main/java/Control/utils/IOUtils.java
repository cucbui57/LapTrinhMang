package Control.utils;

import Model.CloseSocket;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class IOUtils {
    private static final String host = "192.168.1.4";
    private static final int port = 10000;
    private static Socket client;
    private static ObjectInputStream objectInputStream;
    private static ObjectOutputStream objectOutputStream;

    public static void createIOSocket(){
        try {
            client = new Socket(host, port);
            objectOutputStream = null;
            objectInputStream = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeObject(Object object){
        try {
            if(objectOutputStream == null)
                objectOutputStream = new ObjectOutputStream(client.getOutputStream());
            objectOutputStream.writeObject(object);
            System.out.println("write oke");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Object readObject(){
        Object object = null;
        while(client == null || client.isClosed());
        try {
            if(objectInputStream == null)
            {
                objectInputStream = new ObjectInputStream(client.getInputStream());
                System.out.println("LOL");
            }
            while(object == null){
                if(client.isClosed()){
                    objectInputStream = null;
                    objectOutputStream = null;
                    return object;
                }
                try{
                    object = objectInputStream.readObject();
                }catch (EOFException e){

                }
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
