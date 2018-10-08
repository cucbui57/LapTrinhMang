import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.*;
import java.util.*;

public class MessengerServer {
    String URL = "jdbc:sqlserver://localhost:1433;databaseName=Messenger;user=underwear;password=mE39Dola4o";
    private ServerSocket serverSocket;
    HashMap<Integer, Vector<Socket>> hashMapSocket;
    HashMap<Integer, Vector<Object>> hashMapResponse;
    private int _conversation_id;
    private int _user_id;

    MessengerServer() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(URL);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select user_id from users");
            _user_id = resultSet.getFetchSize() + 1;
            resultSet = statement.executeQuery("select conversation_id from conversations");
            _conversation_id = resultSet.getFetchSize() + 1;
            serverSocket = new ServerSocket(9999);
            hashMapSocket = new HashMap<Integer, Vector<Socket>>();
            hashMapResponse = new HashMap<Integer, Vector<Object>>();
            Listening();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null && connection.isClosed() == false) {
                    connection.close();
                }
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
    }

    void Listening() {
        while (true) {
            try {
                Socket client = serverSocket.accept();
                ObjectInputStream objectInputStream = new ObjectInputStream(client.getInputStream());
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(client.getOutputStream());
                Object object = objectInputStream.readObject();
                Integer user_id;
                // check port vs inetadd trung nhau thi k cho no vao sockets
//                Vector<Socket> sockets = hashMapSocket.get(user_id);
//                Iterator it = sockets.iterator();
//                while(it.hasNext()){
//                    Socket socket = (Socket)it.next();
//                    if(socket.getPort() == client.getPort() && socket.getInetAddress() == client.getInetAddress()){
//                        it.remove();
//                        break;
//                    }
//                }
//                sockets.add(client);

//                if (object instanceof RegisterRequest) {
//                    objectOutputStream.writeObject(RegisterHandle((RegisterRequest) object));
//                } else if (object instanceof LoginRequest) {
//                    objectOutputStream.writeObject(LoginHandle((LoginRequest) object));
//                } else if (object instanceof CheckEmailRequest) {
//                    objectOutputStream.writeObject(new CheckEmailResponse(CheckEmailHandle((CheckEmailRequest) object)));
//                } else if (object instanceof CheckUsernameRequest) {
//                    objectOutputStream.writeObject(new CheckEmailResponse(CheckUsernameHandle((CheckUsernameRequest) object)));
//                } else if (object instanceof CreateConversationRequest) {
//                    objectOutputStream.writeObject(new CreateConversationResponse(NewConversationHandle((CreateConversationRequest)object)));
//                } else if(object instanceof AcceptConversationRequest){
//                    objectOutputStream.writeObject(AcceptConversationHandle((AcceptConversationRequest)object));
//                } else if(object instanceof Message){
//
//                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

//    // TODO: check username
//    boolean CheckUsernameHandle(CheckUsernameRequest checkUsernameRequest) { // kiem tra user name có chưa
//        Connection connection = null;
//        boolean exist = false;
//        try {
//            connection = DriverManager.getConnection(URL);
//            PreparedStatement preparedStatement = connection.prepareStatement("call CheckUsername ?");
//            preparedStatement.setString(1, checkUsernameRequest.getUsername());
//            ResultSet resultSet = preparedStatement.executeQuery();
//            if (resultSet.next()) {
//                exist = true;
//            }
//        } catch (SQLException e) {
//            exist = true;
//            e.printStackTrace();
//        } finally {
//            try {
//                if (connection != null && connection.isClosed() == false) {
//                    connection.close();
//                }
//            } catch (SQLException e1) {
//                e1.printStackTrace();
//            }
//        }
//        return exist;
//    }
//
//    // TODO: check email
//    boolean CheckEmailHandle(CheckEmailRequest checkEmailRequest) { // kiem tra user name có chưa
//        Connection connection = null;
//        boolean exist = false;
//        try {
//            connection = DriverManager.getConnection(URL);
//            PreparedStatement preparedStatement = connection.prepareStatement("call CheckEmail ?");
//            preparedStatement.setString(1, checkEmailRequest.getEmail());
//            ResultSet resultSet = preparedStatement.executeQuery();
//            if (resultSet.next()) {
//                exist = true;
//            }
//        } catch (SQLException e) {
//            exist = true;
//            e.printStackTrace();
//        } finally {
//            try {
//                if (connection != null && connection.isClosed() == false) {
//                    connection.close();
//                }
//            } catch (SQLException e1) {
//                e1.printStackTrace();
//            }
//        }
//        return exist;
//    }
//
//    // TODO: register
//    RegisterResponse RegisterHandle(RegisterRequest registerRequest) { // xử lý đăng kí tài khoản
//        boolean success = false;
//        Connection connection = null;
//        try {
//            connection = DriverManager.getConnection(URL);
//            Statement statement = connection.createStatement();
//
//            String string = "select * from users;";
//            ResultSet resultSet = statement.executeQuery(string);
//            int index = resultSet.getFetchSize();
//            PreparedStatement preparedStatement = connection.prepareStatement("call newuser ? ? ? ? ? ? ? ? ?");
//            preparedStatement.setInt(1, index);
//            preparedStatement.setString(2, registerRequest.getUsername());
//            preparedStatement.setString(3, registerRequest.getPassword());
//            preparedStatement.setString(4, registerRequest.getEmail());
//            preparedStatement.setString(5, registerRequest.getPhone_number());
//            preparedStatement.setString(6, registerRequest.getFirst_name());
//            preparedStatement.setString(7, registerRequest.getLast_name());
//            preparedStatement.setBoolean(8, registerRequest.isSex());
//            preparedStatement.setDate(9, registerRequest.getBirthday());
//            preparedStatement.executeUpdate();
//            success = true;
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                if (connection != null && connection.isClosed() == false) {
//                    connection.close();
//                }
//            } catch (SQLException e1) {
//                e1.printStackTrace();
//            }
//        }
//        return new RegisterResponse(success);
//    }
//
//    // TODO: check login
//    boolean checkLogin(LoginRequest loginRequest) { // kiểm tra login
//        Connection connection = null;
//        boolean correct = false;
//        try {
//            connection = DriverManager.getConnection(URL);
//            PreparedStatement preparedStatement = connection.prepareStatement("call CheckLogin ? ? ?");
//            preparedStatement.setString(1, loginRequest.getUsername());
//            preparedStatement.setString(2, loginRequest.getEmail());
//            preparedStatement.setString(3, loginRequest.getPassword());
//            ResultSet resultSet = preparedStatement.executeQuery();
//            if (resultSet.next()) {
//                correct = true;
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                if (connection != null && connection.isClosed() == false) {
//                    connection.close();
//                }
//            } catch (SQLException e1) {
//                e1.printStackTrace();
//            }
//        }
//        return correct;
//    }
//
//    // TODO: LOGIN HANDLE
//    LoginResponse LoginHandle(LoginRequest loginRequest) {
//        return new LoginResponse(checkLogin(loginRequest));
//    }
//
//    // TODO: NEW CONVERSATION HANDLE
//    boolean NewConversationHandle(CreateConversationRequest createConversationRequest) {
//        boolean success = false;
//        Connection connection = null;
//        Conversation conversation;
//        ObjectOutputStream objectOutputStream = null;
//        Vector<Participant> participants = createConversationRequest.getParticipants();
//        try {
//            connection = DriverManager.getConnection(URL);
//            connection.setAutoCommit(false);
//            PreparedStatement preparedStatement;
//            try {
//                String conversation_name = "";
//                Iterator iterator = participants.iterator();
//                Participant part = (Participant) iterator.next();
//                conversation_name += part.getNickname();
//                while (!iterator.hasNext()) {
//                    part = (Participant) iterator.next();
//                    conversation_name += ", " + part.getNickname();
//                }
//                preparedStatement = connection.prepareStatement("call NewConversation ? ?");
//                preparedStatement.setInt(1, _conversation_id);
//                preparedStatement.setString(2, conversation_name);
//                preparedStatement.executeUpdate();
//                preparedStatement = connection.prepareStatement("call AddParticipant ? ?");
//                for (Participant participant : createConversationRequest.getParticipants()) {
//                    preparedStatement.setInt(1, participant.getUser_id());
//                    preparedStatement.setString(2, participant.getNickname());
//                    preparedStatement.executeUpdate();
//                }
//                preparedStatement = connection.prepareStatement("select * from Conversations where conversation_id = " + _conversation_id);
//                ResultSet resultSet = preparedStatement.executeQuery();
//                resultSet.next();
//                conversation = new Conversation(resultSet.getInt(1), resultSet.getString(2));
//                for (Participant participant : createConversationRequest.getParticipants()) {
//                    Vector<Socket> sockets = hashMapSocket.get(participant.getUser_id());
//                    for (Socket client : sockets) {
//                        objectOutputStream = new ObjectOutputStream(client.getOutputStream());
//                        objectOutputStream.writeObject(new AddparticipantResponse(conversation, participants));
//                        objectOutputStream.close();
//                    }
//                }
//                connection.commit();
//                success = true;
//            } catch (SQLException e) {
//                connection.rollback();
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            connection.setAutoCommit(true);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                if (connection != null && connection.isClosed() == false) {
//                    connection.close();
//                }
//            } catch (SQLException e1) {
//                e1.printStackTrace();
//            }
//        }
//        return success;
//    }
//    // TODO: ACCEPT CONVERSATION HANDLE
//    ConversationUpdate AcceptConversationHandle(AcceptConversationRequest acceptConversationRequest){
//        Participant participant = acceptConversationRequest.getParticipant();
//        Conversation conversation = acceptConversationRequest.getConversation();
//        Vector<Integer> participants_id = acceptConversationRequest.getParticipants_id();
//        Connection connection = null;
//        ObjectOutputStream objectOutputStream = null;
//        ConversationUpdate conversationUpdate = null;
//        try {
//            connection = DriverManager.getConnection(URL);
//            connection.setAutoCommit(false);
//            PreparedStatement preparedStatement;
//            ResultSet resultSet;
//            try{
//                if(participant.isAccepted()){
//                    preparedStatement = connection.prepareStatement("call AcceptConversation ? ? ?");
//                    preparedStatement.setInt(1, participant.getUser_id());
//                    preparedStatement.setInt(2, conversation.getConversation_id());
//                    preparedStatement.setBoolean(3, participant.isAccepted());
//
//                }
//                else {
//                    preparedStatement = connection.prepareStatement("call DeleteParticipant ? ?");
//                    preparedStatement.setInt(1, participant.getUser_id());
//                    preparedStatement.setInt(2, conversation.getConversation_id());
//                }
//                preparedStatement.executeUpdate();
//                preparedStatement = connection.prepareStatement("call GetParticipantInConversation ?");
//                preparedStatement.setInt(1, conversation.getConversation_id());
//                resultSet = preparedStatement.executeQuery();
//                Vector<Participant> participants = new Vector<Participant>();
//                while(resultSet.next()){
//                    participants.add(new Participant(resultSet.getInt(1),
//                            resultSet.getInt(2),
//                            resultSet.getBoolean(3),
//                            resultSet.getString(4),
//                            resultSet.getInt(5),
//                            resultSet.getString(6)));
//                }
//                Vector<Message> messages = new Vector<Message>();
//                preparedStatement = connection.prepareStatement("call GetMessageInConversation ?");
//                preparedStatement.setInt(1, conversation.getConversation_id());
//                resultSet = preparedStatement.executeQuery();
//                while(resultSet.next()){
//                    messages.add(new Message(resultSet.getInt(1),
//                            resultSet.getInt(2),
//                            resultSet.getInt(3),
//                            resultSet.getTimestamp(4),
//                            resultSet.getString(5),
//                            resultSet.getString(6)));
//                }
//                Vector<AttachFile> attachFiles = new Vector<AttachFile>();
//                preparedStatement = connection.prepareStatement("call GetFileInConversation ?");
//                for(Message message: messages){
//                    if(message.getType() != "text"){
//                        preparedStatement.setInt(1, message.getMessage_id());
//                        resultSet = preparedStatement.executeQuery();
//                        resultSet.next();
//                        attachFiles.add(new AttachFile(resultSet.getInt(1), resultSet.getInt(2), resultSet.getString(3)));
//                    }
//                }
//                connection.commit();
//                connection.setAutoCommit(true);
//                conversationUpdate = new ConversationUpdate(conversation, participants, messages, attachFiles);
//            }catch (Exception e){
//                connection.rollback();
//                e.printStackTrace();
//            }
//            for (Integer integer : participants_id) {
//                Vector<Socket> sockets = hashMapSocket.get(integer);
//                for (Socket client : sockets) {
//                    objectOutputStream = new ObjectOutputStream(client.getOutputStream());
//                    objectOutputStream.writeObject(new AcceptConversationResponse(participant, conversation));
//                    objectOutputStream.close();
//                }
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                if (connection != null && connection.isClosed() == false) {
//                    connection.close();
//                }
//            } catch (SQLException e1) {
//                e1.printStackTrace();
//            }
//        }
//        return conversationUpdate;
//    }
//    // TODO: SEND MESSAGE HANDLE
//    boolean SendMessageHandle(Message message){
//        boolean success = false;
//        Participant participant = acceptConversationRequest.getParticipant();
//        Conversation conversation = acceptConversationRequest.getConversation();
//        Vector<Integer> participants_id = acceptConversationRequest.getParticipants_id();
//        Connection connection = null;
//        ObjectOutputStream objectOutputStream = null;
//        try {
//            connection = DriverManager.getConnection(URL);
//            connection.setAutoCommit(false);
//            PreparedStatement preparedStatement;
//            ResultSet resultSet;
//            try{
//                if(participant.isAccepted()){
//                    preparedStatement = connection.prepareStatement("call AcceptConversation ? ? ?");
//                    preparedStatement.setInt(1, participant.getUser_id());
//                    preparedStatement.setInt(2, conversation.getConversation_id());
//                    preparedStatement.setBoolean(3, participant.isAccepted());
//
//                }
//                else {
//                    preparedStatement = connection.prepareStatement("call DeleteParticipant ? ?");
//                    preparedStatement.setInt(1, participant.getUser_id());
//                    preparedStatement.setInt(2, conversation.getConversation_id());
//                }
//                preparedStatement.executeUpdate();
//                preparedStatement = connection.prepareStatement("call GetParticipantInConversation ?");
//                preparedStatement.setInt(1, conversation.getConversation_id());
//                resultSet = preparedStatement.executeQuery();
//                Vector<Participant> participants = new Vector<Participant>();
//                while(resultSet.next()){
//                    participants.add(new Participant(resultSet.getInt(1),
//                            resultSet.getInt(2),
//                            resultSet.getBoolean(3),
//                            resultSet.getString(4),
//                            resultSet.getInt(5),
//                            resultSet.getString(6)));
//                }
//                Vector<Message> messages = new Vector<Message>();
//                preparedStatement = connection.prepareStatement("call GetMessageInConversation ?");
//                preparedStatement.setInt(1, conversation.getConversation_id());
//                resultSet = preparedStatement.executeQuery();
//                while(resultSet.next()){
//                    messages.add(new Message(resultSet.getInt(1),
//                            resultSet.getInt(2),
//                            resultSet.getInt(3),
//                            resultSet.getTimestamp(4),
//                            resultSet.getString(5),
//                            resultSet.getString(6)));
//                }
//                Vector<AttachFile> attachFiles = new Vector<AttachFile>();
//                preparedStatement = connection.prepareStatement("call GetFileInConversation ?");
//                for(Message message: messages){
//                    if(message.getType() != "text"){
//                        preparedStatement.setInt(1, message.getMessage_id());
//                        resultSet = preparedStatement.executeQuery();
//                        resultSet.next();
//                        attachFiles.add(new AttachFile(resultSet.getInt(1), resultSet.getInt(2), resultSet.getString(3)));
//                    }
//                }
//                connection.commit();
//                conversationUpdate = new ConversationUpdate(conversation, participants, messages, attachFiles);
//            }catch (Exception e){
//                connection.rollback();
//                e.printStackTrace();
//            }
//            for (Integer integer : participants_id) {
//                Vector<Socket> sockets = hashMapSocket.get(integer);
//                for (Socket client : sockets) {
//                    objectOutputStream = new ObjectOutputStream(client.getOutputStream());
//                    objectOutputStream.writeObject(new AcceptConversationResponse(participant, conversation));
//                    objectOutputStream.close();
//                }
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                if (connection != null && connection.isClosed() == false) {
//                    connection.close();
//                }
//            } catch (SQLException e1) {
//                e1.printStackTrace();
//            }
//        }
//        return true;
//    }
//    void AddContactRequest(Object object) throws IOException {
//        MAddContact mAddContact = (MAddContact)object;
//        if(hashMapSocket.containsKey(mAddContact.getContact_id())){
//            Vector<Socket> sockets = hashMapSocket.get(mAddContact.getContact_id());
//            Iterator it = sockets.iterator();
//            while(it.hasNext()){
//                Socket client = (Socket)it.next();
//                ObjectOutputStream objectOutputStream = new ObjectOutputStream(client.getOutputStream());
//                objectOutputStream.writeObject(object);
//                objectOutputStream.close();
//            }
//        }
//    }


    //    boolean checkBlockID(int user_id1, int user_id2){
//        Connection connection = null;
//        try {
//            connection = DriverManager.getConnection(URL);
//            PreparedStatement preparedStatement = connection.prepareStatement("call CheckBlockID ? ?");
//            preparedStatement.setInt(1, user_id1);
//            preparedStatement.setInt(2, user_id2);
//            ResultSet resultSet = preparedStatement.executeQuery();
//            if(resultSet.next()){
//                return true;
//            }
//            connection.close();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return false;
//    }
}
