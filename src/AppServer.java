
import javax.swing.*;
import java.net.*; // Sockets
import java.io.*; // Streams
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class AppServer extends JFrame implements Runnable {

    private IhmServer ihmServer;
    private ServerSocket serverSocket;
    //stock clients in 1 collection
    List<DataOutputStream> list = new ArrayList<>();
    private MineField mineField;
    private boolean[][] isClicked;
    private int countPlayers = 0;

    public void setStarted(boolean started) {
        isStarted = started;
    }

    private boolean isStarted = false;

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
        setStarted(true);

        isClicked = new boolean[mineField.getDimension()][mineField.getDimension()];
        for (int i = 0; i < mineField.getDimension(); i++) {
            for (int j = 0; j < mineField.getDimension(); j++) {
                isClicked[i][j] = false;
            }
        }


        //colors for users, 6 players maximum
        LinkedList<String> color = new LinkedList<>();
        color.push("RED");
        color.push("GREEN");
        color.push("ORANGE");
        color.push("YELLOW");
        color.push("MAGENTA");
        color.push("PINK");


        for (DataOutputStream client : list) {  //already have name list, which is collected when connecting
            try {
                client.writeInt(2);  //send cmd start
                for (int i = 0; i < mineField.getDimension(); i++) {
                    for (int j = 0; j < mineField.getDimension(); j++) {
                        client.writeBoolean(mineField.getMineField(i, j));  //send all values of minefield to all clients
                    }
                }
                client.writeUTF(color.pop()); //send color

            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        ihmServer.addMessage("Game start!\n");

        countPlayers = list.size();      //count total players online, decrease when someone loses

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
                ihmServer.addMessage("cmd=" + cmd + ", ");
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

                if (isStarted) {  //only when game is started, then the clients can send position
                    if (cmd == 1) { //pos
                        int x = input.readInt();
                        int y = input.readInt();
                        String name = input.readUTF();
                        String color = input.readUTF();

                        if (!isClicked[x][y]) {
                            for (DataOutputStream client : list) {
                                client.writeInt(1);
                                client.writeInt(x);
                                client.writeInt(y);
                                client.writeUTF(name);
                                client.writeUTF(color);
                            }
                            ihmServer.addMessage(name + " clicked (" + x + "," + y + ")\n");
                            isClicked[x][y] = true;
                        } else {
                            ihmServer.addMessage(name + " clicked (" + x + "," + y + "), which is already clicked\n");
                        }
                    }

                    if (cmd == 3) {  //end
                        String name = input.readUTF();
                        int x=input.readInt();
                        int y=input.readInt();
                        countPlayers--;
                        if (countPlayers > 0) {         //continue game, there is still at least 1 player remains
                            ihmServer.addMessage(name + " loses, " + countPlayers + " players remain!\n");
                            for (DataOutputStream client : list) {
                                client.writeInt(3);
                                client.writeBoolean(false);
                                client.writeUTF(name);

                                client.writeInt(x);
                                client.writeInt(y);
                                client.writeInt(countPlayers);
                            }
                        } else {
                            ihmServer.addMessage("Game over!\n");  //end game, no player remains
                            for (DataOutputStream client : list) {
                                client.writeInt(3);
                                client.writeBoolean(true);
                                client.writeUTF(name);
                                client.writeInt(x);
                                client.writeInt(y);
                            }
                        }

                    }
                }

                if (cmd == 2) {  //start

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


//add a 2-D boolean of isCliked of each case
//