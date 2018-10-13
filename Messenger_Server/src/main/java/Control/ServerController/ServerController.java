package Control.ServerController;

import Control.DAO.*;
import Control.utils.IOUtils;
import Model.Login.LoginRequest;
import Model.Login.LoginResponse;
import Model.Participant;
import Model.User;

import java.net.Socket;
import java.util.Vector;

public class ServerController {
    LoginRequest loginRequest;
    DAOUser daoUser;
    DAOParticipant daoParticipant;
    DAOContact daoContact;
    DAOConversation daoConversation;
    DAOMessage daoMessage;
    Socket client;

}
