
import javax.swing.*;
import java.net.*; // Sockets
import java.io.*; // Streams
import java.util.HashMap;

public class AppServer extends JFrame implements Runnable {

    //private Random random = new Random();
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
            //new Thread(this).start();        //launch a wait for client

            //open in/out
            DataInputStream input = new DataInputStream(socket.getInputStream());
            DataOutputStream output = new DataOutputStream(socket.getOutputStream());

            ihmServer.addMessage(input.readUTF() + " is connected!\n");  // display which user is connected
            ihmServer.addMessage(input.readUTF() + " is the level!\n");

            //stock in 2 collection
            HashMap<String, Integer> inputToServer = new HashMap<String, Integer>();
            HashMap<String, Integer> outputFromServer = new HashMap<String, Integer>();


            //infinite loop of waiting clients' messages
//            while(true){
//                String clientInput = input.readUTF();
//                System.out.println("Client sent "+clientInput+" to the server!");
//            }



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
