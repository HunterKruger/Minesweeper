import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ActionChat implements ActionListener {

    private AppChat appChat;
    private int type;

    public static final int SEND=1;
    public static final int QUIT=2;


    @Override
    public void actionPerformed(ActionEvent e) {
        if(type==SEND){
            appChat.sendMessage(appChat.getIhmChat().getMessageField().getText());
        }
        if(type==QUIT){
            appChat.quit();
        }
    }

    public ActionChat(AppChat appChat, int type) {
        this.appChat = appChat;
        this.type = type;
    }


}
