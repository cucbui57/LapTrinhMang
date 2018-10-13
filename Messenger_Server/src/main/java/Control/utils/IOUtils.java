package Control.utils;

import Model.AddParticipant.AcceptConversationResponse;
import javafx.util.Pair;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

public class IOUtils {
    private static ServerSocket serverSocket;
    private static HashMap<Integer, Vector<Socket>> hashMapSocket;
    private static HashMap<Socket, Pair<ObjectInputStream, ObjectOutputStream>> socketIO;
    public IOUtils(){}

    public static ServerSocket getServerSocketSingleton(){
        if(serverSocket == null) {
            try {
                serverSocket = new ServerSocket(50000);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return serverSocket;
    }

    public static HashMap<Integer, Vector<Socket>> getHashMapSocketSingleton (){
        if(hashMapSocket == null) {
            hashMapSocket = new HashMap<Integer, Vector<Socket>>();
            socketIO = new HashMap<Socket, Pair<ObjectInputStream, ObjectOutputStream>>();
        }
        return hashMapSocket;
    }

    public static Object readObject(Socket socket){
        Object object = null;
        ObjectInputStream objectInputStream = null;
        try {
            if(!socketIO.containsKey(socket)){
                Pair<ObjectInputStream, ObjectOutputStream> pair = new Pair<ObjectInputStream, ObjectOutputStream>(
                        new ObjectInputStream(socket.getInputStream()),
                        new ObjectOutputStream(socket.getOutputStream()));
                socketIO.put(socket, pair);
            }
            while(object == null){
                try{
                    objectInputStream = socketIO.get(socket).getKey();
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

    public static boolean writeObject(Socket socket, Object object){
        ObjectOutputStream objectOutputStream = null;
        try {
            if(!socketIO.containsKey(socket)) {
                objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                objectOutputStream.writeObject(object);
                System.out.println("write oke");
                return true;
            }
            objectOutputStream = socketIO.get(socket).getValue();
            objectOutputStream.writeObject(object);
            System.out.println("write oke");
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean writeObject(Vector<Integer> user_id, Object object){
        ObjectOutputStream objectOutputStream = null;
        try{
            for (Integer integer : user_id) {
                Vector<Socket> sockets = hashMapSocket.get(integer);
                for (Socket client : sockets) {
                    objectOutputStream = socketIO.get(client).getValue();
                    objectOutputStream.writeObject(object);
                    System.out.println("write oke");
                }
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void addSocket(int user_id, Socket client){
        if(hashMapSocket.containsKey(user_id)){
            Vector<Socket> sockets = hashMapSocket.get(user_id);
            Iterator it = sockets.iterator();
            while(it.hasNext()){
                Socket socket = (Socket)it.next();
                if(socket.getPort() == client.getPort() && socket.getInetAddress() == client.getInetAddress()){
                    it.remove();
                    break;
                }
            }
            sockets.add(client);
        }
        else{
            Vector<Socket> sockets = new Vector<Socket>();
            sockets.add(client);
            hashMapSocket.put(user_id, sockets);
        }
    }

    public static byte[] File2Byte(String path){
        File file = new File(path);
        byte[] bytes = new byte[(int)file.length()];
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            fileInputStream.read(bytes);
            fileInputStream.close();
        } catch (Exception e) {
            bytes = null;
            e.printStackTrace();
        }
        return bytes;
    }
    public static boolean Byte2File(String path, byte[] bytes){
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(new File(path));
            fileOutputStream.write(bytes);
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
