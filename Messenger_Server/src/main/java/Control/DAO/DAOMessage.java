package Control.DAO;

import Control.utils.IOUtils;
import Model.Message;

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
                            resultSet.getDate(4),
                            resultSet.getTime(5),
                            resultSet.getBoolean(6),
                            resultSet.getString(7),
                            resultSet.getString(8).getBytes()
                    );
                    messages.add(message);
                }else {
                    String path = pathAttachFile + Integer.toString(resultSet.getInt(2)) + resultSet.getString(6);
                    byte[] bytes = IOUtils.File2Byte(path);
                    Message message = new Message(
                            resultSet.getInt(1),
                            resultSet.getInt(2),
                            resultSet.getInt(3),
                            resultSet.getDate(4),
                            resultSet.getTime(5),
                            resultSet.getBoolean(6),
                            resultSet.getString(7),
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
                            resultSet.getDate(4),
                            resultSet.getTime(5),
                            resultSet.getBoolean(6),
                            resultSet.getString(7),
                            resultSet.getString(8).getBytes()
                    );
                    messages.add(message);
                }else {
                    String path = pathAttachFile + Integer.toString(resultSet.getInt(2)) + resultSet.getString(6);
                    byte[] bytes = IOUtils.File2Byte(path);
                    Message message = new Message(
                            resultSet.getInt(1),
                            resultSet.getInt(2),
                            resultSet.getInt(3),
                            resultSet.getDate(4),
                            resultSet.getTime(5),
                            resultSet.getBoolean(6),
                            resultSet.getString(7),
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
            this.preparedStatement = connection.prepareStatement("select top ? * from Messages where conversation_id = ?");
            preparedStatement.setInt(1, numbers);
            preparedStatement.setInt(2, ID);
            resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                if(resultSet.getBoolean(5) == false){
                    Message message = new Message(
                            resultSet.getInt(1),
                            resultSet.getInt(2),
                            resultSet.getInt(3),
                            resultSet.getDate(4),
                            resultSet.getTime(5),
                            resultSet.getBoolean(6),
                            resultSet.getString(7),
                            resultSet.getString(8).getBytes()
                    );
                    messages.add(message);
                }else {
                    String path = pathAttachFile + Integer.toString(resultSet.getInt(2)) + resultSet.getString(6);
                    byte[] bytes = IOUtils.File2Byte(path);
                    Message message = new Message(
                            resultSet.getInt(1),
                            resultSet.getInt(2),
                            resultSet.getInt(3),
                            resultSet.getDate(4),
                            resultSet.getTime(5),
                            resultSet.getBoolean(6),
                            resultSet.getString(7).substring(5),
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
            preparedStatement.setDate(4, message.getDate_create());
            preparedStatement.setTime(5, message.getTime_create());
            preparedStatement.setBoolean(6, message.isFile());
            preparedStatement.setString(7, message.getName());
            if(message.isFile()){
                String path = pathAttachFile + Integer.toString(message.getMessage_id()) + message.getName();
                if(IOUtils.Byte2File(path, message.getContent())){
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
