package Control.DAO;

import Control.utils.FileUtils;
import Model.Message;

import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Vector;

public class DAOMessage extends IDAO<Message> {
    public static final String pathAttachFile = "C://AttachFile/";
    public DAOMessage(Connection connection) {
        this.connection = connection;
        try {
            this.statement = connection.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Vector<Message> selectAll() {
        Vector<Message> messages = new Vector<Message>();
        try{
            String sql = "select * from Messages";
            this.resultSet = this.statement.executeQuery(sql);
            while(resultSet.next()){
                if(resultSet.getBoolean(5) == false){
                    Message message = new Message(
                            resultSet.getInt(1),
                            resultSet.getInt(2),
                            resultSet.getInt(3),
                            resultSet.getTimestamp(4),
                            resultSet.getBoolean(5),
                            resultSet.getString(6),
                            resultSet.getString(7).getBytes()
                    );
                    messages.add(message);
                }else {
                    String path = pathAttachFile + Integer.toString(resultSet.getInt(2)) + resultSet.getString(6);
                    byte[] bytes = FileUtils.File2Byte(path);
                    Message message = new Message(
                            resultSet.getInt(1),
                            resultSet.getInt(2),
                            resultSet.getInt(3),
                            resultSet.getTimestamp(4),
                            resultSet.getBoolean(5),
                            resultSet.getString(6),
                            bytes
                    );
                    messages.add(message);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return messages;
    }

    @Override
    public Vector<Message> selectbyID(int ID) {
        Vector<Message> messages = new Vector<Message>();
        try{
            String sql = "select * from Messages where message_id = " + String.valueOf(ID);
            this.resultSet = this.statement.executeQuery(sql);
            while(resultSet.next()){
                if(resultSet.getBoolean(5) == false){
                    Message message = new Message(
                            resultSet.getInt(1),
                            resultSet.getInt(2),
                            resultSet.getInt(3),
                            resultSet.getTimestamp(4),
                            resultSet.getBoolean(5),
                            resultSet.getString(6),
                            resultSet.getString(7).getBytes()
                    );
                    messages.add(message);
                }else {
                    String path = pathAttachFile + Integer.toString(resultSet.getInt(2)) + resultSet.getString(6);
                    byte[] bytes = FileUtils.File2Byte(path);
                    Message message = new Message(
                            resultSet.getInt(1),
                            resultSet.getInt(2),
                            resultSet.getInt(3),
                            resultSet.getTimestamp(4),
                            resultSet.getBoolean(5),
                            resultSet.getString(6),
                            bytes
                    );
                    messages.add(message);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return messages;
    }

    @Override
    public Vector<Message> selectbyNumbers(int ID, int numbers) {
        Vector<Message> messages = new Vector<Message>();
        try{
            this.preparedStatement = connection.prepareStatement("select * from Messages where conversation_id = ? limit ?");
            preparedStatement.setInt(1, ID);
            preparedStatement.setInt(2, numbers);
            resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                if(resultSet.getBoolean(5) == false){
                    Message message = new Message(
                            resultSet.getInt(1),
                            resultSet.getInt(2),
                            resultSet.getInt(3),
                            resultSet.getTimestamp(4),
                            resultSet.getBoolean(5),
                            resultSet.getString(6),
                            resultSet.getString(7).getBytes()
                    );
                    messages.add(message);
                }else {
                    String path = pathAttachFile + Integer.toString(resultSet.getInt(2)) + resultSet.getString(6);
                    byte[] bytes = FileUtils.File2Byte(path);
                    Message message = new Message(
                            resultSet.getInt(1),
                            resultSet.getInt(2),
                            resultSet.getInt(3),
                            resultSet.getTimestamp(4),
                            resultSet.getBoolean(5),
                            resultSet.getString(6).substring(5),
                            bytes
                    );
                    messages.add(message);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return messages;
    }

    @Override
    public int insert(Message message) {
        int rowResult = 0;
        try{
            this.preparedStatement = connection.prepareStatement("call insertMessages ? ? ? ? ? ? ?");
            preparedStatement.setInt(1, message.getMessage_id());
            preparedStatement.setInt(2, message.getConversation_id());
            preparedStatement.setInt(3, message.getSender_id());
            preparedStatement.setTimestamp(4, message.getCreate_at());
            preparedStatement.setBoolean(5, message.isFile());
            preparedStatement.setString(6, message.getName());
            if(message.isFile()){
                String path = pathAttachFile + Integer.toString(message.getMessage_id()) + message.getName();
                if(FileUtils.Byte2File(path, message.getContent())){
                    preparedStatement.setString(7, null);
                    rowResult = preparedStatement.executeUpdate();
                }
            }else {
                preparedStatement.setString(7, message.getContent().toString());
                rowResult = preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rowResult;
    }

    @Override
    public int update(Message message) {
        return 0;
    }

    @Override
    public void closeConnection() {
        try{
            this.connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
