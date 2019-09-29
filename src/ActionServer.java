
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ActionServer implements ActionListener {


    private AppServer appServer;
    private int type;

    public static final int STARTGAME = 1;

    @Override
    public void actionPerformed(ActionEvent e) {
        if (type==STARTGAME){
            appServer.startGame();
        }
    }

    public ActionServer(AppServer appServer, int type) {
        this.appServer = appServer;
        this.type = type;
    }
}
