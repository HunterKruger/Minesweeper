import javax.swing.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class AppChat extends JFrame implements Runnable {

    public IhmChat getIhmChat() {
        return ihmChat;
    }

    private IhmChat ihmChat;
    private ServerSocket serverSocket;
    private AppMinesweeper appMinesweeper;
    public static final String MESSAGE = "message";


    public AppChat() {
        ihmChat = new IhmChat(this);
        setContentPane(ihmChat);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setVisible(true);
       // startChat();
    }

    void startChat(AppMinesweeper appMinesweeper){
        //Socket socket=new Socket()
    }


    public void quit() {
        int response = JOptionPane.showConfirmDialog(null,
                "Are you sure to quit the chatbox?", "Bye-Bye",
                JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE);
        if (response == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    @Override
    public void run() {

    }


    public void sendMessage(String text) {
        ihmChat.addMessage(new Date().toString()+"\n"+text+"\n");
    }

    public static void main(String[] args) {
        new AppChat();
    }
}
