import javax.swing.*;
import java.awt.*;

public class IhmChat extends JPanel {

    private AppChat appChat;
    private JTextArea messageArea = new JTextArea();
    private JButton sendButton;
    private JButton quitButton;
    private JTextField messageField = new JTextField(AppChat.MESSAGE, 20);

    public JTextField getMessageField() {
        return messageField;
    }

    public IhmChat(AppChat appChat) {
        this.appChat = appChat;
        setBackground(Color.LIGHT_GRAY);
        setLayout(new BorderLayout());

        JLabel present = new JLabel("Chat box");
        add(present, BorderLayout.NORTH);

        messageArea.setEditable(true);
        add(messageArea, BorderLayout.CENTER);

        sendButton = new JButton("Send");
        sendButton.addActionListener(new ActionChat(appChat, ActionChat.SEND));
        quitButton = new JButton("Quit");
        quitButton.addActionListener(new ActionChat(appChat, ActionChat.QUIT));


        JPanel info = new JPanel();
        info.setLayout(new GridLayout(2, 1));

        JPanel infoMessage = new JPanel();
        infoMessage.setLayout(new FlowLayout());

        JPanel messageP = new JPanel();
        messageP.setLayout(new FlowLayout(FlowLayout.CENTER));
        JLabel messageL = new JLabel("Message:");
        messageP.add(messageL);

        JPanel messageContent = new JPanel();
        messageContent.setLayout(new FlowLayout(FlowLayout.CENTER));
        messageContent.add(messageField);

        infoMessage.add(messageP);
        infoMessage.add(messageContent);


        JPanel infoButton = new JPanel();
        infoButton.setLayout(new GridLayout(1, 2));

        infoButton.add(sendButton);
        infoButton.add(quitButton);


        info.add(infoMessage);
        info.add(infoButton);

        add(info, BorderLayout.SOUTH);
    }

    public void addMessage(String s) {
        messageArea.append(s);
    }

}
