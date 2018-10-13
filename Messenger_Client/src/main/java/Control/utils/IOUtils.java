package Control.utils;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class IOUtils {
    private static final String host = "192.168.221.100";
    private static final int port = 50000;
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
        while(client == null || client.isClosed());
        try {
            if(objectInputStream == null)
            {
                objectInputStream = new ObjectInputStream(client.getInputStream());
                System.out.println("LOL");
            }
            Object object = null;
            while(object == null){
                try{
                    object = objectInputStream.readObject();
                }catch (EOFException e){

                }
            }
            return object;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void closeSocket(){
        try {
            client.close();
            objectInputStream = null;
            objectOutputStream = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
