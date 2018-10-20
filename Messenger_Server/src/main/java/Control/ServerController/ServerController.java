package Control.ServerController;

import Control.DAO.*;
import Control.utils.IOUtils;
import Control.utils.SQLServerConnUtils_SQLJDBC;
import Model.*;
import Model.CreateConversation.AcceptNewConversationClientSend2Server;
import Model.CreateConversation.AcceptNewConversationServerSend2Clients;
import Model.CreateConversation.CreateConversationRequest;
import Model.CreateConversation.CreateConversationServerSend2Clients;
import Model.SearchUser.SearchUserRequest;
import Model.SearchUser.SearchUserResponse;
import Model.Login.LoginRequest;
import Model.Login.LoginResponse;
import Model.Register.RegisterRequest;
import Model.Register.RegisterResponse;

import java.net.Socket;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Vector;

public class ServerController {
    private static DAOUser daoUser;
    private static DAOParticipant daoParticipant;
    private static DAOContact daoContact;
    private static DAOConversation daoConversation;
    private static DAOMessage daoMessage;

    public ServerController() {
        Connection connection = SQLServerConnUtils_SQLJDBC.getSQLServerConnection();
        this.daoMessage = new DAOMessage(connection);
        this.daoUser = new DAOUser(connection);
        this.daoContact = new DAOContact(connection);
        this.daoConversation = new DAOConversation(connection);
        this.daoParticipant = new DAOParticipant(connection);
    }

    // TODO: Login
    public static void LoginController(Socket client, LoginRequest loginRequest){
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setAccepted(false);
        Vector<User> users = daoUser.selectbyUsername(loginRequest.getUsername());
        if(!users.isEmpty()) {
            loginResponse.setAccepted(true);
            loginResponse.setUser(users.elementAt(0));
//            loginResponse.setContacts(daoContact.selectAll());
//            Vector<Participant> participants = daoParticipant.selectbyNumbers(users.elementAt(0).getUser_id(), 20);
//            for(Participant participant: participants){
//                loginResponse.setConversations(daoConversation.selectbyID(participant.getConversation_id()));
//                loginResponse.getMessages().put(participant.getConversation_id(), daoMessage.selectbyNumbers(participant.getConversation_id(), 20));
//            }
            IOUtils.addSocket(loginResponse.getUser().getUser_id(), client);
        }
        IOUtils.writeObject(client, loginResponse);
    }

    // TODO: Register
    public static void RegisterController(Socket client, RegisterRequest registerRequest){
        RegisterResponse registerResponse = new RegisterResponse();
        if(!daoUser.isExistUsername(registerRequest.getUser().getUsername()) && !daoUser.isExistEmail(registerRequest.getUser().getEmail())){
            if(daoUser.insert(registerRequest.getUser()) != 0){
                registerResponse.setSuccess(true);
                registerResponse.setUser(daoUser.selectbyUsername(registerRequest.getUser().getUsername()).elementAt(0));
            }
        }
        IOUtils.writeObject(client, registerResponse);
        IOUtils.closeSocket(client);
    }

    // TODO: Seen Message
    public static void SeenMessageController(Socket client, Participant participant, HashMap<Integer, Vector<Socket>> sockets){
        SuccessRespone successRespone = new SuccessRespone(false);
        if(daoParticipant.update(participant) != 0){
            successRespone.setSuccess(true);
            Vector<Participant> participants = daoParticipant.selectbyID(participant.getConversation_id());
            for(Participant par: participants){
                for(Socket socket: sockets.get(par.getUser_id())){
                    IOUtils.writeObject(socket, par);
                }
            }
        }
        IOUtils.writeObject(client, successRespone);
    }

    // TODO: Send Message
    public static void SendMessageController(Socket client, Message message, HashMap<Integer, Vector<Socket>> sockets){
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
        IOUtils.writeObject(client, successRespone);
    }

    // TODO: Update Contact
    public static void UpdateContactController(Socket client, Contact contact){
        SuccessRespone successRespone = new SuccessRespone(false);
        if(daoContact.update(contact) != 0){
            successRespone.setSuccess(true);
        }
        IOUtils.writeObject(client, successRespone);
    }

    // TODO: Search User
    public static void SearchUserController(Socket client, SearchUserRequest searchUserRequest){
        Vector<User> users = daoUser.selectbyName(searchUserRequest.getName());
        IOUtils.writeObject(client, new SearchUserResponse(users));
    }

    // TODO: Create Conversation
    public static void CreateConversationController(Socket client, CreateConversationRequest createConversationRequest, HashMap<Integer, Vector<Socket>> sockets){
        CreateConversationServerSend2Clients createConversationServerSend2Clients = new CreateConversationServerSend2Clients();
        createConversationServerSend2Clients.setConversation(createConversationRequest.getConversation());
        int res = daoConversation.insert(createConversationRequest.getConversation());
//        if(res == 0) return new CreateConversationResponse(false);
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
//            if(res == 0) return new CreateConversationResponse(false);
            createConversationServerSend2Clients.getParticipants().add(participant);
            for(Socket socket: sockets.get(participant.getUser_id())){
                IOUtils.writeObject(socket, createConversationServerSend2Clients);
            }
        }
//        return new CreateConversationResponse(true);
    }

    // TODO: Accept Conversation
    public static void AcceptConversationController(Socket client, AcceptNewConversationClientSend2Server acceptNewConversationClientSend2Server, HashMap<Integer, Vector<Socket>> sockets){
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
