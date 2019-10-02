
import javax.swing.*;
import java.net.*; // Sockets
import java.io.*; // Streams
import java.util.ArrayList;
import java.util.List;

public class AppServer extends JFrame implements Runnable {

    private IhmServer ihmServer;
    private ServerSocket serverSocket;
    //stock in 1 collection
    List<DataOutputStream> list = new ArrayList<>();

    private MineField mineField;



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
        mineField=new MineField("NORMAL");
        mineField.showText();
        mineField.showTextWithMinesNum();
    }

    public void run() {
        try {
            Socket socket = serverSocket.accept();  // wait new client
            ihmServer.addMessage("New client: ");
            new Thread(this).start(); //launch a wait for client

            //open in/out
            DataInputStream input = new DataInputStream(socket.getInputStream());
            DataOutputStream output = new DataOutputStream(socket.getOutputStream());

            ihmServer.addMessage(input.readUTF() + " is connected!\n");  // display which user is connected
            ihmServer.addMessage(input.readUTF() + " is the level!\n");

            list.add(output);

            //infinite loop of waiting clients' cmd and contents
            while (true) {
                int cmd = input.readInt();
                if (cmd == 0) {
                    String message = input.readUTF();
                    String name = input.readUTF();
                    String time = input.readUTF();
                    for (DataOutputStream client : list) {
                        client.writeInt(0);
                        client.writeUTF(message);
                        client.writeUTF(name);
                        client.writeUTF(time);
                    }
                    ihmServer.addMessage(time + " " + name + ":" + message + "\n");
                }
                if(cmd==1){

                }
                if(cmd==2){

                }
                if(cmd==3){

                }
            }


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
