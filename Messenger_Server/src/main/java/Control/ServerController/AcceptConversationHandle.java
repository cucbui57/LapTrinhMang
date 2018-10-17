package Control.ServerController;

import Control.DAO.DAOConversation;
import Control.DAO.DAOMessage;
import Control.DAO.DAOParticipant;
import Control.utils.IOUtils;
import Control.utils.SQLServerConnUtils_SQLJDBC;
import Model.Conversation;
import Model.CreateConversation.AcceptNewConversationClientSend2Server;
import Model.CreateConversation.AcceptNewConversationServerSend2Clients;
import Model.CreateConversation.ConversationServerSend2Client;
import Model.Message;
import Model.Participant;

import java.net.Socket;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Vector;

public class AcceptConversationHandle extends Handle {
    private AcceptNewConversationClientSend2Server acceptNewConversationClientSend2Server;
    DAOParticipant daoParticipant;
    DAOMessage daoMessage;
    DAOConversation daoConversation;
    HashMap<Integer, Vector<Socket>> sockets;
    public AcceptConversationHandle(AcceptNewConversationClientSend2Server acceptNewConversationClientSend2Server, HashMap<Integer, Vector<Socket>> sockets) {
        this.acceptNewConversationClientSend2Server = acceptNewConversationClientSend2Server;
        this.sockets = sockets;
        Connection connection = SQLServerConnUtils_SQLJDBC.getSQLServerConnection();
        this.daoParticipant = new DAOParticipant(connection);
        this.daoMessage = new DAOMessage(connection);
        this.daoConversation = new DAOConversation(connection);
    }

    @Override
    public void execute() {
        if(acceptNewConversationClientSend2Server.getParticipant().isAccepted()){
            int res = daoParticipant.updateAccepted(acceptNewConversationClientSend2Server.getParticipant().getConversation_id(),
                                                    acceptNewConversationClientSend2Server.getParticipant().getUser_id(),
                                                    true);
//            if(res == 0) return null;
            for(Integer user_id: acceptNewConversationClientSend2Server.getParticipants_id()){
                for(Socket socket: sockets.get(user_id)){
                    IOUtils.writeObject(socket, new AcceptNewConversationServerSend2Clients(acceptNewConversationClientSend2Server.getParticipant()));
                }
            }
            Vector<Message> messages = daoMessage.selectbyNumbers(acceptNewConversationClientSend2Server.getParticipant().getConversation_id(), 20);
            Vector<Participant> participants = daoParticipant.selectbyID(acceptNewConversationClientSend2Server.getParticipant().getConversation_id());
            Conversation conversation = daoConversation.selectbyID(acceptNewConversationClientSend2Server.getParticipant().getConversation_id()).elementAt(0);
//            return new ConversationServerSend2Client(conversation, messages, participants);
        }
//        return null;
    }
}
