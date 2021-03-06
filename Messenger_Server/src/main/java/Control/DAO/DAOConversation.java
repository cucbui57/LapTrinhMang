package Control.DAO;

import Model.Conversation;
import Model.Participant;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Vector;

public class DAOConversation extends IDAO<Conversation> {
    DAOParticipant daoParticipant;
    public DAOConversation(Connection connection) {
        this.connection = connection;
        daoParticipant = new DAOParticipant(connection);
        try{
            this.statement = this.connection.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Vector<Conversation> selectAll() {
        Vector<Conversation> conversations = new Vector<Conversation>();
        try{
            String sql = "select * from Conversations";
            this.resultSet = this.statement.executeQuery(sql);
            while(resultSet.next()){
                Conversation conversation = new Conversation(
                        resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getDate(3),
                        resultSet.getDate(4)
                );
                conversation.setParticipants(daoParticipant.selectbyID(conversation.getConversation_id()));
                conversations.add(conversation);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return conversations;
    }

    @Override
    public Vector<Conversation> selectbyID(int ID) {
        Vector<Conversation> conversations = new Vector<Conversation>();
        try{
            this.preparedStatement = this.connection.prepareStatement("call ConversationSelectByID ?");
            this.preparedStatement.setInt(1, ID);
            resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                Conversation conversation = new Conversation(
                        resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getDate(3),
                        resultSet.getDate(4)
                );
                conversation.setParticipants(daoParticipant.selectbyID(conversation.getConversation_id()));
                conversations.add(conversation);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return conversations;
    }

    @Override
    public Vector<Conversation> selectbyNumbers(int ID, int numbers) {
        return null;
    }

    @Override
    public int insert(Conversation conversation) {
        int rowResult;
        try{
            this.preparedStatement = connection.prepareStatement("call ConversationInsert ? ? ? ?");
            preparedStatement.setInt(1, conversation.getConversation_id());
            preparedStatement.setString(2, conversation.getTitle());
            preparedStatement.setDate(3, conversation.getDate_created());
            preparedStatement.setDate(4, conversation.getDate_deleted());
            rowResult = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            rowResult = 0;
        }
        return rowResult;
    }

    @Override
    public int update(Conversation conversation) {
        int rowResult;
        try{
            this.preparedStatement = connection.prepareStatement("call ConversationUpdate ? ? ? ?");
            preparedStatement.setInt(1, conversation.getConversation_id());
            preparedStatement.setString(2, conversation.getTitle());
            preparedStatement.setDate(3, conversation.getDate_created());
            preparedStatement.setDate(4, conversation.getDate_deleted());
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
