
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ActionMinesweeper implements ActionListener {

    public static final int QUIT = 0;
    public static final int NEWGAME = 1;
    public static final int LEVELINFO = 2;
    public static final int ABOUTAUTHOR = 3;
    public static final int EASY = 4;
    public static final int NORMAL = 5;
    public static final int HARD = 6;
    public static final int MIUNSMINES = 7;
    public static final int CONNECT = 8;
    public static final int SENDMSG = 9;

    private AppMinesweeper appMinesweeper;
    private int type;


    public ActionMinesweeper(int type, AppMinesweeper appMinesweeper) {
        this.appMinesweeper = appMinesweeper;
        this.type = type;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (type == QUIT) {
            appMinesweeper.quit();
        }
        if (type == NEWGAME) {
            appMinesweeper.newgame();
        }
        if (type == LEVELINFO) {
            appMinesweeper.levelInformation();
        }
        if (type == ABOUTAUTHOR) {
            appMinesweeper.aboutAuthor();
        }
        if (type == EASY) {
            appMinesweeper.newgame("EASY");
        }
        if (type == NORMAL) {
            appMinesweeper.newgame("NORMAL");
        }
        if (type == HARD) {
            appMinesweeper.newgame("HARD");
        }
        if (type == CONNECT) {
            appMinesweeper.connectToServer(appMinesweeper.getIhmMinesweeper().getHostnameField().getText(),
                    Integer.parseInt(appMinesweeper.getIhmMinesweeper().getPortField().getText()),
                    appMinesweeper.getIhmMinesweeper().getPseudoField().getText());
        }
        if (type == SENDMSG) {

        }

    }
}
