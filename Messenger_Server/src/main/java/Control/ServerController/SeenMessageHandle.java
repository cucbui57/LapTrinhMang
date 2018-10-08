package Control.ServerController;

import Control.DAO.DAOParticipant;
import Control.utils.FileUtils;
import Control.utils.SQLServerConnUtils_SQLJDBC;
import Model.Participant;
import Model.SuccessRespone;

import java.net.Socket;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Vector;

public class SeenMessageHandle extends Handle<SuccessRespone> {
    DAOParticipant daoParticipant;
    Participant participant;
    HashMap<Integer, Vector<Socket>> sockets;
    public SeenMessageHandle(Participant participant, HashMap<Integer, Vector<Socket>> sockets) {
        this.participant = participant;
        this.sockets = sockets;
        try {
            Connection connection = SQLServerConnUtils_SQLJDBC.getSQLServerConnection();
            daoParticipant = new DAOParticipant(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    SuccessRespone execute() {
        SuccessRespone successRespone = new SuccessRespone(false);
        if(daoParticipant.updateSeenMessage(participant.getConversation_id(), participant.getUser_id(), participant.getSeen_message_id()) != 0){
            successRespone.setSuccess(true);
            Vector<Participant> participants = daoParticipant.selectbyID(participant.getConversation_id());
            for(Participant participant: participants){
                for(Socket socket: sockets.get(participant.getUser_id())){
                    FileUtils.writeObject(socket, participant);
                }
            }
        }
        return successRespone;
    }
}