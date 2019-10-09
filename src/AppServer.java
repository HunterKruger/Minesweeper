
import javax.swing.*;
import java.net.*; // Sockets
import java.io.*; // Streams
import java.util.ArrayList;
import java.util.List;

public class AppServer extends JFrame implements Runnable {

    private IhmServer ihmServer;
    private ServerSocket serverSocket;
    //stock clients in 1 collection
    List<DataOutputStream> list = new ArrayList<>();
    private MineField mineField;
    private boolean isStarted=false;

    //constructor of server
    public AppServer() {
        ihmServer = new IhmServer(this);
        setContentPane(ihmServer);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setVisible(true);
        startServer();
    }

    //start the server
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

    //init a minefield in the server, level normal by default
    void startGame() {
        mineField = new MineField("NORMAL");
        mineField.showText();
        mineField.showTextWithMinesNum();
        isStarted=true;
        ihmServer.addMessage("Game start!");
        for (DataOutputStream client : list) {
            try {
                client.writeInt(2);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public void run() {
        try {
            Socket socket = serverSocket.accept();  // wait new client
            ihmServer.addMessage("New client: ");
            new Thread(this).start(); //launch a wait for client

            //open in and out
            DataInputStream input = new DataInputStream(socket.getInputStream());
            DataOutputStream output = new DataOutputStream(socket.getOutputStream());

            ihmServer.addMessage(input.readUTF() + " is connected!\n");  // display which user is connected
            ihmServer.addMessage("Level " + input.readUTF() + "!\n");

            list.add(output);

            //how many gamers?
            ihmServer.addMessage(list.size() + " gamer(s) online!\n");

            //infinite loop of waiting clients' cmd and contents
            while (true) {
                int cmd = input.readInt();
                if (cmd == 0) { //msg
                    //read message
                    String message = input.readUTF();
                    String name = input.readUTF();
                    String time = input.readUTF();
                    for (DataOutputStream client : list) {  //send message to every client
                        client.writeInt(0);
                        client.writeUTF(message);
                        client.writeUTF(name);
                        client.writeUTF(time);
                    }
                    ihmServer.addMessage(time + " " + name + ":" + message + "\n");
                }

                if(isStarted){  //only when game is started, then the clients can send messages
                    if (cmd == 1) { //pos
                        int x = input.readInt();
                        int y = input.readInt();
                        String name = input.readUTF();
                        int minesAround = mineField.calculateMinesAround(x, y);
                        boolean isMine = mineField.isMine(x, y);
                        for (DataOutputStream client : list) {
                            client.writeInt(1);
                            client.writeInt(x);
                            client.writeInt(y);
                            client.writeUTF(name);
                            client.writeInt(minesAround);
                            client.writeBoolean(isMine);
                        }
                        ihmServer.addMessage(name + " clicked (" + x + "," + y + "), it is a mine "
                                + isMine + ", " +
                                minesAround
                                + " mines around \n");
                    }
                }

                if (cmd == 2) {  //start

                }
                if (cmd == 3) {  //end

                }
            }

        } catch (IOException e) {
            ihmServer.addMessage("Exception in server run: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new AppServer();
    }
}
