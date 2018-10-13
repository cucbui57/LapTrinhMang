import Control.LoginController;
import Control.WindowPoolManager;
import Control.utils.IOUtils;
import Model.Login.LoginResponse;
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
                        Thread.sleep(1000);
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
            }
        }
    }

    public static void main(String[] args) {
//        IOUtils.createIOSocket();
        MessengerClient mes = new MessengerClient();
    }
}
