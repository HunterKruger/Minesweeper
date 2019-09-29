
import javax.swing.*;
import java.awt.*;

public class IhmServer extends JPanel {

    private AppServer appServer;
    private JButton startButton;
    private JTextArea messageArea = new JTextArea();


    public IhmServer(AppServer appServer) {
        this.appServer = appServer;
        setBackground(Color.LIGHT_GRAY);
        setLayout(new BorderLayout());

        JLabel present = new JLabel("Minesweeper server");
        add(present, BorderLayout.NORTH);

        messageArea.setEditable(false);
        add(messageArea, BorderLayout.CENTER);

        startButton = new JButton("Start game");
        startButton.addActionListener(new ActionServer(appServer, ActionServer.STARTGAME));

        add(startButton, BorderLayout.SOUTH);

    }

    public void addMessage(String s) {
        messageArea.append(s);
    }

}
