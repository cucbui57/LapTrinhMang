package Control.ServerController;

import Control.DAO.*;
import Control.utils.SQLServerConnUtils_SQLJDBC;
import Model.Login.LoginRequest;
import Model.Login.LoginResponse;
import Model.Participant;
import Model.Register.RegisterRequest;
import Model.Register.RegisterResponse;
import Model.User;

import java.sql.SQLException;
import java.util.Vector;

public class RegisterHandle extends Handle<RegisterResponse> {
    RegisterRequest registerRequest;
    DAOUser daoUser;
    DAOParticipant daoParticipant;
    DAOContact daoContact;
    DAOConversation daoConversation;
    DAOMessage daoMessage;
    public RegisterHandle(RegisterRequest registerRequest) {
        this.registerRequest = registerRequest;
        try {
            this.connection = SQLServerConnUtils_SQLJDBC.getSQLServerConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    RegisterResponse execute() {
        RegisterResponse registerResponse = new RegisterResponse();
        if(!daoUser.isExistUsername(registerRequest.getUser().getUsername()) && !daoUser.isExistEmail(registerRequest.getUser().getEmail())){
            if(daoUser.insert(registerRequest.getUser()) != 0){
                registerResponse.setSuccess(true);
                registerResponse.setUser(daoUser.selectbyUsername(registerRequest.getUser().getUsername()).elementAt(0));
            }
        }
        return registerResponse;
    }
}
