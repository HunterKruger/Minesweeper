
import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AppMinesweeper extends JFrame implements Runnable {

    //network info
    public static final int PORT = 10000;
    public static final String HOSTNAME = "localhost";
    public static final String PSEUDO = "pseudo";
    public static final String MESSAGE = "message";

    //commands
    public static final int MSG = 0;
    public static final int POS = 1;
    public static final int START = 2;
    public static final int END = 3;

    public String getColor() {
        return color;
    }

    private String color = "";
    private MineField mineField = new MineField("NORMAL");
    private int numMineDiscovered = 0;
    private IhmMinesweeper ihmMinesweeper;//gui client
    private boolean multiPlayerStarted = false;
    private boolean started = false;
    private boolean lost = false;

    //inout and output stream
    private DataInputStream inClient;
    private DataOutputStream outClient;

    //thread for multiplayers
    private Thread process;

    public boolean isMultiPlayerStarted() {
        return multiPlayerStarted;
    }

    public void setMultiPlayerStarted(boolean multiPlayerStarted) {
        this.multiPlayerStarted = multiPlayerStarted;
    }

    public DataInputStream getInClient() {
        return inClient;
    }

    public DataOutputStream getOutClient() {
        return outClient;
    }

    public void resetNumMineDiscovered() {
        this.numMineDiscovered = 0;
    }

    public int getNumMineDiscovered() {
        return numMineDiscovered;
    }

    public void increaseNumMineDiscovered() {
        numMineDiscovered++;
    }

    public void setLost(boolean lost) {
        this.lost = lost;
    }

    public boolean isLost() {
        return lost;
    }

    public IhmMinesweeper getIhmMinesweeper() {
        return ihmMinesweeper;
    }

    public boolean isStarted() {
        return started;
    }

    public void setStarted(boolean started) {
        this.started = started;
    }

    public MineField getMineField() {
        return mineField;
    }

    //constructor of client
    public AppMinesweeper() {
        mineField.showText();
        ihmMinesweeper = new IhmMinesweeper(this);
        setContentPane(ihmMinesweeper);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setVisible(true);
        mineField.showTextWithMinesNum();
    }

    public static void main(String[] args) {
        System.out.println("Welcome to Minesweeper!");
        new AppMinesweeper();
    }

    //quit game
    public void quit() {
        int response = JOptionPane.showConfirmDialog(null,
                "Are you sure to quit the game?", "Bye-Bye",
                JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE);
        if (response == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    //new game in the same level
    public void newgame() {
        newgame(mineField.getLevel());
    }

    //new game in specified level
    public void newgame(String level) {

        mineField.initChamp(level);
        mineField.showText();
        ihmMinesweeper = new IhmMinesweeper(this);
        setContentPane(ihmMinesweeper);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();

        ihmMinesweeper.getTime().stopCounter();
        this.setLost(false);
        this.setStarted(false);

        setVisible(true);
        mineField.showTextWithMinesNum();
        for (int i = 0; i < mineField.getDimension(); i++) {
            for (int j = 0; j < mineField.getDimension(); j++) {
                ihmMinesweeper.getTabCases()[i][j].newgame();
            }
        }
        resetNumMineDiscovered();
    }

    public void newMultiGame() {
        mineField.initChamp("NORMAL");
        ihmMinesweeper = new IhmMinesweeper(this);
        setContentPane(ihmMinesweeper);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();

        ihmMinesweeper.getTime().stopCounter();
        this.setLost(false);
        this.setStarted(false);
        setVisible(true);
        mineField.showTextWithMinesNum();
        for (int i = 0; i < mineField.getDimension(); i++) {
            for (int j = 0; j < mineField.getDimension(); j++) {
                ihmMinesweeper.getTabCases()[i][j].newgame();
            }
        }
        resetNumMineDiscovered();
    }

    //level info
    public void levelInformation() {
        int response = JOptionPane.showConfirmDialog(null,
                "Easy:10*10, 20 mines \nNormal:20*20, 80 mines\nHard:30*30, 350 mines", "Level information",
                JOptionPane.CLOSED_OPTION);
        if (response == JOptionPane.CLOSED_OPTION) {
            System.exit(0);
        }
    }

    //author info
    public void aboutAuthor() {
        int response = JOptionPane.showConfirmDialog(null,
                "FENG Yuan\nform EMSE", "About author",
                JOptionPane.CLOSED_OPTION);
        if (response == JOptionPane.CLOSED_OPTION) {
            System.exit(0);
        }
    }

    public boolean isWin() {
        System.out.println("numMineDiscovered=" + getNumMineDiscovered());
        boolean win = numMineDiscovered + mineField.getNumMines() + 1 == mineField.getDimension() * mineField.getDimension();
        return win;
    }

    //connect client to server
    public void connectToServer(String host, int port, String pseudo) {
        ihmMinesweeper.addMessage("Try to connect to " + host + ":" + port + "\n");
        try {
            Socket socket = new Socket(host, port);
            ihmMinesweeper.addMessage("Success!\n");
            inClient = new DataInputStream(socket.getInputStream());
            outClient = new DataOutputStream(socket.getOutputStream());

            outClient.writeUTF(pseudo);  //send username to the server
            outClient.writeUTF(this.mineField.getLevel());   //send level to the server

            process = new Thread(this);
            process.start();


        } catch (UnknownHostException e) {
            ihmMinesweeper.addMessage("Exception in client, unknown host: " + e.getMessage() + "\n");
            e.printStackTrace();
        } catch (IOException e) {
            ihmMinesweeper.addMessage("IO exception in client: " + e.getMessage() + "\n");
            e.printStackTrace();
        }
    }

    //send message from client to server
    public void sendMessage(String message, String name) throws IOException {
        outClient.writeInt(0);
        outClient.writeUTF(message);
        outClient.writeUTF(name);
        SimpleDateFormat sdf = new SimpleDateFormat(" HH:mm:ss ");
        outClient.writeUTF(sdf.format(new Date()));
    }

    //event wait loop of server
    public void run() {
        //infinite loop
        while (process != null) {
            //read command
            int cmd = 0;
            try {
                cmd = inClient.readInt();
                this.ihmMinesweeper.addMessage("cmd=" + cmd + ", ");
            } catch (IOException e) {
                e.printStackTrace();
            }

            //according to what i read, i show the mine/number/game over
            if (cmd == MSG) {  // when cmd is Message
                String message = null;
                String name = null;
                String time = null;
                try {
                    message = inClient.readUTF();
                    name = inClient.readUTF();
                    time = inClient.readUTF();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ihmMinesweeper.addMessage(time + " " + name + ":" + message + "\n");
            }

            if (cmd == START) {  //received cmd Start
                String fixedName = getIhmMinesweeper().getPseudoField().getText(); //keep name
                multiPlayerStarted = true;
                newMultiGame();
                Boolean isMine = false;

                //set minefield local
                for (int i = 0; i < mineField.getDimension(); i++) {
                    for (int j = 0; j < mineField.getDimension(); j++) {
                        try {
                            isMine = inClient.readBoolean();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        mineField.setMineField(isMine, i, j);
                    }
                }
                try {
                    color = inClient.readUTF();  //read color
                } catch (IOException e) {
                    e.printStackTrace();
                }

                mineField.showText();
                mineField.showTextWithMinesNum();
                this.getIhmMinesweeper().getPseudoField().setText(fixedName);//keep name
                ihmMinesweeper.addMessage("Game start!\n");
            }

            if (multiPlayerStarted) {
                if (cmd == POS) {
                    int x = 0;
                    int y = 0;
                    String name = "";
                    String color = "";
                    try {
                        x = getInClient().readInt();
                        y = getInClient().readInt();
                        name = getInClient().readUTF();
                        color = getInClient().readUTF();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Boolean isMine = mineField.isMine(x, y);
                    int countMines = mineField.calculateMinesAround(x, y);

                    this.getIhmMinesweeper().getAcase(x, y).setColor(color);
                    this.getIhmMinesweeper().setTabCasesClickedTrue(x, y);
                    this.getIhmMinesweeper().getAcase(x, y).repaint();

                    ihmMinesweeper.addMessage(name + " clicked (" + x + "," + y + ")" + "\n");
                    ihmMinesweeper.addMessage("Mine? " + isMine + ", around: " + countMines + "\n");
                }

                if (cmd == END) {
                    Boolean isReallyEnd;
                    String name;
                    int countPlayers;
                    int x;
                    int y;
                    try {
                        isReallyEnd = inClient.readBoolean();
                        name = inClient.readUTF();
                        x = inClient.readInt();
                        y = inClient.readInt();
                        this.getIhmMinesweeper().getAcase(x, y).setColor(color);
                        this.getIhmMinesweeper().setTabCasesClickedTrue(x, y);
                        this.getIhmMinesweeper().getAcase(x, y).repaint();
                        if (!isReallyEnd) {
                            countPlayers = inClient.readInt();
                            ihmMinesweeper.addMessage(name + " loses, " + countPlayers + " players remain\n"); //continue game, there is still player remains
                        } else {
                            ihmMinesweeper.addMessage(name + " loses, game over\n");   //end game, only one player remains
                            for (int i = 0; i < this.getMineField().getDimension(); i++) {
                                for (int j = 0; j < this.getMineField().getDimension(); j++) {
                                    this.ihmMinesweeper.getAcase(i, j).setClicked(false);
                                }
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
