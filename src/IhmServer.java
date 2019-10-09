
import javax.swing.*;
import java.awt.*;

public class IhmServer extends JPanel {

    private AppServer appServer;
    private JButton startButton;
    private JTextArea messageArea = new JTextArea();


    public IhmServer(AppServer appServer) {
        this.appServer = appServer;
        setBackground(Color.LIGHT_GRAY);//overall background color
        setLayout(new BorderLayout());//overall layout

        JLabel present = new JLabel("Minesweeper server");
        add(present, BorderLayout.NORTH);

        messageArea.setEditable(false); //set message window
        add(messageArea, BorderLayout.CENTER);

        startButton = new JButton("Start game"); //start button
        startButton.addActionListener(new ActionServer(appServer, ActionServer.STARTGAME)); //let actioner to call related emthod

        add(startButton, BorderLayout.SOUTH);

    }

    //add message to the window
    public void addMessage(String s) {
        messageArea.append(s);
    }

}
