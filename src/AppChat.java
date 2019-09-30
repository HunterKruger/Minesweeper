import javax.swing.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.util.Queue;

public class AppChat extends JFrame{

    public IhmChat getIhmChat() {
        return ihmChat;
    }

    private IhmChat ihmChat;
    private ServerSocket serverSocket;
    public static final String MESSAGE = "message";
    private DataInputStream inClient;
    private DataOutputStream outClient;
    private String messageFromIhm;


    public AppChat() {
        ihmChat = new IhmChat(this);
        setContentPane(ihmChat);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setVisible(true);
    }



    public void quit() {
        int response = JOptionPane.showConfirmDialog(null,
                "Are you sure to quit the chatbox?", "Bye-Bye",
                JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE);
        if (response == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }



   public String getMessageFromChat(){
        return messageFromIhm;
   }

    public void saveMessageFromIhm(String messageToSend) {
        messageFromIhm = messageToSend;
        System.out.println(messageFromIhm);
        //ihmChat.addMessage(new Date().toString()+" says:\n"+text+"\n");
    }

//    public static void main(String[] args) {
//        new AppChat();
//    }
}
