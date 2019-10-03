
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.*;


public class IhmMinesweeper extends JPanel {

    private Case[][] tabCases;
    private Counter counter;
    private JTextField hostnameField = new JTextField(AppMinesweeper.HOSTNAME, 5);
    private JTextField portField = new JTextField(String.valueOf(AppMinesweeper.PORT), 5);
    private JTextField pseudoField = new JTextField(AppMinesweeper.PSEUDO, 5);
    private JTextField messageField = new JTextField(AppMinesweeper.MESSAGE, 15);
    private JTextArea messageArea = new JTextArea();


    public void addMessage(String s) {
        messageArea.append(s);
    }

    public JTextField getHostnameField() {
        return hostnameField;
    }

    public JTextField getMessageField() {
        return messageField;
    }

    public JTextField getPortField() {
        return portField;
    }

    public JTextField getPseudoField() {
        return pseudoField;
    }

    public Counter getTime() {
        return counter;
    }

    public Case[][] getTabCases() {
        return tabCases;
    }

    public IhmMinesweeper(AppMinesweeper appMinesweeper) {

        setBackground(Color.LIGHT_GRAY);
        setLayout(new BorderLayout());

        JMenuBar menuBar = new JMenuBar();

        JMenu start = new JMenu("Start ");
        JMenuItem quit = new JMenuItem("Quit", KeyEvent.VK_Q);
        // click control+E to quit this game
        quit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, ActionEvent.CTRL_MASK));
        quit.addActionListener(new ActionMinesweeper(ActionMinesweeper.QUIT, appMinesweeper));

        JMenuItem newGame = new JMenuItem("New game", KeyEvent.VK_T);
        newGame.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, ActionEvent.CTRL_MASK));
        newGame.addActionListener(new ActionMinesweeper(ActionMinesweeper.NEWGAME, appMinesweeper));

        JMenu newGame2 = new JMenu("New game...");

        JMenuItem easy = new JMenuItem("EASY", KeyEvent.VK_E);
        easy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, ActionEvent.CTRL_MASK));
        easy.addActionListener(new ActionMinesweeper(ActionMinesweeper.EASY, appMinesweeper));


        JMenuItem normal = new JMenuItem("NORMAL", KeyEvent.VK_N);
        normal.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
        normal.addActionListener(new ActionMinesweeper(ActionMinesweeper.NORMAL, appMinesweeper));


        JMenuItem hard = new JMenuItem("HARD", KeyEvent.VK_H);
        hard.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, ActionEvent.CTRL_MASK));
        hard.addActionListener(new ActionMinesweeper(ActionMinesweeper.HARD, appMinesweeper));

        newGame2.add(easy);
        newGame2.add(normal);
        newGame2.add(hard);

        //menuPart1Item2.setEnabled(false); //disable the New game button

        start.add(newGame);  //new game
        start.add(newGame2);  //new game...
        start.add(quit);  //quit

        JMenu help = new JMenu("Help");
        JMenuItem levelInfo = new JMenuItem("Level information", KeyEvent.VK_L);
        levelInfo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, ActionEvent.CTRL_MASK));
        levelInfo.addActionListener(new ActionMinesweeper(ActionMinesweeper.LEVELINFO, appMinesweeper));

        help.add(levelInfo);
        JMenuItem aboutAuthor = new JMenuItem("About author", KeyEvent.VK_A);
        aboutAuthor.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.CTRL_MASK));
        aboutAuthor.addActionListener(new ActionMinesweeper(ActionMinesweeper.ABOUTAUTHOR, appMinesweeper));
        help.add(aboutAuthor);

        menuBar.add(start);
        menuBar.add(Box.createGlue());  //put the next menuPart to the right side
        menuBar.add(help);
        appMinesweeper.setJMenuBar(menuBar);


        JPanel info = new JPanel();
        info.setLayout(new GridLayout(4, 1));


        JPanel infoGame = new JPanel();
        infoGame.setLayout(new GridLayout(1, 3));

        JPanel left = new JPanel();
        left.setLayout(new FlowLayout(FlowLayout.CENTER));
        JLabel mineGame = new JLabel("Mines:" + appMinesweeper.getMineField().getNumMines());
        left.add(mineGame);

        JPanel center = new JPanel();
        center.setLayout(new FlowLayout(FlowLayout.CENTER));
        counter = new Counter();
        counter.setText(String.valueOf(counter.getProcessTime()));
        center.add(counter);

        JPanel right = new JPanel();
        right.setLayout(new FlowLayout(FlowLayout.CENTER));
        JLabel levelGame = new JLabel(appMinesweeper.getMineField().getLevel());
        right.add(levelGame);

        infoGame.add(left);
        infoGame.add(center);
        infoGame.add(right);


        JPanel infoNetwork = new JPanel();
        infoNetwork.setLayout(new FlowLayout());

        JPanel A = new JPanel();
        A.setLayout(new FlowLayout(FlowLayout.CENTER));
        JLabel server = new JLabel("Server:");
        A.add(server);

        JPanel B = new JPanel();
        B.setLayout(new FlowLayout(FlowLayout.CENTER));
        B.add(hostnameField);

        JPanel C = new JPanel();
        C.setLayout(new FlowLayout(FlowLayout.CENTER));
        C.add(portField);

        JPanel D = new JPanel();
        D.setLayout(new FlowLayout(FlowLayout.CENTER));
        JLabel name = new JLabel("Name:");
        D.add(name);

        JPanel E = new JPanel();
        E.setLayout(new FlowLayout(FlowLayout.CENTER));
        E.add(pseudoField);

        infoNetwork.add(A);
        infoNetwork.add(B);
        infoNetwork.add(C);
        infoNetwork.add(D);
        infoNetwork.add(E);


        JPanel infoMessage = new JPanel();
        infoMessage.setLayout(new FlowLayout());

        JPanel F = new JPanel();
        F.setLayout(new FlowLayout(FlowLayout.CENTER));
        JLabel Message = new JLabel("Message:");
        F.add(Message);

        JPanel G = new JPanel();
        G.setLayout(new FlowLayout(FlowLayout.CENTER));
        G.add(messageField);

        infoMessage.add(F);
        infoMessage.add(G);


        JPanel infoButton = new JPanel();
        infoButton.setLayout(new GridLayout(1, 2));

        JButton buttonConnect = new JButton("Connect");
        buttonConnect.addActionListener(new ActionMinesweeper(ActionMinesweeper.CONNECT, appMinesweeper));

        JButton buttonSendMessage = new JButton("Send");
        buttonSendMessage.addActionListener(new ActionMinesweeper(ActionMinesweeper.SENDMSG, appMinesweeper));

        JButton buttonQuit = new JButton("Quit");
        buttonQuit.addActionListener(new ActionMinesweeper(ActionMinesweeper.QUIT, appMinesweeper));

        infoButton.add(buttonConnect);
        infoButton.add(buttonSendMessage);
        infoButton.add(buttonQuit);


        info.add(infoGame);
        info.add(infoNetwork);
        info.add(infoMessage);
        info.add(infoButton);


        add(info, BorderLayout.NORTH);

        JPanel panelMines = new JPanel();
        panelMines.setLayout(new GridLayout(appMinesweeper.getMineField().getDimension(), appMinesweeper.getMineField().getDimension()));

        tabCases = new Case[appMinesweeper.getMineField().getDimension()][appMinesweeper.getMineField().getDimension()];
        for (int i = 0; i < appMinesweeper.getMineField().getDimension(); i++) {
            for (int j = 0; j < appMinesweeper.getMineField().getDimension(); j++) {
                tabCases[i][j] = new Case(i, j, appMinesweeper);
                panelMines.add(tabCases[i][j]);
            }
        }
        add(panelMines, BorderLayout.CENTER);


        messageArea.setEditable(false);
        add(messageArea, BorderLayout.SOUTH);
    }

}