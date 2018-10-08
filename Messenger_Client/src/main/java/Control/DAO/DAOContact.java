package Control.DAO;

import Model.Contact;
import Model.Conversation;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Vector;

public class DAOContact extends IDAO<Contact> {
    public DAOContact(Connection connection) {
        this.connection = connection;
        try{
            this.statement = this.connection.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Vector<Contact> selectAll() {
        Vector<Contact> contacts = new Vector<Contact>();
        try{
            String sql = "select * from Contacts";
            this.resultSet = this.statement.executeQuery(sql);
            while(resultSet.next()){
                Contact contact = new Contact(
                        resultSet.getInt(1),
                        resultSet.getInt(2),
                        resultSet.getInt(3)
                );
                contacts.add(contact);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return contacts;
    }

    @Override
    public Vector<Contact> selectbyID(int ID) {
        Vector<Contact> contacts = new Vector<Contact>();
        try{
            String sql = "select * from Contacts where user_id = " + String.valueOf(ID);
            this.resultSet = this.statement.executeQuery(sql);
            while(resultSet.next()){
                Contact contact = new Contact(
                        resultSet.getInt(1),
                        resultSet.getInt(2),
                        resultSet.getInt(3)
                );
                contacts.add(contact);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return contacts;
    }

    @Override
    public Vector<Contact> selectbyNumbers(int ID, int numbers) {
        return null;
    }

    @Override
    public int insert(Contact contact) {
        int rowResult;
        try{
            this.preparedStatement = connection.prepareStatement("call insertContacts ? ? ?");
            preparedStatement.setInt(1, contact.getUser_id());
            preparedStatement.setInt(2, contact.getContact_id());
            preparedStatement.setInt(3, contact.getStatus());
            rowResult = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            rowResult = 0;
        }
        return rowResult;
    }

    @Override
    public int update(Contact contact) {
        int rowResult;
        try{
            this.preparedStatement = connection.prepareStatement("call updateContacts ? ? ?");
            preparedStatement.setInt(1, contact.getUser_id());
            preparedStatement.setInt(2, contact.getContact_id());
            preparedStatement.setInt(3, contact.getStatus());
            rowResult = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            rowResult = 0;
        }
        return rowResult;
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
