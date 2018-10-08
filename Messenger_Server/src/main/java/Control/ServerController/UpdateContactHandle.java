package Control.ServerController;

import Control.DAO.DAOContact;
import Control.utils.SQLServerConnUtils_SQLJDBC;
import Model.Contact;
import Model.SuccessRespone;

import java.sql.Connection;
import java.sql.SQLException;

public class UpdateContactHandle extends Handle<SuccessRespone> {
    DAOContact daoContact;
    Contact contact;
    public UpdateContactHandle(Contact contact) {
        this.contact = contact;
        try{
            Connection connection = SQLServerConnUtils_SQLJDBC.getSQLServerConnection();
            daoContact = new DAOContact(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    SuccessRespone execute() {
        SuccessRespone successRespone = new SuccessRespone(false);
        if(daoContact.update(contact) != 0){
            successRespone.setSuccess(true);
        }
        return successRespone;
    }
}