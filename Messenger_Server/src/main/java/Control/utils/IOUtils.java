package Control.utils;

import Model.AddParticipant.AcceptConversationResponse;
import Model.CloseSocket;
import javafx.util.Pair;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.*;

public class IOUtils {
    private static ServerSocket serverSocket;
    private static HashMap<Integer, Vector<Socket>> hashMapSocket;
    private static HashMap<Socket, Pair<ObjectInputStream, ObjectOutputStream>> IOSockets;
    public IOUtils(){}

    public static ServerSocket getServerSocketSingleton(){
        System.out.println(Thread.currentThread().getStackTrace()[1].getMethodName());
        if(serverSocket == null) {
            try {
                serverSocket = new ServerSocket(10000);
                getHashMapSocketSingleton();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return serverSocket;
    }

    static HashMap<Integer, Vector<Socket>> getHashMapSocketSingleton (){
        if(hashMapSocket == null) {
            hashMapSocket = new HashMap<Integer, Vector<Socket>>();
            IOSockets = new HashMap<Socket, Pair<ObjectInputStream, ObjectOutputStream>>();
        }
        return hashMapSocket;
    }

    public static Object readObject(Socket socket){
        Object object = null;
        try {
            addIOSocket(socket);
            try{
                object = IOSockets.get(socket).getKey().readObject();
            } catch(EOFException e){}
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return object;
    }

    public static boolean writeObject(Socket socket, Object object){
        try {
            if(socket.isClosed() || socket.equals(null)) {
                removeIOSocket(socket);
            }
            IOSockets.get(socket).getValue().writeObject(object);
            System.out.println("whireObject oke");
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
                Iterator<Socket> iterator = sockets.iterator();
                while(iterator.hasNext()){
                    Socket socket = (Socket)iterator.next();
                    if(socket.equals(null) || socket.isClosed()) {
                        iterator.remove();
                    }
                    else {
                        IOSockets.get(socket).getValue().writeObject(object);
                        System.out.println("writeObject oke");
                    }
                }
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    static void addIOSocket(Socket socket){
        try{
            if(!IOSockets.containsKey(socket)){
                Pair<ObjectInputStream, ObjectOutputStream> pair = new Pair<ObjectInputStream, ObjectOutputStream>(
                        new ObjectInputStream(socket.getInputStream()),
                        new ObjectOutputStream(socket.getOutputStream())
                );
                IOSockets.put(socket, pair);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void removeIOSocket(Socket socket){
        if(IOSockets.containsKey(socket)){
            IOSockets.remove(socket);
        }
    }

    public static void addSocket(int user_id, Socket socket){
        if(socket.isClosed() || socket.equals(null)) return;
        if(hashMapSocket.containsKey(user_id)){
            Vector<Socket> sockets = hashMapSocket.get(user_id);
            if(!sockets.contains(socket)){
                sockets.add(socket);
            }
        }
        else{
            Vector<Socket> sockets = new Vector<Socket>();
            sockets.add(socket);
            hashMapSocket.put(user_id, sockets);
        }
    }

    public static void removeSocket(int user_id, Socket socket){
        if(hashMapSocket.containsKey(user_id)){
            Vector<Socket> sockets = hashMapSocket.get(user_id);
            if(sockets.contains(socket)){
                sockets.remove(socket);
                removeIOSocket(socket);
            }
        }
    }

    public static void removeSocket(Socket socket){
        Set<Map.Entry<Integer, Vector<Socket>>> set = hashMapSocket.entrySet();
        Iterator<Map.Entry<Integer, Vector<Socket>>> iterator = set.iterator();
        if(iterator.hasNext()){
            Vector<Socket> sockets = ((Map.Entry<Integer, Vector<Socket>>)iterator.next()).getValue();
            if(sockets.contains(socket)){
                sockets.remove(socket);
                removeIOSocket(socket);
                return;
            }
        }
    }

    public static void removeSocket(int user_id){
        try{
            if(hashMapSocket.containsKey(user_id)){
                Vector<Socket> sockets = hashMapSocket.get(user_id);
                for(Socket socket: sockets){
                    if(!socket.equals(null) && !socket.isClosed()){
                        socket.close();
                    }
                }
                sockets.clear();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void closeSocket(Socket socket){
        try{
            if(socket != null && !socket.isClosed()) {
                writeObject(socket, new CloseSocket());
                socket.close();
            }
            removeIOSocket(socket);
            removeSocket(socket);
        } catch (IOException e) {
            e.printStackTrace();
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
