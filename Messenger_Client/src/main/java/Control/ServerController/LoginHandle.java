package Control.ServerController;

import Control.DAO.*;
import Control.utils.SQLServerConnUtils_SQLJDBC;
import Model.*;
import Model.Login.LoginRequest;
import Model.Login.LoginResponse;

import javax.jws.soap.SOAPBinding;
import java.sql.*;
import java.util.HashMap;
import java.util.Vector;

public class LoginHandle extends Handle<LoginResponse> {
    LoginRequest loginRequest;
    DAOUser daoUser;
    DAOParticipant daoParticipant;
    DAOContact daoContact;
    DAOConversation daoConversation;
    DAOMessage daoMessage;
    LoginHandle(LoginRequest loginRequest){
        this.loginRequest = loginRequest;
        try {
            this.connection = SQLServerConnUtils_SQLJDBC.getSQLServerConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    LoginResponse execute() {
        LoginResponse loginResponse = new LoginResponse();
        Vector<User> users = daoUser.selectbyUsername(loginRequest.getUsername());
        if(users != null) {
            daoUser = new DAOUser(connection);
            daoContact = new DAOContact(connection);
            daoConversation = new DAOConversation(connection);
            daoMessage = new DAOMessage(connection);
            daoParticipant = new DAOParticipant(connection);
            loginResponse.setAccepted(true);
            loginResponse.setUser(users.elementAt(0));
            loginResponse.setContacts(daoContact.selectAll());
            Vector<Participant> participants = daoParticipant.selectbyNumbers(users.elementAt(0).getUser_id(), 20);
            for(Participant participant: participants){
                loginResponse.setConversations(daoConversation.selectbyID(participant.getConversation_id()));
                loginResponse.getMessages().put(participant.getConversation_id(), daoMessage.selectbyNumbers(participant.getConversation_id(), 20));
            }
        }
        return loginResponse;
    }
}
