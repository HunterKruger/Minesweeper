
import javax.swing.*;
import java.net.*; // Sockets
import java.io.*; // Streams
import java.util.HashMap;

public class AppServer extends JFrame implements Runnable {

    private IhmServer ihmServer;
    private ServerSocket serverSocket;

    public AppServer() {
        ihmServer = new IhmServer(this);
        setContentPane(ihmServer);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setVisible(true);
        startServer();
    }

    void startServer() {
        ihmServer.addMessage("Wait for clients...\n");
        try {
            //launch the management of socket
            serverSocket = new ServerSocket(AppMinesweeper.PORT);
            //launch a thread for waiting client
            new Thread(this).start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void startGame() {

    }

    public void run() {
        try {
            Socket socket = serverSocket.accept();  //new client
            ihmServer.addMessage("New client: ");
            new Thread(this).start();        //launch a wait for client

            //open in/out
            DataInputStream inServer = new DataInputStream(socket.getInputStream());
            DataOutputStream outServer = new DataOutputStream(socket.getOutputStream());

            ihmServer.addMessage(inServer.readUTF() + " is connected!\n");  // display which user is connected


            //stock in 2 collection
            HashMap<String, Integer> inputHashMap = new HashMap<String, Integer>();
            //inputHashMap.put("",);


            //infinite loop of waiting clients' messages


            //re-dispatch others if necessary

        } catch (IOException e) {
            ihmServer.addMessage("Exception in server run: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new AppServer();
    }
}
