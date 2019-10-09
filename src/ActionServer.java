
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ActionServer implements ActionListener {


    private AppServer appServer;
    private int type;

    //code number for each action of server
    public static final int STARTGAME = 1;

    //constructor of server actioner
    public ActionServer(AppServer appServer, int type) {
        this.appServer = appServer;
        this.type = type;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (type==STARTGAME){
            appServer.startGame();
        }
    }


}
