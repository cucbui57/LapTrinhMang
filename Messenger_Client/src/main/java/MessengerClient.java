import Control.LoginController;
import Control.WindowPoolManager;
import Control.utils.IOUtils;
import Model.CreateConversation.AcceptNewConversationServerSend2Clients;
import Model.CreateConversation.CreateConversationRequest;
import Model.CreateConversation.CreateConversationServerSend2Clients;
import Model.Login.LoginResponse;
import Model.Message;
import Model.Participant;
import Model.Register.RegisterResponse;
import Model.SuccessRespone;
import View.Window;

public class MessengerClient {
    public MessengerClient(){
        Window window = new Window();
        window.setjPanel(WindowPoolManager.getLoginViewSingleton());
        LoginController loginController = new LoginController(WindowPoolManager.getLoginViewSingleton());
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    Listenner();
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();
    }

    public void Listenner(){
        Object object = IOUtils.readObject();
        if(object instanceof LoginResponse){
            System.out.println("zxxx");
            if(((LoginResponse) object).isAccepted() == false) {
                IOUtils.closeSocket();
                System.out.println("login fail");
            }
            else {
                System.out.println("login success");
            }
        } else if(object instanceof RegisterResponse){
            RegisterResponse registerResponse = (RegisterResponse)object;
            if(registerResponse.isSuccess()){
                // yeu cau register tai khoan.

            } else {
                // thong bao loi khong dang ki duoc.
            }
        } else if(object instanceof Message){
            Message message = (Message)object;
            // tim trong danh sach cuoc tro chuyen hien co co id conversation khong. neu khong thi gui thong tin leen server de load them
        } else if(object instanceof Participant){
            Participant participant = (Participant)object;
            // tim trong danh sach tro chuyen hien co co id conversation khong, neu co thi them vao thong tin thang nay da doc tin nhan nay
        } else if(object instanceof SuccessRespone){
            SuccessRespone successRespone = (SuccessRespone)object;
            // if tung model xem no thuoc cai nao
        } else if(object instanceof CreateConversationServerSend2Clients){
            CreateConversationServerSend2Clients createConversationServerSend2Clients = (CreateConversationServerSend2Clients)object;
            // tao ra cuoc tro chuyen. lay danh sach user dua vao. bat accept hay khong
        } else if(object instanceof AcceptNewConversationServerSend2Clients){
            AcceptNewConversationServerSend2Clients acceptNewConversationServerSend2Clients = (AcceptNewConversationServerSend2Clients)object;
            // cap nhat lai cuoc tro chuyen co nguoi vua accept hay not accept
        }
    }

    public static void main(String[] args) {
        MessengerClient mes = new MessengerClient();
    }
}
