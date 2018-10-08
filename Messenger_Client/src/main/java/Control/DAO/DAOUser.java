package Control.DAO;

import Control.getPath;
import Control.utils.FileUtils;
import Model.User;

import java.io.*;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.Vector;

public class DAOUser extends IDAO<User> {
    public static final String pathAvata = "C://avata/";
    public DAOUser(Connection connection){
        this.connection = connection;
        try{
            this.statement = connection.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Override
    public Vector<User> selectAll() {
        Vector<User> users = new Vector<User>();
        try{
            String sql = "select * from Users";
            resultSet = statement.executeQuery(sql);
            while(resultSet.next()){
                String path = pathAvata + Integer.toString(resultSet.getInt(1)) + ".jpg";
                byte[] bytes = FileUtils.File2Byte(path);
                User user = new User(
                        resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getString(4),
                        resultSet.getString(5),
                        resultSet.getString(6),
                        resultSet.getString(7),
                        resultSet.getBoolean(8),
                        resultSet.getTimestamp(9),
                        bytes,
                        resultSet.getInt(11),
                        resultSet.getBoolean(12),
                        resultSet.getBoolean(13)
                );
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return users;
    }

    @Override
    public Vector<User> selectbyID(int ID) {
        Vector<User> users = new Vector<User>();
        try{
            String sql = "select * from Users where user_id = " + String.valueOf(ID);
            resultSet = statement.executeQuery(sql);
            while(resultSet.next()){
                String path = pathAvata + Integer.toString(resultSet.getInt(1)) + ".jpg";
                byte[] bytes = FileUtils.File2Byte(path);
                User user = new User(
                        resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getString(4),
                        resultSet.getString(5),
                        resultSet.getString(6),
                        resultSet.getString(7),
                        resultSet.getBoolean(8),
                        resultSet.getTimestamp(9),
                        bytes,
                        resultSet.getInt(11),
                        resultSet.getBoolean(12),
                        resultSet.getBoolean(13)
                );
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return users;
    }

    @Override
    public Vector<User> selectbyNumbers(int ID, int numbers) {
        return null;
    }

    public Vector<User> selectbyUsername(String username) {
        Vector<User> users = new Vector<User>();
        try{
            String sql = "select * from Users where username = '" + username + "'";
            resultSet = statement.executeQuery(sql);
            while(resultSet.next()){
                String path = pathAvata + Integer.toString(resultSet.getInt(1)) + ".jpg";
                byte[] bytes = FileUtils.File2Byte(path);
                User user = new User(
                        resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getString(4),
                        resultSet.getString(5),
                        resultSet.getString(6),
                        resultSet.getString(7),
                        resultSet.getBoolean(8),
                        resultSet.getTimestamp(9),
                        bytes,
                        resultSet.getInt(11),
                        resultSet.getBoolean(12),
                        resultSet.getBoolean(13)
                );
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return users;
    }

    public boolean isExistEmail(String email){
        try{
            String sql = "select user_id from Users where email = '" + email + "'";
            resultSet = statement.executeQuery(sql);
            while(resultSet.next()){
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isExistUsername(String username){
        try{
            String sql = "select user_id from Users where username = '" + username + "'";
            resultSet = statement.executeQuery(sql);
            while(resultSet.next()){
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public int insert(User user) {
        int rowResult;
        try{
            String path = pathAvata + Integer.toString(resultSet.getInt(1)) + ".jpg";
            if(FileUtils.Byte2File(path, user.getAvata())){
                preparedStatement = connection.prepareStatement("call insertUsers ? ? ? ? ? ? ? ? ? ? ? ?");
                preparedStatement.setInt(1, user.getUser_id());
                preparedStatement.setString(2, user.getUsername());
                preparedStatement.setString(3, user.getPassword());
                preparedStatement.setString(4, user.getEmail());
                preparedStatement.setString(5, user.getPhone_number());
                preparedStatement.setString(6, user.getFirst_name());
                preparedStatement.setString(7, user.getLast_name());
                preparedStatement.setBoolean(8, user.isSex());
                preparedStatement.setTimestamp(9, user.getBirthday());
                preparedStatement.setInt(10, user.getStatus());
                preparedStatement.setBoolean(11, user.isActived());
                preparedStatement.setBoolean(12, user.isBlocked());
                rowResult = preparedStatement.executeUpdate();
            } else {
                rowResult = 0;
            }

        } catch (SQLException e) {
            rowResult = 0;
            e.printStackTrace();
        }
        return rowResult;
    }

    @Override
    public int update(User user) {
        int rowResult;
        try{
            String path = pathAvata + Integer.toString(resultSet.getInt(1)) + ".jpg";
            if(FileUtils.Byte2File(path, user.getAvata())){
                preparedStatement = connection.prepareStatement("call updateUsers ? ? ? ? ? ? ? ? ? ? ? ?");
                preparedStatement.setInt(1, user.getUser_id());
                preparedStatement.setString(2, user.getUsername());
                preparedStatement.setString(3, user.getPassword());
                preparedStatement.setString(4, user.getEmail());
                preparedStatement.setString(5, user.getPhone_number());
                preparedStatement.setString(6, user.getFirst_name());
                preparedStatement.setString(7, user.getLast_name());
                preparedStatement.setBoolean(8, user.isSex());
                preparedStatement.setTimestamp(9, user.getBirthday());
                preparedStatement.setInt(10, user.getStatus());
                preparedStatement.setBoolean(11, user.isActived());
                preparedStatement.setBoolean(12, user.isBlocked());
                rowResult = preparedStatement.executeUpdate();
            }else {
                rowResult = 0;
            }
        } catch (SQLException e) {
            rowResult = 0;
            e.printStackTrace();
        }
        return rowResult;
    }

    public int updatePassword(int id, String password){
        int rowResult;
        try{
            preparedStatement = connection.prepareStatement("update Users set password = ? where user_id = ?");
            preparedStatement.setInt(1, id);
            preparedStatement.setString(2, password);
            rowResult = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            rowResult = 0;
            e.printStackTrace();
        }
        return rowResult;
    }
    public int updateEmail(int id, String email){
        int rowResult;
        try{
            preparedStatement = connection.prepareStatement("update Users set email = ? where user_id = ?");
            preparedStatement.setInt(1, id);
            preparedStatement.setString(2, email);
            rowResult = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            rowResult = 0;
            e.printStackTrace();
        }
        return rowResult;
    }
    public int updatePhoneNumber(int id, String phone_number){
        int rowResult;
        try{
            preparedStatement = connection.prepareStatement("update Users set phone_number = ? where user_id = ?");
            preparedStatement.setInt(1, id);
            preparedStatement.setString(2, phone_number);
            rowResult = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            rowResult = 0;
            e.printStackTrace();
        }
        return rowResult;
    }
    public int updateName(int id, String first_name, String last_name){
        int rowResult;
        try{
            preparedStatement = connection.prepareStatement("update Users set first_name = ?, last_name = ? where user_id = ?");
            preparedStatement.setInt(1, id);
            preparedStatement.setString(2, first_name);
            preparedStatement.setString(3, last_name);
            rowResult = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            rowResult = 0;
            e.printStackTrace();
        }
        return rowResult;
    }
    public int updateSex(int id, boolean sex){
        int rowResult;
        try{
            preparedStatement = connection.prepareStatement("update Users set sex = ? where user_id = ?");
            preparedStatement.setInt(1, id);
            preparedStatement.setBoolean(2, sex);
            rowResult = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            rowResult = 0;
            e.printStackTrace();
        }
        return rowResult;
    }
    public int updateActived(int id, Boolean actived){
        int rowResult;
        try{
            preparedStatement = connection.prepareStatement("update Users set is_actived = ? where user_id = ?");
            preparedStatement.setInt(1, id);
            preparedStatement.setBoolean(2, actived);
            rowResult = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            rowResult = 0;
            e.printStackTrace();
        }
        return rowResult;
    }
    public int updateBlocked(int id, Boolean blocked){
        int rowResult;
        try{
            preparedStatement = connection.prepareStatement("update Users set is_block = ? where user_id = ?");
            preparedStatement.setInt(1, id);
            preparedStatement.setBoolean(2, blocked);
            rowResult = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            rowResult = 0;
            e.printStackTrace();
        }
        return rowResult;
    }
    public int updateStatus(int id, int status){
        int rowResult;
        try{
            preparedStatement = connection.prepareStatement("update Users set status = ? where user_id = ?");
            preparedStatement.setInt(1, status);
            preparedStatement.setInt(2, id);
            rowResult = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            rowResult = 0;
            e.printStackTrace();
        }
        return rowResult;
    }
    public int updateAvata(int id, byte[] bytes){
        String path = pathAvata + Integer.toString(id) + ".jpg";
        if(FileUtils.Byte2File(path, bytes)) return 1;
        return 0;
    }
    public int updateBirthday(int id, Date date){
        int rowResult;
        try{
            preparedStatement = connection.prepareStatement("update Users set birthday = ? where user_id = ?");
            preparedStatement.setDate(1, date);
            preparedStatement.setInt(2, id);
            rowResult = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            rowResult = 0;
            e.printStackTrace();
        }
        return rowResult;
    }
    public int updateDelete(int id, Date date){
        int rowResult;
        try{
            preparedStatement = connection.prepareStatement("update Users set delete_at = ? where user_id = ?");
            preparedStatement.setDate(1, date);
            preparedStatement.setInt(2, id);
            rowResult = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            rowResult = 0;
            e.printStackTrace();
        }
        return rowResult;
    }

    @Override
    public void closeConnection() {
        try{
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
