package Control.DAO;

import Model.Participant;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Vector;

public class DAOParticipant extends IDAO<Participant> {
    public DAOParticipant(Connection connection) {
        this.connection = connection;
        try{
            this.statement = this.connection.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Vector<Participant> selectAll() {
        Vector<Participant> participants = new Vector<Participant>();
        try{
            String sql = "select * from Users";
            resultSet = statement.executeQuery(sql);
            while(resultSet.next()){
                Participant participant = new Participant(
                        resultSet.getInt(1),
                        resultSet.getInt(2),
                        resultSet.getBoolean(3),
                        resultSet.getString(4),
                        resultSet.getInt(5),
                        resultSet.getString(6)
                );
                participants.add(participant);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return participants;
    }

    @Override
    public Vector<Participant> selectbyID(int ID) {
        Vector<Participant> participants = new Vector<Participant>();
        try{
            this.preparedStatement = this.connection.prepareStatement("call ParticipantSelectByID ?");
            this.preparedStatement.setInt(1, ID);
            resultSet = this.preparedStatement.executeQuery();
            while(resultSet.next()){
                Participant participant = new Participant(
                        resultSet.getInt(1),
                        resultSet.getInt(2),
                        resultSet.getBoolean(3),
                        resultSet.getString(4),
                        resultSet.getInt(5),
                        resultSet.getString(6)
                );
                participants.add(participant);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return participants;
    }



    public Vector<Participant> selectbyNumbers(int ID, int numbers) {
        Vector<Participant> participants = new Vector<Participant>();
        try{
            this.preparedStatement = this.connection.prepareStatement("call ParticipantSelectByNumbers ? ?");
            this.preparedStatement.setInt(1, ID);
            this.preparedStatement.setInt(2, numbers);
            resultSet = this.preparedStatement.executeQuery();
            while(resultSet.next()){
                Participant participant = new Participant(
                        resultSet.getInt(1),
                        resultSet.getInt(2),
                        resultSet.getBoolean(3),
                        resultSet.getString(4),
                        resultSet.getInt(5),
                        resultSet.getString(6)
                );
                participants.add(participant);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return participants;
    }

    @Override
    public int insert(Participant participant) {
        int rowResult;
        try{
            preparedStatement = connection.prepareStatement("call ParticipantInsert ? ? ? ? ? ?");
            preparedStatement.setInt(1, participant.getConversation_id());
            preparedStatement.setInt(2, participant.getUser_id());
            preparedStatement.setBoolean(3, participant.isAccepted());
            preparedStatement.setString(4, participant.getType());
            preparedStatement.setInt(5, participant.getSeen_message_id());
            preparedStatement.setString(6, participant.getNickname());
            rowResult = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            rowResult = 0;
            e.printStackTrace();
        }
        return rowResult;
    }

    @Override
    public int update(Participant participant) {
        int rowResult;
        try{
            preparedStatement = connection.prepareStatement("call ParticipantUpdate ? ? ? ? ? ?");
            preparedStatement.setInt(1, participant.getConversation_id());
            preparedStatement.setInt(2, participant.getUser_id());
            preparedStatement.setBoolean(3, participant.isAccepted());
            preparedStatement.setString(4, participant.getType());
            preparedStatement.setInt(5, participant.getSeen_message_id());
            preparedStatement.setString(6, participant.getNickname());
            rowResult = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            rowResult = 0;
            e.printStackTrace();
        }
        return rowResult;
    }
    public int updateSeenMessage(int conversation_id, int user_id, int message_id) {
        int rowResult;
        try{
            preparedStatement = connection.prepareStatement("call updateSeenMessageParticipants ? ? ? ? ? ?");
            preparedStatement.setInt(1, conversation_id);
            preparedStatement.setInt(2, user_id);
            preparedStatement.setInt(3, message_id);
            rowResult = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            rowResult = 0;
            e.printStackTrace();
        }
        return rowResult;
    }
    public int updateType(int conversation_id, int user_id, String type) {
        int rowResult;
        try{
            preparedStatement = connection.prepareStatement("call updateTypeParticipants ? ? ? ? ? ?");
            preparedStatement.setInt(1, conversation_id);
            preparedStatement.setInt(2, user_id);
            preparedStatement.setString(3, type);
            rowResult = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            rowResult = 0;
            e.printStackTrace();
        }
        return rowResult;
    }
    public int updateAccepted(int conversation_id, int user_id, boolean accepted) {
        int rowResult;
        try{
            preparedStatement = connection.prepareStatement("call updateAcceptedParticipants ? ? ? ? ? ?");
            preparedStatement.setInt(1, conversation_id);
            preparedStatement.setInt(2, user_id);
            preparedStatement.setBoolean(3, accepted);
            rowResult = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            rowResult = 0;
            e.printStackTrace();
        }
        return rowResult;
    }
    public int updateNickname(int conversation_id, int user_id, String nickname) {
        int rowResult;
        try{
            preparedStatement = connection.prepareStatement("call updateSeenMessageParticipants ? ? ? ? ? ?");
            preparedStatement.setInt(1, conversation_id);
            preparedStatement.setInt(2, user_id);
            preparedStatement.setString(3, nickname);
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
            this.connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
