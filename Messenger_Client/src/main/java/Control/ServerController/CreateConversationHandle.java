package Control.ServerController;

import Control.DAO.DAOConversation;
import Control.DAO.DAOParticipant;
import Control.utils.FileUtils;
import Control.utils.SQLServerConnUtils_SQLJDBC;
import Model.CreateConversation.CreateConversationServerSend2Clients;
import Model.CreateConversation.CreateConversationRequest;
import Model.CreateConversation.CreateConversationResponse;
import Model.Participant;
import Model.User;

import java.net.Socket;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Vector;

public class CreateConversationHandle extends Handle<CreateConversationResponse> {
    CreateConversationRequest createConversationRequest;
    DAOConversation daoConversation;
    DAOParticipant daoParticipant;
    HashMap<Integer, Vector<Socket>> sockets;
    public CreateConversationHandle(CreateConversationRequest createConversationRequest, HashMap<Integer, Vector<Socket>> sockets) {
        this.createConversationRequest = createConversationRequest;
        this.sockets = sockets;
        try{
            Connection connection = SQLServerConnUtils_SQLJDBC.getSQLServerConnection();
            daoConversation = new DAOConversation(connection);
            daoParticipant = new DAOParticipant(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    CreateConversationResponse execute() {
        CreateConversationServerSend2Clients createConversationServerSend2Clients = new CreateConversationServerSend2Clients();
        createConversationServerSend2Clients.setConversation(createConversationRequest.getConversation());
        int res = daoConversation.insert(createConversationRequest.getConversation());
        if(res == 0) return new CreateConversationResponse(false);
        for(User user: createConversationRequest.getUsers()){
            Participant participant = new Participant(
                    createConversationRequest.getConversation().getConversation_id(),
                    user.getUser_id(),
                    null,
                    "admin",
                    null,
                    user.getFirst_name() + " " + user.getLast_name()
            );
            res = daoParticipant.insert(participant);
            if(res == 0) return new CreateConversationResponse(false);
            createConversationServerSend2Clients.getParticipants().add(participant);
            for(Socket socket: sockets.get(participant.getUser_id())){
                FileUtils.writeObject(socket, createConversationServerSend2Clients);
            }
        }
        return new CreateConversationResponse(true);
    }
}
