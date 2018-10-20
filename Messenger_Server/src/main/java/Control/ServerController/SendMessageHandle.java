package Control.ServerController;

import Control.DAO.DAOMessage;
import Control.DAO.DAOParticipant;
import Control.utils.IOUtils;
import Control.utils.SQLServerConnUtils_SQLJDBC;
import Model.Message;
import Model.Participant;
import Model.SuccessRespone;

import java.net.Socket;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Vector;

public class SendMessageHandle extends Handle {
    private DAOMessage daoMessage;
    private DAOParticipant daoParticipant;
    private Message message;
    HashMap<Integer, Vector<Socket>> sockets;
    public SendMessageHandle(Message message, HashMap<Integer, Vector<Socket>> sockets) {
        this.message = message;
        this.sockets = sockets;
        Connection connection = SQLServerConnUtils_SQLJDBC.getSQLServerConnection();
        daoMessage = new DAOMessage(connection);
        daoParticipant = new DAOParticipant(connection);
    }

    @Override
    public void execute() {
        SuccessRespone successRespone = new SuccessRespone(false);
        if(daoMessage.insert(message) != 0){
            successRespone.setSuccess(true);
            Vector<Participant> participants = daoParticipant.selectbyID(message.getConversation_id());
            for(Participant participant: participants){
                for(Socket socket: sockets.get(participant.getUser_id())){
                    IOUtils.writeObject(socket, message);
                }
            }
        }

    }
}
